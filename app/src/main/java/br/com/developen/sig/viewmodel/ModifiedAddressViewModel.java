package br.com.developen.sig.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;

import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.developen.sig.R;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.LatLngModel;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.repository.ModifiedAddressRepository;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.StringUtils;

public class ModifiedAddressViewModel extends AndroidViewModel {


    //KEYS
    private Integer identifier;

    //REPOSITORY
    private ModifiedAddressRepository repository;

    //FIELDS THAT NEED VALIDATION
    public final ValiFieldText denomination = getDenominationField();

    public final ValiFieldText number = getNumberField();

    public final ValiFieldText reference = getReferenceField();

    public final ValiFieldText district = getDistrictField();

    public final ValiFieldText postalCode = getPostalCodeField();

    public final ValiFieldText city = getCityField();

    //FIELDS THAT NOT NEED VALIDATION
    public final ObservableField<LatLngModel> latLng = new ObservableField<>();

    public final ObservableInt address = new ObservableInt();

    public final ObservableBoolean active = new ObservableBoolean();

    //FORM
    public final ValiFiForm form = new ValiFiForm(denomination, number, reference, district, postalCode, city);


    //FACTORY OF ModifiedAddressEdificationDwellerRepository
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final App application;

        private final Integer identifier;

        private final ModifiedAddressRepository repository;

        public Factory(Application application, Integer identifier) {

            this.application = (App) application;

            this.repository = App.getInstance().getModifiedAddressRepository();

            this.identifier = identifier;

        }

        public <T extends ViewModel> T create(Class<T> modelClass) {

            return (T) new ModifiedAddressViewModel(application, identifier, repository);

        }

    }


    public App getApplication(){

        return (App) super.getApplication();

    }


    public ModifiedAddressViewModel(Application application){

        super(application);

        this.repository = App.getInstance().getModifiedAddressRepository();

    }


    public ModifiedAddressViewModel(Application application,
                                    Integer identifier,
                                    ModifiedAddressRepository repository) {


        super(application);

        this.identifier = identifier;

        this.repository = repository;

        ModifiedAddressModel modifiedAddressModel = this.repository.getModifiedAddress(this.identifier);

        denomination.set(modifiedAddressModel.getDenomination());

        number.set(modifiedAddressModel.getNumber());

        reference.set(modifiedAddressModel.getReference());

        district.set(modifiedAddressModel.getDistrict());

        postalCode.set(StringUtils.padPostalCode(modifiedAddressModel.getPostalCode()));

        city.setValue(StringUtils.formatCityWithState(modifiedAddressModel.getCity()));

        latLng.set(modifiedAddressModel.getLatLng());

        active.set(modifiedAddressModel.getActive());

    }


    public LiveData<List<ModifiedAddressEdificationModel>> getEdificationsOfModifiedAddress(){

        return repository.getEdificationsOfModifiedAddress(this.identifier);

    }


    public LiveData<PagedList<ModifiedAddressModel>> getModifiedAddressesThatWasNotSynced(){

        return repository.getModifiedAddressesThatWasNotSynced();

    }


    public void save() throws CityNotFoundException {

        Map<Integer, Object> values = new HashMap<>();

        values.put(ModifiedAddressRepository.IDENTIFIER_PROPERTY, this.identifier);

        values.put(ModifiedAddressRepository.LATLON_PROPERTY, this.latLng.get());

        values.put(ModifiedAddressRepository.DENOMINATION_PROPERTY, this.denomination.get());

        values.put(ModifiedAddressRepository.NUMBER_PROPERTY, this.number.get());

        values.put(ModifiedAddressRepository.REFERENCE_PROPERTY, this.reference.get());

        values.put(ModifiedAddressRepository.DISTRICT_PROPERTY, this.district.get());

        values.put(ModifiedAddressRepository.POSTAL_CODE_PROPERTY, this.postalCode.get());

        values.put(ModifiedAddressRepository.CITY_PROPERTY, this.city.get());

        repository.save(values);

    }


    public void delete(){

        Map<Integer, Object> values = new HashMap<>();

        values.put(ModifiedAddressRepository.IDENTIFIER_PROPERTY, this.identifier);

        repository.delete(values);

    }


    protected void onCleared() {

        form.destroy();

        super.onCleared();

    }


    private static ValiFieldText getDenominationField(){

        return new ValiFieldText().addNotEmptyValidator();

    }


    private static ValiFieldText getNumberField(){

        return new ValiFieldText();

    }


    private static ValiFieldText getReferenceField(){

        return new ValiFieldText();

    }


    private static ValiFieldText getDistrictField(){

        return new ValiFieldText().addNotEmptyValidator();

    }


    private static ValiFieldText getPostalCodeField(){

        return new ValiFieldText().addMinLengthValidator(9);

    }


    private static ValiFieldText getCityField(){

        return (ValiFieldText) new ValiFieldText().
                addNotEmptyValidator().
                addCustomAsyncValidator(R.string.validation_error_invalid_city, string -> {

                    String[] parts = string.split("-");

                    if (parts.length == 2){

                        String city = parts[0].trim();

                        String stateAcronym = parts[1].trim();

                        CityModel cityModel = App.getInstance().
                                getCityRepository().
                                findByCityStateAcronym(city, stateAcronym);

                        return cityModel != null;

                    }

                    return false;

                });

    }


}