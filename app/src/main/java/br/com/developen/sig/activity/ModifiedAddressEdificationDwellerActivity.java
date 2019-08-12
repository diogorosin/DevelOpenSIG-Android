package br.com.developen.sig.activity;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import br.com.developen.sig.R;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerIndividualFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerOrganizationFragment;
import br.com.developen.sig.widget.Editable;

public class ModifiedAddressEdificationDwellerActivity extends AppCompatActivity {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";

    public static final String DWELLER_IDENTIFIER = "ARG_DWELLER_IDENTIFIER";

    public static final String EDIT_FRAGMENT = "EDIT_FRAGMENT";


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

    }


    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_modified_address_edification_dweller, menu);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_modified_address_edification_dweller_save) {

            try {

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            } catch (Exception e) { }

            Fragment f = getSupportFragmentManager().findFragmentByTag(EDIT_FRAGMENT);

            if (f instanceof Editable)

                ((Editable) f).save();

            return true;

        }

        return super.onOptionsItemSelected(item);

    }


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