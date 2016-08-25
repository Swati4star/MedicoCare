package home.medico.com.medicohome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class Splash extends AppCompatActivity {

    ImageView iv;
    TextView t1, t2;
    LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");

        iv = (ImageView) findViewById(R.id.logo);
        t1 = (TextView) findViewById(R.id.medico);
        t1.setTypeface(custom_font);
        t2 = (TextView) findViewById(R.id.home);
        t2.setTypeface(custom_font);
        l = (LinearLayout) findViewById(R.id.text);

        iv.setAnimation(animationFadeIn);
        l.setAnimation(animationFadeIn);


        Thread t = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //Intent i = new Intent("android.intent.action.MEDICOUSER");

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    int in = sharedPreferences.getInt("login", -1);
                    Log.e("YYYYY", "seret  " + in);
                    if (in == 1) {
                        Intent i = new Intent(Splash.this, Options.class);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        startActivity(i);
                        finish();
                    } else {

                        Intent i2 = new Intent(Splash.this, LoginOrSignup.class);
                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                        startActivity(i2);
                        finish();
                    }
                }
            }
        };
        t.start();
        getSupportActionBar().setTitle(" ");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
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
}
