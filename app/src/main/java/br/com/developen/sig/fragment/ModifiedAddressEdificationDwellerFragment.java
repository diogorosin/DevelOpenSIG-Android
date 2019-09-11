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
import br.com.developen.sig.viewmodel.ModifiedAddressEdificationViewModel;
import br.com.developen.sig.widget.ModifiedAddressEdificationDwellerRecyclerViewAdapter;

public class ModifiedAddressEdificationDwellerFragment extends Fragment {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    private static final String ARG_EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";


    private ModifiedAddressEdificationViewModel modifiedAddressEdificationViewModel;

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

        recyclerViewAdapter = new ModifiedAddressEdificationDwellerRecyclerViewAdapter(new ArrayList<>(), fragmentListener);

        recyclerView.setAdapter(recyclerViewAdapter);

        ModifiedAddressEdificationViewModel.Factory factory = new ModifiedAddressEdificationViewModel.Factory(
                requireActivity().getApplication(),
                getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, 0),
                getArguments().getInt(ARG_EDIFICATION_IDENTIFIER, 0));

        modifiedAddressEdificationViewModel = ViewModelProviders.of(requireActivity(), factory).get(ModifiedAddressEdificationViewModel.class);

        modifiedAddressEdificationViewModel.
                getDwellersOfModifiedAddressEdification().
                observe(requireActivity(),
                        modifiedAddressEdificationDwellers -> recyclerViewAdapter.setModifiedAddressEdificationDwellers(modifiedAddressEdificationDwellers));

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
                    + " must implement ModifiedAddressEdificationDwellerListFragmentListener");

    }


    public void onDetach() {

        super.onDetach();

        fragmentListener = null;

    }


}