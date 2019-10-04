package br.com.developen.sig.task;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.util.StringUtils;

public class CreateAddressAsyncTask<L extends CreateAddressAsyncTask.Listener > extends AsyncTask<Double, Integer, Object> {


    private WeakReference<L> listener;

    private SharedPreferences preferences;


    public CreateAddressAsyncTask(L activity) {

        this.listener = new WeakReference<>(activity);

        this.preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    }


    protected void onPreExecute() {

        L listener = this.listener.get();

        if (listener != null)

            listener.onCreateAddressPreExecute();

    }


    protected void onProgressUpdate(Integer... progress){

        L listener = this.listener.get();

        if (listener != null)

            listener.onCreateAddressProgressUpdate(progress[0]);

    }


    public void onCancelled(){

        L listener = this.listener.get();

        if (listener != null)

            listener.onCreateAddressAddressCancelled();

    }


    protected Object doInBackground(Double... params) {

        Double latitude = params[0];

        Double longitude = params[1];

        String thoroughfare = null;

        String number = null;

        String district = null;

        String postalCode = null;

        String country = null;

        String state = null;

        String city = null;

        Integer modifiedBy = this.preferences.getInt(Constants.USER_IDENTIFIER_PROPERTY, 0);

        Date modifiedAt = new Date();

        L listener = this.listener.get();

        if (listener == null)

            return false;

        try {

            Geocoder geocoder = new Geocoder(App.getContext());

            List<Address> addresses = geocoder.
                    getFromLocation(latitude, longitude,1);

            if(addresses != null && addresses.size() > 0){

                Address address = addresses.get(0);

                thoroughfare = address.getThoroughfare();

                number = address.getFeatureName();

                district = address.getSubLocality();

                postalCode = address.getPostalCode();

                city = address.getSubAdminArea();

                state = address.getAdminArea();

                country = address.getCountryName();

            }

            publishProgress(1);

        } catch (IOException ignored) {}

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressVO modifiedAddressVO = new ModifiedAddressVO();

            modifiedAddressVO.setDenomination(thoroughfare);

            modifiedAddressVO.setNumber(number);

            modifiedAddressVO.setDistrict(district);

            modifiedAddressVO.setPostalCode(StringUtils.parsePostalCode(postalCode));

            CityModel cityModel = App.
                    getInstance().
                    getCityRepository().
                    findByCityStateCountry(city, state, country);

            if (cityModel != null)

                modifiedAddressVO.setCity(cityModel.getIdentifier());

            modifiedAddressVO.setModifiedBy(modifiedBy);

            modifiedAddressVO.setModifiedAt(modifiedAt);

            modifiedAddressVO.setLatitude(latitude);

            modifiedAddressVO.setLongitude(longitude);

            modifiedAddressVO.setActive(false);

            Integer identifier = database.
                    modifiedAddressDAO().
                    create(modifiedAddressVO).intValue();

            database.setTransactionSuccessful();

            publishProgress(1);

            return identifier;

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

            if (callResult instanceof Integer){

                listener.onCreateAddressSuccess((Integer) callResult);

            } else {

                if (callResult instanceof Messaging){

                    listener.onCreateAddressFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onCreateAddressPreExecute();

        void onCreateAddressProgressUpdate(int status);

        void onCreateAddressSuccess(Integer identifier);

        void onCreateAddressFailure(Messaging messaging);

        void onCreateAddressAddressCancelled();

    }


}