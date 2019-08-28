package br.com.developen.sig.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.repository.ModifiedAddressRepository;
import br.com.developen.sig.task.UpdateActiveOfModifiedAddressesAsyncTask;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.widget.ModifiedRecyclerViewAdapter;
import br.com.developen.sig.widget.ModifiedToolbarActionModeCallback;
import br.com.developen.sig.widget.RecyclerClickListener;
import br.com.developen.sig.widget.RecyclerTouchListener;

public class ModifiedActivity extends AppCompatActivity implements UpdateActiveOfModifiedAddressesAsyncTask.Listener {


    private ActionMode actionMode;

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;

    private ModifiedRecyclerViewAdapter modifiedRecyclerViewAdapter;

    private ModifiedAddressRepository modifiedAddressRepository;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified);

        Toolbar toolbar = findViewById(R.id.activity_modified_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.new_and_revised);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.activity_modified_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);

        recyclerView.addItemDecoration(dividerItemDecoration);

        modifiedRecyclerViewAdapter = new ModifiedRecyclerViewAdapter();

        recyclerView.setAdapter(modifiedRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerClickListener() {

            public void onClick(View view, int position) {

                if (actionMode != null)

                    onListItemSelect(position);

                else {

                    int identifier = ((ModifiedRecyclerViewAdapter.ModifiedAddressViewHolder) recyclerView.getChildViewHolder(view)).
                            modifiedAddressModel.
                            getIdentifier();

                    new Handler().post(() -> {

                        Intent addIntent = new Intent(ModifiedActivity.this, ModifiedAddressActivity.class);

                        addIntent.putExtra(ModifiedAddressActivity.MODIFIED_ADDRESS_IDENTIFIER, identifier);

                        startActivity(addIntent);

                    });

                }

            }

            public void onLongClick(View view, int position) {

                onListItemSelect(position);

            }

        }));

        modifiedAddressRepository = ViewModelProviders.of(this).get(ModifiedAddressRepository.class);

        modifiedAddressRepository.getModifiedAddressesThatWasNotSynced().observe(this, modifiedAddressesThatWasNotSynced -> {

            if (modifiedAddressesThatWasNotSynced != null)

                modifiedRecyclerViewAdapter.submitList(modifiedAddressesThatWasNotSynced);

        });

        progressDialog = new ProgressDialog(this);

    }


    public void onResume(){

        super.onResume();

        new Handler().postDelayed(() -> recyclerView.scrollToPosition(0), 500);

    }


    private void onListItemSelect(int position) {

        modifiedRecyclerViewAdapter.toggleSelection(position);

        boolean hasCheckedItems = modifiedRecyclerViewAdapter.getSelectedItemsCount() > 0;

        if (hasCheckedItems && actionMode == null)

            actionMode = this.startSupportActionMode(new ModifiedToolbarActionModeCallback(modifiedRecyclerViewAdapter, this));

        else if (!hasCheckedItems && actionMode != null)

            actionMode.finish();

        if (actionMode != null) {

            int count = modifiedRecyclerViewAdapter.getSelectedItemsCount();

            actionMode.setTitle(count + " ediç" + (count > 1 ? "ões" : "ão") + " selecionada" + (count > 1 ? "s" : ""));

        }

    }


    public void setNullToActionMode() {

        if (actionMode != null)

            actionMode = null;

    }


    public void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Deseja realmente excluir o(s) marcador(es) selecionado(s)?");

        builder.setCancelable(true);

        builder.setTitle(R.string.confirm);

        builder.setPositiveButton(R.string.yes, (dialog, which) -> {

            List<ModifiedAddressModel> modifiedAddressModelList = new ArrayList<>();

            SparseBooleanArray selected = modifiedRecyclerViewAdapter.getSelectedItems();

            for (int i = (selected.size() - 1); i >= 0; i--)

                modifiedAddressModelList.add(Objects.requireNonNull(modifiedRecyclerViewAdapter.getCurrentList()).get(selected.keyAt(i)));

            new UpdateActiveOfModifiedAddressesAsyncTask(ModifiedActivity.this).
                    execute(modifiedAddressModelList.toArray(new ModifiedAddressModel[0]));

            actionMode.finish();

        });

        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();

        alert.setCanceledOnTouchOutside(false);

        alert.show();

    }


    public void onDeleteModifiedAddressPreExecute() {

        progressDialog.setCancelable(false);

        progressDialog.setTitle("Aguarde");

        progressDialog.setMessage("Excluindo marcador(es)...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();

    }


    public void onDeleteModifiedAddressProgressInitialize(int progress, int max) {

        progressDialog.setProgress(progress);

        progressDialog.setMax(max);

    }


    public void onDeleteModifiedAddressProgressUpdate(int status) {

        progressDialog.incrementProgressBy(status);

    }


    public void onDeleteModifiedAddressSuccess() {

        progressDialog.hide();

        Toast.makeText(
                getBaseContext(),
                getResources().
                        getString(R.string.success_modified_address_deleted),
                Toast.LENGTH_SHORT).show();

    }


    public void onDeleteModifiedAddressFailure(Messaging message) {

        showAlertDialog(message);

    }


    public void onDeleteModifiedAddressCancelled() {

        progressDialog.hide();

    }


    private void showAlertDialog(Messaging messaging){

        if (progressDialog.isShowing())

            progressDialog.hide();

        AlertDialog.Builder builder = new AlertDialog.Builder(ModifiedActivity.this);

        builder.setMessage(TextUtils.join("\n", messaging.getMessages()));

        builder.setCancelable(true);

        builder.setTitle(R.string.error);

        builder.setPositiveButton(android.R.string.ok,

                (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();

        alert.setCanceledOnTouchOutside(false);

        alert.show();

    }


}