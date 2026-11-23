package com.krish.rentmate.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.krish.rentmate.R;
import com.krish.rentmate.models.Property;

import java.util.List;
import java.util.Locale;

public class MapUtils {

    public static boolean isApiKeyConfigured(Context context) {
        if (context == null) return false;
        String key = context.getString(R.string.google_maps_key);
        return !TextUtils.isEmpty(key) && !"YOUR_GOOGLE_MAPS_API_KEY_HERE".equals(key);
    }

    public static void populatePropertyMarkers(GoogleMap map, List<Property> properties, OnMarkerClickListener listener) {
        if (map == null || properties == null) return;
        map.clear();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        boolean hasValidMarker = false;

        for (Property p : properties) {
            if (p.getLatitude() != 0.0 || p.getLongitude() != 0.0) {
                LatLng pos = new LatLng(p.getLatitude(), p.getLongitude());
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(pos)
                        .title(p.getTitle())
                        .snippet(String.format(Locale.US, "₹%,d/mo • %s", p.getRent(), p.getType()))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                if (marker != null) {
                    marker.setTag(p);
                }
                builder.include(pos);
                hasValidMarker = true;
            }
        }

        if (hasValidMarker) {
            try {
                map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
            } catch (Exception e) {
                // If map padding/size issue on load, default to first marker zoom
                if (!properties.isEmpty() && (properties.get(0).getLatitude() != 0 || properties.get(0).getLongitude() != 0)) {
                    LatLng defaultPos = new LatLng(properties.get(0).getLatitude(), properties.get(0).getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPos, 12f));
                }
            }
        }

        map.setOnInfoWindowClickListener(marker -> {
            if (marker.getTag() instanceof Property && listener != null) {
                listener.onPropertyMarkerClick((Property) marker.getTag());
            }
        });
    }

    public interface OnMarkerClickListener {
        void onPropertyMarkerClick(Property property);
    }
}