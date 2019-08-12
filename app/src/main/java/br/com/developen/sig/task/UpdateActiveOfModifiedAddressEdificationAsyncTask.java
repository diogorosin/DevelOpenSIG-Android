package br.com.developen.sig.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import br.com.developen.sig.database.ModifiedAddressEdificationVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class UpdateActiveOfModifiedAddressEdificationAsyncTask<L extends UpdateActiveOfModifiedAddressEdificationAsyncTask.Listener >
        extends AsyncTask<Integer, Void, Object> {


    private WeakReference<L> listener;


    public UpdateActiveOfModifiedAddressEdificationAsyncTask(L listener) {

        this.listener = new WeakReference<>(listener);

    }


    protected Object doInBackground(Integer... parameters) {

        Integer modifiedAddress = parameters[0];

        Integer edification = parameters[1];

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressEdificationVO modifiedAddressEdificationVO = database.modifiedAddressEdificationDAO().retrieve(modifiedAddress, edification);

            modifiedAddressEdificationVO.setActive(true);

            database.modifiedAddressEdificationDAO().update(modifiedAddressEdificationVO);

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

                listener.onUpdateActiveOfModifiedAddressEdificationSuccess();

            } else {

                if (callResult instanceof Messaging){

                    listener.onUpdateActiveOfModifiedAddressEdificationFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onUpdateActiveOfModifiedAddressEdificationSuccess();

        void onUpdateActiveOfModifiedAddressEdificationFailure(Messaging messaging);

    }


}