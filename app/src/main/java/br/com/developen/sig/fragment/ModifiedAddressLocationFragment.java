package br.com.developen.sig.fragment;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import br.com.developen.sig.R;
import br.com.developen.sig.repository.ModifiedAddressRepository;

public class ModifiedAddressLocationFragment extends Fragment implements OnMapReadyCallback {


    private static final String ARG_MODIFIED_ADDRESS_IDENTIFIER = "ARG_MODIFIED_ADDRESS_IDENTIFIER";

    private ModifiedAddressRepository modifiedAddressRepository;

    private LocationListener locationListener;

    private GoogleMap googleMap;

    private MapView mapView;

    private Marker marker;

    private boolean recreateSnapshot = false;


    public static ModifiedAddressLocationFragment newInstance(LocationListener locationListener, Integer modifiedAddressIdentifier) {

        Bundle args = new Bundle();

        ModifiedAddressLocationFragment fragment = new ModifiedAddressLocationFragment();

        fragment.setLocationListener(locationListener);

        args.putInt(ARG_MODIFIED_ADDRESS_IDENTIFIER, modifiedAddressIdentifier);

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

            MapsInitializer.initialize(getActivity().getApplicationContext());

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

        getGoogleMap().setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            public void onMarkerDragStart(Marker marker) {}
            public void onMarkerDrag(Marker marker) {}
            public void onMarkerDragEnd(Marker marker) {

                if (getLocationListener() != null) {

                    recreateSnapshot = true;

                    getLocationListener().onLocationChanged(marker.getPosition());

                }

            }

        });

        getGoogleMap().setOnMapClickListener(latLng -> {

            if (getLocationListener() != null) {

                recreateSnapshot = true;

                getLocationListener().onLocationChanged(latLng);

            }

        });

        modifiedAddressRepository = ViewModelProviders.of(getActivity()).get(ModifiedAddressRepository.class);

        modifiedAddressRepository.getLatLng(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER)).observe(this, latLngModel -> {

            if (latLngModel != null)

                moveToLocation(new LatLng(latLngModel.getLatitude(),
                        latLngModel.getLongitude()));

        });

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

                        if (recreateSnapshot) {

                            GoogleMap.SnapshotReadyCallback callback = snapshot -> {

                                ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());

                                File directory = cw.getDir("images", Context.MODE_PRIVATE);

                                String fileName = String.format("map_%s.jpg", String.valueOf(getArguments().getInt(ARG_MODIFIED_ADDRESS_IDENTIFIER)));

                                File path = new File(directory, fileName);

                                FileOutputStream fos = null;

                                try {

                                    fos = new FileOutputStream(path);

                                    Bitmap cropped = Bitmap.createBitmap(snapshot, snapshot.getWidth() / 2 - 100, snapshot.getHeight() / 2 - 100, 200, 200);

                                    cropped.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                                } catch (Exception e) {

                                    e.printStackTrace();

                                } finally {

                                    try {

                                        fos.close();

                                    } catch (IOException e) {

                                        e.printStackTrace();

                                    }

                                }

                            };

                            getGoogleMap().snapshot(callback);

                        }

                        recreateSnapshot = false;

                    }

                    public void onCancel() {}

                });

    }


    public LocationListener getLocationListener() {

        return locationListener;

    }


    public void setLocationListener(LocationListener locationListener) {

        this.locationListener = locationListener;

    }


    public interface LocationListener {

        void onLocationChanged(LatLng latLng);

    }


}