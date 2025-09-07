package com.example.gpslocationapp;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.gpslocationapp.databinding.ActivityMapSearchBinding;

import java.io.IOException;
import java.util.List;

public class MapSearchActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapSearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Button backBtn = findViewById(R.id.searchBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapSearch);
        mapFragment.getMapAsync(this);



        SearchView searchView = findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                String locationSearchString = searchView.getQuery().toString();
                List<Address> locations = null;
                if(locationSearchString != null || !locationSearchString.equals("")){
                    Log.d("SearchString",locationSearchString);
                    Geocoder geocoder = new Geocoder(MapSearchActivity.this);
                    try {
                        locations = geocoder.getFromLocationName(locationSearchString,1);
                    } catch (IOException e) {
                        // retry once
                        try {
                            Thread.sleep(500);
                            locations = geocoder.getFromLocationName(locationSearchString, 1);
                        } catch (Exception ignored) {}
                    }
                    
                    if(locations != null && !locations.isEmpty()){
                        Address address = locations.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(locationSearchString));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                    }else{
                        Toast.makeText(getApplicationContext(),"Cannot search Place", Toast.LENGTH_SHORT).show();
                    }
                    
                }
                return false;
            }



            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });


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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}