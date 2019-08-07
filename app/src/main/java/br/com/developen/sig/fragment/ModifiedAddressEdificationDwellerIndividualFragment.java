package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.com.developen.sig.R;
import br.com.developen.sig.database.AgencyModel;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.StateModel;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.repository.AgencyRepository;
import br.com.developen.sig.repository.CityRepository;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;
import br.com.developen.sig.repository.StateRepository;
import br.com.developen.sig.util.StringUtils;
import br.com.developen.sig.widget.Editable;
import br.com.developen.sig.widget.MyArrayAdapter;
import br.com.developen.sig.widget.validator.Birthdate;
import br.com.developen.sig.widget.validator.CPF;
import br.com.developen.sig.widget.validator.IndividualName;
import mk.webfactory.dz.maskededittext.MaskedEditText;

public class ModifiedAddressEdificationDwellerIndividualFragment
        extends Fragment
        implements Validator.ValidationListener, Editable {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";

    public static final String DWELLER_IDENTIFIER = "ARG_DWELLER_IDENTIFIER";

    public static final String GENDER_FEMALE = "Feminino";

    public static final String GENDER_MALE = "Masculino";

    public static final String[] genderOptions = new String[]{GENDER_FEMALE, GENDER_MALE};


    private ModifiedAddressEdificationDwellerRepository modifiedAddressEdificationDwellerRepository;

    private AgencyRepository agencyRepository;

    private StateRepository stateRepository;

    private CityRepository cityRepository;

    private Validator validator;


    @Order(0)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    @IndividualName
    private TextInputEditText nameEditText;

    @Order(1)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    @IndividualName
    private TextInputEditText motherNameEditText;

    @Order(2)
    @IndividualName
    private TextInputEditText fatherNameEditText;

    @Order(3)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    @CPF
    private MaskedEditText cpfEditText;

    @Order(4)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    private TextInputEditText rgNumberEditText;

    private Spinner rgAgencySpinner;

    private Spinner rgStateSpinner;

    @Order(5)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    private AutoCompleteTextView birthPlaceEditText;

    @Order(5)
    @NotEmpty(trim = true, messageResId = R.string.error_field_required)
    @Birthdate
    private MaskedEditText birthDateEditText;

    private Spinner genderSpinner;


    private ArrayAdapter rgAgencySpinnerAdapter;

    private ArrayAdapter rgStateSpinnerAdapter;

    private MyArrayAdapter birthPlaceAdapter;

    private ArrayAdapter genderSpinnerAdapter;


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
                observe(this, modifiedAddressEdificationDwellerModel -> {

                    if (modifiedAddressEdificationDwellerModel != null){

                        nameEditText.setText(modifiedAddressEdificationDwellerModel.getNameOrDenomination());

                        fatherNameEditText.setText(modifiedAddressEdificationDwellerModel.getFatherName());

                        motherNameEditText.setText(modifiedAddressEdificationDwellerModel.getMotherName());

                        //CPF
                        if (modifiedAddressEdificationDwellerModel.getCpf()!=null && modifiedAddressEdificationDwellerModel.getCpf() > 0)

                            cpfEditText.setText(StringUtils.leftPad(String.valueOf(modifiedAddressEdificationDwellerModel.getCpf()), 11, '0'));

                        //NUMERO DO RG
                        if (modifiedAddressEdificationDwellerModel.getRgNumber()!=null && modifiedAddressEdificationDwellerModel.getRgNumber() > 0)

                            rgNumberEditText.setText(String.valueOf(modifiedAddressEdificationDwellerModel.getRgNumber()));

                        int position;

                        //ORGAO EXPEDIDOR DO RG
                        if (modifiedAddressEdificationDwellerModel.getRgAgency() != null && modifiedAddressEdificationDwellerModel.getRgAgency().getIdentifier() > 0) {

                            position = rgAgencySpinnerAdapter.getPosition(modifiedAddressEdificationDwellerModel.getRgAgency());

                            if (position != -1)

                                rgAgencySpinner.setSelection(position, false);

                        }

                        //ESTADO DO RG
                        if (modifiedAddressEdificationDwellerModel.getRgState() != null && modifiedAddressEdificationDwellerModel.getRgState().getIdentifier() > 0) {

                            position = rgStateSpinnerAdapter.getPosition(modifiedAddressEdificationDwellerModel.getRgState());

                            if (position != -1)

                                rgStateSpinner.setSelection(position, false);

                        }

                        //LOCAL DE NASCIMENTO
                        birthPlaceEditText.setText(modifiedAddressEdificationDwellerModel.getBirthPlace() == null ? null : modifiedAddressEdificationDwellerModel.getBirthPlace().toString());

                        //DATA DE NASCIMENTO
                        birthDateEditText.setText(modifiedAddressEdificationDwellerModel.getBirthDate() == null ? null : StringUtils.formatDate(modifiedAddressEdificationDwellerModel.getBirthDate()));

                        //GENERO
                        if (modifiedAddressEdificationDwellerModel.getGender() != null) {

                            position = genderSpinnerAdapter.getPosition(modifiedAddressEdificationDwellerModel.getGender().equals("M") ? GENDER_MALE : GENDER_FEMALE);

                            if (position != -1)

                                genderSpinner.setSelection(position, false);

                        }

                    }

                });

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_modified_address_edification_dweller_individual, container, false);

        nameEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_name_edittext);

        fatherNameEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_fathername_edittext);

        motherNameEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_mothername_edittext);

        cpfEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_cpf_edittext);

        rgNumberEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_rgnumber_edittext);


        rgAgencySpinner = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_rgagency_spinner);

        rgAgencySpinnerAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()),
                R.layout.fragment_modified_address_edification_dweller_individual_rg_agency_spinner,
                new ArrayList<>());

        rgAgencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rgAgencySpinner.setAdapter(rgAgencySpinnerAdapter);


        rgStateSpinner = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_rgstate_spinner);

        rgStateSpinnerAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()),
                R.layout.fragment_modified_address_edification_dweller_individual_rg_state_spinner,
                new ArrayList<>());

        rgStateSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        rgStateSpinner.setAdapter(rgStateSpinnerAdapter);


        birthPlaceEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_birthplace_edittext);

        birthPlaceAdapter = new MyArrayAdapter(Objects.requireNonNull(getActivity()), R.layout.fragment_modified_address_edification_dweller_individual_birthplace_autocomplete, new ArrayList<>());

        birthPlaceEditText.setAdapter(birthPlaceAdapter);


        birthDateEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_birthdate_edittext);

        genderSpinner = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_gender_spinner);


        agencyRepository = ViewModelProviders.of(this).get(AgencyRepository.class);

        agencyRepository.getAgencies().observe(this, agencyModels -> {

            AgencyModel index = (AgencyModel) rgAgencySpinner.getSelectedItem();

            rgAgencySpinnerAdapter.clear();

            for (AgencyModel agencyModel : agencyModels)

                rgAgencySpinnerAdapter.add(agencyModel);

            if (index != null){

                int position = rgAgencySpinnerAdapter.getPosition(index);

                rgAgencySpinner.setSelection(position, false);

            }

        });


        stateRepository = ViewModelProviders.of(this).get(StateRepository.class);

        stateRepository.getStates().observe(this, stateModels -> {

            StateModel index = (StateModel) rgStateSpinner.getSelectedItem();

            rgStateSpinnerAdapter.clear();

            for (StateModel stateModel : stateModels)

                rgStateSpinnerAdapter.add(stateModel);

            if (index != null){

                int position = rgStateSpinnerAdapter.getPosition(index);

                rgStateSpinner.setSelection(position, false);

            }

        });


        cityRepository = ViewModelProviders.of(this).get(CityRepository.class);

        cityRepository.getCities().observe(this, citiesModels -> {

            int index = birthPlaceEditText.getListSelection();

            birthPlaceAdapter.clear();

            for (CityModel cityModel : citiesModels)

                birthPlaceAdapter.add(cityModel);

            if (index != -1)

                birthPlaceEditText.setListSelection(index);

        });


        genderSpinner = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_gender_spinner);

        genderSpinnerAdapter = new ArrayAdapter<>(
                Objects.requireNonNull(getActivity()),
                R.layout.fragment_modified_address_edification_dweller_individual_gender_spinner,
                Arrays.asList(genderOptions));

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderSpinner.setAdapter(genderSpinnerAdapter);


        validator = new Validator(this);

        validator.setValidationListener(this);


        return v;

    }


    public void onValidationSucceeded() {


        String name = nameEditText.getText().toString();

        String motherName = motherNameEditText.getText().toString();

        String fatherName = fatherNameEditText.getText().toString();

        Long cpf = Long.valueOf(cpfEditText.getRawInput());

        Long rgNumber = Long.valueOf(rgNumberEditText.getText().toString());

        Integer rgAgency = ((AgencyModel) rgAgencySpinner.getSelectedItem()).getIdentifier();

        Integer rgState = ((StateModel) rgStateSpinner.getSelectedItem()).getIdentifier();

        String birthPlace = birthPlaceEditText.getText().toString();

        Date birthDate = StringUtils.stringToDate(birthDateEditText.getText().toString());

        String gender = (String) genderSpinner.getSelectedItem();

        gender = gender.equals(GENDER_FEMALE) ? "F" : "M";


        try {

            modifiedAddressEdificationDwellerRepository.saveAsIndividual(
                    name,
                    motherName,
                    fatherName,
                    cpf,
                    rgNumber,
                    rgAgency,
                    rgState,
                    birthPlace,
                    birthDate,
                    gender);

            getActivity().finish();

        } catch (CityNotFoundException e) {

            birthPlaceEditText.setError(e.getMessage());

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

    }


    public void save() {

        validator.validate();

    }


}