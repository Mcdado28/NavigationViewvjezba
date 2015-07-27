package com.example.adismurtic.navigationviewvjezba;


import android.app.AlertDialog;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;

import android.view.Menu;
import android.view.MenuItem;


import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.support.v7.widget.SearchView;


import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

import com.parse.SaveCallback;


import org.json.JSONObject;



public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBar actionBar;
    TextView textView;
    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    Intent intent;
    TextView pushnotifikacije;
    String SITE_URL;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        handleIntent(getIntent());



        ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    e.printStackTrace();

                }
            }
        });



        ParseObject testObject = new ParseObject("Navvjezba");
        testObject.put("imtec", "hit");
        testObject.saveInBackground();





        webView = (WebView) findViewById(R.id.webi);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.setWebViewClient(new WebViewClient());

        webView.getSettings().setGeolocationEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);


        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if (getIntent().getExtras() !=null) {

            try {
                Bundle b = getIntent().getExtras();
                JSONObject jsonObject = new JSONObject(b.getString("com.parse.Data"));
                String data=jsonObject.getString("alert");
                String url =jsonObject.getString("url");
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("SITE_URL",url);
                startActivity(intent);

                pushnotifikacije.setText(data);


            }catch(Exception e){
                e.printStackTrace();
            }

            webView.loadUrl(getIntent().getStringExtra("SITE_URL"));
        } else {

            webView.loadUrl("http://shop.imtec.ba");

        }
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if (errorCode == -2) {
                    view.loadData("", "", null);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color='#388E3C'><b>Imtec Web Shop</b></font>"));
                builder.setMessage(Html.fromHtml("<font color='#120049'>Nemate pristup internetu. Konektujte se i pokušajte ponovo!</font>"));
                builder.setPositiveButton(Html.fromHtml("<font color='#7F02AE'><b>Ukljuci WIFI</b></font>"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);

                    }
                });


                builder.setNegativeButton(Html.fromHtml("<font color='#7F02AE'><b>Zatvori</b></font>"), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                });


                AlertDialog alert = builder.create();
                alert.show();
            }
        });




        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeit);
        swipeRefreshLayout.setOnRefreshListener(this);






        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);


        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);




        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        if (navigationView != null) {
            setupNavigationDrawerContent(navigationView);



        }
    }




    public class AppConfig {

        public static final String PARSE_APPLICATION_ID = "uO9dYlbnifSdkdYIo9V8oNYyAibdfUbyTQASYQqf";
        public static final String PARSE_CLIENT_KEY = "Bodqz4Lq0hGnn4XAQP8Su7qZ57WgqSlrYfY034kI";
        public static final int NOTIFICATION_ID = 100;
    }

    // Sta se desava kada pritisnemo BACK dugmic
    // Napravimo ga pametnim :)
    @Override
    public void onBackPressed() {

        // Ako je navigation drawer otvoren zatvori ga
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);

        }

        // Ako je navigation drawer zatvoren i ako u memoriji ima jos jedna stranica idi jednu stranicu nazad
        else if (!drawerLayout.isDrawerOpen(GravityCompat.START) && webView.canGoBack()) {

            webView.goBack();

        } else {

            // Ako nista od navedenog nije istina napusti aplikaciju
            super.onBackPressed();

        }
    }

    // Osvjezimo trenutnu stranicu (ovu funkciju poziva Swipe to Refresh komponenta)
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3, R.color.refresh_progress_4);

        swipeRefreshLayout.setRefreshing(true);

        (new Handler()).postDelayed(new Runnable() {
            @Override
            public void run() {

                swipeRefreshLayout.setRefreshing(false);
                webView.reload();
            }
        }, 4000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchbox).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.homedugme:
                webView.loadUrl("http://shop.imtec.ba");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        setIntent(intent);
        handleIntent(intent);



    }
    private void handleIntent (Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {

            String query = intent.getStringExtra(SearchManager.QUERY);
            webView.loadUrl("http://shop.imtec.ba/search?search_query=" + query + "&controller=search&orderby=position&orderway=desc");
        }
    }


    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {


                        switch (menuItem.getItemId()) {
                            case R.id.item_navigation_drawer_inbox:
                                menuItem.setChecked(true);
                                webView.loadUrl("http://shop.imtec.ba");

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_starred:
                                menuItem.setChecked(true);
                                webView.loadUrl("http://shop.imtec.ba/stores");
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_sent_mail:
                                menuItem.setChecked(true);
                                webView.loadUrl("http://shop.imtec.ba/content/2-placanje");
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_drafts:
                                menuItem.setChecked(true);
                                webView.loadUrl("http://shop.imtec.ba/content/4-servis");
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.korpa:
                                menuItem.setChecked(true);
                                webView.loadUrl("https://shop.imtec.ba/order");
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.prijava:
                                menuItem.setChecked(true);
                                webView.loadUrl("https://shop.imtec.ba/authentication?back=my-account");
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            case R.id.item_navigation_drawer_settings:
                                menuItem.setChecked(true);
                                textView.setText(menuItem.getTitle());
                                Toast.makeText(MainActivity.this, "Launching " + menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(intent);
                                return true;
                            case R.id.item_navigation_drawer_help_and_feedback:
                                menuItem.setChecked(true);
                                Toast.makeText(MainActivity.this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                        }
                        return true;
                    }
                });
    }





}