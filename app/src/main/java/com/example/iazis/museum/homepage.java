package com.example.iazis.museum;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class homepage extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        TextView desc = (TextView) findViewById(R.id.desc);
        TextView descdua = (TextView) findViewById(R.id.descdua);
        TextView tittle = (TextView) findViewById(R.id.tittle);
        tittle.setText("Museum V.01");
        desc.setText("Membantu Anda Menemukan Musesum dengan Jarak terdekat dari Lokasi Anda");
        descdua.setText("this app created by arief h for final exam ");
        desc.setTypeface(Typeface.SANS_SERIF, 1);
        descdua.setTypeface(Typeface.MONOSPACE, 0);
        tittle.setTypeface(Typeface.DEFAULT_BOLD);
        desc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        descdua.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
        tittle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        desc.setTextSize(14);
        descdua.setTextSize(12);
        tittle.setTextSize(13);
        Button enterbutton = (Button) findViewById(R.id.button);
        enterbutton.setText("Enter");
        enterbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent i = new Intent(getApplicationContext(), mainpage.class);
                startActivity(i);
            }
        });


    }
}
