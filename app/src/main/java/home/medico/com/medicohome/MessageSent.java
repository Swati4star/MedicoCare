package home.medico.com.medicohome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MessageSent extends AppCompatActivity {


    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Regular.ttf");
        next = (Button) findViewById(R.id.next);
        next.setTypeface(custom_font);
        TextView t = (TextView) findViewById(R.id.yo);
                t.setTypeface(custom_font);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(MessageSent.this, LoginOrSignup.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);

                startActivity(i);
                finish();
            }
        });
        getSupportActionBar().setTitle(" Verification ");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_sent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
          int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
