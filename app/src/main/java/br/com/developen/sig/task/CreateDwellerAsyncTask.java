package br.com.developen.sig.task;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.Date;

import br.com.developen.sig.database.ModifiedAddressEdificationDwellerVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class CreateDwellerAsyncTask<L extends CreateDwellerAsyncTask.Listener>
        extends AsyncTask<Object, Void, Object> {


    private WeakReference<L> listener;


    public CreateDwellerAsyncTask(L listener) {

        this.listener = new WeakReference<>(listener);

    }


    protected Object doInBackground(Object... parameters) {


        Integer modifiedAddress = (Integer) parameters[0];

        Integer edification = (Integer) parameters[1];


        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressEdificationDwellerVO modifiedAddressEdificationDwellerVO = new ModifiedAddressEdificationDwellerVO();

            modifiedAddressEdificationDwellerVO.setModifiedAddress(modifiedAddress);

            modifiedAddressEdificationDwellerVO.setEdification(edification);

            modifiedAddressEdificationDwellerVO.setDweller(
                    database.modifiedAddressEdificationDwellerDAO().
                            retrieveLastDwellerIdOfModifiedAddressEdification(modifiedAddress, edification) + 1);

            modifiedAddressEdificationDwellerVO.setIndividual(null);

//            modifiedAddressEdificationDwellerVO.setName("Diogo Buzin Rosin");

//            modifiedAddressEdificationDwellerVO.setMotherName("Odilete Buzin");

//            modifiedAddressEdificationDwellerVO.setFatherName("Ilonir Rosin");

            modifiedAddressEdificationDwellerVO.setGender("M");

//            modifiedAddressEdificationDwellerVO.setBirthDate(new Date());

//            modifiedAddressEdificationDwellerVO.setBirthPlace(null);

//            modifiedAddressEdificationDwellerVO.setCpf(5369609926L);

//            modifiedAddressEdificationDwellerVO.setRgNumber(3100191L);

            modifiedAddressEdificationDwellerVO.setRgAgency(1);

            modifiedAddressEdificationDwellerVO.setRgState(24);

            modifiedAddressEdificationDwellerVO.setFrom(new Date());

            modifiedAddressEdificationDwellerVO.setActive(false);

            database.modifiedAddressEdificationDwellerDAO().create(modifiedAddressEdificationDwellerVO);


            database.setTransactionSuccessful();

            return modifiedAddressEdificationDwellerVO.getDweller();

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

                listener.onCreateDwellerSuccess((Integer) callResult);

            } else {

                if (callResult instanceof Messaging){

                    listener.onCreateDwellerFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onCreateDwellerSuccess(Integer identifier);

        void onCreateDwellerFailure(Messaging messaging);

    }


}