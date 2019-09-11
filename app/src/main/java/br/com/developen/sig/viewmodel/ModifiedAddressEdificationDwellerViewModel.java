package br.com.developen.sig.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldText;
import com.mlykotom.valifi.fields.number.ValiFieldLong;

import java.util.HashMap;
import java.util.Map;

import br.com.developen.sig.R;
import br.com.developen.sig.database.AgencyModel;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.GenderModel;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.database.StateModel;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.exception.DocumentNotFoundException;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.StringUtils;

public class ModifiedAddressEdificationDwellerViewModel extends AndroidViewModel {

    //KEYS
    private Integer modifiedAddress, edification, dweller;

    //REPOSITORY
    private ModifiedAddressEdificationDwellerRepository repository;

    //FIELDS THAT NEED VALIDATION
    public final ValiFieldText name = getNameField();

    public final ValiFieldText motherName = getMotherNameField();

    public final ValiFieldText fatherName = getFatherNameField();

    public final ValiFieldText cpf = getCpfField();

    public final ValiFieldLong rgNumber = getRgNumber();

    public final ValiFieldText birthDate = getBirthDateField();

    public final ValiFieldText birthPlace = getBirthPlaceField();

    //FIELDS THAT NEED VALIDATION
    public final ObservableBoolean active = new ObservableBoolean();

    public final ObservableField<AgencyModel> rgAgency = new ObservableField<>();

    public final ObservableField<StateModel> rgState = new ObservableField<>();

    public final ObservableField<GenderModel> gender = new ObservableField<>();

    //FORM
    public final ValiFiForm form = new ValiFiForm(name, motherName, fatherName, cpf, rgNumber, birthDate, birthPlace);


    //FACTORY OF ModifiedAddressEdificationDwellerRepository
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final App application;

        private final Integer modifiedAddress;

        private final Integer edification;

        private final Integer dweller;

        private final ModifiedAddressEdificationDwellerRepository repository;

        public Factory(Application application, Integer modifiedAddress, Integer edification, Integer dweller) {

            this.application = (App) application;

            this.repository = this.application.getModifiedAddressEdificationDwellerRepository();

            this.modifiedAddress = modifiedAddress;

            this.edification = edification;

            this.dweller = dweller;

        }

