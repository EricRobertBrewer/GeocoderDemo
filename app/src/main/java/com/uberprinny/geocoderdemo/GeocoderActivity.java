package com.uberprinny.geocoderdemo;

import android.location.Address;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class GeocoderActivity extends FragmentActivity implements ForwardGeocoderFragment.OnForwardGeocoderListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoder);
        setUpMapIfNeeded();

        ForwardGeocoderFragment forwardGeocoderFragment = ForwardGeocoderFragment.newInstance("");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_geocoder_frame, forwardGeocoderFragment, TAG_FORWARD).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.main_map_fragment);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                    if (mMap != null) {
                        setUpMap();
                    }
                }
            });
        }
    }

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private void setUpMap() {
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                ForwardGeocoderFragment forward = (ForwardGeocoderFragment) getSupportFragmentManager().findFragmentByTag(TAG_FORWARD);
                if (forward == null) {
                    return false;
                }
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                ForwardGeocoderFragment forward = (ForwardGeocoderFragment) getSupportFragmentManager().findFragmentByTag(TAG_FORWARD);
                if (forward != null) {
                }
            }
        });
    }

    private static final String TAG_FORWARD = "forward_geocoder";

    @Override
    public void onForwardGeocoderFinished(String description, List<Address> addresses) {
        if (mMap != null) {
            mMap.clear();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (int i = 0; i < addresses.size(); i++) {
                double latitude = addresses.get(i).getLatitude();
                double longitude = addresses.get(i).getLongitude();
                LatLng point = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(point));

                builder.include(point);
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15), null);
        }
    }
}
