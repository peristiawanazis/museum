package com.example.iazis.museum;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class profile extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.profile,container,false);
        View view = inflater.inflate(R.layout.profile,container,false);
        TextView isiprofile = (TextView) view.findViewById(R.id.isiprofile);

        isiprofile.setText("Nama : Arif\n"+"Tempat Tanggal Lahir: \n"+"NRP : \n"+"Program Studi Teknink Informatika \n"+"Email : arrief@yahoo.co.id\n");
        isiprofile.setTypeface(Typeface.SANS_SERIF, 1);
        isiprofile.setTextSize(14);
         return  view;

    }


}
