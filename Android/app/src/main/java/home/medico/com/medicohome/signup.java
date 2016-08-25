package home.medico.com.medicohome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class signup extends AppCompatActivity implements Constants {
Button next;
    ProgressDialog pd;
    EditText first,last,num,pass,cpass,email;
    TextView error;
    String First,Last,Num,Pass,Cpass,Email, regId;
    public static String name,emails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        next = (Button) findViewById(R.id.next);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        pd = new ProgressDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setTitle("Signing Up...");
        first = (EditText) findViewById(R.id.first);
        last = (EditText) findViewById(R.id.last);
        num = (EditText) findViewById(R.id.number);
        pass = (EditText) findViewById(R.id.password);
        cpass = (EditText) findViewById(R.id.confirmpassword);
        email = (EditText) findViewById(R.id.emailid);

        error = (TextView) findViewById(R.id.error);

        next.setTypeface(custom_font);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                First = first.getText().toString();
                Last = last.getText().toString();
                Num = num.getText().toString();
                Pass = pass.getText().toString();
                Cpass = cpass.getText().toString();
                Email = email.getText().toString();

                if (First.matches("") || Last.matches(""))
                    error.setText("Enter Name");
                else if (!validateEmailAddress(Email))
                    error.setText("Invalid Email address");
                else if (Num.length() < 10)
                    error.setText("Enter Mobile Number");
                else if (!Pass.matches(Cpass) || Pass.matches(""))
                    error.setText("Passwords do not match");
                else
                    new DownloadWebPageTask().execute();

            }
        });
        getSupportActionBar().setTitle("Welcome");


    }
    private boolean validateEmailAddress(String emailAddress){
        String  expression= "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
            HttpPost httppost = new HttpPost(apilink + "/signup.php");
            JSONObject json = new JSONObject();

            try {
                // JSON data:


                json.put("fname", First);
                json.put("lname", Last);
                json.put("phonenumber", Num);
                json.put("pass",Pass);
                json.put("email",Email);
                json.put("regId",regId);

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
            Toast.makeText(signup.this,"Response : " + text,Toast.LENGTH_LONG).show();
            pd.dismiss();

            if(text.contains("Registered Successfully")) {
                name = First + " " + Last;
                emails = Email;
                Intent i = new Intent(signup.this, LoginOrSignup.class);
                i.putExtra("name", name);
                i.putExtra("email", emails);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(i);
                finish();
            }

        }

    }
}
