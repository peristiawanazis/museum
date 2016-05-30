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
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class help extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.profile,container,false);
        View view = inflater.inflate(R.layout.help,container,false);
        TextView help = (TextView) view.findViewById(R.id.help);

        /*help.setText("Ini adalah aplikasi pencari Museum.\n"+
                "Pada aplikasi ini ada beberapa fitur yang tersedia \n"+
                "- Anda dapat Get Current Location dengan menekan tombol location\n"+
                "Anda juga dapat mencari lokasi museum terdekat dari lokasi anda. ");
        help.setTypeface(Typeface.SANS_SERIF, 1);
        help.setTextSize(14);*/


        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append("Ini adalah aplikasi pencari Museum.\n").append(" ");

        builder.setSpan(new ImageSpan(getActivity(), android.R.drawable.ic_dialog_dialer),
        builder.length() - 1, builder.length(), 0);
        builder.append(" Tombol di samping berfungsi untuk mengetahui posisi keberadaan");

        builder.setSpan(new ImageSpan(getActivity(), android.R.drawable.ic_menu_mylocation),
                builder.length() - 1, builder.length(), 0);
        builder.append(" Tombol berfungsi untuk mencarilokasi yang akan diilih \n" +" \n" + "Menu search by dengan menggunaan drawing, user hanya menggambarkan circle,\n"+
        "polygon maka marker museum akan tampil di daerah yang dibatasi gambar");

        help.setText(builder);
        return  view;
    }


}
