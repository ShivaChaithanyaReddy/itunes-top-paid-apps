package com.example.first.itunestoppaidapps;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Chaithanya on 2/22/2017.
 */

public class AppsAdapter extends ArrayAdapter<Apps> {

    List<Apps> mData;
    Context mContext;
    int mResource;
    GetAppsAsyncTask.IApp iApp;
    SharedPreferences sharedPreferences;

    public AppsAdapter(Context context, int resource, List<Apps> objects, GetAppsAsyncTask.IApp iApp) {
        super(context, resource, objects);
        this.mContext = context;
        this.mData = objects;
        this.mResource = resource;
        this.iApp = iApp;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Apps apps = mData.get(position);
        Log.d("I am called","");

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewLogo);
        ImageView star = (ImageView) convertView.findViewById(R.id.imageViewFav);


        if(apps.isFavourite())
            star.setImageResource(R.drawable.black);
        else
            star.setImageResource(R.drawable.white);

        TextView name = (TextView) convertView.findViewById(R.id.appName);
        name.setText(apps.getName());

        TextView price = (TextView) convertView.findViewById(R.id.appPrice);
        price.setText("Price: USD "+apps.getPrice());

        new GetImageAsyncTask(imageView).execute(apps.getImage());

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                iApp.onClickStar(position);
            }
        });
        return convertView;
    }
}
