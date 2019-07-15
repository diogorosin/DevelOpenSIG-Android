package br.com.developen.sig.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.repository.ModifiedAddressEdificationRepository;
import br.com.developen.sig.widget.ModifiedAddressEdificationRecyclerViewAdapter;

public class ModifiedAddressEdificationFragment extends Fragment {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    private static final String ARG_COLUMNS = "ARG_COLUMNS";


    private ModifiedAddressEdificationRepository repository;

    private ModifiedAddressEdificationRecyclerViewAdapter recyclerViewAdapter;

    private EdificationFragmentListener fragmentListener;


    public static ModifiedAddressEdificationFragment newInstance(Integer modifiedAddressIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressEdificationFragment fragment = new ModifiedAddressEdificationFragment();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        args.putInt(ARG_COLUMNS, 3);

        fragment.setArguments(args);

        return fragment;

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_modified_address_edification_list, container, false);

        int columns = getArguments().getInt(ARG_COLUMNS);

        if (columns <= 1)

            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        else

            recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), columns));

        recyclerViewAdapter = new ModifiedAddressEdificationRecyclerViewAdapter(new ArrayList<>(), fragmentListener);

        recyclerView.setAdapter(recyclerViewAdapter);

        repository = ViewModelProviders.of(this).get(ModifiedAddressEdificationRepository.class);

        repository.getEdificationsOfModifiedAddress(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER)).observe(ModifiedAddressEdificationFragment.this, modifiedAddressEdifications -> recyclerViewAdapter.setModifiedAddressEdifications(modifiedAddressEdifications));

        return recyclerView;

    }


    public interface EdificationFragmentListener {

        void onEdificationClicked(ModifiedAddressEdificationModel modifiedAddressEdificationModel);

        void onEdificationLongClick(ModifiedAddressEdificationModel modifiedAddressEdificationModel);

    }


    public void onAttach(Context context) {

        super.onAttach(context);

        if (context instanceof EdificationFragmentListener)

            fragmentListener = (EdificationFragmentListener) context;

        else

            throw new RuntimeException(context.toString()
                    + " must implement EdificationFragmentListener");

    }


    public void onDetach() {

        super.onDetach();

        fragmentListener = null;

    }


}