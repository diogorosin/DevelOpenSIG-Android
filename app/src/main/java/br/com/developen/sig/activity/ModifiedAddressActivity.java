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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.fragment.ModifiedAddressAddressFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationFragment;
import br.com.developen.sig.fragment.ModifiedAddressLocationFragment;
import br.com.developen.sig.repository.ModifiedAddressRepository;
import br.com.developen.sig.task.CreateEdificationAsyncTask;
import br.com.developen.sig.task.UpdateActiveOfModifiedAddressAsyncTask;
import br.com.developen.sig.task.UpdateAddressLocationAsyncTask;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.widget.ValidableFragment;

public class ModifiedAddressActivity extends AppCompatActivity
        implements ModifiedAddressLocationFragment.LocationListener,
        ModifiedAddressEdificationFragment.EdificationFragmentListener,
        UpdateActiveOfModifiedAddressAsyncTask.Listener,
        CreateEdificationAsyncTask.Listener,
        ValidableFragment.Listener,
        UpdateAddressLocationAsyncTask.Listener{


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";


    private ModifiedAddressRepository modifiedAddressRepository;

    private SectionsPagerAdapter sectionsPagerAdapter;

    private FloatingActionButton floatingActionButton;

    private ViewPager viewPager;

    private boolean active = false;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.activity_modified_address_container);

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @SuppressLint("RestrictedApi")
            public void onPageSelected(int position) {

                floatingActionButton.setVisibility(position == 2 ? View.VISIBLE : View.GONE);

            }

            public void onPageScrollStateChanged(int state) {}

        });

        final TabLayout tabLayout = findViewById(R.id.activity_modified_address_tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        floatingActionButton = findViewById(R.id.activity_modified_address_fab);

        floatingActionButton.setOnClickListener(v ->

                new CreateEdificationAsyncTask(ModifiedAddressActivity.this).
                        execute(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0))

        );

        modifiedAddressRepository = ViewModelProviders.of(this).get(ModifiedAddressRepository.class);

        modifiedAddressRepository.getActive(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0)).
                observe(this, isActive -> {

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

        menuInflater.inflate(R.menu.menu_modified_address, menu);

        MenuItem deleteItem = menu.findItem(R.id.menu_modified_address_delete);

        deleteItem.setVisible(active);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.menu_modified_address_save: {

                for (Fragment f: getSupportFragmentManager().getFragments())

                    if (f instanceof ValidableFragment)

                        ((ValidableFragment) f).validate();

                return true;

            }

            case R.id.menu_modified_address_delete: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {}

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:

                            new UpdateActiveOfModifiedAddressAsyncTask<>(this,
                                    getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0)).execute(false);

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;

                    }

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Deseja realmente excluir o endereço?").
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


    public void onLocationChanged(LatLng latLng) {

        new UpdateAddressLocationAsyncTask<>(this).execute(
                getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER,0),
                latLng.latitude,
                latLng.longitude);

    }


    public void onUpdateAddressLocationSuccess() {}


    public void onUpdateAddressLocationFailure(Messaging messaging) {}


    public void onEdificationClicked(ModifiedAddressEdificationModel modifiedAddressEdificationModel) {

        editEdification(modifiedAddressEdificationModel.
                getModifiedAddress().
                getIdentifier(), modifiedAddressEdificationModel.
                getEdification());

    }


    public void onEdificationLongClick(ModifiedAddressEdificationModel modifiedAddressEdificationModel) {}


    public void onCreateEdificationSuccess(Integer identifier) {

        editEdification(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER,0), identifier);

    }


    public void onCreateEdificationFailure(Messaging messaging) {}


    public void editEdification(Integer modifiedAddress, Integer edification){

        Intent edificationIntent = new Intent(ModifiedAddressActivity.this, ModifiedAddressEdificationActivity.class);

        edificationIntent.putExtra(ModifiedAddressEdificationDwellerActivity.MODIFIED_ADDRESS_IDENTIFIER, modifiedAddress);

        edificationIntent.putExtra(ModifiedAddressEdificationDwellerActivity.EDIFICATION_IDENTIFIER, edification);

        startActivity(edificationIntent);

    }


    public void onUpdateActiveOfModifiedAddressSuccess() {

        finish();

    }


    public void onUpdateActiveOfModifiedAddressFailure(Messaging messaging) {}


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] tabTitles = new String[]{"Localização", "Endereço", "Edificação"};

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

                    f = ModifiedAddressLocationFragment.newInstance(ModifiedAddressActivity.this,
                            getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

                    break;

                case 1:

                    f = ModifiedAddressAddressFragment.newInstance(getIntent().
                            getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

                    break;

                case 2:

                    f = ModifiedAddressEdificationFragment.newInstance(getIntent().
                            getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

                    break;

            }

            return f;

        }

        public int getCount() {

            return 3;

        }

    }


    public void onValidationFailed(Fragment f) {

        if (f instanceof ModifiedAddressAddressFragment)

            viewPager.setCurrentItem(1, true);

    }


    public void onValidationSucceeded(Fragment f) {

        finish();

    }


}