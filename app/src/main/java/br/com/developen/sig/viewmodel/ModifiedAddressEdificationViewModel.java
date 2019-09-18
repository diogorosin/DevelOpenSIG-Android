package br.com.developen.sig.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mlykotom.valifi.ValiFiForm;
import com.mlykotom.valifi.fields.ValiFieldText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.database.TypeModel;
import br.com.developen.sig.repository.ModifiedAddressEdificationRepository;
import br.com.developen.sig.util.App;

public class ModifiedAddressEdificationViewModel extends AndroidViewModel {


    //KEYS
    private Integer modifiedAddress, edification;

    //REPOSITORY
    private ModifiedAddressEdificationRepository repository;

    //FIELDS THAT NEED VALIDATION
    public final ValiFieldText reference = getReferenceField();

    //FIELDS THAT NOT NEED VALIDATION
    public final ObservableField<TypeModel> type = new ObservableField<>();

    public final ObservableBoolean active = new ObservableBoolean();

    //FORM
    public final ValiFiForm form = new ValiFiForm(reference);


    //FACTORY OF ModifiedAddressEdificationDwellerRepository
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final App application;

        private final Integer modifiedAddress;

        private final Integer edification;

        private final ModifiedAddressEdificationRepository repository;

        public Factory(Application application, Integer modifiedAddress, Integer edification) {

            this.application = (App) application;

            this.repository = this.application.getModifiedAddressEdificationRepository();

            this.modifiedAddress = modifiedAddress;

            this.edification = edification;

        }

        public <T extends ViewModel> T create(Class<T> modelClass) {

            return (T) new ModifiedAddressEdificationViewModel(application, modifiedAddress, edification, repository);

        }

    }


    public ModifiedAddressEdificationViewModel(Application application,
                                               Integer modifiedAddress,
                                               Integer edification,
                                               ModifiedAddressEdificationRepository repository) {


        super(application);

        this.modifiedAddress = modifiedAddress;

        this.edification = edification;

        this.repository = repository;

        ModifiedAddressEdificationModel modifiedAddressEdificationModel =
                this.repository.getModifiedAddressEdification(this.modifiedAddress, this.edification);

        reference.set(modifiedAddressEdificationModel.getReference());

        type.set(modifiedAddressEdificationModel.getType());

        active.set(modifiedAddressEdificationModel.getActive());

    }


    public LiveData<List<ModifiedAddressEdificationDwellerModel>> getDwellersOfModifiedAddressEdification(){

        return repository.getDwellersOfModifiedAddressEdification(this.modifiedAddress, this.edification);

    }


    public void save() {

        Map<Integer, Object> values = new HashMap<>();

        values.put(ModifiedAddressEdificationRepository.MODIFIED_ADDRESS_PROPERTY, this.modifiedAddress);

        values.put(ModifiedAddressEdificationRepository.EDIFICATION_PROPERTY, this.edification);

        values.put(ModifiedAddressEdificationRepository.TYPE_PROPERTY, this.type.get());

        values.put(ModifiedAddressEdificationRepository.REFERENCE_PROPERTY, this.reference.get());

        repository.save(values);

    }


    public void delete(){

        Map<Integer, Object> values = new HashMap<>();

        values.put(ModifiedAddressEdificationRepository.MODIFIED_ADDRESS_PROPERTY, this.modifiedAddress);

        values.put(ModifiedAddressEdificationRepository.EDIFICATION_PROPERTY, this.edification);

        repository.delete(values);

    }


    protected void onCleared() {

        form.destroy();

        super.onCleared();

    }


    private static ValiFieldText getReferenceField(){

        return new ValiFieldText().addMaxLengthValidator(10);

    }


}