package com.example.iazis.museum;

import android.Manifest;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


public class map extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {
    GoogleMap googleMap;
    Circle myCircle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

       LatLng marker = new LatLng(-6.12144, 106.774);
        LatLng marker2 = new LatLng(-6.27119, 106.895);
        LatLng marker3 = new LatLng(-6.18561, 106.829);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 11));
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions().title("Museum Fatahillah").position(marker));
        googleMap.addMarker(new MarkerOptions().title("Museum Agung Sasono Adi Guno").position(marker2));
        googleMap.addMarker(new MarkerOptions().title("testing").position(marker3));


    }
}


