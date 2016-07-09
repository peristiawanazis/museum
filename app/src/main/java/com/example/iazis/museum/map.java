package com.example.iazis.museum;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static com.google.android.gms.internal.zzir.runOnUiThread;


public class map extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    private static final String LOG_TAG = "ExampleApp";
    private static final String SERVICE_URL = "http://arifmuseum.esy.es/peta.php";
    MapView mMapView;
    private GoogleMap googleMap;
    private LatLng myLocation;
    Location location;
    ArrayList<museum> actorsList;


    public static final String	KEY_NAMA	= "museum_nama";
    public static final String	KEY_LAT_TUJUAN	= "lat_tujuan";
    public static final String	KEY_LNG_TUJUAN	= "lng_tujuan";
    public static final String	KEY_LAT_ASAL	= "museum_lat";
    public static final String	KEY_LNG_ASAL	= "museum_long";
    public static final String	KEY_REGIONAL	= "regional_name";
    public static final String	KEY_DESC	= "museum_desc";
    public static final String	KEY_JAMBUKA	= "museum_open";
    public static final String	KEY_JAMTUTUP	= "museum_close";
    public static final String	KEY_INFO	= "museum_info";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_gmaps, container,
                false);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        mMapView = (MapView) v.findViewById(R.id.mapView);

        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately


        return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        // mMapView.onResume();
        //setUpMapIfNeeded();
        setUpMap();


    }

    private void setUpMapIfNeeded() {
        if (mMapView == null) {
            googleMap = mMapView.getMap();
            if (mMapView != null) {
                setUpMap();

            }
        }
    }
    private void setUpMap() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    retrieveAndAddCities();

                } catch (IOException e) {
                    Log.e(LOG_TAG, "Cannot retrive cities", e);
                    return;
                }
            }
        }).start();
    }

    protected void retrieveAndAddCities() throws IOException {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = new URL(SERVICE_URL);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Read the JSON data into the StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                json.append(buff, 0, read);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to service", e);
            throw new IOException("Error connecting to service", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        // Create markers for the city data.
        // Must run this on the UI thread since it's a UI operation.
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    createMarkersFromJson(json.toString());
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Error processing JSON", e);
                }
            }
        });
    }

    void createMarkersFromJson(String json) throws JSONException {
        googleMap = mMapView.getMap();
        // De-serialize the JSON string into an array of city objects
        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            // Create a marker for each city in the JSON data.
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            museum museum = new museum();
            googleMap.addMarker(new MarkerOptions()
                            .title(jsonObj.getString("museum_name"))
                            .snippet(jsonObj.getString("museum_desc"))
                            .position(new LatLng(
                                    jsonObj.getDouble("museum_lat"),
                                    jsonObj.getDouble("museum_long")
                            )).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            );
            museum.setmuseum_info(jsonObj.getString("museum_info"));

            moveToMyLocation();

            googleMap.setOnInfoWindowClickListener(this);
        }
 }

    private void moveToMyLocation() {

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        if(location!=null){

            Double s = location.getLatitude();
            Double ss = location.getLongitude();

//            googleMap.setMyLocationEnabled(true);
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s, ss))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                .snippet(new String("myloc")));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(s, ss), 13));


        }

    }

    @Override
    public void onInfoWindowClick(Marker marker)
    {

        // marker id -> m0, m1, m2 dst..
        String id = marker.getId();
        id = id.substring(1);
        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        if(location!=null){

            Double s = location.getLatitude();
            Double ss = location.getLongitude();
            Double as =  marker.getPosition().latitude;
            Double sa =  marker.getPosition().longitude;
            String sss = marker.getTitle();
            String asdsad = marker.getSnippet();
            Bundle bundle = new Bundle();

             bundle.putString(KEY_DESC, asdsad);

            bundle.putString(KEY_NAMA, sss);
            bundle.putDouble(KEY_LAT_TUJUAN, as);
            bundle.putDouble(KEY_LNG_TUJUAN, sa);
            bundle.putDouble(KEY_LAT_ASAL, s);
            bundle.putDouble(KEY_LNG_ASAL, ss);
            bundle.putString(KEY_REGIONAL, sss);

           // Intent i = new Intent(map.this, InfoMuseum.class);
            Intent i = new Intent(map.this.getActivity(), InfoMuseum.class);
            i.putExtras(bundle);
            startActivity(i);

        }
    }


}



