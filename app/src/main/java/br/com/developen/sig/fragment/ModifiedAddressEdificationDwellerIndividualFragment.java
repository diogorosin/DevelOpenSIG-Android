package br.com.developen.sig.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.textfield.TextInputEditText;

import br.com.developen.sig.R;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;

public class ModifiedAddressEdificationDwellerIndividualFragment extends Fragment {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";

    public static final String DWELLER_IDENTIFIER = "ARG_DWELLER_IDENTIFIER";


    private ModifiedAddressEdificationDwellerRepository modifiedAddressEdificationDwellerRepository;

    private TextInputEditText nameEditText;

    private TextInputEditText fatherNameEditText;

    private TextInputEditText motherNameEditText;

    private TextInputEditText cpfEditText;

    private TextInputEditText rgNumberEditText;

    private TextInputEditText rgAgencyEditText;

    private TextInputEditText rgStateEditText;

    private TextInputEditText birthPlaceEditText;

    private TextInputEditText birthDateEditText;

    private TextInputEditText genderEditText;


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


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_modified_address_edification_dweller_individual, container, false);

        nameEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_name_edittext);

        fatherNameEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_fathername_edittext);

        motherNameEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_mothername_edittext);

        cpfEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_cpf_edittext);

        rgNumberEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_rgnumber_edittext);

        rgAgencyEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_rgagency_edittext);

        rgStateEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_rgstate_edittext);

        birthPlaceEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_birthplace_edittext);

        birthDateEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_birthdate_edittext);

        genderEditText = v.findViewById(R.id.fragment_modified_address_edification_dweller_individual_gender_edittext);

        modifiedAddressEdificationDwellerRepository = ViewModelProviders.of(getActivity()).get(ModifiedAddressEdificationDwellerRepository.class);

        modifiedAddressEdificationDwellerRepository.getModifiedAddressEdificationDweller(
                getArguments().getInt(MODIFIED_ADDRESS_IDENTIFIER),
                getArguments().getInt(EDIFICATION_IDENTIFIER),
                getArguments().getInt(DWELLER_IDENTIFIER)).
                observe(getActivity(), modifiedAddressEdificationDwellerModel -> {

                    if (modifiedAddressEdificationDwellerModel != null){

                        nameEditText.setText(modifiedAddressEdificationDwellerModel.getNameOrDenomination());

                        fatherNameEditText.setText(modifiedAddressEdificationDwellerModel.getFatherName());

                        motherNameEditText.setText(modifiedAddressEdificationDwellerModel.getMotherName());

                        cpfEditText.setText(modifiedAddressEdificationDwellerModel.getCpf().toString());

                        rgNumberEditText.setText(modifiedAddressEdificationDwellerModel.getRgNumber().toString());

                        rgAgencyEditText.setText(modifiedAddressEdificationDwellerModel.getRgAgency().getDenomination());

                        rgStateEditText.setText(modifiedAddressEdificationDwellerModel.getRgState().getDenomination());

                        birthPlaceEditText.setText(modifiedAddressEdificationDwellerModel.getBirthPlace().getDenomination());

                        birthDateEditText.setText(modifiedAddressEdificationDwellerModel.getBirthDate().toString());

                        genderEditText.setText(modifiedAddressEdificationDwellerModel.getGender());

                    }

                });

        return v;

    }


}