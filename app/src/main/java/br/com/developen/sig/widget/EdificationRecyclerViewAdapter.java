package br.com.developen.sig.widget;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.database.Edification;

public class EdificationRecyclerViewAdapter extends RecyclerView.Adapter<EdificationRecyclerViewAdapter.EdificationViewHolder> {


    private static final int HOUSE_EDIFICATION = 1;

    private static final int APARTMENT_EDIFICATION = 2;

    private static final int COMMERCIAL_ROOM_EDIFICATION = 3;

    private static final int FARM_EDIFICATION = 4;

    private static final int OTHER_EDIFICATION = 5;


    private List<Edification> edifications;

    private EdificationClickListener listener;

    private int focusedItem = 0;


    public EdificationRecyclerViewAdapter(List<Edification> edifications, EdificationClickListener listener) {

        this.edifications = edifications;

        this.listener = listener;

        setHasStableIds(true);

    }


    public EdificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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

        return new EdificationViewHolder(view);

    }


    public int getItemViewType(int position) {

        switch (edifications.get(position).getType().getIdentifier()){

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
    public void onBindViewHolder(final EdificationViewHolder holder, int position) {

        holder.edification = edifications.get(position);

        holder.itemView.setSelected(position==focusedItem);

        if (edifications.get(position).getReference() != null){

            holder.reference.setText(edifications.get(position).getReference());

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

        return edifications.size();

    }


    public long getItemId(int position){

        return edifications.get(position).hashCode();

    }


    public void setEdifications(List<Edification> edifications){

        this.edifications = edifications;

        notifyDataSetChanged();

    }


    public void resetFocusedItem(){

        focusedItem = 0;

    }


    public class EdificationViewHolder extends RecyclerView.ViewHolder {

        public Edification edification;

        public TextView reference;

        public EdificationViewHolder(View view) {

            super(view);

            reference = view.findViewById(R.id.activity_map_bottom_sheet_edification_reference);

        }

    }


    public interface EdificationClickListener {

        void onEdificationClicked(EdificationViewHolder edificationViewHolder);

        void onEdificationLongClick(EdificationViewHolder edificationViewHolder);

    }


}