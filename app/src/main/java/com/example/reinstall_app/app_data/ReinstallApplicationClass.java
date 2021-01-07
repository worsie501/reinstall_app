package com.example.reinstall_app.app_data;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.ArrayList;
import java.util.List;

public class ReinstallApplicationClass extends Application {

    public static final String APPLICATION_ID = "75A538D0-1849-8BBB-FFEF-9D766ECC4500";
    public static final String API_KEY = "7D16AB1D-FD2D-4CC1-A03F-B71841B23F96";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;

    public static List<ProblemType> problemTypes;

    public static List<Suburb> suburbList;

    public static List<Suburb> hotspotList;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );

    }
}
