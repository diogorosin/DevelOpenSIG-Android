package br.com.developen.sig.widget;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.database.AddressEdificationModel;

public class AddressEdificationRecyclerViewAdapter extends RecyclerView.Adapter<AddressEdificationRecyclerViewAdapter.AddressEdificationViewHolder> {


    private static final int HOUSE_EDIFICATION = 1;

    private static final int APARTMENT_EDIFICATION = 2;

    private static final int COMMERCIAL_ROOM_EDIFICATION = 3;

    private static final int FARM_EDIFICATION = 4;

    private static final int OTHER_EDIFICATION = 5;


    private List<AddressEdificationModel> addressEdifications;

    private EdificationClickListener listener;

    private int focusedItem = 0;


    public AddressEdificationRecyclerViewAdapter(List<AddressEdificationModel> addressEdifications,
                                                 EdificationClickListener listener) {

        this.addressEdifications = addressEdifications;

        this.listener = listener;

        setHasStableIds(true);

    }


    public AddressEdificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = 0;

        switch (viewType){

            case HOUSE_EDIFICATION: layout = R.layout.activity_map_bottom_sheet_house;
                break;

            case APARTMENT_EDIFICATION: layout = R.layout.activity_map_bottom_sheet_apartment;
                break;

            case COMMERCIAL_ROOM_EDIFICATION: layout = R.layout.activity_map_bottom_sheet_commercial_room;
                break;

            case FARM_EDIFICATION: layout = R.layout.activity_map_bottom_sheet_farm;
                break;

            case OTHER_EDIFICATION: layout = R.layout.activity_map_bottom_sheet_other;
                break;

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new AddressEdificationViewHolder(view);

    }


    public int getItemViewType(int position) {

        switch (addressEdifications.get(position).getType().getIdentifier()){

            case 1: return HOUSE_EDIFICATION;

            case 2: return APARTMENT_EDIFICATION;

            case 3: return COMMERCIAL_ROOM_EDIFICATION;

            case 4: return FARM_EDIFICATION;

            case 5: return OTHER_EDIFICATION;

            default:

                return 0;

        }

    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onBindViewHolder(final AddressEdificationViewHolder holder, int position) {

        holder.addressEdificationModel = addressEdifications.get(position);

        holder.itemView.setSelected(position==focusedItem);

        if (addressEdifications.get(position).getReference() != null){

            holder.reference.setText(addressEdifications.get(position).getReference());

            holder.reference.setVisibility(View.VISIBLE);

        } else {

            holder.reference.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(v -> {

            int lastFocusedItem = focusedItem;

            focusedItem = position;

            notifyItemChanged(lastFocusedItem);

            notifyItemChanged(focusedItem);

            if (listener != null)

                listener.onEdificationClicked(holder);

        });

        holder.itemView.setOnLongClickListener(v -> {

            if (listener != null)

                listener.onEdificationLongClick(holder);

            return false;

        });

    }


    public int getItemCount() {

        return addressEdifications.size();

    }


    public long getItemId(int position){

        return addressEdifications.get(position).hashCode();

    }


    public void setAddressEdifications(List<AddressEdificationModel> addressEdifications){

        this.addressEdifications = addressEdifications;

        notifyDataSetChanged();

    }


    public void resetFocusedItem(){

        focusedItem = 0;

    }


    public class AddressEdificationViewHolder extends RecyclerView.ViewHolder {

        public AddressEdificationModel addressEdificationModel;

        public TextView reference;

        public AddressEdificationViewHolder(View view) {

            super(view);

            reference = view.findViewById(R.id.activity_map_bottom_sheet_edification_reference);

        }

    }

    public interface EdificationClickListener {

        void onEdificationClicked(AddressEdificationRecyclerViewAdapter.AddressEdificationViewHolder addressEdificationModel);

        void onEdificationLongClick(AddressEdificationRecyclerViewAdapter.AddressEdificationViewHolder addressEdificationModel);

    }


}