package br.com.developen.sig.widget;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import br.com.developen.sig.util.Constants;

public class MarkerClusterRenderer extends DefaultClusterRenderer<MarkerClusterItem> {

    public MarkerClusterRenderer(Context context, GoogleMap googleMap, ClusterManager<MarkerClusterItem> clusterManager) {

        super(context, googleMap, clusterManager);

        setMinClusterSize(2);

    }

    protected void onClusterItemRendered(MarkerClusterItem markerClusterItem, Marker marker) {

        marker.setTag(markerClusterItem);

        marker.setIcon(BitmapDescriptorFactory.defaultMarker(markerClusterItem.getType().equals(Constants.MARKER_TYPE_ADDRESS) ? BitmapDescriptorFactory.HUE_GREEN : BitmapDescriptorFactory.HUE_ORANGE));

    }

}