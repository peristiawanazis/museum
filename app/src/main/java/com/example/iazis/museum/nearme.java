package com.example.iazis.museum;

import android.Manifest;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.model.Marker;
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
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.google.android.gms.internal.zzir.runOnUiThread;


public class nearme extends Fragment   {
    Circle myCircle;
    ArrayList<LatLng> locations;
    private static final String LOG_TAG = "ExampleApp";
    private static final String SERVICE_URL = "http://arifmuseum.esy.es/peta.php";
    MapView mMapView;
    private GoogleMap googleMap;
    ListView lv;
    ArrayList<museum> actorsList;
    infoadapter adapter;
    public static final String	KEY_NAMA	= "museum_nama";
    public static final String	KEY_LAT_TUJUAN	= "lat_tujuan";
    public static final String	KEY_LNG_TUJUAN	= "lng_tujuan";
    public static final String	KEY_LAT_ASAL	= "museum_lat";
    public static final String	KEY_LNG_ASAL	= "museum_long";
    public static final String	KEY_REGIONAL	= "regional_name";
    public static final String	KEY_DESC	= "museum_desc";

    private static final LatLng POINTA = new LatLng(-6.231143, 106.819085);
    private static final LatLng POINTB = new LatLng(-6.231143, 106.819085);
    private static final LatLng POINTC = new LatLng(-6.231143, 106.819085);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_nearme, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        lv=(ListView) v.findViewById(R.id.listnear);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                moveToMyLocation();

            }
        });
        actorsList = new ArrayList<museum>();
       // new JSONAsynTask().execute("http://arifmuseum.esy.es/peta.php");

        lv.setClickable(true);

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


        }
        final Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(KEY_REGIONAL, actorsList.get(position).getregional_name());
                bundle.putString(KEY_DESC, actorsList.get(position).getmuseum_desc());
                String sss = actorsList.get(position).getmuseum_name();
                Double as = actorsList.get(position).getLatitude();
                Double sa = actorsList.get(position).getLongitude();
                Double s = location.getLatitude();
                Double ss = location.getLongitude();
                bundle.putString(KEY_NAMA, sss);
                bundle.putDouble(KEY_LAT_TUJUAN, as);
                bundle.putDouble(KEY_LNG_TUJUAN, sa);
                bundle.putDouble(KEY_LAT_ASAL, s);
                bundle.putDouble(KEY_LNG_ASAL, ss);

                // Intent i = new Intent(map.this, InfoMuseum.class);
                Intent i = new Intent(nearme.this.getActivity(), InfoMuseum.class);
                i.putExtras(bundle);
                startActivity(i);


            }
        });

        mMapView.onResume();

        return v;
    }

    class JSONAsynTask extends AsyncTask<String, Void, Boolean> {
        String result;
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
         /*   super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false); */
        }

        @Override
        protected Boolean doInBackground(String... urls) {
          /*  try {
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
                        museum.setLatitude(object.getDouble("museum_lat"));
                        museum.setLongitude(object.getDouble("museum_long"));
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
            }*/
            return false;

        }

        protected void onPostExecute(Boolean result) {

           /* dialog.dismiss();
            adapter.notifyDataSetChanged();
            if(result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
*/
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
           Marker dsa = googleMap.addMarker(new MarkerOptions()
                            .title(jsonObj.getString("museum_name"))
                            .snippet(jsonObj.getString("museum_desc"))
                            .position(new LatLng(
                                    jsonObj.getDouble("museum_lat"),
                                    jsonObj.getDouble("museum_long")
                            ))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mrk))

            );
            googleMap.getUiSettings().setMapToolbarEnabled(false);
            moveToMyLocation();
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

            Location X = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

            Double s = X.getLatitude();
            Double ss = X.getLongitude();
            Location asdas = new Location("SD");
            asdas.setLatitude(s);
            asdas.setLongitude(ss);
            Location target = new Location("target");

                target.setLatitude(dsa.getPosition().latitude);
                target.setLongitude(dsa.getPosition().longitude);
                BigDecimal _bdDistance;
                float distance = asdas.distanceTo(target);
                _bdDistance = round(distance,2);
                String _strDistance = _bdDistance.toString();

                if(asdas.distanceTo(target) < 2500) {
                    for(String dfgdfgd : new String[]{dsa.getTitle()}) {
                        //Toast.makeText(getActivity(), ""+dfgdfgd, Toast.LENGTH_SHORT).show();


                        actorsList.isEmpty();
                        museum museum = new museum();
                        museum.setmuseum_name(dfgdfgd);
                        museum.setregional_name(dfgdfgd);
                        museum.setmuseum_desc(dfgdfgd);
                        museum.setLatitude(dsa.getPosition().latitude);
                        museum.setLongitude(dsa.getPosition().longitude);

                        adapter = new infoadapter(getActivity(), R.layout.isi_info_listview, actorsList);
                        lv.setAdapter(adapter);

                        actorsList.add(museum);

                    }

                }



        }

    }




    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
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
                    .fillColor(0x30ff0000)   //default
                    .strokeColor(Color.BLUE).zIndex(8)
                    .strokeWidth(5));


        }



    }

  }



