package com.example.snapfind;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.snapfind.StorageHandler.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static HashMap<String, ArrayList<String>> data;
//    public static DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        databaseHelper = new DatabaseHelper(this);
        data = new HashMap();

//        loadDataFromDataBase();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

//    public void loadDataFromDataBase(){
//        Cursor data = databaseHelper.getData();
//        while(data.moveToNext()){
//            Log.d( "data is",  data.getString(1));
//            System.out.print(" the data from data base is" + data.getString(1));
//        }
//    }
}