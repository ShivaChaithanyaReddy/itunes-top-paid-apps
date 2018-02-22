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
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Favourites extends AppCompatActivity implements GetAppsAsyncTask.IApp {

    ArrayList<Apps> arrayList;
    AppsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        arrayList = new ArrayList<>();
        arrayList = (ArrayList<Apps>) getIntent().getSerializableExtra(MainActivity.FAV);


        adapter = new AppsAdapter(this, R.layout.row_item_layout, arrayList, Favourites.this);
        ListView favView = (ListView) findViewById(R.id.FavView);

        favView.setAdapter(adapter);
        adapter.setNotifyOnChange(true);



        Button backButton = (Button)this.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
    });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void sendFromAppsAsync(ArrayList<Apps> arrayList) {

    }

    @Override
    public void onClickStar(final int position) {
        final Apps apps = arrayList.get(position);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Remove from Favouries")
                .setMessage("Are you sure you want to \n remove this app from favourites?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyFav", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //to remove from main list
                            String name = sharedPreferences.getString(apps.getName(),"");
                        //
                        editor.remove(apps.getName());
                        editor.commit();
                        Toast.makeText(Favourites.this,"Successfully removed to Favourite list :)",Toast.LENGTH_SHORT).show();

                        arrayList.remove(position);
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

    private void notifyListUpdate() {
        adapter.notifyDataSetChanged();
    }


}
