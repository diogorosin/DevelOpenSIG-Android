package br.com.developen.sig.task;

import android.os.AsyncTask;

import br.com.developen.sig.database.ModifiedAddressEdificationVO;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;

public class UpdateReferenceOfModifiedAddressEdificationAsyncTask extends AsyncTask<Object, Void, Void> {


    protected Void doInBackground(Object... parameters) {

        Integer modifiedAddress = (Integer) parameters[0];

        Integer edification = (Integer) parameters[1];

        String reference = (String) parameters[2];

        DB database = DB.getInstance(App.getInstance());

        try {

            database.beginTransaction();

            ModifiedAddressEdificationVO modifiedAddressEdificationVO = database.modifiedAddressEdificationDAO().retrieve(modifiedAddress, edification);

            modifiedAddressEdificationVO.setReference(reference);

            database.modifiedAddressEdificationDAO().update(modifiedAddressEdificationVO);

            database.setTransactionSuccessful();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (database.inTransaction())

                database.endTransaction();

        }

        return null;

    }


}