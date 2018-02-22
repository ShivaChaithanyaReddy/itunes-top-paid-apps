package com.example.first.itunestoppaidapps;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Chaithanya on 2/22/2017.
 */

public class GetAppsAsyncTask extends AsyncTask<String, Void, ArrayList<Apps>> {

    IApp iapp;

    public GetAppsAsyncTask(IApp iapp) {
        this.iapp = iapp;
    }

    @Override
    protected ArrayList<Apps> doInBackground(String... params) {

        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int status = connection.getResponseCode();
            Log.d("In GetAppsAsyncTsak","");

            if(status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line = reader.readLine();
                while(line != null) {
                    stringBuilder.append(line);
                    line = reader.readLine();
                }

return AppsUtil.AppJsonParser.parseApp(stringBuilder.toString());
               // Log.d("asd",""+stringBuilder.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Apps> appses) {
        super.onPostExecute(appses);

        if(appses != null) {
            //Log.d("asd",appses.toString());
            iapp.sendFromAppsAsync(appses);
        }
    }

    public interface IApp {
       void sendFromAppsAsync(ArrayList<Apps> arrayList);
        void onClickStar(int position);
    }
}
