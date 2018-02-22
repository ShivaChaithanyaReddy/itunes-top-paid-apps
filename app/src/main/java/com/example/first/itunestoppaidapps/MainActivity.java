package com.example.first.itunestoppaidapps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GetAppsAsyncTask.IApp{

    String UrlToParse;
    ListView listView;
    ProgressBar progressBar;
    TextView textView;
    ArrayList<Apps> AllApps;
    AppsAdapter adapter;
    SharedPreferences sharedPreferences;
    ArrayList<Apps> temp;

    public static final String FAV = "fav";
    static final int PICK_CONTACT_REQUEST = 100;  // The request code




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

temp = new ArrayList<Apps>();
        sharedPreferences = getSharedPreferences("MyFav", Context.MODE_PRIVATE);
        AllApps = new ArrayList<>();
        listView = (ListView) findViewById(R.id.listView);
        UrlToParse = "https://itunes.apple.com/us/rss/toppaidapplications/limit=25/json";
        new GetAppsAsyncTask(this).execute(UrlToParse);


        Log.v("Example", "onCreate");
        getIntent().setAction("Already created");

    }

    @Override
    public void sendFromAppsAsync(ArrayList<Apps> arrayList) {

        AllApps = arrayList;

        if(temp == null || temp.isEmpty())
        temp = AllApps;
        progressBar = (ProgressBar) findViewById(R.id.progressbarMain);
        progressBar.setVisibility(View.GONE);
        textView = (TextView) findViewById(R.id.loading);
        textView.setVisibility(View.GONE);

        setAllObjFavFromSharedPreferences();

    adapter = new AppsAdapter(this, R.layout.row_item_layout, AllApps, MainActivity.this);


        listView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

    }

    private void setAllObjFavFromSharedPreferences() {

        Map<String,?> keys = sharedPreferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
            Apps app = new Apps();
            app = AllApps.get(Integer.parseInt(entry.getValue().toString()));
            app.setFavourite(true);
        }


    }

    public  void onClickStar(final int position) {

        final Apps apps = AllApps.get(position);

        if(apps.isFavourite()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Remove From Favouries")
                    .setMessage("Are you sure you want to \n remove this app to favourites?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = getSharedPreferences("MyFav", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.remove(apps.getName());
                            editor.commit();
                            apps.setFavourite(false);

                            Toast.makeText(MainActivity.this, "Successfully removed from Favourite list :)", Toast.LENGTH_SHORT).show();
                            notifyListUpdate();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Add to Favouries")
                    .setMessage("Are you sure you want to \n add this app to favourites?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(apps.getName(), String.valueOf(position));
                            editor.commit();
                            Toast.makeText(MainActivity.this, "Successfully saved to Favourite list :)", Toast.LENGTH_SHORT).show();

                            AllApps.get(position).setFavourite(true);
                            notifyListUpdate();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                refreshList();
                return true;
            case R.id.favourites:
                getFavourites();
                return true;
            case R.id.AscSort:
                sortAsc();
                notifyListUpdate();
                return true;
            case R.id.descSort:
                sortDsc();
                notifyListUpdate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void notifyListUpdate() {
        adapter.notifyDataSetChanged();
    }

    private void sortDsc() {
        sortAsc();
        Collections.reverse(AllApps);

    }

    private void refreshList() {
        adapter.notifyDataSetChanged();
        Toast.makeText(this,"Refresh successful!",Toast.LENGTH_SHORT).show();
    }


    private void sortAsc() {
        Collections.sort(AllApps, new Comparator<Apps>() {
            @Override
            public int compare(Apps c1, Apps c2) {
                return Double.compare(Double.parseDouble(c1.getPrice()),Double.parseDouble( c2.getPrice()));
            }
        });
    }

    public void getFavourites() {
        SharedPreferences sharedPreferencess = getSharedPreferences("MyFav", Context.MODE_PRIVATE);
        ArrayList<Apps> favlist = getSharedPreferencesList(sharedPreferencess);

        Log.d("aa",""+favlist.toString());
        Intent intent = new Intent(MainActivity.this, Favourites.class);
        intent.putExtra(FAV, favlist);
        startActivityForResult(intent,PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
               // Toast.makeText(MainActivity.this,"Successfully removed to Favourite list :)",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Example", "onResume");

        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
           // Log.v("Example", "Force restart");
            startActivity(getIntent());
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);
    }

/*    private void checkSharedPref() {

        Map<String,?> keys = sharedPreferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
           for(Apps as : AllApps) {
               if(!as.getName().equals(entry.getKey())) {
                   as.setFavourite(false);
               }
           }
        }
        refreshList();
    }*/

    private ArrayList<Apps> getSharedPreferencesList(SharedPreferences sharedPreferences) {

        ArrayList<Apps> arr = new ArrayList<>();

        Map<String,?> keys = sharedPreferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " + entry.getValue().toString());
            Apps app = new Apps();
            Log.d("temp values",temp.get(Integer.parseInt(entry.getValue().toString())).getName()+"");

            for(Apps as: AllApps) {
                if(as.getName().equals(entry.getKey())) {
                    app = as;
                }
            }

           // app = temp.get(Integer.parseInt(entry.getValue().toString()));
            arr.add(app);
        }
return arr;
    }
}
