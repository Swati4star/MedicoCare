package home.medico.com.medicohome;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import home.medico.com.medicohome.AlertDialogManager;
import home.medico.com.medicohome.ConnectionDetector;
import home.medico.com.medicohome.ServerUtilities;
import home.medico.com.medicohome.WakeLocker;
import home.medico.com.medicohome.R;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.List;


public class ShowMessage extends AppCompatActivity {
    // label to display gcm messages
    TextView msg;

    Button call;
    ListView lv;
    // Asyntask
    AsyncTask<Void, Void, Void> mRegisterTask;

    // Alert dialog manager
    AlertDialogManager alert = new AlertDialogManager();

    // Connection detector
    ConnectionDetector cd;

    public static String name;
    public static String email;
    String userid;
    Button b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        setTitle("My orders");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userid = sharedPreferences.getString("userid", "null");

        lv = (ListView) findViewById(R.id.list);
        call = (Button) findViewById(R.id.call);
        cd = new ConnectionDetector(getApplicationContext());
call.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(isNetworkAvailable())
            new DownloadWebPageTask2().execute();
        else
            Toast.makeText(ShowMessage.this,"Cannot Connect to Internet",Toast.LENGTH_SHORT).show();

    }
});


        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);


        registerReceiver(mHandleMessageReceiver, new IntentFilter(CommonUtilities.DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        Log.e("agbe",""+regId);
        // Check if regid already presents
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            Log.e("dbsd","null regID");
            GCMRegistrar.register(this, Values.SENDER_ID);
        }else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
             //   Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        // Register on our server
                        // On server creates a new user
                        ServerUtilities.register(context, name, email, regId);
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
    }

    /**
     * Receiving push messages
     * */


    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
            // Waking up mobile if it is sleeping
            WakeLocker.acquire(getApplicationContext());
            WakeLocker.release();
        }
    };



    @Override
    protected void onDestroy() {
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            unregisterReceiver(mHandleMessageReceiver);
            GCMRegistrar.onDestroy(this);
        } catch (Exception e) {
            //  Log.e("UnRegister Receiver Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_message, menu);
        return true;
    }



    class DownloadWebPageTask2 extends AsyncTask<String, Void, String> {
        String text;

        @Override
        protected void onPreExecute() {
        }


        @Override
        protected String doInBackground(String... urls) {

            Log.e("Yo", "Started");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(
                    "http://api.medicohome.com/myorders.php");
            JSONObject json = new JSONObject();

            try {
                // JSON data:
                json.put("userid", userid);
                json.put("pass","yolo");


                JSONArray postjson = new JSONArray();
                postjson.put(json);
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

            // t.setText(text);
            List<String> list = new ArrayList<String>();
            List<String> list1 = new ArrayList<String>();
            List<String> list2 = new ArrayList<String>();
            List<String> list3 = new ArrayList<String>();
            List<String> list4 = new ArrayList<String>();
            /*json.put("regId",regId);
            json.put("userid",userid);
            json.put("coord1",latitude);
            json.put("coord2",longitude);
            json.put("order", Arrays.toString(values));
            json.put("address",""+address);*/
            int j=0;
            JSONObject ob;
            JSONArray arr;
            try {
                ob = new JSONObject(text);
                arr = ob.getJSONArray("myorders");

                //Log.e("ewgweg","\nLength is : "  + arr.length());

                if(arr.length()<=0)
                   // Toast.makeText(MainActivity.this,"No orders yet",Toast.LENGTH_SHORT).show();



                Log.e("yo", " " + arr + arr.length());
                for(int i = 0; i < arr.length(); i++){
                    try {

                        if(!arr.getJSONObject(i).has("order"))
                            continue;
                        list1.add(arr.getJSONObject(i).getString("order"));
                        JSONObject ob1;
                        JSONArray arr1;
                        if(arr.getJSONObject(i).getString("accepted").contains("true")) {
                            ob1 = new JSONObject(arr.getJSONObject(i).getString("0"));
                            ob1 = new JSONObject(ob1.getString("orders"));

                            list2.add(ob1.getString("store_name") + "\n" + "owner_name" + "\n" + "address" + "\n" + "time" + "\n");

                        }
                        else
                        list2.add("Not yet accepted");
                        list3.add(arr.getJSONObject(i).getString("id"));



                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Log.e("Error","Errror at : " + i + " "+e.getMessage());
                    }
                }

            } catch (Exception e) {

                Log.e("here",e.getMessage());
            }

            String[] order = new String[list1.size()];
            String[] address = new String[list1.size()];
            String[] orderid = new String[list1.size()];


            order = list1.toArray(order);
            address = list2.toArray(address);
            orderid = list3.toArray(orderid);


           CustomList adapter = new CustomList(ShowMessage.this, order,address, orderid);
            lv.setAdapter(adapter);


        }}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
