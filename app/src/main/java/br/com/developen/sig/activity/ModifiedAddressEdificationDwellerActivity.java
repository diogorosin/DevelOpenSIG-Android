package br.com.developen.sig.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import br.com.developen.sig.R;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerIndividualFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerOrganizationFragment;
import br.com.developen.sig.repository.ModifiedAddressEdificationDwellerRepository;
import br.com.developen.sig.task.UpdateActiveOfModifiedAddressEdificationDwellerAsyncTask;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.widget.Editable;

public class ModifiedAddressEdificationDwellerActivity extends AppCompatActivity
        implements  UpdateActiveOfModifiedAddressEdificationDwellerAsyncTask.Listener{


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";

    public static final String DWELLER_IDENTIFIER = "ARG_DWELLER_IDENTIFIER";

    public static final String EDIT_FRAGMENT = "EDIT_FRAGMENT";


    private ModifiedAddressEdificationDwellerRepository modifiedAddressEdificationDwellerRepository;

    private boolean active = false;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address_edification_dweller);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_edification_dweller_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = findViewById(R.id.activity_modified_address_edification_dweller_spinner);

        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Pessoa Física",
                        "Pessoa Jurídica"
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0: getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ModifiedAddressEdificationDwellerIndividualFragment.newInstance(
                                    getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                                    getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0),
                                    getIntent().getIntExtra(DWELLER_IDENTIFIER, 0)), EDIT_FRAGMENT)
                            .commit();

                        break;

                    case 1: getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ModifiedAddressEdificationDwellerOrganizationFragment.newInstance(
                                    getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                                    getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0),
                                    getIntent().getIntExtra(DWELLER_IDENTIFIER, 0)), EDIT_FRAGMENT)
                            .commit();

                        break;

                }

            }

            public void onNothingSelected(AdapterView<?> parent) {}

        });

        modifiedAddressEdificationDwellerRepository = ViewModelProviders.of(this).get(ModifiedAddressEdificationDwellerRepository.class);

        modifiedAddressEdificationDwellerRepository.getModifiedAddressEdificationDweller(
                getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0),
                getIntent().getIntExtra(DWELLER_IDENTIFIER, 0)).observe(this, modifiedAddressEdificationDwellerModel -> {

                    active = modifiedAddressEdificationDwellerModel.getActive();

                    invalidateOptionsMenu();

        });

    }


    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_modified_address_edification_dweller, menu);

        MenuItem deleteItem = menu.findItem(R.id.menu_modified_address_edification_dweller_delete);

        deleteItem.setVisible(active);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.menu_modified_address_edification_dweller_save: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {}

                Fragment f = getSupportFragmentManager().findFragmentByTag(EDIT_FRAGMENT);

                if (f instanceof Editable)

                    ((Editable) f).save();

                return true;

            }

            case R.id.menu_modified_address_edification_dweller_delete: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {}

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:

                            new UpdateActiveOfModifiedAddressEdificationDwellerAsyncTask<>(this,
                                    getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                                    getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0),
                                    getIntent().getIntExtra(DWELLER_IDENTIFIER, 0)).execute(false);

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;

                    }

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Deseja realmente excluir o morador?").
                        setTitle("Atenção").
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).
                        show();

                return true;

            }

        }

        return super.onOptionsItemSelected(item);

    }


    public void onUpdateActiveOfModifiedAddressEdificationDwellerSuccess() {

        finish();

    }


    public void onUpdateActiveOfModifiedAddressEdificationDwellerFailure(Messaging messaging) {}


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {

        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {

            super(context, android.R.layout.simple_list_item_1, objects);

            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);

        }

        public View getDropDownView(int position, View convertView, ViewGroup parent) {

            View view;

            if (convertView == null) {

                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();

                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            } else {

                view = convertView;

            }

            TextView textView = view.findViewById(android.R.id.text1);

            textView.setText(getItem(position));

            return view;

        }

        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        public void setDropDownViewTheme(Theme theme) {

            mDropDownHelper.setDropDownViewTheme(theme);

        }

    }

}