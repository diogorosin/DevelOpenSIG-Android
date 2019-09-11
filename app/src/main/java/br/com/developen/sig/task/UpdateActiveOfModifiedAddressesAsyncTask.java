package br.com.developen.sig.task;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.TaskException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public final class UpdateActiveOfModifiedAddressesAsyncTask<L extends UpdateActiveOfModifiedAddressesAsyncTask.Listener>
        extends AsyncTask<ModifiedAddressModel, Void, Object> {


    private WeakReference<L> listener;

    private SharedPreferences preferences;


    public UpdateActiveOfModifiedAddressesAsyncTask(L listener) {

        this.listener = new WeakReference<>(listener);

        this.preferences = App.getInstance().
                getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);

    }


    protected void onPreExecute() {

        L l = this.listener.get();

        if (l != null)

            l.onDeleteModifiedAddressPreExecute();

    }


    @SuppressLint("DefaultLocale")
    protected Object doInBackground(ModifiedAddressModel... parameters) {

        List<String> exceptions = new ArrayList<>();

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        L l = listener.get();

        if (l != null)

            l.onDeleteModifiedAddressProgressInitialize(
                    0,
                    parameters.length);

        try {

            //INICIA A TRANSACAO
            database.beginTransaction();

            for (ModifiedAddressModel modifiedAddressModel: parameters) {

                ModifiedAddressVO modifiedAddressVO = database.
                        modifiedAddressDAO().
                        retrieve(modifiedAddressModel.getIdentifier());

                modifiedAddressVO.setActive(false);

                database.modifiedAddressDAO().update(modifiedAddressVO);

                //ATUALIZA O STATUS
                if (l != null)

                    l.onDeleteModifiedAddressProgressUpdate(1);

                Thread.sleep(200);

            }

            database.setTransactionSuccessful();

            return 0;

        } catch(Exception exception) {

            return exception;

        } finally {

            if (database.isOpen() && database.inTransaction())

                database.endTransaction();

        }

    }


    protected void onPostExecute(Object callResult) {

        L listener = this.listener.get();

        if (listener != null) {

            if (callResult instanceof Integer) {

                listener.onDeleteModifiedAddressSuccess();

            } else {

                if (callResult instanceof Messaging) {

                    listener.onDeleteModifiedAddressFailure((Messaging) callResult);

                }

            }

        }

    }


    protected void onProgressUpdate(Integer... progress) {

        L l = this.listener.get();

        if (l != null)

            l.onDeleteModifiedAddressProgressUpdate(progress[0]);

    }


    protected void onCancelled() {

        L l = this.listener.get();

        if (l != null)

            l.onDeleteModifiedAddressCancelled();

    }


    public interface Listener {

        void onDeleteModifiedAddressPreExecute();

        void onDeleteModifiedAddressProgressInitialize(int progress, int max);

        void onDeleteModifiedAddressProgressUpdate(int status);

        void onDeleteModifiedAddressSuccess();

        void onDeleteModifiedAddressFailure(Messaging message);

        void onDeleteModifiedAddressCancelled();

    }


}