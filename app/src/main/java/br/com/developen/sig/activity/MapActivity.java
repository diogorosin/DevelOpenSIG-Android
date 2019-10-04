// https://developer.android.com/training/location/retrieve-current
// https://developer.android.com/training/location/receive-location-updates

package br.com.developen.sig.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
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
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.developen.sig.R;
import br.com.developen.sig.bean.DatasetBean;
import br.com.developen.sig.bean.ExceptionBean;
import br.com.developen.sig.database.AddressEdificationDwellerModel;
import br.com.developen.sig.database.AddressEdificationModel;
import br.com.developen.sig.database.AddressModel;
import br.com.developen.sig.task.CreateAddressAsyncTask;
import br.com.developen.sig.task.EditAddressAsyncTask;
import br.com.developen.sig.task.FindAddressesByIndividualNameAsyncTask;
import br.com.developen.sig.task.ImportAsyncTask;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.IconUtils;
import br.com.developen.sig.util.Messaging;
import br.com.developen.sig.util.StringUtils;
import br.com.developen.sig.viewmodel.AddressViewModel;
import br.com.developen.sig.widget.AddressClusterItem;
import br.com.developen.sig.widget.AddressClusterRenderer;
import br.com.developen.sig.widget.AddressEdificationDwellerRecyclerViewAdapter;
import br.com.developen.sig.widget.AddressEdificationDwellerSuggestions;
import br.com.developen.sig.widget.AddressEdificationRecyclerViewAdapter;


