package br.com.developen.sig.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.mlykotom.valifi.BR;
import com.mlykotom.valifi.ValiFiValidable;

import java.util.Date;

import br.com.developen.sig.R;
import br.com.developen.sig.exception.CityNotFoundException;
import br.com.developen.sig.exception.DocumentNotFoundException;
import br.com.developen.sig.fragment.ModifiedAddressEdificationDwellerIndividualFragment;
import br.com.developen.sig.viewmodel.ModifiedAddressEdificationDwellerViewModel;

public class ModifiedAddressEdificationDwellerActivity extends AppCompatActivity {


    public static final String MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    public static final String EDIFICATION_IDENTIFIER = "ARG_EDIFICATION_IDENTIFIER";

    public static final String DWELLER_IDENTIFIER = "ARG_DWELLER_IDENTIFIER";


    private ModifiedAddressEdificationDwellerViewModel modifiedAddressEdificationDwellerViewModel;

    private Snackbar movedSnackbar;

    private boolean isActive = false;

    private boolean isValid = false;

    private boolean isNew = true;

    private boolean wasMoved = false;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modified_address_edification_dweller);

        Toolbar toolbar = findViewById(R.id.activity_modified_address_edification_dweller_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_modified_address_edification_dweller_scrollview, ModifiedAddressEdificationDwellerIndividualFragment.newInstance(
                        getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                        getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0),
                        getIntent().getIntExtra(DWELLER_IDENTIFIER, 0)))
                .commit();

        ModifiedAddressEdificationDwellerViewModel.Factory factory = new ModifiedAddressEdificationDwellerViewModel.Factory(
                getApplication(),
                getIntent().getIntExtra(MODIFIED_ADDRESS_IDENTIFIER, 0),
                getIntent().getIntExtra(EDIFICATION_IDENTIFIER, 0),
                getIntent().getIntExtra(DWELLER_IDENTIFIER, 0));

        modifiedAddressEdificationDwellerViewModel = new ViewModelProvider(this, factory).get(ModifiedAddressEdificationDwellerViewModel.class);

        modifiedAddressEdificationDwellerViewModel.form.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                switch (propertyId){

                    case BR.valid:

                        isValid = ((ValiFiValidable) sender).isValid();

                        invalidateOptionsMenu();

                        break;

                }

            }

        });

        modifiedAddressEdificationDwellerViewModel.active.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                isActive = ((ObservableBoolean) sender).get();

                invalidateOptionsMenu();

            }

        });

        modifiedAddressEdificationDwellerViewModel.from.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                isNew = ((ObservableField<Date>) sender).get() == null;

                invalidateOptionsMenu();

            }

        });

        modifiedAddressEdificationDwellerViewModel.to.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                wasMoved = ((ObservableField<Date>) sender).get() != null;

                if (wasMoved)

                    getMovedSnackBar().show();

                else

                    getMovedSnackBar().dismiss();

                invalidateOptionsMenu();

            }

        });

        //UPDATE VIEW
        modifiedAddressEdificationDwellerViewModel.active.notifyChange();

        modifiedAddressEdificationDwellerViewModel.from.notifyChange();

        modifiedAddressEdificationDwellerViewModel.to.notifyChange();

    }


    public boolean onSupportNavigateUp(){

        finish();

        return true;

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_modified_address_edification_dweller, menu);

        MenuItem saveItem = menu.findItem(R.id.menu_modified_address_edification_dweller_save);

        saveItem.setEnabled(isValid);

        MenuItem deleteItem = menu.findItem(R.id.menu_modified_address_edification_dweller_delete);

        deleteItem.setVisible(isActive && isNew);

        MenuItem moveItem = menu.findItem(R.id.menu_modified_address_edification_dweller_move);

        Drawable drawable = moveItem.getIcon();

        drawable = DrawableCompat.wrap(drawable);

        DrawableCompat.setTint(drawable, ContextCompat.getColor(this,R.color.colorWhite));

        moveItem.setIcon(drawable);

        moveItem.setVisible(isActive && !isNew && !wasMoved);

        return true;

    }


    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.menu_modified_address_edification_dweller_save: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception ignored) {}

                try {

                    modifiedAddressEdificationDwellerViewModel.save();

                    finish();

                } catch (CityNotFoundException e) {

                    e.printStackTrace();

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Atenção").
                            setMessage(e.getMessage()).
                            setPositiveButton(R.string.button_ok, (dialog, which) -> {

                                dialog.dismiss();

                                findViewById(R.id.fragment_modified_address_edification_dweller_birthplace_autocomplete).requestFocus();

                            }).show();

                } catch (DocumentNotFoundException e) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("Atenção").
                            setMessage(e.getMessage()).
                            setPositiveButton(R.string.button_ok, (dialog, which) -> {

                                dialog.dismiss();

                                findViewById(R.id.fragment_modified_address_edification_dweller_cpf_edittext).requestFocus();

                            }).show();

                }

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

                            modifiedAddressEdificationDwellerViewModel.delete();

                            finish();

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


            case R.id.menu_modified_address_edification_dweller_move: {

                try {

                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {}

                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {

                    switch (which){

                        case DialogInterface.BUTTON_POSITIVE:

                            modifiedAddressEdificationDwellerViewModel.move();

                            finish();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;

                    }

                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("Deseja realmente mover o morador?").
                        setTitle("Atenção").
                        setPositiveButton("Sim", dialogClickListener).
                        setNegativeButton("Não", dialogClickListener).
                        show();

                return true;

            }


        }

        return super.onOptionsItemSelected(item);

    }


    private Snackbar getMovedSnackBar(){

        if (movedSnackbar == null){

            movedSnackbar = Snackbar.make(findViewById(R.id.activity_modified_address_edification_dweller_scrollview), "Mudou-se" , Snackbar.LENGTH_INDEFINITE);

            movedSnackbar.setActionTextColor(Color.WHITE);

            movedSnackbar.getView().setBackgroundResource(R.color.colorRedMedium);

            movedSnackbar.setAction("Desfazer", view -> {

                modifiedAddressEdificationDwellerViewModel.undoMove();

                finish();

            });

        }

        return movedSnackbar;

    }


}