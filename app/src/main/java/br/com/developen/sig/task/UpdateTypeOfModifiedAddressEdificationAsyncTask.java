package br.com.developen.sig.task;

import android.os.AsyncTask;

import br.com.developen.sig.database.ModifiedAddressEdificationVO;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;

public class UpdateTypeOfModifiedAddressEdificationAsyncTask extends AsyncTask<Object, Void, Void> {

    protected Void doInBackground(Object... parameters) {

        Integer modifiedAddress = (Integer) parameters[0];

        Integer edification = (Integer) parameters[1];

        Integer type = (Integer) parameters[2];

        DB database = DB.getInstance(App.getInstance());

        try {

            database.beginTransaction();

            ModifiedAddressEdificationVO modifiedAddressEdificationVO = database.modifiedAddressEdificationDAO().retrieve(modifiedAddress, edification);

            modifiedAddressEdificationVO.setType(type);

            database.modifiedAddressEdificationDAO().update(modifiedAddressEdificationVO);

            database.setTransactionSuccessful();

        } catch(Exception e) {

            e.printStackTrace();

        } finally {

            if (database.inTransaction())

                database.endTransaction();

        }

        return null;

    }

}