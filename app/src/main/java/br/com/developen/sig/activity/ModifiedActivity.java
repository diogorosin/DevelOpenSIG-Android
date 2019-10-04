package br.com.developen.sig.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import br.com.developen.sig.R;
import br.com.developen.sig.bean.ExceptionBean;
import br.com.developen.sig.bean.UploadDatasetReaderResultBean;
import br.com.developen.sig.database.ModifiedAddressModel;
import br.com.developen.sig.task.UpdateActiveOfModifiedAddressesAsyncTask;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.util.UploadDatasetBuilder;
import br.com.developen.sig.viewmodel.ModifiedAddressViewModel;
import br.com.developen.sig.widget.ModifiedRecyclerViewAdapter;
import br.com.developen.sig.widget.ModifiedToolbarActionModeCallback;
import br.com.developen.sig.widget.RecyclerClickListener;
import br.com.developen.sig.widget.RecyclerTouchListener;

public class ModifiedActivity extends AppCompatActivity implements UpdateActiveOfModifiedAddressesAsyncTask.Listener {


    private ActionMode actionMode;

    private ProgressDialog progressDialog;

    private RecyclerView recyclerView;

    private SharedPreferences preferences;

    private ModifiedRecyclerViewAdapter modifiedRecyclerViewAdapter;

    private RequestQueue requestQueue;

    private Gson gson;


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


        ModifiedAddressViewModel modifiedAddressViewModel = ViewModelProviders.of(this).get(ModifiedAddressViewModel.class);

        modifiedAddressViewModel.getModifiedAddressesThatWasNotSynced().observe(this, modifiedAddressesThatWasNotSynced -> {

            if (modifiedAddressesThatWasNotSynced != null)

                modifiedRecyclerViewAdapter.submitList(modifiedAddressesThatWasNotSynced);

            invalidateOptionsMenu();

        });


        progressDialog = new ProgressDialog(this);

        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        gson = gsonBuilder.create();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_modified, menu);

        MenuItem uploadItem = menu.findItem(R.id.menu_modified_upload);

        uploadItem.setEnabled(true);

        Drawable drawable = uploadItem.getIcon();

        drawable = DrawableCompat.wrap(drawable);

        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.colorWhite));

        uploadItem.setIcon(drawable);

        uploadItem.setVisible( modifiedRecyclerViewAdapter.getItemCount() > 0 );

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.menu_modified_upload: {

                progressDialog.setCancelable(false);

                progressDialog.setTitle("Aguarde");

                progressDialog.setMessage("Enviando para o servidor...");

                progressDialog.setIndeterminate(true);

                progressDialog.show();

                StringRequest request = new StringRequest(Request.Method.POST, Constants.SERVER_BASE_URL + "upload/dataset",

                        response -> {

                            UploadDatasetReaderResultBean resultBean = gson.fromJson(response, UploadDatasetReaderResultBean.class);

                            Set<Integer> keys = resultBean.getModifiedAddressesThatWasImported().keySet();

                            App.getInstance().getModifiedAddressRepository().synced(keys.toArray(new Integer[0]));

                            Toast.makeText(getApplicationContext(), R.string.upload_sucess, Toast.LENGTH_LONG).show();

                        },

                        error -> {

                            if (error instanceof NoConnectionError){

                                Toast.makeText(getApplicationContext(), R.string.error_connection_failure, Toast.LENGTH_LONG).show();

                            } else {

                                try {

                                    if (error.networkResponse.data != null) {

                                        String responseBody = new String(error.networkResponse.data, "utf-8");

                                        Messaging messaging = gson.fromJson(responseBody, ExceptionBean.class);

                                        if (messaging != null && messaging.getMessages().length > 0)

                                            Toast.makeText(getApplicationContext(), messaging.getMessages()[0], Toast.LENGTH_LONG).show();

                                    }

                                } catch (UnsupportedEncodingException ignored) {}

                            }

                        })

                {

                    public String getBodyContentType() {

                        return Constants.JSON_CONTENT_TYPE;

                    }


                    public byte[] getBody() {

                        return gson.toJson(new UploadDatasetBuilder().withModifiedAddresses().build()).getBytes(StandardCharsets.UTF_8);

                    }


                    protected Response<String> parseNetworkResponse(NetworkResponse response) {

                        String uft8String = new String(response.data, StandardCharsets.UTF_8);

                        return Response.success(uft8String, HttpHeaderParser.parseCacheHeaders(response));

                    }

                    public Map<String, String> getHeaders() {

                        Map<String, String> headers = new HashMap<>();

                        headers.put(Constants.AUTHORIZATION_HEADER, "Bearer " + preferences.getString(Constants.TOKEN_IDENTIFIER_PROPERTY, null));

                        return headers;

                    }

                };

                request.setRetryPolicy(new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                requestQueue.add(request);

                requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request1 -> {

                    invalidateOptionsMenu();

                    progressDialog.hide();

                });

                return true;

            }

        }

        return super.onOptionsItemSelected(item);

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

            actionMode.setTitle(count + " registro" + (count > 1 ? "s" : "") + " selecionado" + (count > 1 ? "s" : ""));

        }

    }


    public void setNullToActionMode() {

        if (actionMode != null)

            actionMode = null;

    }


    public void delete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Deseja realmente excluir o(s) registro(s) selecionado(s)?");

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

        progressDialog.setMessage("Excluindo registro(s)...");

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

        invalidateOptionsMenu();

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