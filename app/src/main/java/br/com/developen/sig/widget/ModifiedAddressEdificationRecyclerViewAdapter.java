package br.com.developen.sig.widget;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.fragment.ModifiedAddressEdificationFragment;
import br.com.developen.sig.util.StringUtils;

public class ModifiedAddressEdificationRecyclerViewAdapter
        extends RecyclerView.Adapter<ModifiedAddressEdificationRecyclerViewAdapter.ModifiedAddressEdificationViewHolder> {


    public static final int HOUSE_EDIFICATION = 1;

    public static final int APARTMENT_EDIFICATION = 2;

    public static final int COMMERCIAL_ROOM_EDIFICATION = 3;

    public static final int FARM_EDIFICATION = 4;

    public static final int OTHER_EDIFICATION = 5;


    private List<ModifiedAddressEdificationModel> modifiedAddressEdifications;

    private ModifiedAddressEdificationFragment.EdificationFragmentListener fragmentListener;


    public ModifiedAddressEdificationRecyclerViewAdapter(List<ModifiedAddressEdificationModel> modifiedAddressEdifications, ModifiedAddressEdificationFragment.EdificationFragmentListener listener) {

        this.modifiedAddressEdifications = modifiedAddressEdifications;

        this.fragmentListener = listener;

        setHasStableIds(true);

    }


    public ModifiedAddressEdificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = 0;

        switch (viewType){

            case HOUSE_EDIFICATION: layout = R.layout.fragment_modified_address_edification_list_house;
                break;

            case APARTMENT_EDIFICATION: layout = R.layout.fragment_modified_address_edification_list_apartment;
                break;

            case COMMERCIAL_ROOM_EDIFICATION: layout = R.layout.fragment_modified_address_edification_list_commercial_room;
                break;

            case FARM_EDIFICATION: layout = R.layout.fragment_modified_address_edification_list_farm;
                break;

            case OTHER_EDIFICATION: layout = R.layout.fragment_modified_address_edification_list_other;
                break;

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ModifiedAddressEdificationViewHolder(view);

    }


    public int getItemViewType(int position) {

        switch (modifiedAddressEdifications.get(position).getType().getIdentifier()){

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
    public void onBindViewHolder(final ModifiedAddressEdificationViewHolder holder, int position) {

        holder.modifiedAddressEdificationModel = modifiedAddressEdifications.get(position);

        holder.type.setText(modifiedAddressEdifications.get(position).getType().getDenomination());

        if (modifiedAddressEdifications.get(position).getReference() != null){

            holder.reference.setText(modifiedAddressEdifications.get(position).getReference());

            holder.reference.setVisibility(View.VISIBLE);

        } else

            holder.reference.setVisibility(View.GONE);

        if (modifiedAddressEdifications.get(position).getDwellersCount() > 0){

            String dweller = String.format("%s", modifiedAddressEdifications.get(position).getFirstDwellerName());

            String count = "";

            if (modifiedAddressEdifications.get(position).getDwellersCount() == 2)

                count = " + 1 morador";

            else if (modifiedAddressEdifications.get(position).getDwellersCount() > 2)

                count = String.format(" + %d moradores", modifiedAddressEdifications.get(position).getDwellersCount() - 1);

            holder.dweller.setText(dweller + count);

        } else {

            holder.dweller.setText("Sem moradores.");

        }

        holder.from.setText("desde " + StringUtils.formatDate(modifiedAddressEdifications.get(position).getFrom()));

        holder.itemView.setOnClickListener(v -> {

            if (fragmentListener != null)

                fragmentListener.onEdificationClicked(holder.modifiedAddressEdificationModel);

        });

        holder.itemView.setOnLongClickListener(v -> {

            if (fragmentListener != null)

                fragmentListener.onEdificationLongClick(holder.modifiedAddressEdificationModel);

            return false;

        });

    }


    public int getItemCount() {

        return modifiedAddressEdifications.size();

    }


    public long getItemId(int position){

        return modifiedAddressEdifications.get(position).hashCode();

    }


    public void setModifiedAddressEdifications(List<ModifiedAddressEdificationModel> modifiedAddressEdifications){

        this.modifiedAddressEdifications = modifiedAddressEdifications;

        notifyDataSetChanged();

    }


    public class ModifiedAddressEdificationViewHolder extends RecyclerView.ViewHolder {

        public ModifiedAddressEdificationModel modifiedAddressEdificationModel;

        public TextView type;

        public TextView reference;

        public TextView dweller;

        public TextView from;

        public ModifiedAddressEdificationViewHolder(View view) {

            super(view);

            type = view.findViewById(R.id.fragment_modified_address_edification_list_type_textview);

            reference = view.findViewById(R.id.fragment_modified_address_edification_list_reference_textview);

            from = view.findViewById(R.id.fragment_modified_address_edification_list_from_textview);

            dweller = view.findViewById(R.id.fragment_modified_address_edification_list_dweller_textview);

        }

    }


}