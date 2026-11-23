package com.krish.rentmate.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.List;
import java.util.Locale;

public class LocationHelper {

    public interface LocationResultCallback {
        void onSuccess(Location location);
        void onError(String errorMessage);
    }

    public interface AddressResultCallback {
        void onSuccess(String readableAddress);
        void onError(String errorMessage);
    }

    public static boolean hasLocationPermission(Context context) {
        if (context == null) return false;
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean isLocationEnabled(Context context) {
        if (context == null) return false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) return false;
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static void getCurrentLocation(Context context, LocationResultCallback callback) {
        if (!hasLocationPermission(context)) {
            callback.onError("Location permission not granted.");
            return;
        }

        if (!isLocationEnabled(context)) {
            callback.onError("Location services are disabled on device.");
            return;
        }

        try {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
            CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

            fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            callback.onSuccess(location);
                        } else {
                            // Fallback to last known location
                            fusedLocationClient.getLastLocation().addOnSuccessListener(lastLoc -> {
                                if (lastLoc != null) {
                                    callback.onSuccess(lastLoc);
                                } else {
                                    callback.onError("Unable to retrieve location.");
                                }
                            }).addOnFailureListener(e -> callback.onError("Failed to get location: " + e.getLocalizedMessage()));
                        }
                    })
                    .addOnFailureListener(e -> callback.onError("Location error: " + e.getLocalizedMessage()));
        } catch (SecurityException e) {
            callback.onError("Permission error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            callback.onError("Location error: " + e.getLocalizedMessage());
        }
    }

    public static void getAddressFromCoordinates(Context context, double lat, double lng, AddressResultCallback callback) {
        new Thread(() -> {
            try {
                if (Geocoder.isPresent()) {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(lat, lng, 1, new Geocoder.GeocodeListener() {
                            @Override
                            public void onGeocode(List<Address> addresses) {
                                parseAddressList(addresses, callback);
                            }

                            @Override
                            public void onError(String errorMessage) {
                                callback.onError(errorMessage);
                            }
                        });
                    } else {
                        List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                        parseAddressList(addresses, callback);
                    }
                } else {
                    callback.onError("Geocoder not available.");
                }
            } catch (Exception e) {
                callback.onError(e.getLocalizedMessage() != null ? e.getLocalizedMessage() : "Address resolution failed.");
            }
        }).start();
    }

    private static void parseAddressList(List<Address> addresses, AddressResultCallback callback) {
        if (addresses != null && !addresses.isEmpty()) {
            Address address = addresses.get(0);
            String locality = address.getLocality() != null ? address.getLocality() : address.getSubLocality();
            String city = address.getAdminArea() != null ? address.getAdminArea() : "";
            String full = (locality != null ? locality : "") + (city.isEmpty() ? "" : ", " + city);
            callback.onSuccess(full.isEmpty() ? "Location (" + String.format(Locale.US, "%.2f, %.2f", address.getLatitude(), address.getLongitude()) + ")" : full);
        } else {
            callback.onError("No address found.");
        }
    }

    public static float calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }

    public static String formatDistance(float distanceMeters) {
        if (distanceMeters < 1000) {
            return String.format(Locale.US, "%d m away", (int) distanceMeters);
        } else {
            return String.format(Locale.US, "%.1f km away", distanceMeters / 1000f);
        }
    }
}