package br.com.developen.sig.task;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import br.com.developen.sig.database.CityVO;
import br.com.developen.sig.database.ModifiedAddressVO;
import br.com.developen.sig.exception.CannotInitializeDatabaseException;
import br.com.developen.sig.exception.InternalException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.util.Messaging;

public class UpdateAddressLocationAsyncTask<L extends UpdateAddressLocationAsyncTask.Listener >
        extends AsyncTask<Object, Void, Object> {


    private WeakReference<L> listener;

    private boolean updateAddressInfo;


    public UpdateAddressLocationAsyncTask(L listener, Boolean updateAddressInfo) {

        this.listener = new WeakReference<>(listener);

        this.updateAddressInfo = updateAddressInfo;

    }


    protected Object doInBackground(Object... params) {


        Integer modifiedAddressIdentifier = (Integer) params[0];

        Double latitude = (Double) params[1];

        Double longitude = (Double) params[2];

        String thoroughfare = null;

        String number = null;

        String district = null;

        String postalCode = null;

        String country = null;

        String state = null;

        String city = null;


        boolean hasAddressInfo = false;


        L listener = this.listener.get();

        if (listener == null)

            return false;

        //BUSCA O ENDERECO DA LOCALIZACAO
        if (updateAddressInfo) {

            try {

                Geocoder geocoder = new Geocoder(App.getContext());

                List<Address> addresses = geocoder.
                        getFromLocation(latitude, longitude, 1);

                if (addresses != null && addresses.size() > 0) {

                    Address address = addresses.get(0);

                    thoroughfare = address.getThoroughfare();

                    number = address.getFeatureName();

                    district = address.getSubLocality();

                    postalCode = address.getPostalCode();

                    city = address.getSubAdminArea();

                    state = address.getAdminArea();

                    country = address.getCountryName();

                    hasAddressInfo = true;

                }

            } catch (IOException ignored){}

        }

        DB database = DB.getInstance(App.getInstance());

        if (database==null)

            return new CannotInitializeDatabaseException();

        try {

            database.beginTransaction();

            ModifiedAddressVO modifiedAddressVO = database.
                    modifiedAddressDAO().
                    retrieve(modifiedAddressIdentifier);

            modifiedAddressVO.setLatitude(latitude);

            modifiedAddressVO.setLongitude(longitude);

            if (hasAddressInfo) {

                modifiedAddressVO.setDenomination(thoroughfare);

                modifiedAddressVO.setNumber(number);

                modifiedAddressVO.setDistrict(district);

                modifiedAddressVO.setPostalCode(postalCode == null ? null : Integer.valueOf(postalCode.replaceAll("\\D+", "")));

                if (city != null && !city.isEmpty()) {

                    CityVO cityVO = database.cityDAO().findByCityStateCountry(city, state, country);

                    if (cityVO != null)

                        modifiedAddressVO.setCity(cityVO.getIdentifier());

                }

            }

            database.modifiedAddressDAO().update(modifiedAddressVO);

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

                listener.onUpdateAddressLocationSuccess();

            } else {

                if (callResult instanceof Messaging){

                    listener.onUpdateAddressLocationFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onUpdateAddressLocationSuccess();

        void onUpdateAddressLocationFailure(Messaging messaging);

    }


}