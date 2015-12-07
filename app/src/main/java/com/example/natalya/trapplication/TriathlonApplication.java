package com.example.natalya.trapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.sql.SQLException;

import db.DataSource;


public class TriathlonApplication extends Application{
    private static Context context;

    private db.DataSource ds;


        @Override
        public void onCreate() {
            super.onCreate();
            context = getApplicationContext();
        }

        public static Context getContext(){
            return context;
        }

        public DataSource getDataSource() {

            if(ds == null) {
                ds = new db.DataSource(this);
                try {
                    ds.open();
                } catch (SQLException e) {
                    Log.e("freestyle", "error during creating database");
                    e.printStackTrace();
                }
            }
            return ds;
        }
}
