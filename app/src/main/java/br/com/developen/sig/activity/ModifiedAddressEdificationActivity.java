package br.com.developen.sig.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationTypeFragment;
import br.com.developen.sig.repository.ModifiedAddressEdificationRepository;
import br.com.developen.sig.task.CreateDwellerAsyncTask;
import br.com.developen.sig.task.UpdateActiveOfModifiedAddressEdificationAsyncTask;
import br.com.developen.sig.util.Messaging;

public class ModifiedAddressEdificationActivity extends AppCompatActivity
        implements ModifiedAddressEdificationDwellerFragment.ModifiedAddressEdificationDwellerFragmentListener,
        UpdateActiveOfModifiedAddressEdificationAsyncTask.Listener,
        CreateDwellerAsyncTask.Listener{


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";


    private ModifiedAddressEdificationRepository modifiedAddressEdificationRepository;

    private SectionsPagerAdapter sectionsPagerAdapter;

    private FloatingActionButton floatingActionButton;

    private ViewPager viewPager;

    private boolean active = false;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address_edification);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_edification_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.activity_modified_address_edification_container);

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @SuppressLint("RestrictedApi")
            public void onPageSelected(int position) {

                floatingActionButton.setVisibility(position==1 ? View.VISIBLE : View.GONE);

            }

            public void onPageScrollStateChanged(int state) {}

        });

        final TabLayout tabLayout = findViewById(R.id.activity_modified_address_edification_tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        floatingActionButton = findViewById(R.id.activity_modified_address_edification_fab);

        floatingActionButton.setOnClickListener(v ->

            new CreateDwellerAsyncTask(ModifiedAddressEdificationActivity.this).execute(
                    getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                    getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0))

        );

        modifiedAddressEdificationRepository = ViewModelProviders.of(this).get(ModifiedAddressEdificationRepository.class);

        modifiedAddressEdificationRepository.getActiveOfModifiedAddressEdification(
                getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0)).observe(this, isActive -> {

            active = isActive;

            invalidateOptionsMenu();

        });

    }


    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_modified_address_edification, menu);

        MenuItem deleteItem = menu.findItem(R.id.menu_modified_address_edification_delete);

        deleteItem.setVisible(active);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.menu_modified_address_edification_save: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) { }

                new UpdateActiveOfModifiedAddressEdificationAsyncTask<>(
                        ModifiedAddressEdificationActivity.this,
                        getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                        getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0)).execute(true);

                return true;

            }

            case R.id.menu_modified_address_edification_delete: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {}

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:

                            new UpdateActiveOfModifiedAddressEdificationAsyncTask<>(this,
                                    getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                                    getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0)).execute(false);

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;

                    }

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Deseja realmente excluir a edificação?").
                        setTitle("Atenção").
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).
                        show();

                return true;

            }

        }

        return super.onOptionsItemSelected(item);

    }


    public void onPointerCaptureChanged(boolean hasCapture) {}


    public void onDwellerClicked(ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel) {

        editDweller(modifiedAddressEdificationDwellerModel.
                getModifiedAddressEdification().
                getModifiedAddress().
                getIdentifier(), modifiedAddressEdificationDwellerModel.
                getModifiedAddressEdification().
                getEdification(), modifiedAddressEdificationDwellerModel.
                getDweller());

    }


    public void onDwellerLongClick(ModifiedAddressEdificationDwellerModel modifiedAddressEdificationDwellerModel) {}


    public void onCreateDwellerSuccess(Integer dweller) {

        editDweller(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER,0),
                getIntent().getIntExtra(EDIFICATION_IDENTIFIER,0), dweller);

    }


    public void editDweller(Integer modifiedAddress, Integer edification, Integer dweller){

        Intent dwellerIntent = new Intent(ModifiedAddressEdificationActivity.this, ModifiedAddressEdificationDwellerActivity.class);

        dwellerIntent.putExtra(ModifiedAddressEdificationDwellerActivity.MODIFIED_ADDRESS_IDENTIFIER, modifiedAddress);

        dwellerIntent.putExtra(ModifiedAddressEdificationDwellerActivity.EDIFICATION_IDENTIFIER, edification);

        dwellerIntent.putExtra(ModifiedAddressEdificationDwellerActivity.DWELLER_IDENTIFIER, dweller);

        startActivity(dwellerIntent);

    }


    public void onCreateDwellerFailure(Messaging messaging) {}


    public void onUpdateActiveOfModifiedAddressEdificationSuccess() {

        finish();

    }


    public void onUpdateActiveOfModifiedAddressEdificationFailure(Messaging messaging) {}


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"Tipo", "Moradores"};

        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);

        }

        public CharSequence getPageTitle(int position) {

            return tabTitles[position];

        }

        public Fragment getItem(int position) {

            Fragment f = null;

            switch (position){

                case 0:

                    f = ModifiedAddressEdificationTypeFragment.newInstance(
                            getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                            getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0));

                    break;

                case 1:

                    f = ModifiedAddressEdificationDwellerFragment.newInstance(
                            getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                            getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0));

                    break;

            }

            return f;

        }

        public int getCount() {

            return 2;

        }

    }


}