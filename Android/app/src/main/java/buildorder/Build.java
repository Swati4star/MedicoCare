package buildorder;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import home.medico.com.medicohome.ConfirmAdd;
import home.medico.com.medicohome.Constants;
import home.medico.com.medicohome.LoginOrSignup;
import home.medico.com.medicohome.R;
import home.medico.com.medicohome.ShowMessage;
import reminder.profile;

public class Build extends AppCompatActivity implements Constants {

    ProgressDialog prgDialog;
    String encodedString;
    Boolean preupload;
    String preid = "";
    RequestParams params = new RequestParams();
    String imgPath;
    Bitmap bitmap;
    private static int RESULT_LOAD_IMG = 1;
    public static boolean valid = false;
    AutoCompleteTextView medname;
    TextView t1, t2, amount;
    Button plus, minus;
    String nameyet, latitude, longitude;
    Button next, addmed;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    static final String[] sideitems = new String[]{"Reminder", "My Orders", "Share", "Feedback", "Rate us", "Log Out"};    //items on navigation drawer
    public static String name, emails;
    Button pic, up;
    List<String> list;
    List<String> flags;
    Integer[] imageId = {
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill,
            R.drawable.pill};

    String[] medinames = new String[100];
    ArrayList<String> mec = new ArrayList<String>();
    ArrayList<Integer> qty;
    Button add;
    int in;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");

        preupload = false;
        prgDialog = new ProgressDialog(this);
        prgDialog.setCancelable(false);
        Intent i = getIntent();
        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");

        qty = new ArrayList<>(100);

        pic = (Button) findViewById(R.id.cam);
        up = (Button) findViewById(R.id.up);
        amount = (TextView) findViewById(R.id.amount);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        t1 = (TextView) findViewById(R.id.textView1);
        t2 = (TextView) findViewById(R.id.textView2);
        t1.setTypeface(custom_font);
        t2.setTypeface(custom_font);

