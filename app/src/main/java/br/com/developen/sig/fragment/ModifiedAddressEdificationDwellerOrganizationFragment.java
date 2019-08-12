package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;
import br.com.developen.sig.widget.Editable;
import br.com.developen.sig.widget.validator.IndividualName;

public class ModifiedAddressEdificationDwellerOrganizationFragment
        extends Fragment
        implements Validator.ValidationListener, Editable {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";

    public static final String DWELLER_IDENTIFIER = "ARG_DWELLER_IDENTIFIER";


    private ModifiedAddressEdificationDwellerRepository modifiedAddressEdificationDwellerRepository;

    private Validator validator;


    @Order(0)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    @IndividualName
    private TextInputEditText denominationEditText;

    @Order(1)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    private TextInputEditText fancyNameEditText;


    public static ModifiedAddressEdificationDwellerOrganizationFragment newInstance(Integer modifiedAddressIdentifier,
                                                                                    Integer edificationIdentifier,
                                                                                    Integer dweller) {

        Bundle args = new Bundle();

        ModifiedAddressEdificationDwellerOrganizationFragment fragment = new ModifiedAddressEdificationDwellerOrganizationFragment();

        args.putInt(MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        args.putInt(EDIFICATION_IDENTIFIER, edificationIdentifier);

        args.putInt(DWELLER_IDENTIFIER, dweller);

        fragment.setArguments(args);

        return fragment;

    }


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }


    public void onResume() {

        super.onResume();

        modifiedAddressEdificationDwellerRepository = ViewModelProviders.of(this).get(ModifiedAddressEdificationDwellerRepository.class);

        modifiedAddressEdificationDwellerRepository.getModifiedAddressEdificationDweller(
                getArguments().getInt(MODIFIED_ADDRESS_IDENTIFIER),
                getArguments().getInt(EDIFICATION_IDENTIFIER),
                getArguments().getInt(DWELLER_IDENTIFIER)).
                observe(getActivity(), modifiedAddressEdificationDwellerModel -> {

                    if (modifiedAddressEdificationDwellerModel != null){

                        denominationEditText.setText(modifiedAddressEdificationDwellerModel.getNameOrDenomination());

                        fancyNameEditText.setText(modifiedAddressEdificationDwellerModel.getFancyName());

                    }

                });

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_modified_address_edification_dweller_organization, container, false);

        denominationEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_organization_denomination_edittext);

        fancyNameEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_organization_fancyname_edittext);

        validator = new Validator(this);

        validator.setValidationListener(this);

        return v;


    }


    public void onValidationSucceeded() {


        String denomination = denominationEditText.getText().toString();

        String fancyName = fancyNameEditText.getText().toString();

        modifiedAddressEdificationDwellerRepository.saveAsOrganization(denomination, fancyName);

        getActivity().finish();


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


    }


    public void save() {

        validator.validate();

    }


}