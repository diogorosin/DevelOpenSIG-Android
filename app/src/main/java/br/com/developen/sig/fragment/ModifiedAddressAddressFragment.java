package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.developen.sig.R;
import br.com.developen.sig.databinding.FragmentModifiedAddressAddressBinding;
import br.com.developen.sig.repository.CityRepository;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.viewmodel.ModifiedAddressViewModel;
import br.com.developen.sig.widget.MyArrayAdapter;

public class ModifiedAddressAddressFragment extends Fragment {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";


    public static ModifiedAddressAddressFragment newInstance(Integer modifiedAddressIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressAddressFragment fragment = new ModifiedAddressAddressFragment();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        fragment.setArguments(args);

        return fragment;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentModifiedAddressAddressBinding binding = FragmentModifiedAddressAddressBinding.inflate(inflater);

        //City
        MyArrayAdapter cityAutocompleteAdapter = new MyArrayAdapter<>(
                requireActivity(),
                R.layout.fragment_modified_address_address_city_autocomplete,
                CityRepository.getInstance(DB.getInstance(App.getContext())).getCities());

        cityAutocompleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fragmentModifiedAddressAddressCityEdittext.setAdapter(cityAutocompleteAdapter);

        binding.setLifecycleOwner(requireActivity());

        ModifiedAddressViewModel.Factory factory = new ModifiedAddressViewModel.Factory(
                requireActivity().getApplication(),
                getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0));

        ModifiedAddressViewModel modifiedAddressViewModel = new ViewModelProvider(requireActivity(), factory).get(ModifiedAddressViewModel.class);

        binding.setModifiedAddressViewModel(modifiedAddressViewModel);

        return binding.getRoot();

    }


}