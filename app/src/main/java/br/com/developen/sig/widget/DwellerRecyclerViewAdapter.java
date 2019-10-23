package br.com.developen.sig.widget;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.database.Dweller;

public class DwellerRecyclerViewAdapter extends
        RecyclerView.Adapter<DwellerRecyclerViewAdapter.DwellerViewHolder> {


    private static final int FEMALE_DWELLER = 1;

    private static final int MALE_DWELLER = 2;


    private List<Dweller> dwellers;


    public DwellerRecyclerViewAdapter(List<Dweller> dwellers) {

        this.dwellers = dwellers;

        setHasStableIds(true);

    }


    public DwellerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout = 0;

        switch (viewType){

            case FEMALE_DWELLER: layout = R.layout.activity_map_bottom_sheet_female;

                break;

            case MALE_DWELLER: layout = R.layout.activity_map_bottom_sheet_male;

                break;

        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        return new DwellerViewHolder(view);

    }


    public int getItemViewType(int position) {

        switch (dwellers.get(position).getGender().getIdentifier()){

            case "F": return FEMALE_DWELLER;

            case "M": return MALE_DWELLER;

            default:

                return 0;

        }

    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    public void onBindViewHolder(final DwellerViewHolder holder, int position) {

        holder.dweller = dwellers.get(position);

        holder.name.setText(holder.dweller.getName());

    }


    public int getItemCount() {

        return dwellers.size();

    }


    public long getItemId(int position){

        return dwellers.get(position).hashCode();

    }


    public void setDwellers(List<Dweller> dwellers){

        this.dwellers = dwellers;

        notifyDataSetChanged();

    }


    public class DwellerViewHolder extends RecyclerView.ViewHolder {

        private Dweller dweller;

        public TextView name;

        public DwellerViewHolder(View view) {

            super(view);

            name = view.findViewById(R.id.activity_map_bottom_sheet_dweller_name);

        }

    }


}