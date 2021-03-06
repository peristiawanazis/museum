package com.example.iazis.museum;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import java.net.URL;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

public class directionold extends FragmentActivity  implements GoogleMap.OnMyLocationChangeListener {
    private final String URL = "http://maps.googleapis.com/maps/api/directions/json?";
    private LatLng start;
    private LatLng end;
    private String nama;
    private String jarak;


    private GoogleMap map;
    private jsonhelperold json;
    private ProgressDialog pDialog;
    private List listDirections;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        json = new jsonhelperold();
        setupMapIfNeeded();

        Bundle b = getIntent().getExtras();
        if (b != null) {
            start = new LatLng(b.getDouble(com.example.iazis.museum.map.KEY_LAT_ASAL), b.getDouble(com.example.iazis.museum.map.KEY_LNG_ASAL));
            end = new LatLng(b.getDouble(com.example.iazis.museum.map.KEY_LAT_TUJUAN), b.getDouble(com.example.iazis.museum.map.KEY_LNG_TUJUAN));
            nama = b.getString(com.example.iazis.museum.map.KEY_NAMA);

        }



        new AsyncTaskDirection().execute();
    }

    private void setupMapIfNeeded() {
        if (map == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager
                    .findFragmentById(R.id.mapsdirections);
            map = supportMapFragment.getMap();

            if (map != null) {
                setupMap();
            }
        }

    }

    private void setupMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(this);
        moveToMyLocation();
    }

    private void moveToMyLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 11));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume()
    {
// TODO Auto-generated method stub
        super.onResume();
        int resCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resCode != ConnectionResult.SUCCESS)
        {
            GooglePlayServicesUtil.getErrorDialog(resCode, this, 1);
        }
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    private class AsyncTaskDirection extends AsyncTask<String, Void, Boolean> {

      @Override
        protected Boolean doInBackground(String... params) {
            String uri = URL
                    + "origin=" + start.latitude + "," + start.longitude
                    + "&destination=" + end.latitude + "," + end.longitude
                    + "&sensor=true&mode=driving&alternatives=true&units=metric";

            JSONObject jObject = json.getJSONFromURL(uri);
            listDirections = json.getDirection(jObject);

            return null;
        }

        @Override
        protected void onPreExecute()
        {
// TODO Auto-generated method stub
            super.onPreExecute();
            pDialog = new ProgressDialog(directionold.this);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
// TODO Auto-generated method stub
            super.onPostExecute(result);
            pDialog.dismiss();
            gambarDirection();


    }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
            }finally{
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

    public void gambarDirection()
    {
        PolylineOptions line = new PolylineOptions().width(9).color(Color.RED);
        for (int i = 0; i < listDirections.size(); i++) {
            line.add((LatLng) listDirections.get(i)); } map.addPolyline(line);
        // tambah marker di posisi end
        map.addMarker(new MarkerOptions()
                        .position(end)
                        .title(nama)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.mrk))

         );
         String sd = end.toString();

        TextView a = (TextView) findViewById(R.id.isijarakdjikstra)   ;
        //TextView b = (TextView) findViewById(R.id.isijarak)   ;
        String numbers = sd.substring(15, 16);
        Integer xc = Integer.parseInt(numbers);
        Integer cc = xc+1;
        a.setText(numbers+"km");
      //  b.setText(cc+"km");
    }


}}
