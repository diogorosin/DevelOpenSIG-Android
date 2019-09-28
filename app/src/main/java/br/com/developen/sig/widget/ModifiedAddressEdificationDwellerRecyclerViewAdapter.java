package br.com.developen.sig.widget;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerFragment;
import br.com.developen.sig.util.App;
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

        holder.cpf.setVisibility(modifiedAddressEdificationDwellers.get(position).getCpf() != null ? View.VISIBLE : View.GONE);

        holder.cpf.setText(StringUtils.formatCpfWithPrefix(modifiedAddressEdificationDwellers.get(position).getCpf()));

        holder.rg.setText(StringUtils.formatRgWithPrefix(
                modifiedAddressEdificationDwellers.get(position).getRgNumber(),
                modifiedAddressEdificationDwellers.get(position).getRgAgency(),
                modifiedAddressEdificationDwellers.get(position).getRgState()));

        if (modifiedAddressEdificationDwellers.get(position).getFrom()!=null)

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

        if (modifiedAddressEdificationDwellers.get(position).getTo() != null) {

            holder.itemView.setBackgroundResource(R.color.colorRedLight);

            holder.thumbnail.setBackgroundTintList(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorWhite)));

            holder.thumbnailBackground.setBackgroundResource(R.color.colorRedMedium);

            holder.name.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorWhite)));

            holder.rg.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorWhite)));

            holder.cpf.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorWhite)));

            holder.from.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorWhite)));

        } else {

            holder.itemView.setBackgroundResource(R.color.colorWhite);

            holder.thumbnail.setBackgroundTintList(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorBlackMedium)));

            holder.thumbnailBackground.setBackgroundResource(R.color.colorGreyMedium);

            holder.name.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorBlackMedium)));

            holder.rg.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorBlackMedium)));

            holder.cpf.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorBlackMedium)));

            holder.from.setTextColor(ColorStateList.valueOf( ContextCompat.getColor(App.getContext(), R.color.colorBlackMedium)));

        }

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

        public ImageView thumbnail;

        public View thumbnailBackground;

        public TextView name;

        public TextView cpf;

        public TextView rg;

        public TextView from;

        public ModifiedAddressEdificationDwellerViewHolder(View view) {

            super(view);

            thumbnail = view.findViewById(R.id.fragment_modified_address_edification_list_thumbnail_imageview);

            thumbnailBackground = view.findViewById(R.id.fragment_modified_address_edification_list_thumbnail_background);

            name = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_name_textview);

            cpf = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_cpf_textview);

            rg = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_rg_textview);

            from = view.findViewById(R.id.fragment_modified_address_edification_dweller_list_item_from_textview);

        }

    }


}