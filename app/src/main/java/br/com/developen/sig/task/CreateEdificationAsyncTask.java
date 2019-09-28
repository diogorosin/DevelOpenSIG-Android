package br.com.developen.sig.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Date;

import br.com.developen.sig.database.ModifiedAddressEdificationVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class CreateEdificationAsyncTask<L extends CreateEdificationAsyncTask.Listener> extends AsyncTask<Object, Void, Object> {


    private WeakReference<L> listener;


    public CreateEdificationAsyncTask(L listener) {

        this.listener = new WeakReference<>(listener);

    }


    protected Object doInBackground(Object... parameters) {

        Integer modifiedAddress = (Integer) parameters[0];

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressEdificationVO modifiedAddressEdificationVO = new ModifiedAddressEdificationVO();

            modifiedAddressEdificationVO.setModifiedAddress(modifiedAddress);

            modifiedAddressEdificationVO.setEdification(database.modifiedAddressEdificationDAO().retrieveLastEdificationIdOfModifiedAddress(modifiedAddress)+1);

            modifiedAddressEdificationVO.setType(1);

            modifiedAddressEdificationVO.setReference(null);

            //DEFINIDA PELO SERVIDOR NO MOMENTO DA IMPORTACAO
            //modifiedAddressEdificationVO.setFrom(new Date());

            modifiedAddressEdificationVO.setActive(false);

            database.modifiedAddressEdificationDAO().create(modifiedAddressEdificationVO);

            database.setTransactionSuccessful();

            return modifiedAddressEdificationVO.getEdification();

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

            if (callResult instanceof Integer){

                listener.onCreateEdificationSuccess((Integer) callResult);

            } else {

                if (callResult instanceof Messaging){

                    listener.onCreateEdificationFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onCreateEdificationSuccess(Integer identifier);

        void onCreateEdificationFailure(Messaging messaging);

    }


}