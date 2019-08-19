package br.com.developen.sig.widget;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.view.ActionMode;

import br.com.developen.sig.R;
import br.com.developen.sig.activity.ModifiedActivity;

public class ModifiedToolbarActionModeCallback implements ActionMode.Callback {


    private ModifiedRecyclerViewAdapter adapter;

    private ModifiedActivity activity;


    public ModifiedToolbarActionModeCallback(ModifiedRecyclerViewAdapter adapter, ModifiedActivity activity) {

        this.adapter = adapter;

        this.activity = activity;

    }


    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        mode.getMenuInflater().inflate(R.menu.menu_modified, menu);

        return true;

    }


    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

        menu.findItem(R.id.activity_modified_menu_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;

    }


    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {

            case R.id.activity_modified_menu_delete:

                activity.delete();

                break;

        }

        return false;

    }


    public void onDestroyActionMode(ActionMode mode) {

        adapter.removeSelection();

        activity.setNullToActionMode();

    }


}