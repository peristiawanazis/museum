package com.example.iazis.museum;



import java.io.InputStream;
import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class infoadapter extends ArrayAdapter<museum> {
    ArrayList<museum> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public infoadapter(Context context, int resource, ArrayList<museum> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.tvName = (TextView) v.findViewById(R.id.nama_museum);
            holder.regional = (TextView) v.findViewById(R.id.regional_museum);
            holder.desc = (TextView) v.findViewById(R.id.desc_museum);
           v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.tvName.setText(actorList.get(position).getmuseum_name());
        holder.desc.setText(actorList.get(position).getmuseum_desc());
        holder.regional.setText(actorList.get(position).getregional_name());
        return v;

    }

    static class ViewHolder {

        public TextView tvName;
        public TextView desc;
        public TextView regional;


    }


}