package com.example.adismurtic.navigationviewvjezba;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by adis.murtic on 10/07/2015.
 */
public class RecieverNotification extends Application {




    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "uO9dYlbnifSdkdYIo9V8oNYyAibdfUbyTQASYQqf", "Bodqz4Lq0hGnn4XAQP8Su7qZ57WgqSlrYfY034kI");







    }
}
