package com.example.abhishek.navdrawer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Tag values to read from json
    public String MENU_ID = "menu_id";
    public String MENU_NM = "menu_nm";
    public String MENU_ICON = "menu_icon";
    Menu menu;
    SubMenu subMenu;
    //ArrayList for Storing menu ids and names
    private ArrayList<Integer> menu_ids;
    private ArrayList<String> menu_nms;
    private ArrayList<String> menu_icons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menu_ids = new ArrayList<>();
        menu_nms = new ArrayList<>();
        menu_icons = new ArrayList<>();


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ReadJSON().execute("http://69.89.31.191/~webtech6/android_api/menu.php");
            }
        });

        menu = navigationView.getMenu();
    }

    //Add Dynamic Menu Item
    class ReadJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }



        @Override
        protected void onPostExecute(String content) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray =  jsonObject.getJSONArray("menu_dtls");

                for(int i =0;i<jsonArray.length(); i++){
                    //getting json object from current index
                    JSONObject obj = jsonArray.getJSONObject(i);
                    //getting menuid and menuname from json object
                    menu_ids.add(obj.getInt(MENU_ID));
                    menu_nms.add(obj.getString(MENU_NM));
                    menu_icons.add(obj.getString(MENU_ICON));

                    //Toast.makeText(MainActivity.this, menu_icons.get(i), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < menu_ids.size(); i++) {
               MenuItem item = menu.add(R.id.group1,menu_ids.get(i),Menu.NONE,menu_nms.get(i));
                try {
                    URL url = new URL(menu_icons.get(i));
                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    Drawable d = new BitmapDrawable(getResources(), bitmap);
                    menu.getItem(0).setIcon(d);
                }catch (Exception e){
                    System.out.println(e);
                }
                //Bitmap bmp = imageLoader.loadImageSync(menu_icons.get(i));

               // Bitmap bitmap = getBitmapFromURL(menu_icons.get(i));
                // Drawable d = new BitmapDrawable(getResources(), bitmap);
               //menu.getItem(0).setIcon(d);
              //  Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata.length);
            //   item.setIcon(R.drawable.ic_menu_gallery); // add icon with drawable resource

            }


        }
    }


    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }


////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
      Toast.makeText(getBaseContext(), id + "", Toast.LENGTH_LONG).show();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
