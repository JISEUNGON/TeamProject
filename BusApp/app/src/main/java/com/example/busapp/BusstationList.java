package com.example.busapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BusstationList extends BaseAdapter {

    ArrayList<BusStation> bs = new ArrayList<>();

    @Override
    public int getCount() {
        return bs.size();
    }

    @Override
    public Object getItem(int position) {
        return bs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        Context c = viewGroup.getContext();
        if(view==null){
            LayoutInflater li = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.listview,viewGroup,false);
        }


        TextView textview = view.findViewById(R.id.tv);
        ImageView imageview = view.findViewById(R.id.iv);

        textview.setTextColor(Color.BLACK);

        BusStation arr = bs.get(position);

        textview.setText(arr.getText() );
        imageview.setImageDrawable(arr.getD() );

        return view;
    }

    public void addStation(Drawable d,  String t){
        BusStation b = new BusStation();

        b.setD(d);
        b.setText(t);

        bs.add(b);
    }
}
