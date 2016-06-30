package com.example.iazis.museum;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
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
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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

import static com.google.android.gms.internal.zzir.runOnUiThread;


public class nearme extends Fragment  {
    Circle myCircle;
    ArrayList<LatLng> locations;
    private static final String LOG_TAG = "ExampleApp";
    private static final String SERVICE_URL = "http://arifmuseum.esy.es/peta.php";
    MapView mMapView;
    private GoogleMap googleMap;
    ListView lv;
    ArrayList<museum> actorsList;
    infoadapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_nearme, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        lv=(ListView) v.findViewById(R.id.listnear);

        actorsList = new ArrayList<museum>();
        new JSONAsynTask().execute("http://arifmuseum.esy.es/peta.php");
        adapter = new infoadapter(getActivity(), R.layout.isi_info_listview, actorsList);
        lv.setAdapter(adapter);

        mMapView.onResume();

        return v;
    }

    class JSONAsynTask extends AsyncTask<String, Void, Boolean> {
        String result;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                HttpGet httppost = new HttpGet(urls[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(httppost);

                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    //  String data = EntityUtils.toString(entity);
                    InputStream inputStream = entity.getContent();
                    String a;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                    a = bufferedReader.readLine(); inputStream.close();
                    // JSONObject jsono = new JSONObject(data);
                    JSONArray jarray = new JSONArray(a);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        museum museum = new museum();
                        museum.setmuseum_name(object.getString("museum_name"));
                        museum.setregional_name(object.getString("regional_name"));
                        museum.setmuseum_desc(object.getString("museum_desc"));
                       actorsList.add(museum);


                    }
                    return true;
                }

            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;

        }

        protected void onPostExecute(Boolean result) {

            dialog.dismiss();
            adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }

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
            googleMap.addMarker(new MarkerOptions()
                            .title(jsonObj.getString("museum_name"))
                            .snippet(jsonObj.getString("museum_desc"))
                            .position(new LatLng(
                                    jsonObj.getDouble("museum_lat"),
                                    jsonObj.getDouble("museum_long")
                            ))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mrk))

            );

            moveToMyLocation();

        }

    }


    private void moveToMyLocation() {
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

//            googleMap.setMyLocationEnabled(true);
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(s, ss))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            //.snippet("My Current Location")
                    .snippet(new String("asdfasd")));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(s,ss), 12));
            googleMap.addCircle(new CircleOptions()
                    .center(new LatLng(s, ss))
                    .radius(2500)   //set radius in meters
                    .fillColor(0x00000000)   //default
                    .strokeColor(Color.BLUE)
                    .strokeWidth(5));


        }



    }

  }



