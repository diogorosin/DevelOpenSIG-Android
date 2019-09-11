package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.developen.sig.R;
import br.com.developen.sig.databinding.FragmentModifiedAddressEdificationDwellerBinding;
import br.com.developen.sig.repository.AgencyRepository;
import br.com.developen.sig.repository.CityRepository;
import br.com.developen.sig.repository.GenderRepository;
import br.com.developen.sig.repository.StateRepository;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.viewmodel.ModifiedAddressEdificationDwellerViewModel;
import br.com.developen.sig.widget.MyArrayAdapter;

public class ModifiedAddressEdificationDwellerIndividualFragment extends Fragment {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";

    public static final String DWELLER_IDENTIFIER = "ARG_DWELLER_IDENTIFIER";


    public static ModifiedAddressEdificationDwellerIndividualFragment newInstance(Integer modifiedAddressIdentifier,
                                                                                  Integer edificationIdentifier,
                                                                                  Integer dweller) {

        Bundle args = new Bundle();

        ModifiedAddressEdificationDwellerIndividualFragment fragment = new ModifiedAddressEdificationDwellerIndividualFragment();

        args.putInt(MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        args.putInt(EDIFICATION_IDENTIFIER, edificationIdentifier);

        args.putInt(DWELLER_IDENTIFIER, dweller);

        fragment.setArguments(args);

        return fragment;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentModifiedAddressEdificationDwellerBinding binding = FragmentModifiedAddressEdificationDwellerBinding.inflate(inflater);

        //RG State
        ArrayAdapter rgStateSpinnerAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.fragment_modified_address_edification_dweller_rg_state_spinner,
                StateRepository.getInstance(DB.getInstance(App.getContext())).getStatesOfCountry(1));

        rgStateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fragmentModifiedAddressEdificationDwellerRgstateSpinner.
                setAdapter(rgStateSpinnerAdapter);

        //RG Agency
        ArrayAdapter rgAgencySpinnerAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.fragment_modified_address_edification_dweller_rg_agency_spinner,
                AgencyRepository.getInstance(DB.getInstance(App.getContext())).getAgencies());

        rgAgencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fragmentModifiedAddressEdificationDwellerRgagencySpinner.
                setAdapter(rgAgencySpinnerAdapter);

        //Birth Place
        MyArrayAdapter birthPlaceAutocompleteAdapter = new MyArrayAdapter<>(
                requireActivity(),
                R.layout.fragment_modified_address_edification_dweller_birthplace_autocomplete,
                CityRepository.getInstance(DB.getInstance(App.getContext())).getCities());

        birthPlaceAutocompleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fragmentModifiedAddressEdificationDwellerBirthplaceEdittext.
                setAdapter(birthPlaceAutocompleteAdapter);

        //Gender
        ArrayAdapter genderSpinnerAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.fragment_modified_address_edification_dweller_gender_spinner,
                GenderRepository.getInstance().getGenders());

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fragmentModifiedAddressEdificationDwellerGenderSpinner.
                setAdapter(genderSpinnerAdapter);

        binding.setLifecycleOwner(requireActivity());

        ModifiedAddressEdificationDwellerViewModel.Factory factory = new ModifiedAddressEdificationDwellerViewModel.Factory(
                requireActivity().getApplication(),
                getArguments().getInt(MODIFIED_ADDRESS_IDENTIFIER, 0),
                getArguments().getInt(EDIFICATION_IDENTIFIER, 0),
                getArguments().getInt(DWELLER_IDENTIFIER, 0));

        ModifiedAddressEdificationDwellerViewModel modifiedAddressEdificationDwellerViewModel = new ViewModelProvider(requireActivity(), factory).get(ModifiedAddressEdificationDwellerViewModel.class);

        binding.setModifiedAddressEdificationDwellerViewModel(modifiedAddressEdificationDwellerViewModel);

        return binding.getRoot();

    }


}