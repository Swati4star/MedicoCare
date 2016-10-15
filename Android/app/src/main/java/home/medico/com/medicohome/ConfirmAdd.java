package home.medico.com.medicohome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import buildorder.CustomList2;
import buildorder.MyCart;
import reminder.profile;


public class ConfirmAdd extends AppCompatActivity {
    AsyncTask<Void, Void, Void> mRegisterTask;
    ConnectionDetector cd;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    static final String[] sideitems = new String[] { "Reminder","My Orders","Refer and Earn" , "Feedback","Rate us","Log Out" };	//items on navigation drawer
    EditText name,flat,street,locality,city;
    String Name,Flat,Street,Locality,City;
    ArrayList<Integer> qty;
    ArrayList<String> meci;
    Button  text,next;
    Integer[] imageId = {
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill};
    int in;
   String regId;
    String preid;
    String values[] = new String[1000];
    String latitude,longitude,address;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        Intent it = getIntent();
        values = it.getStringArrayExtra("Mednames");
        qty = it.getIntegerArrayListExtra("qty");
         in = it.getIntExtra("num", 0);
        latitude = it.getStringExtra("latitude");
        longitude = it.getStringExtra("longitude");
        meci = it.getStringArrayListExtra("Names");
        preid=it.getStringExtra("preid");
        cd = new ConnectionDetector(getApplicationContext());


        GCMRegistrar.checkDevice(this);
        regId = GCMRegistrar.getRegistrationId(this);
        Log.e("agbe",""+regId);
        if (regId.equals("")) {
            Log.e("dbsd","null regID");
            GCMRegistrar.register(this, Values.SENDER_ID);
        }else {
            if (GCMRegistrar.isRegisteredOnServer(this)) {
            } else {
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, "name", "email", regId);
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }
                };
                mRegisterTask.execute(null, null, null);
            }
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", "null");
        Object[] x = qty.toArray();
        text = (Button) findViewById(R.id.text);
        next = (Button) findViewById(R.id.next);
        text.setTypeface(custom_font);
        next.setTypeface(custom_font);
        name = (EditText) findViewById(R.id.a);
        flat = (EditText) findViewById(R.id.b);
        street = (EditText) findViewById(R.id.c);
        locality = (EditText) findViewById(R.id.d);
        city = (EditText) findViewById(R.id.e);
        next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Name = name.getText().toString();
        Flat = flat.getText().toString();
        Street = street.getText().toString();
        Locality = locality.getText().toString();
        City = city.getText().toString();
        address = Name + " \n"+Flat+"\n" + Street+"\n"+Locality+"\n"+City;
        if(Name.matches("")||Flat.matches("")||Street.matches("")||Locality.matches("")||City.matches(""))
            Toast.makeText(ConfirmAdd.this,"Enter complete address ",Toast.LENGTH_SHORT).show();
        else if(in==0)
            Toast.makeText(ConfirmAdd.this, "No items in cart.", Toast.LENGTH_LONG).show();
        else
        {
            new DownloadWebPageTask().execute();
        }
        }
    });
        //All for navigation drawer
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        CustomList2 adapter2 = new CustomList2(this, sideitems, imageId);
        mDrawerList.addHeaderView(new View(this));
        mDrawerList.addFooterView(new View(this));
        mDrawerList.setAdapter(adapter2);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* navigation drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */) ;


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                if(position==1)
                {
                    Intent i = new Intent(ConfirmAdd.this, profile.class);
                    startActivity(i);
                }
                if(position==2)
                {
                    Intent i = new Intent(ConfirmAdd.this,ShowMessage.class);
                    startActivity(i);
                }

                if(position==4)
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "abc@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback : MedicoHome");
                    intent.putExtra(Intent.EXTRA_TEXT, "...");
                    startActivity(Intent.createChooser(intent, "Choose..."));

                }
                if(position==3)
                {
                    try
                    { Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Medicohome");
                        String sAux = "\nCheck out this application\n\n";
                        sAux = sAux + "https://www.facebook.com/Medicohomeinc?fref=ts \n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "Choose.."));
                    }
                    catch(Exception e)
                    { //e.toString();
                    }

                }

                if(position==5){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.appify4u.infonizer")));

                }

                if (position == 6) {
                    new AlertDialog.Builder(ConfirmAdd.this)
                            .setMessage("Are you sure you want to Log Out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("login", 0);
                                    editor.apply();


                                    Intent intent = new Intent(ConfirmAdd.this, LoginOrSignup.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
                                    startActivity(intent);
                                    finishAffinity();

                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }

            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name2 = sharedPreferences2.getString("name",null);
        getSupportActionBar().setTitle(name2);
        name.setText(name2);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

    }

    class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        String text;

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... urls) {

            Log.e("Yo", "Started");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.medicohome.com/sendpush.php");
            JSONObject json = new JSONObject();

            try {
                 json.put("regId",regId);
                json.put("userid",userid);
                json.put("coord1",latitude);
                json.put("coord2", longitude);
                json.put("preid", preid);

                String s  ="Order : \n";



                for(int  i=0;i<meci.size();i++)
                {
                    s = s + meci.get(i);
                }
                json.put("order",s);
                json.put("address",""+address);
                Log.e("gssegs", s + " \n" + address+"\n"+latitude+"\n"+longitude);
                JSONArray postjson = new JSONArray();
                postjson.put(json);

                Log.e("Yo", "Post data" + regId);
                // Post the data:
                httppost.setHeader("json", json.toString());
                httppost.getParams().setParameter("jsonpost", postjson);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Log.e("Yo", "Posting data");

                // for JSON Response :
                if (response != null) {

                    InputStream is = response.getEntity().getContent();

                    Log.e("Yo", "inside data" + response + "    " + is);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    try {
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    text = sb.toString();
                    Log.e("YO-Response", text);

                }


            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("YO", "Done" + text);
            if(!text.contains("Error")) {
                Toast.makeText(ConfirmAdd.this, "Your order has been successfully placed", Toast.LENGTH_LONG).show();

                Intent i = new Intent(ConfirmAdd.this, ShowMessage.class);
                startActivity(i);

                Toast.makeText(ConfirmAdd.this, text, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_add, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
         if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        if(id== R.id.action_cart)
        {
            if(in==0)
                Toast.makeText(ConfirmAdd.this,"No items in cart yet",Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(ConfirmAdd.this, MyCart.class);
                i.putExtra("Mednames", values);
                i.putIntegerArrayListExtra("qty", qty);
                i.putExtra("num", in);
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
