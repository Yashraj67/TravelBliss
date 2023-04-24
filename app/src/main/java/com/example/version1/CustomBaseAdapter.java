package com.example.version1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    ArrayList<TripData> arr;
    LayoutInflater inflater;

    public CustomBaseAdapter(Context context, ArrayList<TripData> arr){
        this.context = context;
        this.arr = arr;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.list_item, null);
        String title = arr.get(position).getTitle();
        String rating  = arr.get(position).getRating();
        String dis = arr.get(position).getDistance();
        String imgurl = arr.get(position).getImg();


        ImageView img = convertView.findViewById(R.id.profile_img);
        TextView name = convertView.findViewById(R.id.textView5);
        TextView rt = convertView.findViewById(R.id.rt);
        TextView dis1 = convertView.findViewById(R.id.dis);
        name.setText(title);
        dis1.setText(dis);
        rt.setText(rating);
        Picasso.get().load(imgurl).into(img);

        return convertView;
    }
}
