package com.example.tp2;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ColorAdapter extends BaseAdapter {
    private Context context;
    private int[] colors;

    public ColorAdapter(Context context, int[] colors) {
        this.context = context;
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int position) {
        return colors[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView colorView;

        if (convertView == null) {
            colorView = new ImageView(context);
            colorView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        } else {
            colorView = (ImageView) convertView;
        }

        colorView.setBackgroundColor(colors[position]);
        return colorView;
    }
}

