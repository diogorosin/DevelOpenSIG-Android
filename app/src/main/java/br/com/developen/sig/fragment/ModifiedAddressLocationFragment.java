package br.com.developen.sig.fragment;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import br.com.developen.sig.R;
import br.com.developen.sig.activity.MapActivity;
import br.com.developen.sig.database.CityModel;
import br.com.developen.sig.database.LatLngModel;
import br.com.developen.sig.util.App;
import br.com.developen.sig.util.StringUtils;
import br.com.developen.sig.viewmodel.ModifiedAddressViewModel;

public class ModifiedAddressLocationFragment extends Fragment implements OnMapReadyCallback {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    private static final int SNAPSHOT_WIDTH = 300;

    private static final int SNAPSHOT_HEIGHT = 300;


    private ModifiedAddressViewModel modifiedAddressViewModel;

    private GoogleMap googleMap;

    private MapView mapView;

    private Marker marker;

    private boolean recreateSnapshot = false;

    private boolean updateAddress = false;


    GoogleMap.SnapshotReadyCallback snapshotCallback = snapshot -> {

        ContextWrapper cw = new ContextWrapper(getActivity());

        File directory = cw.getDir("images", Context.MODE_PRIVATE);

        String fileName = String.format("map_%s.jpg", String.valueOf(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER)));

        File path = new File(directory, fileName);

        FileOutputStream fileOutputStream = null;

        try {

            fileOutputStream = new FileOutputStream(path);

            Bitmap cropped = Bitmap.createBitmap(snapshot, snapshot.getWidth() / 2 - SNAPSHOT_WIDTH / 2, snapshot.getHeight() / 2 - SNAPSHOT_HEIGHT / 2, SNAPSHOT_WIDTH, SNAPSHOT_HEIGHT);

            cropped.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            try {

                fileOutputStream.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    };


    public static ModifiedAddressLocationFragment newInstance(Integer modifiedAddressIdentifier) {

        Bundle args = new Bundle();

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

        ModifiedAddressLocationFragment fragment = new ModifiedAddressLocationFragment();

        fragment.setArguments(args);

        return fragment;

    }


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_modified_address_location, container, false);

        mapView = v.findViewById(R.id.fragment_modified_address_location_mapview);

        mapView.onCreate(savedInstanceState);

        mapView.onResume();

        try {

            MapsInitializer.initialize(requireActivity());

        } catch (Exception e) {

            e.printStackTrace();

        }

        mapView.getMapAsync(this);

        return v;

    }


    public void onResume() {

        super.onResume();

        mapView.onResume();

    }


    public void onPause() {

        super.onPause();

        mapView.onPause();

        marker = null;

    }


    public void onDestroy() {

        super.onDestroy();

        mapView.onDestroy();

        marker = null;

    }


    public void onLowMemory() {

        super.onLowMemory();

        mapView.onLowMemory();

        marker = null;

    }


    public void onMapReady(GoogleMap googleMap) {

        setGoogleMap(googleMap);

        getGoogleMap().setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        getGoogleMap().getUiSettings().setCompassEnabled(false);

        getGoogleMap().getUiSettings().setZoomControlsEnabled(false);

        getGoogleMap().getUiSettings().setMapToolbarEnabled(false);

        getGoogleMap().getUiSettings().setMyLocationButtonEnabled(false);

        if (getActivity() instanceof GoogleMap.OnMyLocationChangeListener)

            getGoogleMap().setOnMyLocationChangeListener((GoogleMap.OnMyLocationChangeListener) getActivity());

        getGoogleMap().setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            public void onMarkerDragStart(Marker marker) {}
            public void onMarkerDrag(Marker marker) {}
            public void onMarkerDragEnd(Marker marker) {

                recreateSnapshot = true;

                updateAddress = true;

                setNewLocation(marker.getPosition());

            }

        });

        getGoogleMap().setOnMapClickListener(latLng -> {

            recreateSnapshot = true;

            updateAddress = true;

            setNewLocation(latLng);

        });

        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MapActivity.MY_LOCATION_PERMISSION_REQUEST);

        } else

            getGoogleMap().setMyLocationEnabled(true);

        getGoogleMap().setOnMapLoadedCallback(() -> getGoogleMap().snapshot(snapshotCallback));

        ModifiedAddressViewModel.Factory factory = new ModifiedAddressViewModel.Factory(
                requireActivity().getApplication(), getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER));

        modifiedAddressViewModel = ViewModelProviders.of(requireActivity(), factory).get(ModifiedAddressViewModel.class);

        modifiedAddressViewModel.latLng.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {

            public void onPropertyChanged(Observable sender, int propertyId) {

                if (sender != null) {

                    LatLngModel latLng = ((ObservableField<LatLngModel>) sender).get();

                    moveToLocation(new LatLng(latLng.getLatitude(), latLng.getLongitude()));

                }

            }

        });

        modifiedAddressViewModel.latLng.notifyChange();

    }


    public GoogleMap getGoogleMap() {

        return googleMap;

    }


    public void setGoogleMap(GoogleMap googleMap) {

        this.googleMap = googleMap;

    }


    private void moveToLocation(LatLng latLng) {

        if (marker == null) {

            marker = getGoogleMap().addMarker(
                    new MarkerOptions().
                            position(latLng).
                            draggable(true));

        } else {

            if (!marker.getPosition().equals(latLng))

                marker.setPosition(latLng);

        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(18f)
                .bearing(0)
                .build();

        getGoogleMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {

            public void onFinish() {

                if (recreateSnapshot)

                    getGoogleMap().snapshot(snapshotCallback);

                recreateSnapshot = false;

                if (updateAddress) {

                    Runnable updateAddressRunnable = () -> {

                        try {

                            Geocoder geocoder = new Geocoder(requireActivity().getBaseContext());

                            List<Address> addresses = geocoder.
                                    getFromLocation(latLng.latitude, latLng.longitude, 1);

                            if (addresses != null && addresses.size() > 0) {

                                Address address = addresses.get(0);

                                modifiedAddressViewModel.denomination.set(address.getThoroughfare());

                                modifiedAddressViewModel.number.set(address.getFeatureName());

                                modifiedAddressViewModel.district.set(address.getSubLocality());

                                modifiedAddressViewModel.postalCode.set(address.getPostalCode());

                                CityModel cityModel = App.getInstance().
                                        getCityRepository().
                                        findByCityStateCountry(address.getSubAdminArea(),
                                                address.getAdminArea(),
                                                address.getCountryName());

                                modifiedAddressViewModel.city.set(StringUtils.formatCityWithState(cityModel));

                            }

                        } catch (IOException ignored) {}

                    };

                    updateAddressRunnable.run();

                }

                updateAddress = false;

            }

            public void onCancel() {}

        });

    }


    private void setNewLocation(LatLng latLng){

        LatLngModel latLngModel = new LatLngModel();

        latLngModel.setLatitude(latLng.latitude);

        latLngModel.setLongitude(latLng.longitude);

        modifiedAddressViewModel.latLng.set(latLngModel);

    }


    public void moveMarkerToLocation(LatLng latLng){

        recreateSnapshot = true;

        updateAddress = true;

        setNewLocation(latLng);

    }


}