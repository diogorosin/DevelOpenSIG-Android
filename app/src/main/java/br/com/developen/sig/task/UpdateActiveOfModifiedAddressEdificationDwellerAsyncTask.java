package br.com.developen.sig.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import br.com.developen.sig.database.ModifiedAddressEdificationDwellerVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class UpdateActiveOfModifiedAddressEdificationDwellerAsyncTask<L extends UpdateActiveOfModifiedAddressEdificationDwellerAsyncTask.Listener >
        extends AsyncTask<Boolean, Void, Object> {


    private WeakReference<L> listener;

    private Integer modifiedAddress;

    private Integer edification;

    private Integer dweller;


    public UpdateActiveOfModifiedAddressEdificationDwellerAsyncTask(L listener, Integer modifiedAddress, Integer edification, Integer dweller) {

        this.listener = new WeakReference<>(listener);

        this.modifiedAddress = modifiedAddress;

        this.edification = edification;

        this.dweller = dweller;

    }


    protected Object doInBackground(Boolean... parameters) {

        Boolean active = parameters[0];

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressEdificationDwellerVO modifiedAddressEdificationDwellerVO = database.modifiedAddressEdificationDwellerDAO().retrieve(modifiedAddress, edification, dweller);

            modifiedAddressEdificationDwellerVO.setActive(active);

            database.modifiedAddressEdificationDwellerDAO().update(modifiedAddressEdificationDwellerVO);

            database.setTransactionSuccessful();

            return true;

        } catch(Exception e) {

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

                listener.onUpdateActiveOfModifiedAddressEdificationDwellerSuccess();

            } else {

                if (callResult instanceof Messaging){

                    listener.onUpdateActiveOfModifiedAddressEdificationDwellerFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onUpdateActiveOfModifiedAddressEdificationDwellerSuccess();

        void onUpdateActiveOfModifiedAddressEdificationDwellerFailure(Messaging messaging);

    }


}