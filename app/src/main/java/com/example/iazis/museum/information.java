package com.example.iazis.museum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class information extends android.app.Fragment {
    ListView lv;
    public ArrayList<museum> actorsList;
    infoadapter adapter;
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
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View vw=inflater.inflate(R.layout.info_listview, container, false);
        lv=(ListView) vw.findViewById(R.id.listView1);

        actorsList = new ArrayList<museum>();
        new JSONAsynTask().execute("http://arifmuseum.esy.es/peta.php");
        adapter = new infoadapter(getActivity(), R.layout.isi_info_listview, actorsList);
        lv.setAdapter(adapter);
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
                bundle.putString(KEY_JAMBUKA, actorsList.get(position).getmuseum_open());
                bundle.putString(KEY_JAMTUTUP, actorsList.get(position).getmuseum_close());
                bundle.putString(KEY_INFO, actorsList.get(position).getmuseum_info());
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
                Intent i = new Intent(information.this.getActivity(), InfoMuseum.class);
                i.putExtras(bundle);
                startActivity(i);


            }
        });
        return  vw;
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
                        museum.setLatitude(object.getDouble("museum_lat"));
                        museum.setLongitude(object.getDouble("museum_long"));
                        museum.setregional_name(object.getString("regional_name"));
                        museum.setmuseum_open(object.getString("museum_open"));
                        museum.setmuseum_close(object.getString("museum_close"));
                        museum.setmuseum_info(object.getString("museum_info"));
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



}
