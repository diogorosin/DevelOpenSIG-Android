package br.com.developen.sig.viewmodel;

import android.app.Application;

import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import br.com.developen.sig.database.MarkerModel;
import br.com.developen.sig.repository.MarkerRepository;
import br.com.developen.sig.util.App;
import br.com.developen.sig.widget.MarkerClusterItem;

public class MarkerViewModel extends AndroidViewModel {


    //KEYS

    //REPOSITORY
    private MarkerRepository repository;

    //FIELDS THAT NEED VALIDATION
    public final ObservableField<MarkerClusterItem> selectedClusterItem = new ObservableField<>();

    //FIELDS THAT NOT NEED VALIDATION

    //FORM


    //FACTORY OF ModifiedAddressEdificationDwellerRepository
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final App application;

        private final MarkerRepository repository;

        public Factory(Application application) {

            this.application = (App) application;

            this.repository = this.application.getMarkerRepository();

        }

        public <T extends ViewModel> T create(Class<T> modelClass) {

            return (T) new MarkerViewModel(application, repository);

        }

    }


    public MarkerViewModel(Application application){

        super(application);

        this.repository = App.getInstance().getMarkerRepository();

        this.selectedClusterItem.set(null);

    }


    public MarkerViewModel(Application application,
                           MarkerRepository repository) {


        super(application);

        this.repository = repository;

        this.selectedClusterItem.set(null);

    }


    public LiveData<List<MarkerModel>> getMarkers(){

        return repository.getMarkers();

    }


}