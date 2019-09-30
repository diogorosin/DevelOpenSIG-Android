package br.com.developen.sig.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.mlykotom.valifi.BR;
import com.mlykotom.valifi.ValiFiValidable;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationModel;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.fragment.ModifiedAddressAddressFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationFragment;
import br.com.developen.sig.fragment.ModifiedAddressLocationFragment;
import br.com.developen.sig.task.CreateEdificationAsyncTask;
import br.com.developen.sig.util.IconUtils;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.viewmodel.ModifiedAddressViewModel;

public class ModifiedAddressActivity extends AppCompatActivity implements
        ModifiedAddressEdificationFragment.EdificationFragmentListener,
        CreateEdificationAsyncTask.Listener, GoogleMap.OnMyLocationChangeListener {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";


    private ModifiedAddressViewModel modifiedAddressViewModel;

    private FloatingActionButton floatingActionButton;

    private Location lastKnowLocation;

    private ViewPager viewPager;

    private boolean isActive = false;

    private boolean isValid = false;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.activity_modified_address_container);

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @SuppressLint("RestrictedApi")
            public void onPageSelected(int position) {

                floatingActionButton.setVisibility(position == 0 || position == 2 ? View.VISIBLE : View.GONE);

                floatingActionButton.setImageResource(position == 0 ? R.drawable.icon_gps_24 : R.drawable.icon_add_24);

            }

            public void onPageScrollStateChanged(int state) {}

        });

        final TabLayout tabLayout = findViewById(R.id.activity_modified_address_tabs);

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        floatingActionButton = findViewById(R.id.activity_modified_address_fab);

        floatingActionButton.setOnClickListener(v -> {

            switch (viewPager.getCurrentItem()) {

                case 0:

                    if (getLastKnowLocation() != null) {

                        for (Fragment f: getSupportFragmentManager().getFragments())

                            if (f instanceof ModifiedAddressLocationFragment) {

                                LatLng latLng = new LatLng(getLastKnowLocation().getLatitude(),
                                        getLastKnowLocation().getLongitude());

                                ((ModifiedAddressLocationFragment) f).moveMarkerToLocation(latLng);

                            }

                    }

                    break;

                case 2:

                    new CreateEdificationAsyncTask(ModifiedAddressActivity.this).
                            execute(getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

                    break;

            }

        });

        ModifiedAddressViewModel.Factory factory = new ModifiedAddressViewModel.Factory(
                getApplication(),
                getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

        modifiedAddressViewModel = new ViewModelProvider(this, factory).get(ModifiedAddressViewModel.class);

        modifiedAddressViewModel.form.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                if (propertyId == BR.valid) {

                    isValid = ((ValiFiValidable) sender).isValid();

                    invalidateOptionsMenu();

                }

            }

        });

        //UPDATE VIEW
        modifiedAddressViewModel.form.validate();

        modifiedAddressViewModel.active.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                isActive = ((ObservableBoolean) sender).get();

                invalidateOptionsMenu();

            }

        });

        //UPDATE VIEW
        modifiedAddressViewModel.active.notifyChange();

    }


    public void onStart(){

        super.onStart();

        new Handler().post(() -> {

            if (MapActivity.progressDialog != null && MapActivity.progressDialog.isShowing())

                MapActivity.progressDialog.hide();

        });

    }


    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_modified_address, menu);

        MenuItem saveItem = menu.findItem(R.id.menu_modified_address_save);

        IconUtils.paintItWhite(saveItem);

        saveItem.setEnabled(isValid);

        MenuItem deleteItem = menu.findItem(R.id.menu_modified_address_delete);

        IconUtils.paintItWhite(deleteItem);

        deleteItem.setVisible(isActive);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.menu_modified_address_save: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception ignored) {}

                try {

                    modifiedAddressViewModel.save();

                    finish();

                } catch (CityNotFoundException e) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Atenção").
                            setMessage(e.getMessage()).
                            setPositiveButton(R.string.button_ok, (dialog, which) -> {

                                dialog.dismiss();

                                viewPager.setCurrentItem(1);

                            }).
                            show();

                }

                return true;

            }

            case R.id.menu_modified_address_delete: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception ignored) {}

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:

                            modifiedAddressViewModel.delete();

                            finish();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;

                    }

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Deseja realmente excluir o marcador?").
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


    public void onMyLocationChange(Location location) {

        setLastKnowLocation(location);

    }


    public Location getLastKnowLocation() {

        return lastKnowLocation;

    }


    public void setLastKnowLocation(Location lastKnowLocation) {

        this.lastKnowLocation = lastKnowLocation;

    }


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

                    f = ModifiedAddressLocationFragment.newInstance(getIntent().
                            getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0));

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

}