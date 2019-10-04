package br.com.developen.sig.task;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.preference.PreferenceManager;

import java.lang.ref.WeakReference;
import java.util.Date;

import br.com.developen.sig.database.AddressEdificationDwellerModel;
import br.com.developen.sig.database.AddressEdificationModel;
import br.com.developen.sig.database.AddressModel;
import br.com.developen.sig.database.LatLngModel;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.exception.AddressNotFoundException;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.Messaging;

public class EditAddressAsyncTask<L extends EditAddressAsyncTask.Listener > extends AsyncTask<Integer, Integer, Object> {


    private WeakReference<L> listener;

    private SharedPreferences preferences;


    public EditAddressAsyncTask(L activity) {

        listener = new WeakReference<>(activity);

        preferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());

    }


    protected void onPreExecute() {

        L listener = this.listener.get();

        if (listener != null)

            listener.onEditAddressPreExecute();

    }


    protected void onProgressUpdate(Integer... progress){

        L listener = this.listener.get();

        if (listener != null)

            listener.onEditAddressProgressUpdate(progress[0]);

    }


    public void onCancelled(){

        L listener = this.listener.get();

        if (listener != null)

            listener.onEditAddressAddressCancelled();

    }


    protected Object doInBackground(Integer... params) {

        Integer address = params[0];

        Integer modifiedBy = preferences.getInt(Constants.USER_IDENTIFIER_PROPERTY, 0);

        Date modifiedAt = new Date();

        AddressModel addressModel = App.
                getInstance().
                getAddressRepository().
                getAddress(address);

        if (addressModel == null)

            return new AddressNotFoundException();

        ModifiedAddressModel modifiedAddressModel = new ModifiedAddressModel();

        modifiedAddressModel.setDenomination(addressModel.getDenomination());

        modifiedAddressModel.setNumber(addressModel.getNumber());

        modifiedAddressModel.setReference(addressModel.getReference());

        modifiedAddressModel.setDistrict(addressModel.getDistrict());

        modifiedAddressModel.setCity(addressModel.getCity());

        modifiedAddressModel.setPostalCode(addressModel.getPostalCode());

        modifiedAddressModel.setAddress(addressModel.getIdentifier());

        modifiedAddressModel.setModifiedBy(modifiedBy);

        modifiedAddressModel.setModifiedAt(modifiedAt);

        modifiedAddressModel.setLatLng(new LatLngModel(
                addressModel.getLatitude(),
                addressModel.getLongitude()));

        modifiedAddressModel.setActive(false);

        App.getInstance().
                getModifiedAddressRepository().
                create(modifiedAddressModel);

        for (AddressEdificationModel addressEdificationModel: App.
                getInstance().
                getAddressRepository().
                getEdificationsOfAddress(addressModel.getIdentifier())) {

            ModifiedAddressEdificationModel modifiedAddressEdificationModel = new ModifiedAddressEdificationModel();

            modifiedAddressEdificationModel.setModifiedAddress(modifiedAddressModel);

            modifiedAddressEdificationModel.setEdification(addressEdificationModel.getEdification());

            modifiedAddressEdificationModel.setType(addressEdificationModel.getType());

            modifiedAddressEdificationModel.setReference(addressEdificationModel.getReference());

            modifiedAddressEdificationModel.setFrom(addressEdificationModel.getFrom());

            modifiedAddressEdificationModel.setTo(addressEdificationModel.getTo());

            modifiedAddressEdificationModel.setActive(true);

            App.getInstance().
                    getModifiedAddressEdificationRepository().
                    create(modifiedAddressEdificationModel);

            for (AddressEdificationDwellerModel addressEdificationDwellerModel : App.
                    getInstance().
                    getAddressEdificationRepository().
                    getDwellersOfAddressEdifications(
                            addressEdificationModel.getAddress().getIdentifier(),
                            addressEdificationModel.getEdification())){

                ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel = new ModifiedAddressEdificationDwellerModel();

                modifiedAddressEdificationDwellerModel.setModifiedAddressEdification(modifiedAddressEdificationModel);

                modifiedAddressEdificationDwellerModel.setDweller(addressEdificationDwellerModel.getDweller());

                modifiedAddressEdificationDwellerModel.setIndividual(addressEdificationDwellerModel.getIndividual());

                modifiedAddressEdificationDwellerModel.setName(addressEdificationDwellerModel.getIndividual().getName());

                modifiedAddressEdificationDwellerModel.setMotherName(addressEdificationDwellerModel.getIndividual().getMotherName());

                modifiedAddressEdificationDwellerModel.setFatherName(addressEdificationDwellerModel.getIndividual().getFatherName());

                modifiedAddressEdificationDwellerModel.setCpf(addressEdificationDwellerModel.getIndividual().getCpf());

                modifiedAddressEdificationDwellerModel.setRgNumber(addressEdificationDwellerModel.getIndividual().getRgNumber());

                modifiedAddressEdificationDwellerModel.setRgAgency(addressEdificationDwellerModel.getIndividual().getRgAgency());

                modifiedAddressEdificationDwellerModel.setRgState(addressEdificationDwellerModel.getIndividual().getRgState());

                modifiedAddressEdificationDwellerModel.setBirthDate(addressEdificationDwellerModel.getIndividual().getBirthDate());

                modifiedAddressEdificationDwellerModel.setBirthPlace(addressEdificationDwellerModel.getIndividual().getBirthPlace());

                modifiedAddressEdificationDwellerModel.setGender(addressEdificationDwellerModel.getIndividual().getGender());

                modifiedAddressEdificationDwellerModel.setFrom(addressEdificationDwellerModel.getFrom());

                modifiedAddressEdificationDwellerModel.setTo(addressEdificationDwellerModel.getTo());

                modifiedAddressEdificationDwellerModel.setActive(true);

                App.getInstance().
                        getModifiedAddressEdificationDwellerRepository().
                        create(modifiedAddressEdificationDwellerModel);

            }

        }

        return modifiedAddressModel.getIdentifier();

    }


    protected void onPostExecute(Object callResult) {

        L listener = this.listener.get();

        if (listener != null) {

            if (callResult instanceof Integer){

                listener.onEditAddressSuccess((Integer) callResult);

            } else {

                if (callResult instanceof Messaging){

                    listener.onEditAddressFailure((Messaging) callResult);

                }

            }

        }

    }


    public interface Listener {

        void onEditAddressPreExecute();

        void onEditAddressProgressUpdate(int status);

        void onEditAddressSuccess(Integer identifier);

        void onEditAddressFailure(Messaging messaging);

        void onEditAddressAddressCancelled();

    }


}