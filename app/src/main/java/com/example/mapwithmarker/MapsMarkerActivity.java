package com.example.mapwithmarker;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener {

    private GoogleMap mMap;
    private int MY_LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return(true);
            case R.id.satellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return(true);
            case R.id.hybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return(true);
            case R.id.unzoom:
                mMap.animateCamera(CameraUpdateFactory.zoomTo(1));
                return(true);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-33.852, 151.211);
        LatLng winnipeg = new LatLng(49.8, -97.05);
        LatLng amsterdam = new LatLng(52.378, 4.9);

        googleMap.addMarker(new MarkerOptions()
                 .position(sydney)
                 .title("Sydney")
                 .snippet("Land Down Under")
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.roling_pin)));

        googleMap.addMarker(new MarkerOptions()
                 .position(winnipeg)
                 .title("Winnipeg")
                 .snippet("My Home Town")
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.redpin)));

        googleMap.addMarker(new MarkerOptions()
                 .position(amsterdam)
                 .title("Amsterdam")
                 .snippet("Sis Lives Here")
                 .icon(BitmapDescriptorFactory.fromResource(R.drawable.another_pin)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(winnipeg));

        turnOnMyLocation();
    }

    public void turnOnMyLocation() {
        // Show rationale and request permission.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("Jody", "enabling my location (permissions granted earlier)");
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(MapsMarkerActivity.this);
        } else {
            ActivityCompat.requestPermissions(MapsMarkerActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals(android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //either check to see if permission is available, or handle a potential SecurityException before calling mMap.setMyLocationEnabled
                try {
                    Log.d("Jody", "enabling my location (permissions were just granted)");
                    mMap.setMyLocationEnabled(true);
                    mMap.setOnMyLocationButtonClickListener(MapsMarkerActivity.this);
                } catch(SecurityException e) {
                    Log.d("Jody", "SecurityException in MapsActivity.onRequestPermissionsResult: " + e.getMessage());
                }
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(MapsMarkerActivity.this, "Permission to access your location was denied so your location cannot be displayed on the map.", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }
}
