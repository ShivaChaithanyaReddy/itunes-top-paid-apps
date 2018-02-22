package com.example.first.itunestoppaidapps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Chaithanya on 2/22/2017.
 */

public class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    Bitmap bitmap= null;
    ImageView imageView;

    public GetImageAsyncTask(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        try {
          //  Log.d("In ImageAsync\n"," "+params[0]);
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int status = connection.getResponseCode();
            if(status == HttpURLConnection.HTTP_OK) {
                bitmap = BitmapFactory.decodeStream(connection.getInputStream());
            }
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap != null) {
            imageView.setImageBitmap(bitmap);
          //  Log.d("Image is:",""+bitmap.toString());
        }
    }
}
