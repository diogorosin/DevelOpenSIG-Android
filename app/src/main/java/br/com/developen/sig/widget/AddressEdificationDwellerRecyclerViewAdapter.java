package br.com.developen.sig.widget;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.database.AddressEdificationDwellerModel;

public class AddressEdificationDwellerRecyclerViewAdapter extends
        RecyclerView.Adapter<AddressEdificationDwellerRecyclerViewAdapter.AddressEdificationDwellerViewHolder> {


    private static final int FEMALE_EDIFICATION = 1;

    private static final int MALE_EDIFICATION = 2;


    private List<AddressEdificationDwellerModel> addressEdificationDwellers;


    public AddressEdificationDwellerRecyclerViewAdapter(List<AddressEdificationDwellerModel> addressEdificationDwellers) {

        this.addressEdificationDwellers = addressEdificationDwellers;

        setHasStableIds(true);

    }


    public AddressEdificationDwellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = 0;

        switch (viewType){

            case FEMALE_EDIFICATION: layout = R.layout.activity_map_bottom_sheet_female;
                break;

            case MALE_EDIFICATION: layout = R.layout.activity_map_bottom_sheet_male;
                break;

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new AddressEdificationDwellerViewHolder(view);

    }


    public int getItemViewType(int position) {

        switch (addressEdificationDwellers.get(position).getIndividual().getGender().getIdentifier()){

            case "F": return FEMALE_EDIFICATION;

            case "M": return MALE_EDIFICATION;

            default:

                return 0;

        }

    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onBindViewHolder(final AddressEdificationDwellerViewHolder holder, int position) {

        holder.addressEdificationDwellerModel = addressEdificationDwellers.get(position);

        holder.name.setText(holder.addressEdificationDwellerModel.getIndividual().getName());

    }


    public int getItemCount() {

        return addressEdificationDwellers.size();

    }


    public long getItemId(int position){

        return addressEdificationDwellers.get(position).hashCode();

    }


    public void setAddressEdificationDwellers(List<AddressEdificationDwellerModel> addressEdificationDwellers){

        this.addressEdificationDwellers = addressEdificationDwellers;

        notifyDataSetChanged();

    }


    public class AddressEdificationDwellerViewHolder extends RecyclerView.ViewHolder {

        private AddressEdificationDwellerModel addressEdificationDwellerModel;

        public TextView name;

        public AddressEdificationDwellerViewHolder(View view) {

            super(view);

            name = view.findViewById(R.id.activity_map_bottom_sheet_dweller_name);

        }

    }


}