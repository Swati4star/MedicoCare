package home.medico.com.medicohome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class Options extends AppCompatActivity implements Constants {

    Button med, rem;
    Button amb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);


        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        amb = (Button) findViewById(R.id.amb);
        amb.setTypeface(custom_font);
        //((GradientDrawable)amb.getBackground()).setColor(Color.parseColor("#1f9c62"));
        med = (Button) findViewById(R.id.med);
        med.setTypeface(custom_font);
        //((GradientDrawable)med.getBackground()).setColor(Color.parseColor("#1f9c62"));
        rem = (Button) findViewById(R.id.rem);
        rem.setTypeface(custom_font);
        //((GradientDrawable)rem.getBackground()).setColor(Color.parseColor("#1f9c62"));

        rem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Options.this, reminder.profile.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(i);


            }
        });

        med.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(Options.this, buildorder.location.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(i);

            }
        });
        amb.setVisibility(View.GONE);
        getSupportActionBar().setTitle("MedicoCare");
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(Options.this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
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
            HttpPost httppost = new HttpPost(apilink + "/signup.php");
            JSONObject json = new JSONObject();

            try {
                json.put("name", "yolo");

                JSONArray postjson = new JSONArray();
                postjson.put(json);

                Log.e("Yo", "Post data");
                // Post the data:
                httppost.setHeader("json", json.toString());
                httppost.getParams().setParameter("jsonpost", postjson);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                Log.e("Yo", "Posting data");

                // for JSON Response :
                if (response != null) {

                    InputStream is = response.getEntity().getContent();

                    Log.e("Yo", "inside data     :      " + response + "    " + is);
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
            Log.e("YO", "Done");
            Toast.makeText(Options.this, "Response : " + text, Toast.LENGTH_LONG).show();


        }
    }
}
