package br.com.developen.sig.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import br.com.developen.sig.database.AddressModel;
import br.com.developen.sig.repository.AddressRepository;
import br.com.developen.sig.util.App;
import br.com.developen.sig.widget.AddressClusterItem;

public class AddressViewModel extends AndroidViewModel {


    //KEYS
    private Integer identifier;

    //REPOSITORY
    private AddressRepository repository;

    //FIELDS THAT NEED VALIDATION
    public final ObservableField<AddressClusterItem> selectedClusterItem = new ObservableField<>();

    //FIELDS THAT NOT NEED VALIDATION

    //FORM


    //FACTORY OF ModifiedAddressEdificationDwellerRepository
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final App application;

        private final Integer identifier;

        private final AddressRepository repository;

        public Factory(Application application, Integer identifier) {

            this.application = (App) application;

            this.repository = this.application.getAddressRepository();

            this.identifier = identifier;

        }

        public Factory(Application application) {

            this.application = (App) application;

            this.repository = this.application.getAddressRepository();

            this.identifier = null;

        }

        public <T extends ViewModel> T create(Class<T> modelClass) {

            return (T) new AddressViewModel(application, identifier, repository);

        }

    }


    public AddressViewModel(Application application){

        super(application);

        this.repository = App.getInstance().getAddressRepository();

        this.selectedClusterItem.set(null);

    }


    public AddressViewModel(Application application,
                            Integer identifier,
                            AddressRepository repository) {


        super(application);

        this.identifier = identifier;

        this.repository = repository;

        this.selectedClusterItem.set(null);

    }


    public LiveData<List<AddressModel>> getAddresses(){

        return repository.getAddresses();

    }


}