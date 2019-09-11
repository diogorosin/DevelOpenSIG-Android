package br.com.developen.sig.widget;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerFragment;
import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.util.StringUtils;

public class ModifiedAddressEdificationDwellerRecyclerViewAdapter
        extends RecyclerView.Adapter<ModifiedAddressEdificationDwellerRecyclerViewAdapter.ModifiedAddressEdificationDwellerViewHolder> {


    public static final int FEMALE_GENDER = 1;

    public static final int MALE_GENDER = 2;


    private List<ModifiedAddressEdificationDwellerModel> modifiedAddressEdificationDwellers;

    private ModifiedAddressEdificationDwellerFragment.ModifiedAddressEdificationDwellerFragmentListener fragmentListener;


    public ModifiedAddressEdificationDwellerRecyclerViewAdapter(List<ModifiedAddressEdificationDwellerModel> modifiedAddressEdifications, ModifiedAddressEdificationDwellerFragment.ModifiedAddressEdificationDwellerFragmentListener listener) {

        this.modifiedAddressEdificationDwellers = modifiedAddressEdifications;

        this.fragmentListener = listener;

        setHasStableIds(true);

    }


    public ModifiedAddressEdificationDwellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = 0;

        switch (viewType){

            case FEMALE_GENDER: layout = R.layout.fragment_modified_address_edification_dweller_list_female;
                break;

            case MALE_GENDER: layout = R.layout.fragment_modified_address_edification_dweller_list_male;
                break;

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new ModifiedAddressEdificationDwellerViewHolder(view);

    }


    public int getItemViewType(int position) {

        switch (modifiedAddressEdificationDwellers.get(position).getGender().getIdentifier()){

            case "F": return FEMALE_GENDER;

            case "M": return MALE_GENDER;

            default:

                return 0;

        }

    }


    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(final ModifiedAddressEdificationDwellerViewHolder holder, int position) {

        holder.modifiedAddressEdificationDwellerModel = modifiedAddressEdificationDwellers.get(position);

        holder.name.setText(modifiedAddressEdificationDwellers.get(position).getName());

        holder.cpf.setText(StringUtils.formatCpfWithPrefix(modifiedAddressEdificationDwellers.get(position).getCpf()));

        holder.rg.setText(StringUtils.formatRgWithPrefix(
                modifiedAddressEdificationDwellers.get(position).getRgNumber(),
                modifiedAddressEdificationDwellers.get(position).getRgAgency(),
                modifiedAddressEdificationDwellers.get(position).getRgState()));

        holder.from.setText("desde " + StringUtils.formatDate(modifiedAddressEdificationDwellers.get(position).getFrom()));

        holder.itemView.setOnClickListener(v -> {

            if (fragmentListener != null)

                fragmentListener.onDwellerClicked(holder.modifiedAddressEdificationDwellerModel);

        });

        holder.itemView.setOnLongClickListener(v -> {

            if (fragmentListener != null)

                fragmentListener.onDwellerLongClick(holder.modifiedAddressEdificationDwellerModel);

            return false;

        });

    }


    public int getItemCount() {

        return modifiedAddressEdificationDwellers.size();

    }


    public long getItemId(int position){

        return modifiedAddressEdificationDwellers.get(position).hashCode();

    }


    public void setModifiedAddressEdificationDwellers(List<ModifiedAddressEdificationDwellerModel> modifiedAddressEdificationDwellers){

        this.modifiedAddressEdificationDwellers = modifiedAddressEdificationDwellers;

        notifyDataSetChanged();

    }


    public class ModifiedAddressEdificationDwellerViewHolder extends RecyclerView.ViewHolder {

        public ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel;

        public TextView name;

        public TextView cpf;

        public TextView rg;

        public TextView from;

        public ModifiedAddressEdificationDwellerViewHolder(View view) {

            super(view);

            name = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_name_textview);

            cpf = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_cpf_textview);

            rg = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_rg_textview);

            from = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_from_textview);

        }

    }


}