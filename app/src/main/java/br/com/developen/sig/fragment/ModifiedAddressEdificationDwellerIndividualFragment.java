package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import br.com.developen.sig.R;
import br.com.developen.sig.database.AgencyModel;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.StateModel;
import br.com.developen.sig.repository.AgencyRepository;
import br.com.developen.sig.repository.CityRepository;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;
import br.com.developen.sig.repository.StateRepository;
import br.com.developen.sig.util.StringUtils;
import br.com.developen.sig.widget.MyArrayAdapter;
import mk.webfactory.dz.maskededittext.MaskedEditText;

public class ModifiedAddressEdificationDwellerIndividualFragment extends Fragment {


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


    private TextInputEditText nameEditText;

    private TextInputEditText fatherNameEditText;

    private TextInputEditText motherNameEditText;

    private MaskedEditText cpfEditText;

    private TextInputEditText rgNumberEditText;

    private Spinner rgAgencySpinner;

    private Spinner rgStateSpinner;

    private AutoCompleteTextView birthPlaceEditText;

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

        initData();

    }


    public void initData(){

        modifiedAddressEdificationDwellerRepository = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(ModifiedAddressEdificationDwellerRepository.class);

        modifiedAddressEdificationDwellerRepository.getModifiedAddressEdificationDweller(
                getArguments().getInt(MODIFIED_ADDRESS_IDENTIFIER),
                getArguments().getInt(EDIFICATION_IDENTIFIER),
                getArguments().getInt(DWELLER_IDENTIFIER)).
                observe(getActivity(), modifiedAddressEdificationDwellerModel -> {

                    if (modifiedAddressEdificationDwellerModel != null){

                        nameEditText.setText(modifiedAddressEdificationDwellerModel.getNameOrDenomination());

                        fatherNameEditText.setText(modifiedAddressEdificationDwellerModel.getFatherName());

                        motherNameEditText.setText(modifiedAddressEdificationDwellerModel.getMotherName());

                        cpfEditText.setText(StringUtils.leftPad(String.valueOf(modifiedAddressEdificationDwellerModel.getCpf()), 11, '0'));

                        rgNumberEditText.setText(String.valueOf(modifiedAddressEdificationDwellerModel.getRgNumber()));

                        int position;

                        position = rgAgencySpinnerAdapter.getPosition(modifiedAddressEdificationDwellerModel.getRgAgency());

                        if (position != -1)

                            rgAgencySpinner.setSelection(position, false);

                        position = rgStateSpinnerAdapter.getPosition(modifiedAddressEdificationDwellerModel.getRgState());

                        if (position != -1)

                            rgStateSpinner.setSelection(position, false);

                        birthPlaceEditText.setText(modifiedAddressEdificationDwellerModel.getBirthPlace().toString());

                        position = birthPlaceAdapter.getPosition(modifiedAddressEdificationDwellerModel.getBirthPlace());

                        if (position != -1)

                            birthPlaceEditText.setListSelection(position);

                        birthDateEditText.setText(StringUtils.formatDate(modifiedAddressEdificationDwellerModel.getBirthDate()));

                        position = genderSpinnerAdapter.getPosition(modifiedAddressEdificationDwellerModel.getGender().equals("M") ? GENDER_MALE : GENDER_FEMALE);

                        if (position != -1)

                            genderSpinner.setSelection(position, false);

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


        agencyRepository = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(AgencyRepository.class);

        agencyRepository.getAgencies().observe(getActivity(), agencyModels -> {

            AgencyModel index = (AgencyModel) rgAgencySpinner.getSelectedItem();

            rgAgencySpinnerAdapter.clear();

            for (AgencyModel agencyModel : agencyModels)

                rgAgencySpinnerAdapter.add(agencyModel);

            if (index != null){

                int position = rgAgencySpinnerAdapter.getPosition(index);

                rgAgencySpinner.setSelection(position, false);

            }

        });


        stateRepository = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(StateRepository.class);

        stateRepository.getStates().observe(getActivity(), stateModels -> {

            StateModel index = (StateModel) rgStateSpinner.getSelectedItem();

            rgStateSpinnerAdapter.clear();

            for (StateModel stateModel : stateModels)

                rgStateSpinnerAdapter.add(stateModel);

            if (index != null){

                int position = rgStateSpinnerAdapter.getPosition(index);

                rgStateSpinner.setSelection(position, false);

            }

        });


        cityRepository = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(CityRepository.class);

        cityRepository.getCities().observe(getActivity(), citiesModels -> {

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


        return v;

    }


}