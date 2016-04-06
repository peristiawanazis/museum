package com.example.iazis.museum;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class homepage extends Activity {

    ListView list;
    String[] web = {
            "Google Plus",
            "Twitter",
            "Windows",
            "Bing",
            "Itunes",
            "Wordpress",
            "Drupal"
    } ;
    Integer[] imageId = {
            R.drawable.ic_location_city_24dp,
            R.drawable.ic_location_city_24dp,
            R.drawable.ic_location_city_24dp,
            R.drawable.ic_location_city_24dp,
            R.drawable.ic_location_city_24dp,
            R.drawable.ic_location_city_24dp,
            R.drawable.ic_location_city_24dp

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        CustomList adapter = new
                CustomList(homepage.this, web, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(homepage.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();

            }
        });



    }
}
