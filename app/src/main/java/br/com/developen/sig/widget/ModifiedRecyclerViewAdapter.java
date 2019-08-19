package br.com.developen.sig.widget;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.StringUtils;


public class ModifiedRecyclerViewAdapter extends PagedListAdapter<ModifiedAddressModel,  ModifiedRecyclerViewAdapter.ModifiedAddressViewHolder> {


    private SparseBooleanArray selectedItems;


    public ModifiedRecyclerViewAdapter() {

        super(new ModifiedDiffCallback());

        selectedItems = new SparseBooleanArray();

    }


    public ModifiedAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_modified_item, parent, false);

        return new ModifiedAddressViewHolder(view);

    }


    public void onBindViewHolder(final ModifiedAddressViewHolder holder, int position) {

        holder.modifiedAddressModel = getItem(position);

        holder.addressTextView.setText(holder.modifiedAddressModel.getDenomination());

        holder.modifiedAtTextView.setText(StringUtils.formatDate(holder.modifiedAddressModel.getModifiedAt()));

        holder.itemView.setBackgroundColor(selectedItems.get(position) ?
                ContextCompat.getColor(App.getContext(), R.color.colorBlackMedium) :
                Color.TRANSPARENT);

    }


    public class ModifiedAddressViewHolder extends RecyclerView.ViewHolder {

        public ModifiedAddressModel modifiedAddressModel;

        TextView addressTextView;

        TextView modifiedAtTextView;

        ModifiedAddressViewHolder(View view) {

            super(view);

            addressTextView = view.findViewById(R.id.activity_modified_item_address_textview);

            modifiedAtTextView = view.findViewById(R.id.activity_modified_item_modified_at_textview);

        }

    }


    public void toggleSelection(int position) {

        selectView(position, !selectedItems.get(position));

    }


    public void removeSelection() {

        selectedItems = new SparseBooleanArray();

        notifyDataSetChanged();

    }


    public void selectView(int position, boolean value) {

        if (value)

            selectedItems.put(position, true);

        else

            selectedItems.delete(position);

        notifyDataSetChanged();

    }


    public int getSelectedItemsCount() {

        return selectedItems.size();

    }


    public SparseBooleanArray getSelectedItems() {

        return selectedItems;

    }


}