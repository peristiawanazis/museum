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
    private Button		btnGetDirection;

    private LatLng		lokasiTujuan;
    private LatLng		lokasiAwal;
    private String		nama;


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
        }

        setTeksView();

    }

    private void setTeksView()
    {
        tvNama.setText(nama);
        tvAlamat.setText(lokasiTujuan.toString());

    }

    private void initialize()
    {
        tvAlamat = (TextView) findViewById(R.id.alamatTempatMakan);
        tvNama = (TextView) findViewById(R.id.namaTempatMakan);
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