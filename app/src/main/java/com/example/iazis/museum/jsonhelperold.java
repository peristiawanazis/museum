package com.example.iazis.museum;

import android.location.Location;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class jsonhelperold {
    private InputStream is				= null;
    private JSONObject jsonObject		= null;
    private String			json			= "";
    ArrayList<museum> actorsList;

    private final String TAG_TEMPATMAKAN = "museum";
    private final String museum_id = "museum_id";
    private final String museum_name = "museum_name";
    private final String museum_price = "museum_price";
    private final String museum_desc = "museum_desc";
    private final  String museum_open = "museum_open";
    private final String museum_close = "museum_close";
    private final String regional_id = "regional_id";
    private final String museum_foto = "museum_foto";
    private final String museum_temp = "museum_temp";
    private final String regional_name = "regional_name";
    private final String latitude = "latitude";
    private final String longitude = "longitude";


    private final String	TAG_LAT	= "lat";
    private final String	TAG_LNG	= "lng";
    private final String	TAG_ROUTES	= "routes";
    private final String	TAG_LEGS	= "legs";
    private final String	TAG_STEPS	= "steps";
    private final String	TAG_POLYLINE	= "polyline";
    private final String	TAG_POINTS	= "points";
    private final String	TAG_START	= "start_location";
    private final String	TAG_END	= "end_location";

    public JSONObject getJSONFromURL(String url)
    {
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (ClientProtocolException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            is.close();
            json = sb.toString();
        } catch (Exception e)
        {
            // TODO: handle exception
        }

        try
        {
            jsonObject = new JSONObject(json);

        } catch (JSONException e)
        {
            // TODO: handle exception
        }

        return jsonObject;
    }

  /*  public ArrayList<museum> getTempatMakanAll(JSONObject jobj)
    {
       actorsList = new ArrayList<museum>();

        try
        {
            JSONArray arrayTempatMakan = jobj.getJSONArray(TAG_TEMPATMAKAN);

            for (int i = 0; i < arrayTempatMakan.length(); i++)
            {

                JSONObject jobject = arrayTempatMakan.getJSONObject(i);
                JSONObject objRoute = jobject.getJSONArray(TAG_ROUTES).getJSONObject(0);
                JSONObject objLegs = objRoute.getJSONArray(TAG_LEGS).getJSONObject(0);
                JSONObject objDistance = objLegs.getJSONObject("distance");
                JSONObject objDuration = objLegs.getJSONObject("duration");




                Log.d("log", "muter ke " + i);
                museum museum = new museum();
                museum.setmuseum_name(jobject.getString("museum_name"));
                museum.setregional_name(jobject.getString("regional_name"));
                museum.setmuseum_desc(jobject.getString("museum_desc"));
                museum.setLatitude(jobject.getDouble("museum_lat"));
                museum.setLongitude(jobject.getDouble("museum_long"));
                museum.setregional_name(jobject.getString("regional_name"));
                museum.setmuseum_open(jobject.getString("museum_open"));
                museum.setmuseum_close(jobject.getString("museum_close"));
                museum.setmuseum_info(jobject.getString("museum_info"));
                museum.setDurasi(objDuration.getString("durasi"));
                museum.setDurasi(objDistance.getString("jarak"));
                actorsList.add(museum);
                //JSONObject jobject = arrayTempatMakan.getJSONObject(i);

              Log.d("log", "muter ke " + i);
                actorsList.add(new museum(jobject.getString(museum_id), jobject.getString(museum_name),
                                                jobject.getString(museum_price), jobject.getString(museum_desc),
                                                jobject.getString(museum_open), jobject.getString(museum_close),
                                                jobject.getString(regional_id), jobject.getString(museum_foto),
                                                jobject.getString(museum_temp), jobject.getString(regional_name),
                                                jobject.getDouble(latitude), jobject.getDouble(ngitude)));

            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return actorsList;
    }
          */



    private ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }

    /*
    * Untuk mendapatkan directionold
    *
    * @params JSONObject
    *
    * @return List
    */
    public List getDirection(JSONObject jObj)
    {

        List directions = new ArrayList();

        try
        {
            JSONObject objRoute = jObj.getJSONArray(TAG_ROUTES).getJSONObject(0);
            JSONObject objLegs = objRoute.getJSONArray(TAG_LEGS).getJSONObject(0);
            JSONObject objDistance = objLegs.getJSONObject("distance");
            JSONArray arraySteps = objLegs.getJSONArray(TAG_STEPS);
            for (int wi2t = 0; wi2t < arraySteps.length(); wi2t++) {
                JSONObject step = arraySteps.getJSONObject(wi2t);
                JSONObject objStart = step.getJSONObject(TAG_START);
                JSONObject objEnd = step.getJSONObject(TAG_END);
                double latStart = objStart.getDouble(TAG_LAT);
                double lngStart = objStart.getDouble(TAG_LNG);
                directions.add(new LatLng(latStart, lngStart));
                JSONObject poly = step.getJSONObject(TAG_POLYLINE);
                String encodedPoly = poly.getString(TAG_POINTS);
                String text_duration = objDistance.getString("text");
                ArrayList<LatLng> decodedPoly = decodePoly(encodedPoly);
                for (int eka = 0; eka < decodedPoly.size(); eka++) {
                    directions.add(new LatLng(decodedPoly.get(eka).latitude, decodedPoly.get(eka).longitude));

                    String distanceText = objDistance.getString("text");
                    String distanceValue = String.valueOf(objDistance.getString("value"));
                  //  directions.add(text_duration);

                }
                double latEnd = objEnd.getDouble(TAG_LAT);
                double lngEnd = objEnd.getDouble(TAG_LNG);
                directions.add(new LatLng(latEnd, lngEnd));


                }
            } catch (JSONException e) {
             }
            return directions;
            } }
