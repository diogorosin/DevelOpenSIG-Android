package br.com.developen.sig.task;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

import br.com.developen.sig.database.CityVO;
import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class CreateAddressAsyncTask<A extends Activity & CreateAddressAsyncTask.Listener >
        extends AsyncTask<Double, Void, Object> {


    private WeakReference<A> activity;

    private SharedPreferences preferences;


    public CreateAddressAsyncTask(A activity) {

        this.activity = new WeakReference<>(activity);

        this.preferences = this.activity.get().
                getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, 0);

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

        A activity = this.activity.get();

        if (activity == null)

            return false;

        try {

            Geocoder geocoder = new Geocoder(activity.getBaseContext());

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

        } catch (IOException e) {}

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressVO modifiedAddressVO = new ModifiedAddressVO();

            modifiedAddressVO.setDenomination(thoroughfare);

            modifiedAddressVO.setNumber(number);

            modifiedAddressVO.setDistrict(district);

            modifiedAddressVO.setPostalCode(postalCode == null ? null : Integer.valueOf(postalCode.replaceAll("\\D+","")));

            if (city != null && !city.isEmpty()){

                CityVO cityVO = database.cityDAO().findByCityStateCountry(city, state, country);

                if (cityVO != null)

                    modifiedAddressVO.setCity(cityVO.getIdentifier());

            }

            modifiedAddressVO.setModifiedBy(modifiedBy);

            modifiedAddressVO.setModifiedAt(modifiedAt);

            modifiedAddressVO.setLatitude(latitude);

            modifiedAddressVO.setLongitude(longitude);

            modifiedAddressVO.setActive(false);

            Integer identifier = database.
                    modifiedAddressDAO().
                    create(modifiedAddressVO).intValue();

            database.setTransactionSuccessful();

            return identifier;

        } catch(Exception e) {

            return new InternalException();

        } finally {

            if (database.inTransaction())

                database.endTransaction();

        }

    }


    protected void onPostExecute(Object callResult) {

        A listener = this.activity.get();

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

        void onCreateAddressSuccess(Integer identifier);

        void onCreateAddressFailure(Messaging messaging);

    }


}