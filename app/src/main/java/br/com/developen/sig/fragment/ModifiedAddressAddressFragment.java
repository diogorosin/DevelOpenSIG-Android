package br.com.developen.sig.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.developen.sig.R;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.repository.CityRepository;
import br.com.developen.sig.repository.ModifiedAddressRepository;
import br.com.developen.sig.util.StringUtils;
import br.com.developen.sig.widget.MyArrayAdapter;
import br.com.developen.sig.widget.ValidableFragment;
import mk.webfactory.dz.maskededittext.MaskedEditText;

public class ModifiedAddressAddressFragment
        extends Fragment implements ValidableFragment, Validator.ValidationListener {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    @Order(0)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    private TextInputEditText thoroughfareEditText;

    private TextInputEditText numberEditText;

    @Order(1)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    private TextInputEditText districtEditText;

    private TextInputEditText complementEditText;

    @Order(2)
    @Length(trim = true, min = 9, messageResId = R.string.error_postal_code)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    private MaskedEditText postalCodeEditText;

    @Order(3)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    private AutoCompleteTextView cityEditText;


    private ModifiedAddressRepository modifiedAddressRepository;

    private ValidableFragment.Listener listener;

    private MyArrayAdapter cityAdapter;

    private Validator validator;


    public static ModifiedAddressAddressFragment newInstance(Integer modifiedAddressIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressAddressFragment fragment = new ModifiedAddressAddressFragment();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        fragment.setArguments(args);

        return fragment;

    }


    public void onResume() {

        super.onResume();

        modifiedAddressRepository = ViewModelProviders.of(this).get(ModifiedAddressRepository.class);

        modifiedAddressRepository.getDenomination(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0)).observe(this, denomination ->
                thoroughfareEditText.setText(denomination));

        modifiedAddressRepository.getNumber(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0)).observe(this, number ->
                numberEditText.setText(number));

        modifiedAddressRepository.getDistrict(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0)).observe(this, district ->
                districtEditText.setText(district));

        modifiedAddressRepository.getReference(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0)).observe(this, reference ->
                complementEditText.setText(reference));

        modifiedAddressRepository.getPostalCode(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0)).observe(this, postalCode ->{

            //BUG NO COMPONENTE. NECESSARIO LIMPAR.
            postalCodeEditText.setText("");

            postalCodeEditText.setText(postalCode == null ? null : StringUtils.leftPad(String.valueOf(postalCode), 8, '0'));

        });

        modifiedAddressRepository.getCity(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0)).observe(this, city ->
                cityEditText.setText(city == null ? null : city.toString()));

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_modified_address_address, container, false);

        thoroughfareEditText = v.findViewById(R.id.fragment_modified_address_address_thoroughfare_edittext);

        numberEditText = v.findViewById(R.id.fragment_modified_address_address_number_edittext);

        districtEditText = v.findViewById(R.id.fragment_modified_address_address_district_edittext);

        complementEditText = v.findViewById(R.id.fragment_modified_address_address_complement_edittext);

        postalCodeEditText = v.findViewById(R.id.fragment_modified_address_address_postal_code_edittext);

        cityEditText = v.findViewById(R.id.fragment_modified_address_address_city_edittext);

        cityAdapter = new MyArrayAdapter(Objects.requireNonNull(getActivity()),
                R.layout.fragment_modified_address_edification_dweller_individual_birthplace_autocomplete, new ArrayList());

        cityEditText.setAdapter(cityAdapter);

        CityRepository cityRepository = ViewModelProviders.of(this).get(CityRepository.class);

        cityRepository.getCities().observe(this, citiesModels -> {

            int index = cityEditText.getListSelection();

            cityAdapter.clear();

            for (CityModel cityModel : citiesModels)

                cityAdapter.add(cityModel);

            if (index != -1)

                cityEditText.setListSelection(index);

        });

        validator = new Validator(this);

        validator.setValidationListener(this);

        return v;

    }


    public void validate() {

        validator.validate();

    }


    public void onValidationSucceeded() {

        Map<Integer, Object> values = new HashMap<>();

        values.put(ModifiedAddressRepository.IDENTIFIER_PROPERTY, getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0));

        values.put(ModifiedAddressRepository.DENOMINATION_PROPERTY, thoroughfareEditText.getText().toString());

        values.put(ModifiedAddressRepository.NUMBER_PROPERTY, numberEditText.getText().toString());

        values.put(ModifiedAddressRepository.DISTRICT_PROPERTY, districtEditText.getText().toString());

        values.put(ModifiedAddressRepository.REFERENCE_PROPERTY, complementEditText.getText().toString());

        values.put(ModifiedAddressRepository.POSTAL_CODE_PROPERTY, Integer.valueOf(postalCodeEditText.getRawInput()));

        values.put(ModifiedAddressRepository.CITY_PROPERTY, cityEditText.getText().toString());

        try {

            modifiedAddressRepository.save(values);

            listener.onValidationSucceeded(this);

        } catch (CityNotFoundException e) {

            cityEditText.setError(e.getMessage());

            listener.onValidationFailed(this);

        }

    }


    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {

            View view = error.getView();

            String message = error.getCollatedErrorMessage(getActivity());

            if (view instanceof EditText) {

                ((EditText) view).setError(message);

            } else {

                Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

            }

        }

        listener.onValidationFailed(this);

    }


    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof ValidableFragment.Listener)

            listener = (ValidableFragment.Listener) context;

        else

            throw new RuntimeException(context.toString()
                    + " must implement ValidableFragment.Listener");

    }


    public void onDetach() {

        super.onDetach();

        listener = null;

    }


}