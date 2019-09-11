package br.com.developen.sig.widget;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import br.com.developen.sig.database.ModifiedAddressModel;

public class ModifiedDiffCallback extends DiffUtil.ItemCallback<ModifiedAddressModel> {

    public boolean areItemsTheSame(@NonNull ModifiedAddressModel oldItem, @NonNull ModifiedAddressModel newItem) {

        return oldItem.equals(newItem);

    }

    public boolean areContentsTheSame(@NonNull ModifiedAddressModel oldItem, @NonNull ModifiedAddressModel newItem) {

        return oldItem.hasSameContent(newItem);

    }

}