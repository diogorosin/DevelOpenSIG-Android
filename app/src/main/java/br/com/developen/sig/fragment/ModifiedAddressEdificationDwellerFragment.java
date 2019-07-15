package br.com.developen.sig.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;
import br.com.developen.sig.widget.ModifiedAddressEdificationDwellerRecyclerViewAdapter;

public class ModifiedAddressEdificationDwellerFragment extends Fragment {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    private static final String ARG_EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";


    private ModifiedAddressEdificationDwellerRepository repository;

    private ModifiedAddressEdificationDwellerRecyclerViewAdapter recyclerViewAdapter;

    private ModifiedAddressEdificationDwellerFragmentListener fragmentListener;


    public static ModifiedAddressEdificationDwellerFragment newInstance(Integer modifiedAddressIdentifier, Integer edificationIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressEdificationDwellerFragment fragment = new ModifiedAddressEdificationDwellerFragment();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        args.putInt(ARG_EDIFICATION_IDENTIFIER, edificationIdentifier);

        fragment.setArguments(args);

        return fragment;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_modified_address_edification_dweller_list, container, false);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerViewAdapter = new ModifiedAddressEdificationDwellerRecyclerViewAdapter(new ArrayList<ModifiedAddressEdificationDwellerModel>(), fragmentListener);

        recyclerView.setAdapter(recyclerViewAdapter);

        repository = ViewModelProviders.of(this).get(ModifiedAddressEdificationDwellerRepository.class);

        repository.getDwellersOfModifiedAddressEdification(
                getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER),
                getArguments().getInt(ARG_EDIFICATION_IDENTIFIER)).observe(ModifiedAddressEdificationDwellerFragment.this,
                modifiedAddressEdifications -> recyclerViewAdapter.setModifiedAddressEdificationDwellers(modifiedAddressEdifications));

        return recyclerView;

    }


    public interface ModifiedAddressEdificationDwellerFragmentListener {

        void onDwellerClicked(ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel);

        void onDwellerLongClick(ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel);

    }


    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof ModifiedAddressEdificationDwellerFragmentListener)

            fragmentListener = (ModifiedAddressEdificationDwellerFragmentListener) context;

        else

            throw new RuntimeException(context.toString()
                    + " must implement ModifiedAddressEdificationDwellerFragmentListener");

    }


    public void onDetach() {

        super.onDetach();

        fragmentListener = null;

    }


}