package br.com.developen.sig.task;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class UpdateActiveOfModifiedAddressAsyncTask<L extends UpdateActiveOfModifiedAddressAsyncTask.Listener >
        extends AsyncTask<Boolean, Void, Object> {


    private WeakReference<L> listener;

    private Integer modifiedAddress;


    public UpdateActiveOfModifiedAddressAsyncTask(L listener, Integer modifiedAddress) {

        this.listener = new WeakReference<>(listener);

        this.modifiedAddress = modifiedAddress;

    }


    protected Object doInBackground(Boolean... parameters) {

        Boolean active = parameters[0];

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressVO modifiedAddressVO = database.modifiedAddressDAO().retrieve(modifiedAddress);

            Log.d("DIOGO", modifiedAddressVO.getDenomination());

            modifiedAddressVO.setActive(active);

            database.modifiedAddressDAO().update(modifiedAddressVO);

            database.setTransactionSuccessful();

            return true;

        } catch(Exception e) {

            e.printStackTrace();

            return new InternalException();

        } finally {

            if (database.inTransaction())

                database.endTransaction();

        }


    }


    protected void onPostExecute(Object callResult) {

        L listener = this.listener.get();

        if (listener != null) {

            if (callResult instanceof Boolean){

                listener.onUpdateActiveOfModifiedAddressSuccess();

            } else {

                if (callResult instanceof Messaging){

                    listener.onUpdateActiveOfModifiedAddressFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onUpdateActiveOfModifiedAddressSuccess();

        void onUpdateActiveOfModifiedAddressFailure(Messaging messaging);

    }


}