public class MapActivity
        extends FragmentActivity
        implements OnMapReadyCallback,
        ImportAsyncTask.Listener,
        AddressEdificationRecyclerViewAdapter.EdificationClickListener,
        GoogleMap.OnMyLocationChangeListener,
        CreateAddressAsyncTask.Listener,
        NavigationView.OnNavigationItemSelectedListener,
        FindAddressesByIndividualNameAsyncTask.Listener,
        EditAddressAsyncTask.Listener,
        ClusterManager.OnClusterClickListener<AddressClusterItem>,
        ClusterManager.OnClusterInfoWindowClickListener<AddressClusterItem>,
        ClusterManager.OnClusterItemClickListener<AddressClusterItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<AddressClusterItem> {


    public static final int MY_LOCATION_PERMISSION_REQUEST = 1;

    private ClusterManager<AddressClusterItem> clusterManager;


    public static ProgressDialog progressDialog;

    private Location lastKnowLocation;

    private GoogleMap googleMap;

    private FloatingSearchView searchView;

    private SharedPreferences preferences;

    private AddressViewModel addressViewModel;

    private BottomSheetBehavior bottomSheetBehavior;

    private FloatingActionButton fab;


    private TextView denominationTextView;

    private TextView referenceTextView;

    private TextView districtTextView;

    private TextView cityTextView;

    private TextView noDataFoundTextView;

    private ProgressBar progressBar;

    private AddressEdificationRecyclerViewAdapter edificationAdapter;

    private AddressEdificationDwellerRecyclerViewAdapter dwellerAdapter;


    private MenuItem menuItemNormal;

    private MenuItem menuItemTerrain;

    private MenuItem menuItemSatellite;

    private MenuItem menuItemEditions;


    private Integer defaultAddress = -1;

    private Boolean reloadEdifications = false;


    private RequestQueue requestQueue;

    private Gson gson;


    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        defaultAddress = getIntent().getIntExtra(Constants.DEFAULT_ADDRESS, -1);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_map_fragment);

        mapFragment.getMapAsync(this);


        NavigationView navigationView = findViewById(R.id.activity_main_navigator);

        navigationView.setNavigationItemSelectedListener(this);


        menuItemNormal = navigationView.getMenu().findItem(R.id.menu_map_type_normal);

        menuItemSatellite = navigationView.getMenu().findItem(R.id.menu_map_type_satellite);

        menuItemTerrain = navigationView.getMenu().findItem(R.id.menu_map_type_terrain);

        menuItemEditions = navigationView.getMenu().findItem(R.id.menu_markers_edit);


        denominationTextView = findViewById(R.id.activity_map_bottom_sheet_denomination);

        referenceTextView = findViewById(R.id.activity_map_bottom_sheet_reference);

        districtTextView = findViewById(R.id.activity_map_bottom_sheet_district);

        cityTextView = findViewById(R.id.activity_map_bottom_sheet_city);


        Button navigateButton = findViewById(R.id.activity_map_bottom_sheet_navigate);

        navigateButton.setOnClickListener(v -> {

            if (addressViewModel.selectedClusterItem.get()==null)

                return;

            Uri gmmIntentUri = Uri.parse("google.navigation:q=" +
                    addressViewModel.selectedClusterItem.get().getPosition().latitude + "," +
                    addressViewModel.selectedClusterItem.get().getPosition().longitude);

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

            mapIntent.setPackage("com.google.android.apps.maps");

            startActivity(mapIntent);

        });


        Button shareButton = findViewById(R.id.activity_map_bottom_sheet_share);

        shareButton.setOnClickListener(v -> {

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            if (addressViewModel.selectedClusterItem.get()==null)

                return;

            AddressClusterItem addressClusterItem = addressViewModel.selectedClusterItem.get();

            Intent sendIntent = new Intent();

            sendIntent.setAction(Intent.ACTION_SEND);

            sendIntent.putExtra(Intent.EXTRA_TEXT, StringUtils.formatAddressForShare(

                    addressClusterItem.getIdentifier(),

                    addressClusterItem.getDenomination(),

                    addressClusterItem.getNumber(),

                    addressClusterItem.getReference(),

                    addressClusterItem.getDistrict(),

                    addressClusterItem.getPostalCode(),

                    addressClusterItem.getCity(),

                    addressClusterItem.getPosition()

            ));

            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);

            startActivity(shareIntent);

        });

        noDataFoundTextView = findViewById(R.id.activity_map_bottom_sheet_empty);


        DrawerLayout drawerLayout = findViewById(R.id.activity_map_drawer);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView userTextView = navigationView.getHeaderView(0).findViewById(R.id.activity_map_navigator_header_name);

        TextView governmentTextView = navigationView.getHeaderView(0).findViewById(R.id.activity_map_navigator_header_government);

        userTextView.setText(preferences.getString(Constants.USER_NAME_PROPERTY, "Desconhecido"));

        governmentTextView.setText(preferences.getString(Constants.GOVERNMENT_DENOMINATION_PROPERTY, "Desconhecido"));


        searchView = findViewById(R.id.activity_map_fsv);

        searchView.attachNavigationDrawerToMenuButton(drawerLayout);

        searchView.setOnBindSuggestionCallback((suggestionView, leftIcon, textView, item, itemPosition) -> {

            AddressEdificationDwellerSuggestions addressEdificationDwellerSuggestions = (AddressEdificationDwellerSuggestions) item;

            leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), IconUtils.getListIconByType(
                    addressEdificationDwellerSuggestions.
                            getAddressEdificationDwellerModel().
                            getIndividual().
                            getGender().getIdentifier()), null));

            leftIcon.setMaxHeight(24);

            leftIcon.setMaxWidth(24);

            leftIcon.setColorFilter(Color.parseColor("#000000"));

            leftIcon.setAlpha(.36f);

        });

        searchView.setOnQueryChangeListener((oldQuery, newQuery) -> {

            if (!oldQuery.equals("") && newQuery.equals(""))

                searchView.clearSuggestions();

            else

                new FindAddressesByIndividualNameAsyncTask<>(MapActivity.this).execute(newQuery);

        });

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                final AddressEdificationDwellerSuggestions suggestion = (AddressEdificationDwellerSuggestions) searchSuggestion;

                goToLocation(new LatLng(suggestion.
                        getAddressEdificationDwellerModel().
                        getAddressEdification().
                        getAddress().
                        getLatitude(), suggestion.
                        getAddressEdificationDwellerModel().
                        getAddressEdification().
                        getAddress().
                        getLongitude()), suggestion.
                        getAddressEdificationDwellerModel().
                        getAddressEdification().
                        getAddress().
                        getIdentifier());

                searchView.clearSearchFocus();

                searchView.setSearchText(((AddressEdificationDwellerSuggestions) searchSuggestion).
                        getAddressEdificationDwellerModel().
                        getIndividual().
                        getName());

            }

            public void onSearchAction(String query) {}

        });

        searchView.setOnMenuItemClickListener(item -> {

            switch (item.getItemId()) {

                case R.id.menu_map_search_localization:

                    goToMyLocation();

                    break;

            }

        });

        fab = findViewById(R.id.activity_map_fab);

        fab.setOnClickListener(view -> {

            if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN){

                if (getLastKnowLocation() != null)

                    new CreateAddressAsyncTask<>(MapActivity.this).execute(
                            getLastKnowLocation().getLatitude(), getLastKnowLocation().getLongitude());

            } else {

                new EditAddressAsyncTask<>(MapActivity.this).execute(
                        addressViewModel.selectedClusterItem.get().getIdentifier());

            }

        });

        progressDialog = new ProgressDialog(this);

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.activity_map_bottom_sheet));

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            public void onStateChanged(View bottomSheet, int newState) {

                //WHEN BOTTOM SHEET SLIDING, CHECK IF NEED START RELOAD EDIFICATIONS
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {

                    if (reloadEdifications)

                        refreshBottomSheetBehavior();

                }

                fab.setImageResource(newState == BottomSheetBehavior.STATE_HIDDEN ?
                        R.drawable.icon_add_24 :
                        R.drawable.icon_edit_24);

                fab.setImageTintList(newState == BottomSheetBehavior.STATE_HIDDEN ?
                        ColorStateList.valueOf(getResources().getColor(R.color.colorBlackDark)):
                        ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

                fab.setBackgroundTintList(newState == BottomSheetBehavior.STATE_HIDDEN ?
                        ColorStateList.valueOf(getResources().getColor(R.color.colorGreyDark)):
                        ColorStateList.valueOf(getResources().getColor(R.color.colorGreenHiMedium)));

                fab.setRippleColor(newState == BottomSheetBehavior.STATE_HIDDEN ?
                        getResources().getColor(R.color.colorGreyDark) :
                        getResources().getColor(R.color.colorGreenHiMedium));

            }

            public void onSlide(View bottomSheet, float slideOffset) {}

        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        progressBar = findViewById(R.id.activity_map_bottom_sheet_progress);


        List<AddressEdificationModel> addressEdificationModels = new ArrayList<>();

        edificationAdapter = new AddressEdificationRecyclerViewAdapter(addressEdificationModels, this);

        RecyclerView edificationRecyclerView = findViewById(R.id.activity_map_bottom_sheet_edifications);

        edificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        edificationRecyclerView.setAdapter(edificationAdapter);


        List<AddressEdificationDwellerModel> addressEdificationDwellerModels = new ArrayList<>();

        dwellerAdapter = new AddressEdificationDwellerRecyclerViewAdapter(addressEdificationDwellerModels);

        RecyclerView dwellerRecyclerView = findViewById(R.id.activity_map_bottom_sheet_dwellers);

        dwellerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        dwellerRecyclerView.setAdapter(dwellerAdapter);


        AddressViewModel.Factory factory = new AddressViewModel.Factory(getApplication());

        addressViewModel = new ViewModelProvider(this, factory).get(AddressViewModel.class);

        addressViewModel.selectedClusterItem.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                reloadEdifications = true;

                AddressClusterItem addressClusterItem = ((ObservableField<AddressClusterItem>) sender).get();

                denominationTextView.setText(StringUtils.formatDenominationWithNumber(addressClusterItem.getDenomination(), addressClusterItem.getNumber()));

                referenceTextView.setText(addressClusterItem.getReference());

                referenceTextView.setVisibility(addressClusterItem.getReference() != null && !addressClusterItem.getReference().isEmpty() ? View.VISIBLE : View.GONE);

                districtTextView.setText(addressClusterItem.getDistrict());

                cityTextView.setText(addressClusterItem.getCity());

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && reloadEdifications)

                    refreshBottomSheetBehavior();

            }

        });


        requestQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        gson = gsonBuilder.create();


    }


    public void onSuccess(List<AddressEdificationDwellerModel> list) {

        List<SearchSuggestion> result = new ArrayList<>();

        for (AddressEdificationDwellerModel addressEdificationDwellerModel : list)

            result.add(new AddressEdificationDwellerSuggestions(addressEdificationDwellerModel));

        searchView.swapSuggestions(result);

    }


    public void onFailure(Messaging messaging) {}


    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.activity_map_drawer);

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }

    }


    public boolean onNavigationItemSelected(MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.activity_map_drawer);

        switch (item.getItemId()) {

            case R.id.menu_map_type_normal:

                menuItemTerrain.setChecked(false);

                menuItemSatellite.setChecked(false);

                getGoogleMap().setMapType(GoogleMap.MAP_TYPE_NORMAL);

                drawer.closeDrawers();

                break;

            case R.id.menu_map_type_terrain:

                menuItemNormal.setChecked(false);

                menuItemSatellite.setChecked(false);

                getGoogleMap().setMapType(GoogleMap.MAP_TYPE_TERRAIN);

                drawer.closeDrawers();

                break;

            case R.id.menu_map_type_satellite:

                menuItemNormal.setChecked(false);

                menuItemTerrain.setChecked(false);

                getGoogleMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                drawer.closeDrawers();

                break;

            case R.id.menu_markers_edit:

                Intent editIntent = new Intent(MapActivity.this, ModifiedActivity.class);

                startActivity(editIntent);

                drawer.closeDrawers();

                break;

            case R.id.menu_markers_refresh:

                drawer.closeDrawers();

                refresh();

                break;

            case R.id.menu_settings:

                Intent settingsIntent = new Intent(MapActivity.this, SettingsActivity.class);

                startActivity(settingsIntent);

                drawer.closeDrawers();

                break;

            case R.id.menu_logout:

                SharedPreferences.Editor editor = preferences.edit();

                editor.remove(Constants.USER_IDENTIFIER_PROPERTY);

                editor.remove(Constants.USER_NAME_PROPERTY);

                editor.remove(Constants.USER_LOGIN_PROPERTY);

                editor.apply();

                drawer.closeDrawer(GravityCompat.START);

                Intent intent = new Intent(MapActivity.this, LoginActivity.class);

                startActivity(intent);

                finish();

                break;

        }

        return true;

    }


    //MAPS------------------------------------------------------------


    public GoogleMap getGoogleMap() {

        return googleMap;

    }


    public void setGoogleMap(GoogleMap googleMap) {

        this.googleMap = googleMap;

    }


    @SuppressLint("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case MY_LOCATION_PERMISSION_REQUEST:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)

                    getGoogleMap().setMyLocationEnabled(true);

                break;

        }

    }


    public void goToMyLocation() {

        if (getLastKnowLocation() != null) {

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(getLastKnowLocation().getLatitude(), getLastKnowLocation().getLongitude()))
                    .zoom(18f)
                    .bearing(0)
                    .build();

            getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        }

    }


    public ClusterManager getClusterManager(){

        if (clusterManager==null){

            DisplayMetrics metrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            clusterManager = new ClusterManager<>(this, getGoogleMap());

            clusterManager.setAlgorithm(new NonHierarchicalViewBasedAlgorithm<>(
                    metrics.widthPixels, metrics.heightPixels));

            clusterManager.setOnClusterClickListener(this);

            clusterManager.setOnClusterInfoWindowClickListener(this);

            clusterManager.setOnClusterItemClickListener(this);

            clusterManager.setOnClusterItemInfoWindowClickListener(this);

            clusterManager.setRenderer(new AddressClusterRenderer(
                    getApplicationContext(),
                    getGoogleMap(),
                    clusterManager));

        }

        return clusterManager;

    }


    public void onMapReady(GoogleMap googleMap) {

        setGoogleMap(googleMap);

        getGoogleMap().getUiSettings().setCompassEnabled(false);

        getGoogleMap().getUiSettings().setMapToolbarEnabled(false);

        getGoogleMap().getUiSettings().setMyLocationButtonEnabled(false);

        getGoogleMap().setMinZoomPreference(12f);

        getGoogleMap().setInfoWindowAdapter(getClusterManager().getMarkerManager());

        getGoogleMap().setOnMyLocationChangeListener(this);

        getGoogleMap().setOnCameraIdleListener(getClusterManager());

        getGoogleMap().setOnMarkerClickListener(getClusterManager());

        getGoogleMap().setOnInfoWindowClickListener(getClusterManager());

        addressViewModel = ViewModelProviders.of(this).get(AddressViewModel.class);

        addressViewModel.getAddresses().observe(MapActivity.this, addresses -> {

            getClusterManager().clearItems();

            if (addresses!=null && !addresses.isEmpty()){

                for (AddressModel address: addresses) {

                    AddressClusterItem addressClusterItem = new AddressClusterItem();

                    addressClusterItem.setIdentifier(address.getIdentifier());

                    addressClusterItem.setDenomination(address.getDenomination());

                    addressClusterItem.setNumber(address.getNumber());

                    addressClusterItem.setReference(address.getReference());

                    addressClusterItem.setDistrict(address.getDistrict());

                    addressClusterItem.setCity(StringUtils.formatCityWithState(address.getCity()));

                    addressClusterItem.setPostalCode(address.getPostalCode());

                    addressClusterItem.setPosition(new LatLng(address.getLatitude(), address.getLongitude()));

                    getClusterManager().addItem(addressClusterItem);

                }

            }

        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_PERMISSION_REQUEST);

        } else

            getGoogleMap().setMyLocationEnabled(true);

    }


    public boolean onClusterClick(Cluster<AddressClusterItem> cluster) {

        return false;

    }


    public void onClusterInfoWindowClick(Cluster<AddressClusterItem> cluster) {}


    public boolean onClusterItemClick(AddressClusterItem addressClusterItem) {

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN)

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        addressViewModel.selectedClusterItem.set(addressClusterItem);

        return true;

    }


    public void refreshBottomSheetBehavior(){

        new Handler().post(() -> {

            progressBar.setVisibility(View.VISIBLE);

            List<AddressEdificationModel> edifications = App.
                    getInstance().
                    getAddressRepository().
                    getEdificationsOfAddress(addressViewModel.selectedClusterItem.get().getIdentifier());

            List<AddressEdificationDwellerModel> dwellers = new ArrayList<>();

            if (!edifications.isEmpty())

                dwellers = App.getInstance().getAddressEdificationRepository().getDwellersOfAddressEdifications(
                        edifications.get(0).getAddress().getIdentifier(),
                        edifications.get(0).getEdification());

            //SET DATA TO EDIFICATIONS RECYCLER VIEW
            edificationAdapter.resetFocusedItem();

            edificationAdapter.setAddressEdifications(edifications);

            //SET DATA TO DWELLERS RECYCLER VIEW
            dwellerAdapter.setAddressEdificationDwellers(dwellers);

            //HIDE OR SHOW NO DATA FOUND
            noDataFoundTextView.setVisibility(edifications.isEmpty() ? View.VISIBLE : View.GONE);

            //WAIT TO HIDE PROGRESS. RECYCLERVIEW STILL WORKING...
            new Handler().postDelayed(() -> {

                progressBar.setVisibility(View.GONE);

                reloadEdifications = false;

            }, 500);

        });

    }


    public void onClusterItemInfoWindowClick(AddressClusterItem addressClusterItem) {}


    private void showAlertDialog(Messaging messaging){

        if (progressDialog.isShowing())

            progressDialog.dismiss();

        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);

        builder.setMessage(TextUtils.join("\n", messaging.getMessages()));

        builder.setCancelable(true);

        builder.setTitle(R.string.error);

        builder.setPositiveButton(android.R.string.ok,

                (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();

        alert.setCanceledOnTouchOutside(false);

        alert.show();

    }


    public void goToLocation(LatLng latLng, Integer address){

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(getGoogleMap().getMaxZoomLevel())
                .bearing(0)
                .build();

        getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {

            public void onFinish() {

                new Handler().postDelayed(() -> {

                    for (Marker marker : clusterManager.getMarkerCollection().getMarkers()) {

                        AddressClusterItem addressClusterItem = (AddressClusterItem) marker.getTag();

                        if (marker.getTag() != null && addressClusterItem.getIdentifier().equals(address)) {

                            marker.showInfoWindow();

                        }

                    }

                }, 100);

            }

            public void onCancel() {}

        });

    }


    public void onMyLocationChange(Location location) {

        if (getLastKnowLocation()==null) {

            //CHECK IF SOME MARKER PASSED AS PARAM ON INITIALIZE
            if (defaultAddress != -1){

                AddressModel addressModel = App.getInstance().getAddressRepository().getAddress(defaultAddress);

                if (addressModel != null)

                    goToLocation(new LatLng(addressModel.getLatitude(),
                                    addressModel.getLongitude()),
                            addressModel.getIdentifier());

                else

                    Toast.makeText(this, R.string.error_address_not_found, Toast.LENGTH_LONG).show();

            } else {

                getGoogleMap().animateCamera(
                        CameraUpdateFactory.
                                newLatLngZoom(new LatLng(location.getLatitude(),
                                        location.getLongitude()), 15f));

            }

        }

        setLastKnowLocation(location);

    }


    public Location getLastKnowLocation() {

        return lastKnowLocation;

    }


    public void setLastKnowLocation(Location lastKnowLocation) {

        this.lastKnowLocation = lastKnowLocation;

    }


    public void onEdificationClicked(AddressEdificationRecyclerViewAdapter.AddressEdificationViewHolder addressEdificationModel) {

        addressEdificationModel.itemView.setSelected(true);

        new Handler().post(() -> {

            List<AddressEdificationDwellerModel> dwellers = App.
                    getInstance().
                    getAddressEdificationRepository().
                    getDwellersOfAddressEdifications(
                            addressEdificationModel.addressEdificationModel.getAddress().getIdentifier(),
                            addressEdificationModel.addressEdificationModel.getEdification());

            dwellerAdapter.setAddressEdificationDwellers(dwellers);

//            noDataFoundTextView.setVisibility(dwellers.isEmpty() ? View.VISIBLE : View.GONE);

            //WAIT TO HIDE PROGRESS. RECYCLERVIEW STILL WORKING...
//            new Handler().postDelayed(() -> progressBar.setVisibility(View.GONE), 500);

        });

    }


    public void onEdificationLongClick(AddressEdificationRecyclerViewAdapter.AddressEdificationViewHolder addressEdificationModel) {}


    public void refresh(){

        progressDialog.setCancelable(false);

        progressDialog.setTitle("Aguarde");

        progressDialog.setMessage("Baixando atualização...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setIndeterminate(true);

        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_BASE_URL + "download/dataset",

                response -> {

                    DatasetBean datasetBean = gson.fromJson(response, DatasetBean.class);

                    new ImportAsyncTask<>(this).execute(datasetBean);

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
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(request);

    }


    public void onImportPreExecute() {

        progressDialog.setMessage("Importando dados...");

    }


    public void onImportProgressUpdate(Integer progress) {

        progressDialog.incrementProgressBy(progress);

    }


    public void onImportSuccess() {

        progressDialog.hide();

        Toast.makeText(this, R.string.download_sucess, Toast.LENGTH_LONG).show();

    }


    public void onImportFailure(Messaging messaging) {

        showAlertDialog(messaging);

    }


    public void onImportCancelled() {

        progressDialog.hide();

    }


    //////////////////////////////////
    //////////////////////////////////
    /// MARKERS //////////////////////


    public void onEditAddressPreExecute() {

        progressDialog.setCancelable(false);

        progressDialog.setTitle("Aguarde");

        progressDialog.setMessage("Editando marcador...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setIndeterminate(true);

        progressDialog.show();

    }


    public void onEditAddressProgressUpdate(int status) {

        //progressDialog.incrementProgressBy(status);

    }


    public void onEditAddressSuccess(Integer identifier) {

        Intent addIntent = new Intent(MapActivity.this, ModifiedAddressActivity.class);

        addIntent.putExtra(ModifiedAddressActivity.MODIFIED_ADDRESS_IDENTIFIER, identifier);

        startActivity(addIntent);

    }


    public void onEditAddressFailure(Messaging messaging) {

        showAlertDialog(messaging);

    }


    public void onEditAddressAddressCancelled() {

        progressDialog.hide();

    }


    //////////////////////////////////


    public void onCreateAddressPreExecute() {

        progressDialog.setCancelable(false);

        progressDialog.setTitle("Aguarde");

        progressDialog.setMessage("Criando marcador...");

        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progressDialog.setIndeterminate(true);

        progressDialog.show();

    }


    public void onCreateAddressProgressUpdate(int status) {

        //progressDialog.incrementProgressBy(status);

    }


    public void onCreateAddressSuccess(Integer identifier) {

        Intent addIntent = new Intent(MapActivity.this, ModifiedAddressActivity.class);

        addIntent.putExtra(ModifiedAddressActivity.MODIFIED_ADDRESS_IDENTIFIER, identifier);

        startActivity(addIntent);

    }


    public void onCreateAddressFailure(Messaging messaging) {

        showAlertDialog(messaging);

    }


    public void onCreateAddressAddressCancelled() {

        progressDialog.hide();

    }


    /// MARKERS //////////////////////
    //////////////////////////////////
    //////////////////////////////////


}