        public <T extends ViewModel> T create(Class<T> modelClass) {

            return (T) new ModifiedAddressEdificationDwellerViewModel(application, modifiedAddress, edification, dweller, repository);

        }

    }


    public ModifiedAddressEdificationDwellerViewModel(Application application,
                                                      Integer modifiedAddress,
                                                      Integer edification,
                                                      Integer dweller,
                                                      ModifiedAddressEdificationDwellerRepository repository) {


        super(application);

        this.modifiedAddress = modifiedAddress;

        this.edification = edification;

        this.dweller = dweller;

        this.repository = repository;

        ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel =
                this.repository.getModifiedAddressEdificationDweller(this.modifiedAddress, this.edification, this.dweller);

        active.set(modifiedAddressEdificationDwellerModel.getActive());

        name.setValue(modifiedAddressEdificationDwellerModel.getName());

        motherName.setValue(modifiedAddressEdificationDwellerModel.getMotherName());

        fatherName.setValue(modifiedAddressEdificationDwellerModel.getFatherName());

        cpf.setValue(StringUtils.padCpf(modifiedAddressEdificationDwellerModel.getCpf()));

        rgNumber.setValue(StringUtils.formatRgNumberOfState(
                modifiedAddressEdificationDwellerModel.getRgNumber(),
                modifiedAddressEdificationDwellerModel.getRgState()));

        rgAgency.set(modifiedAddressEdificationDwellerModel.getRgAgency());

        rgState.set(modifiedAddressEdificationDwellerModel.getRgState());

        birthPlace.setValue(StringUtils.formatCityWithState(modifiedAddressEdificationDwellerModel.getBirthPlace()));

        birthDate.setValue(StringUtils.formatDate(modifiedAddressEdificationDwellerModel.getBirthDate()));

        gender.set(modifiedAddressEdificationDwellerModel.getGender());

    }


    public void save() throws CityNotFoundException, DocumentNotFoundException {

        Map<Integer, Object> values = new HashMap<>();

        values.put(ModifiedAddressEdificationDwellerRepository.MODIFIED_ADDRESS_PROPERTY, this.modifiedAddress);

        values.put(ModifiedAddressEdificationDwellerRepository.EDIFICATION_PROPERTY, this.edification);

        values.put(ModifiedAddressEdificationDwellerRepository.DWELLER_PROPERTY, this.dweller);

        values.put(ModifiedAddressEdificationDwellerRepository.NAME_PROPERTY, this.name.get());

        values.put(ModifiedAddressEdificationDwellerRepository.MOTHER_NAME_PROPERTY, this.motherName.get());

        values.put(ModifiedAddressEdificationDwellerRepository.FATHER_NAME_PROPERTY, this.fatherName.get());

        values.put(ModifiedAddressEdificationDwellerRepository.SUBJECT_PROPERTY, null);

        values.put(ModifiedAddressEdificationDwellerRepository.CPF_PROPERTY, StringUtils.parseCpf(this.cpf.get()));

        values.put(ModifiedAddressEdificationDwellerRepository.RG_NUMBER_PROPERTY, StringUtils.parseRgNumber(this.rgNumber.get()));

        values.put(ModifiedAddressEdificationDwellerRepository.RG_AGENCY_PROPERTY, this.rgAgency.get());

        values.put(ModifiedAddressEdificationDwellerRepository.RG_STATE_PROPERTY, this.rgState.get());

        values.put(ModifiedAddressEdificationDwellerRepository.BIRTH_DATE_PROPERTY, StringUtils.stringToDate(this.birthDate.get()));

        values.put(ModifiedAddressEdificationDwellerRepository.BIRTH_PLACE_PROPERTY, this.birthPlace.get());

        values.put(ModifiedAddressEdificationDwellerRepository.GENDER_PROPERTY, this.gender.get());

        repository.save(values);

    }


    public void delete(){

        Map<Integer, Object> values = new HashMap<>();

        values.put(ModifiedAddressEdificationDwellerRepository.MODIFIED_ADDRESS_PROPERTY, this.modifiedAddress);

        values.put(ModifiedAddressEdificationDwellerRepository.EDIFICATION_PROPERTY, this.edification);

        values.put(ModifiedAddressEdificationDwellerRepository.DWELLER_PROPERTY, this.dweller);

        repository.delete(values);

    }


    protected void onCleared() {

        form.destroy();

        super.onCleared();

    }


    private static ValiFieldText getNameField(){

        return (ValiFieldText) new ValiFieldText().
                addNotEmptyValidator().
                addCustomValidator(R.string.validation_error_invalid_name, text -> {

                    boolean isValid = true;

                    if (text != null && text.trim().length() > 0)

                        isValid = text.trim().split(" ").length >= 2;

                    return isValid;

                });

    }


    private static ValiFieldText getMotherNameField(){

        return (ValiFieldText) new ValiFieldText().
                addNotEmptyValidator().
                addCustomValidator(R.string.validation_error_invalid_name, text -> {

                    boolean isValid = true;

                    if (text != null && text.trim().length() > 0)

                        isValid = text.trim().split(" ").length >= 2;

                    return isValid;

                });

    }


    private static ValiFieldText getFatherNameField(){

        return (ValiFieldText) new ValiFieldText().
                setEmptyAllowed(true).
                addCustomValidator(R.string.validation_error_invalid_name, text -> {

                    boolean isValid = true;

                    if (text != null && text.trim().length() > 0)

                        isValid = text.trim().split(" ").length >= 2;

                    return isValid;

                });

    }


    private static ValiFieldText getCpfField(){

        return (ValiFieldText) new ValiFieldText().
                setEmptyAllowed(true).
                addCustomValidator(R.string.validation_error_invalid_cpf, data -> {

                    if (data == null || data.isEmpty())

                        return true;

                    return StringUtils.isValidCpf(data);

                });

    }


    private static ValiFieldLong getRgNumber(){

        return (ValiFieldLong) new ValiFieldLong().setEmptyAllowed(true);

    }


    private static ValiFieldText getBirthDateField(){

        return (ValiFieldText) new ValiFieldText().
                addNotEmptyValidator().
                addCustomValidator(R.string.validation_error_invalid_birthdate, StringUtils::stringCanBeAnBirthDate);

    }


    private static ValiFieldText getBirthPlaceField(){

        return (ValiFieldText) new ValiFieldText().
                addNotEmptyValidator().
                addCustomAsyncValidator(R.string.validation_error_invalid_city, birthPlace -> {

                    String[] parts = birthPlace.split("-");

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