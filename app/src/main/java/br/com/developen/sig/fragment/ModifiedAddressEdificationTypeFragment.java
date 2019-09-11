package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import br.com.developen.sig.R;
import br.com.developen.sig.databinding.FragmentModifiedAddressEdificationTypeBinding;
import br.com.developen.sig.repository.TypeRepository;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.DB;
import br.com.developen.sig.viewmodel.ModifiedAddressEdificationViewModel;

public class ModifiedAddressEdificationTypeFragment extends Fragment {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    private static final String ARG_EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";


    public static ModifiedAddressEdificationTypeFragment newInstance(Integer modifiedAddressIdentifier, Integer edificationIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressEdificationTypeFragment fragment = new ModifiedAddressEdificationTypeFragment();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        args.putInt(ARG_EDIFICATION_IDENTIFIER, edificationIdentifier);

        fragment.setArguments(args);

        return fragment;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentModifiedAddressEdificationTypeBinding binding = FragmentModifiedAddressEdificationTypeBinding.inflate(inflater);

        ArrayAdapter typeSpinnerAdapter = new ArrayAdapter<>(
                requireActivity(),
                R.layout.fragment_modified_address_edification_type_spinner,
                TypeRepository.getInstance(DB.getInstance(App.getContext())).getList());

        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.fragmentModifiedAddressEdificationTypeTypeSpinner.setAdapter(typeSpinnerAdapter);

        binding.setLifecycleOwner(requireActivity());

        ModifiedAddressEdificationViewModel.Factory factory = new ModifiedAddressEdificationViewModel.Factory(
                requireActivity().getApplication(),
                getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0),
                getArguments().getInt(ARG_EDIFICATION_IDENTIFIER, 0));

        ModifiedAddressEdificationViewModel modifiedAddressEdificationViewModel = new ViewModelProvider(requireActivity(), factory).get(ModifiedAddressEdificationViewModel.class);

        binding.setModifiedAddressEdificationViewModel(modifiedAddressEdificationViewModel);

        return binding.getRoot();

    }


}