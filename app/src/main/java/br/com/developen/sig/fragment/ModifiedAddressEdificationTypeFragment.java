package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import br.com.developen.sig.R;
import br.com.developen.sig.database.TypeModel;
import br.com.developen.sig.repository.ModifiedAddressEdificationRepository;
import br.com.developen.sig.repository.TypeRepository;
import br.com.developen.sig.task.UpdateReferenceOfModifiedAddressEdificationAsyncTask;
import br.com.developen.sig.task.UpdateTypeOfModifiedAddressEdificationAsyncTask;

public class ModifiedAddressEdificationTypeFragment extends Fragment {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    private static final String ARG_EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";


    private ModifiedAddressEdificationRepository modifiedAddressEdificationRepository;

    private TypeRepository typeRepository;

    private ArrayAdapter typeSpinnerAdapter;

    private Spinner typeSpinner;

    private TextInputEditText referenceEditText;


    public static ModifiedAddressEdificationTypeFragment newInstance(Integer modifiedAddressIdentifier, Integer edificationIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressEdificationTypeFragment fragment = new ModifiedAddressEdificationTypeFragment();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        args.putInt(ARG_EDIFICATION_IDENTIFIER, edificationIdentifier);

        fragment.setArguments(args);

        return fragment;

    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    public void onResume(){

        super.onResume();

        modifiedAddressEdificationRepository = ViewModelProviders.of(this).get(ModifiedAddressEdificationRepository.class);

        modifiedAddressEdificationRepository.getTypeOfModifiedAddressEdification(
                getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER),
                getArguments().getInt(ARG_EDIFICATION_IDENTIFIER)).
                observe(this, typeOfModifiedAddressEdification -> {

                    TypeModel typeModel = new TypeModel();

                    typeModel.setIdentifier(typeOfModifiedAddressEdification);

                    int position = typeSpinnerAdapter.getPosition(typeModel);

                    if (position != -1)

                        typeSpinner.setSelection(position, false);

                });

        modifiedAddressEdificationRepository.getReferenceOfModifiedAddressEdification(
                getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER),
                getArguments().getInt(ARG_EDIFICATION_IDENTIFIER)).
                observe(this, referenceOfModifiedAddressEdification -> {

                    if (referenceOfModifiedAddressEdification != null) {

                        if (!referenceOfModifiedAddressEdification.equals(referenceEditText.getText().toString()))

                            referenceEditText.setText(referenceOfModifiedAddressEdification);

                    } else {

                        if (referenceEditText.getText().toString() != null)

                            referenceEditText.setText(null);

                    }

                });

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_modified_address_edification_type, container, false);

        typeSpinner = v.findViewById(R.id.fragment_modified_address_edification_type_type_spinner);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new UpdateTypeOfModifiedAddressEdificationAsyncTask().
                        execute(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER),
                                getArguments().getInt(ARG_EDIFICATION_IDENTIFIER),
                                ((TypeModel)typeSpinnerAdapter.getItem(position)).getIdentifier());

            }

            public void onNothingSelected(AdapterView<?> parent) {}

        });

        typeSpinnerAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()),
                R.layout.fragment_modified_address_edification_type_spinner,
                new ArrayList<>());

        typeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(typeSpinnerAdapter);

        typeRepository = ViewModelProviders.of(this).get(TypeRepository.class);

        typeRepository.getTypes().observe(this, typeModels -> {

            TypeModel index = (TypeModel) typeSpinner.getSelectedItem();

            typeSpinnerAdapter.clear();

            for (TypeModel typeModel : typeModels)

                typeSpinnerAdapter.add(typeModel);

            if (index != null){

                int position = typeSpinnerAdapter.getPosition(index);

                typeSpinner.setSelection(position, false);

            }

        });

        referenceEditText = v.findViewById(R.id.fragment_modified_address_edification_type_reference_edittext);

        referenceEditText.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                new UpdateReferenceOfModifiedAddressEdificationAsyncTask()
                        .execute(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER),
                                getArguments().getInt(ARG_EDIFICATION_IDENTIFIER),
                                String.valueOf(s));

            }

            public void afterTextChanged(Editable s) {}

        });

        return v;

    }


}