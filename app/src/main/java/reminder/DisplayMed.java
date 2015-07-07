package reminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import home.medico.com.medicohome.R;


public class DisplayMed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_med);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        Intent i = getIntent();
        TextView t = (TextView) findViewById(R.id.medname);
        TextView t1 = (TextView) findViewById(R.id.meddays);
        TextView t2= (TextView) findViewById(R.id.meddosage);
        TextView t3 = (TextView) findViewById(R.id.medtime);
        TextView t4 = (TextView) findViewById(R.id.medmsg);
        t.setText(i.getStringExtra(DBhelp.KEY_NAME) + " " );
        t1.setText(i.getStringExtra(DBhelp.KEY_DAYS) + " ");
        t2.setText(i.getStringExtra(DBhelp.KEY_DOSAGE) + " ");
        t3.setText(i.getStringExtra(DBhelp.KEY_TIME) + " ");
        t4.setText(i.getStringExtra(DBhelp.KEY_MESSAGE) + " ");

        getSupportActionBar().setTitle("Medicine");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_med, menu);
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