        in = 0;

        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        addmed = (Button) findViewById(R.id.addMed);
        addmed.setTypeface(custom_font);


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(amount.getText().toString());
                x++;
                amount.setText(Integer.toString(x));

            }

        });


        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(amount.getText().toString());
                x--;
                if (x > 0)
                    amount.setText(Integer.toString(x));

            }
        });
        medname = (AutoCompleteTextView) findViewById(R.id.medname);

        String[] mednames = getResources().getStringArray(R.array.medicines);

        medname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nameyet = medname.getText().toString();
                new DownloadWebPageTask().execute();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        next = (Button) findViewById(R.id.next);
        next.setTypeface(custom_font);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preupload && preid.equals("")) {
                    Toast.makeText(Build.this, "You need to upload Prescription", Toast.LENGTH_SHORT).show();

                } else {

                    Intent i = new Intent(Build.this, ConfirmAdd.class);
                    overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                    i.putExtra("Mednames", medinames);
                    i.putIntegerArrayListExtra("qty", qty);
                    i.putExtra("num", in);
                    i.putStringArrayListExtra("Names", mec);
                    i.putExtra("latitude", latitude);
                    i.putExtra("longitude", longitude);
                    i.putExtra("preid", preid);
                    startActivity(i);
                }
            }
        });


        add = (Button) findViewById(R.id.add);
        add.setTypeface(custom_font);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (medname.getText().toString().matches("")) {
                    Toast.makeText(Build.this, "Enter medicine name", Toast.LENGTH_SHORT).show();
                } else if (valid == false) {
                    Toast.makeText(Build.this, "Enter valid medicine name", Toast.LENGTH_SHORT).show();

                } else {
                    medinames[in] = medname.getText().toString();
                    qty.add(Integer.parseInt(amount.getText().toString()));
                    Toast.makeText(Build.this, "Medicine Added", Toast.LENGTH_SHORT).show();
                    medname.setText("");
                    mec.add(medinames[in]);
                    in++;
                    valid = false;
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

/*        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.side_panel, sideitems));*/
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                toolbar,  /* navigation drawer icon to replace 'Up' caret */
                R.string.app_name,  /* "open drawer" description */
                R.string.app_name  /* "close drawer" description */);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (position == 1) {
                    Intent i = new Intent(Build.this, profile.class);
                    startActivity(i);
                }
                if (position == 2) {
                    Intent i = new Intent(Build.this, ShowMessage.class);
                    startActivity(i);
                }

                if (position == 4) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"abc@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback : MedicoHome");
                    intent.putExtra(Intent.EXTRA_TEXT, "...");
                    startActivity(Intent.createChooser(intent, "Choose..."));

                }
                if (position == 3) {
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Medicohome");
                        String sAux = "\nCheck out this application\n\n";
                        sAux = sAux + "https://www.facebook.com/Medicohomeinc?fref=ts \n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "Choose.."));
                    } catch (Exception e) { //e.toString();
                    }

                }

                if (position == 5) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.appify4u.infonizer")));

                }

                if (position == 6) {
                    new AlertDialog.Builder(Build.this)
                            .setMessage("Are you sure you want to Log Out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("login", 0);
                                    editor.apply();


                                    Intent intent = new Intent(Build.this, LoginOrSignup.class);
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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = sharedPreferences.getString("name", null);

        getSupportActionBar().setTitle(name);
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        // getActionBar().hide();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_build, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        ActionBar bar = getActionBar();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_cart) {
            if (in == 0)
                Toast.makeText(Build.this, "No items in cart yet", Toast.LENGTH_SHORT).show();
            else {
                Intent i = new Intent(Build.this, MyCart.class);
                i.putExtra("Mednames", medinames);
                i.putIntegerArrayListExtra("qty", qty);
                i.putExtra("num", in);
                startActivity(i);
            }
        }
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
            HttpPost httppost = new HttpPost(apilink + "/autocomplete.php");
            JSONObject json = new JSONObject();

            try {
                // JSON data:


                json.put("stringq", nameyet);

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
            JSONObject ob;
            JSONArray arr;
            try {
                ob = new JSONObject(text);
                arr = ob.getJSONArray("suggest_medicine");
                list = new ArrayList<String>();
                flags = new ArrayList<String>();
                for (int i = 0; i < arr.length(); i++) {
                    try {
                        list.add(arr.getJSONObject(i).getString("medicine_name"));
                        flags.add(arr.getJSONObject(i).getString("prescription_required"));

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                        (getApplicationContext(), R.layout.spinner_layout, list);

                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                medname.setThreshold(1);
                medname.setAdapter(dataAdapter);

                medname.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        valid = true;
                        if (flags.get(arg2).toString().equals("1"))
                            preupload = true;
                    }
                });

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                imgPath = DocumentUtils.getPath(Build.this, data.getData());
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);

                params.put("filename", imgPath);

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }

    // When Upload button is clicked
    public void uploadImage() {
        // When Image is selected from Gallery
        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Uploading Prescription");
            prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "You must select image from gallery before you try to upload",
                    Toast.LENGTH_LONG).show();
        }
    }

    // AsyncTask - To convert Image to String
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {
                prgDialog.setMessage("Uploading...");
                // Put converted Image string into Async Http Post param
                params.put("image", encodedString);
                // Trigger Image upload
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Invoking Php");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://api.medicohome.com/upload-prescriptions.php",
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        prgDialog.hide();

                        String str = "";
                        str = convertByteArrayToString(responseBody);

                        Log.e("yeah", str);
                        preid = str;
                        Toast.makeText(getApplicationContext(),
                                "Success : " + str,
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }


                });
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String convertByteArrayToString(byte[] byteArray) {
        String s = null;
        if (byteArray != null) {
            if (byteArray.length > 0) {
                try {
                    s = new String(byteArray, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return s;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


}
