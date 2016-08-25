package home.medico.com.medicohome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


public class LoginOrSignup extends AppCompatActivity {

    EditText num,pass;
    String Num,Pass;
    ProgressDialog pd,pd2;
    TextView error,forget,signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_signup);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        pd = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setTitle("Logging In...");
        pd2 = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd2.setTitle("Requesting for new password...");

        error = (TextView) findViewById(R.id.error);
        num = (EditText) findViewById(R.id.number);
        pass = (EditText) findViewById(R.id.password);
        forget = (TextView) findViewById(R.id.forget);
        signup = (TextView) findViewById(R.id.signup);

        Button login = (Button) findViewById(R.id.login);
        signup.setTypeface(custom_font);
        forget.setTypeface(custom_font);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Num = num.getText().toString();
                if (Num.matches("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginOrSignup.this);
                    builder.setMessage("Enter your Mobile Number")
                            .setCancelable(false)
                            .setPositiveButton("OK", null);
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    new DownloadWebPageTask2().execute();
                }
            }
        });

        login.setTypeface(custom_font);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Num = num.getText().toString();
                Pass = pass.getText().toString();

                //Intent i = new Intent("android.intent.action.MEDICOUSER");
                if(Num.matches(""))
                    error.setText("Enter number");
                else if(Pass.matches(""))
                    error.setText("Enter Password");
                else
                {
                    new DownloadWebPageTask().execute();
                }
            }
        });




        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(LoginOrSignup.this, signup.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(i);
               // finish();
            }
        });

        getSupportActionBar().setTitle("Welcome");
    }


    class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        String text;

        @Override
        protected void onPreExecute() {

            pd.show();

        }


        @Override
        protected String doInBackground(String... urls) {

            Log.e("Yo", "Started");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.medicohome.com/login.php");
            JSONObject json = new JSONObject();

            try {
                // JSON data:


                json.put("pass", Pass);
                json.put("phonenumber", Num);


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
            pd.dismiss();
            try {
                JSONObject o = new JSONObject(text);

                String s = o.getString("success");
                if(s.contains("0"))
                    error.setText(o.getString("message"));
                else
                {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("login", 1);
                    editor.putString("name", o.getString("first name"));
                    String[] x = text.split(" ");
                    editor.putString("userid",o.getString("id"));
                    editor.commit();
                    Log.e("bbes","id : " + x[0]);
                    Intent i = new Intent(LoginOrSignup.this, Options.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // this will clear all the stack
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    class DownloadWebPageTask2 extends AsyncTask<String, Void, String> {
        String text;

        @Override
        protected void onPreExecute() {
            pd2.show();
        }


        @Override
        protected String doInBackground(String... urls) {

            Log.e("Yo", "Started");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://api.medicohome.com/forget-password.php");
            JSONObject json = new JSONObject();

            try {
                // JSON data:



                json.put("phone", Num);


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
            Log.e("YO", "Done");
            pd2.dismiss();

            if(text.contains("Success"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginOrSignup.this);
                builder.setMessage("A password reset link has been sent to your email address.")
                        .setCancelable(false)
                        .setPositiveButton("OK",null);
                AlertDialog alert = builder.create();
                alert.show();
            }
            else
                Toast.makeText(LoginOrSignup.this, text, Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_or_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
