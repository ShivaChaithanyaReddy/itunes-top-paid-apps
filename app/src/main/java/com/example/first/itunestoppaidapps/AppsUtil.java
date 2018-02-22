package com.example.first.itunestoppaidapps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chaithanya on 2/22/2017.
 */

public class AppsUtil {
    static public class AppJsonParser{
        static ArrayList<Apps> parseApp(String in) throws JSONException {

            ArrayList<Apps> arrayList = new ArrayList<>();
            JSONObject root = new JSONObject(in);
            JSONObject authorObj = root.getJSONObject("feed");
            JSONArray jsonArray = authorObj.getJSONArray("entry");

            for(int i=0; i<jsonArray.length();i++) {
                JSONObject appJSONObject = jsonArray.getJSONObject(i);
                Apps apps = new Apps();

                    JSONObject appJSONDetails = appJSONObject.getJSONObject("im:name");
                    apps.setName(appJSONDetails.getString("label"));

                JSONArray imageArray = appJSONObject.getJSONArray("im:image");
                JSONObject imageObject = imageArray.getJSONObject(0);
                apps.setImage(imageObject.getString("label"));

                    JSONObject priceObject = appJSONObject.getJSONObject("im:price");
                    JSONObject attrObject  = priceObject.getJSONObject("attributes");
                    double amount = Double.parseDouble(attrObject.getString("amount"));
                    double amt = (double) Math.round(amount*100)/100;
                    apps.setPrice(String.valueOf(amt));


apps.setFavourite(false);
                arrayList.add(apps);
            }
            return arrayList;
        }
    }
}
