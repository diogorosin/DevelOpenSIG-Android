package br.com.developen.sig.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.StringUtils;


public class ModifiedRecyclerViewAdapter extends PagedListAdapter<ModifiedAddressModel, ModifiedRecyclerViewAdapter.ModifiedAddressViewHolder> {


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

        holder.denominationTextView.setText(holder.modifiedAddressModel.getDenomination() + (holder.modifiedAddressModel.getNumber() == null ? "" : ", Nº " + holder.modifiedAddressModel.getNumber()));

        holder.districtTextView.setText(holder.modifiedAddressModel.getDistrict());

        holder.cityTextView.setText(holder.modifiedAddressModel.getCity().toString());

        holder.modifiedAtTextView.setText(StringUtils.formatShortDateTime(holder.modifiedAddressModel.getModifiedAt()));

        holder.itemView.setBackgroundColor(selectedItems.get(position) ?
                ContextCompat.getColor(App.getContext(), R.color.colorBlackMedium) :
                Color.TRANSPARENT);

        ContextWrapper cw = new ContextWrapper(App.getContext());

        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        String fileName = String.format("map_%s.jpg", String.valueOf(holder.modifiedAddressModel.getIdentifier()));

        File path = new File(directory, fileName);

        try {

            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(path));

            holder.snapshotImageView.setImageBitmap(b);

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

    }


    public class ModifiedAddressViewHolder extends RecyclerView.ViewHolder {

        public ModifiedAddressModel modifiedAddressModel;

        ImageView snapshotImageView;

        TextView denominationTextView;

        TextView districtTextView;

        TextView cityTextView;

        TextView modifiedAtTextView;

        ModifiedAddressViewHolder(View view) {

            super(view);

            snapshotImageView = view.findViewById(R.id.activity_modified_item_snapshot_imageview);

            denominationTextView = view.findViewById(R.id.activity_modified_item_denomination_textview);

            districtTextView = view.findViewById(R.id.activity_modified_item_district_textview);

            cityTextView = view.findViewById(R.id.activity_modified_item_city_textview);

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