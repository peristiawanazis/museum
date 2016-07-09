package com.example.iazis.museum;

import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InfoMuseum extends Activity implements OnClickListener
{

    private TextView	tvNama;
    private TextView	tvAlamat;
    private TextView	tvr;
    private TextView	tvd;
    private TextView	jmbk;
    private TextView	jmttp;
    private TextView	tinfo;
    private Button		btnGetDirection;

    private LatLng		lokasiTujuan;
    private LatLng		lokasiAwal;
    private String		nama;
    private String		regional;
    private String		desc;
    private String		jambuka;
    private String		jamtutup;
    private String		info;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.infomuseum);

        initialize();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            nama = bundle.getString(map.KEY_NAMA);
            lokasiTujuan = new LatLng(bundle.getDouble(map.KEY_LAT_TUJUAN),bundle.getDouble(map.KEY_LNG_TUJUAN));
            lokasiAwal = new LatLng(bundle.getDouble(map.KEY_LAT_ASAL),bundle.getDouble(map.KEY_LNG_ASAL));
            regional = bundle.getString(map.KEY_REGIONAL);
            desc = bundle.getString(map.KEY_DESC);
            jambuka = bundle.getString(map.KEY_JAMBUKA);
            jamtutup = bundle.getString(map.KEY_JAMTUTUP);
            info = bundle.getString(map.KEY_INFO);

        }

        setTeksView();

    }

    private void setTeksView()
    {
        tvNama.setText(nama);
        tvAlamat.setText(lokasiTujuan.toString());
        tvr.setText(regional);
        tvd.setText(desc);
        jmbk.setText(jambuka);
        jmttp.setText(jamtutup);
        tinfo.setText(info);

    }

    private void initialize()
    {
        tvAlamat = (TextView) findViewById(R.id.alamatTempatMakan);
        tvNama = (TextView) findViewById(R.id.namaTempatMakan);
        tvr = (TextView) findViewById(R.id.isiregionalmuseum);
        tvd = (TextView) findViewById(R.id.isidescmuseum);
        jmbk = (TextView) findViewById(R.id.isijambukamuseum);
        jmttp = (TextView) findViewById(R.id.isijamtutupmuseum);
        tinfo = (TextView) findViewById(R.id.isiinfomuesum);
        btnGetDirection = (Button) findViewById(R.id.btnDirection);
        btnGetDirection.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        Bundle bundle = new Bundle();
        bundle.putDouble(map.KEY_LAT_ASAL, lokasiAwal.latitude);
        bundle.putDouble(map.KEY_LNG_ASAL, lokasiAwal.longitude);
        bundle.putDouble(map.KEY_LAT_TUJUAN, lokasiTujuan.latitude);
        bundle.putDouble(map.KEY_LNG_TUJUAN, lokasiTujuan.longitude);
        bundle.putString(map.KEY_NAMA, nama);

        Intent intent = new Intent(this, DirectionActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);
    }

}