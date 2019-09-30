package br.com.developen.sig.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;

import br.com.developen.sig.R;
import br.com.developen.sig.database.ModifiedAddressEdificationDwellerModel;
import br.com.developen.sig.exception.ThereAreDwellersOnThisEdificationException;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerFragment;
import br.com.developen.sig.fragment.ModifiedAddressEdificationTypeFragment;
import br.com.developen.sig.task.CreateDwellerAsyncTask;
import br.com.developen.sig.util.IconUtils;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.viewmodel.ModifiedAddressEdificationViewModel;

public class ModifiedAddressEdificationActivity
        extends AppCompatActivity
        implements ModifiedAddressEdificationDwellerFragment.ModifiedAddressEdificationDwellerFragmentListener, CreateDwellerAsyncTask.Listener{


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";


    private ModifiedAddressEdificationViewModel modifiedAddressEdificationViewModel;

    private FloatingActionButton floatingActionButton;

    private Snackbar demolishedSnackbar;

    private boolean isActive = false;

    private boolean isNew = true;

    private boolean wasDemolished = false;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address_edification);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_edification_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.activity_modified_address_edification_container);

        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @SuppressLint("RestrictedApi")
            public void onPageSelected(int position) {

                floatingActionButton.setVisibility(position==1 && !wasDemolished ? View.VISIBLE : View.GONE);

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

        ModifiedAddressEdificationViewModel.Factory factory = new ModifiedAddressEdificationViewModel.Factory(
                getApplication(),
                getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0));

        modifiedAddressEdificationViewModel = new ViewModelProvider(this, factory).get(ModifiedAddressEdificationViewModel.class);

        modifiedAddressEdificationViewModel.active.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                isActive = ((ObservableBoolean) sender).get();

                invalidateOptionsMenu();

            }

        });

        modifiedAddressEdificationViewModel.from.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                isNew = ((ObservableField<Date>) sender).get() == null;

                invalidateOptionsMenu();

            }

        });

        modifiedAddressEdificationViewModel.to.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            @SuppressLint("RestrictedApi")
            public void onPropertyChanged(Observable sender, int propertyId) {

                wasDemolished = ((ObservableField<Date>) sender).get() != null;

                if (wasDemolished)

                    getDemolishedSnackBar().show();

                else

                    getDemolishedSnackBar().dismiss();

                floatingActionButton.setVisibility(viewPager.getCurrentItem() == 1 && !wasDemolished ? View.VISIBLE : View.GONE);

                invalidateOptionsMenu();

            }

        });

        //UPDATE VIEW
        modifiedAddressEdificationViewModel.active.notifyChange();

        modifiedAddressEdificationViewModel.from.notifyChange();

        modifiedAddressEdificationViewModel.to.notifyChange();


    }


    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_modified_address_edification, menu);

        MenuItem deleteItem = menu.findItem(R.id.menu_modified_address_edification_delete);

        IconUtils.paintItWhite(deleteItem);

        deleteItem.setVisible(isActive && isNew);

        MenuItem demolishItem = menu.findItem(R.id.menu_modified_address_edification_demolish);

        IconUtils.paintItWhite(demolishItem);

        demolishItem.setVisible(isActive && !isNew && !wasDemolished);

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

                modifiedAddressEdificationViewModel.save();

                finish();

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

                            modifiedAddressEdificationViewModel.delete();

                            finish();

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

            case R.id.menu_modified_address_edification_demolish: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {}

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:

                            try {

                                modifiedAddressEdificationViewModel.demolish();

                                finish();

                            } catch (ThereAreDwellersOnThisEdificationException e) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                                builder.setTitle("Atenção").
                                        setMessage(e.getMessage()).
                                        setPositiveButton(R.string.button_ok, (dialog1, which1) -> dialog1.dismiss()).
                                        show();

                            }

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;

                    }

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Deseja realmente demolir a edificação?").
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


    private Snackbar getDemolishedSnackBar(){

        if (demolishedSnackbar == null){

            demolishedSnackbar = Snackbar.make(findViewById(R.id.activity_modified_address_edification), "Demolida" , Snackbar.LENGTH_INDEFINITE);

            demolishedSnackbar.setActionTextColor(Color.WHITE);

            demolishedSnackbar.getView().setBackgroundResource(R.color.colorRedMedium);

            demolishedSnackbar.setAction("Desfazer", view -> {

                modifiedAddressEdificationViewModel.undoDemolish();

                finish();

            });

        }

        return demolishedSnackbar;

    }


}