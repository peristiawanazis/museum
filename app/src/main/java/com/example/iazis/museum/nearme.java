package com.example.iazis.museum;

import android.Manifest;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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


public class nearme extends Fragment implements OnMapReadyCallback {
    GoogleMap googleMap;
    Circle myCircle;
    ArrayList<LatLng> locations;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearme, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);

        ListView lstItems = (ListView)getView().findViewById(R.id.listnear);

        ArrayList<String> prueba = new ArrayList<String>();
        prueba.add("Museum Fatahillah");
        prueba.add("testing");
        prueba.add("Museum Agung Sasono Adi Guno");

        ArrayAdapter<String> allItemsAdapter = new ArrayAdapter<String>(getActivity().getBaseContext(), android.R.layout.simple_list_item_1,prueba);

        lstItems.setAdapter(allItemsAdapter);
        lstItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Snackbar snackbar = Snackbar.make(view, "Direct", Snackbar.LENGTH_LONG)
                        .setAction("DONE", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                View sbView = snackbar.getView();
                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                snackbar.show();


            }

        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        LatLng marker = new LatLng(-6.12144, 106.774);
        LatLng marker2 = new LatLng(-6.27119, 106.895);
        LatLng marker3 = new LatLng(-6.18561, 106.829);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker3, 10));
        googleMap.setMyLocationEnabled(true);
        googleMap.addMarker(new MarkerOptions().title("Museum Fatahillah").position(marker));
        googleMap.addMarker(new MarkerOptions().title("Museum Agung Sasono Adi Guno").position(marker2));
        googleMap.addMarker(new MarkerOptions().title("testing").position(marker3));

        CircleOptions options = new CircleOptions();
        options.center(marker3);
        //Radius in meters
        options.radius(12000);
        options.fillColor(getResources()
                .getColor(R.color.wallet_holo_blue_light));
        options.strokeColor(getResources()
                .getColor(R.color.wallet_holo_blue_light));
        options.strokeWidth(10);
        googleMap.addCircle(options);



        googleMap.addPolyline(new PolylineOptions().geodesic(true)
                .add(new LatLng(-6.12144, 106.774))  // Sydney
                .add(new LatLng(-6.27119, 106.895))  // Fiji
                 // Hawaii

        );


    }





}


