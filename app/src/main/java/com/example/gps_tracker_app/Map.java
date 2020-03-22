package com.example.gps_tracker_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Map extends FragmentActivity implements OnMapReadyCallback {

    public double la = 54.683333;
    public double lg = 35.283333;
    int TAG_CODE_PERMISSION_LOCATION;
    int DO_NOTHING;
    ArrayList<Loc> location_list;
    LocationManager locationManager;
    private GoogleMap mMap;

    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        } else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            // dist in km
            dist = dist * 1.609344;
            return (dist);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        location_list = (ArrayList<Loc>) getIntent().getSerializableExtra("list");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationListener locationListener = new MyLocationListener();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            DO_NOTHING = 0;
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);

        }

        LatLng vilnius = new LatLng(la, lg);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vilnius, 10));
    }

    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            lg = loc.getLongitude();
            la = loc.getLatitude();

            LatLng current_location = new LatLng(la, lg);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(current_location));

            if (location_list.size() == 0) {
                throw new IllegalStateException("List is empty");
            }

            for (int i = 0; i < location_list.size(); i++) {

                if (!location_list.get(i).isVisited()) {

                    double current_distance = distance(la, lg, location_list.get(i).getLatitude(), location_list.get(i).getLongitude());

                    if (current_distance < location_list.get(i).getKmDistance()) {

                        location_list.get(i).setVisited(true);

                        Intent intent;

                        switch (location_list.get(i).getName()) {
                            case "cathedral":
                                intent = new Intent(getBaseContext(), Cathedral.class);
                                break;
                            case "riders":
                                intent = new Intent(getBaseContext(), Riders.class);
                                break;
                            case "work":
                                intent = new Intent(getBaseContext(), Work.class);
                                break;
                            case "home":
                                intent = new Intent(getBaseContext(), Home.class);
                                break;
                            default:
                                throw new IllegalStateException("Unexpected value: " + location_list.get(i).getName());
                        }

                        intent.putExtra("list", location_list);
                        startActivity(intent);

                    }
                }

            }

        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

}
