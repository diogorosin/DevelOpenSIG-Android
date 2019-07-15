package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import br.com.developen.sig.R;
import br.com.developen.sig.repository.ModifiedAddressRepository;

public class ModifiedAddressAddressFragment extends Fragment {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";


    private ModifiedAddressRepository modifiedAddressRepository;

    private TextInputEditText thoroughfareEditText;

    private TextInputEditText numberEditText;

    private TextInputEditText districtEditText;

    private TextInputEditText complementEditText;

    private TextInputEditText cityEditText;

    private TextInputEditText stateEditText;

    private TextInputEditText countryEditText;


    public static ModifiedAddressAddressFragment newInstance(Integer modifiedAddressIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressAddressFragment fragment = new ModifiedAddressAddressFragment();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        fragment.setArguments(args);

        return fragment;

    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        modifiedAddressRepository = ViewModelProviders.of(getActivity()).get(ModifiedAddressRepository.class);

        modifiedAddressRepository.getModifiedAddress(
                getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER)).
                observe(getActivity(), modifiedAddressModel -> {

                    if (modifiedAddressModel != null){

                        thoroughfareEditText.setText(modifiedAddressModel.getDenomination());

                        numberEditText.setText(modifiedAddressModel.getNumber());

                        districtEditText.setText(modifiedAddressModel.getDistrict());

                        complementEditText.setText(modifiedAddressModel.getReference());

                        if (modifiedAddressModel.getCity() != null){

                            cityEditText.setText(modifiedAddressModel.getCity().getDenomination());

                            stateEditText.setText(modifiedAddressModel.getCity().getState().getDenomination());

                            countryEditText.setText(modifiedAddressModel.getCity().getState().getCountry().getDenomination());

                        }

                    }

                });

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_modified_address_address, container, false);

        thoroughfareEditText = v.findViewById(R.id.fragment_modified_address_address_thoroughfare_edittext);

        numberEditText = v.findViewById(R.id.fragment_modified_address_address_number_edittext);

        districtEditText = v.findViewById(R.id.fragment_modified_address_address_district_edittext);

        complementEditText = v.findViewById(R.id.fragment_modified_address_address_complement_edittext);

        cityEditText = v.findViewById(R.id.fragment_modified_address_address_city_edittext);

        stateEditText = v.findViewById(R.id.fragment_modified_address_address_state_edittext);

        countryEditText = v.findViewById(R.id.fragment_modified_address_address_country_edittext);

        return v;

    }


}