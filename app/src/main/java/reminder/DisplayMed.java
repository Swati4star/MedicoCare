package reminder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

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
        t2.setText("Dosage : " + i.getStringExtra(DBhelp.KEY_DOSAGE) + " ");
        t3.setText("Take at : " + i.getStringExtra(DBhelp.KEY_TIME) + " ");
        t4.setText(i.getStringExtra(DBhelp.KEY_MESSAGE) + " ");
        String col =  i.getStringExtra(DBhelp.KEY_COLOR);
        Button im = (Button) findViewById(R.id.col);
        if(!col.equals(" "))
        {
            if(col.equals("Blue"))
                im.setBackgroundResource(R.drawable.blue_selected);
            if(col.equals("Dblue"))
                im.setBackgroundResource(R.drawable.dblue_selected);
            if(col.equals("Green"))
                im.setBackgroundResource(R.drawable.green_selected);
            if(col.equals("Dgreen"))
                im.setBackgroundResource(R.drawable.dgreen_selected);
            if(col.equals("Orange"))
                im.setBackgroundResource(R.drawable.orange_selected);
            if(col.equals("Orange2"))
                im.setBackgroundResource(R.drawable.orange2_selected);
            if(col.equals("Purple2"))
                im.setBackgroundResource(R.drawable.purple2_selected);
            if(col.equals("Purple"))
                im.setBackgroundResource(R.drawable.purple_selected);
            if(col.equals("Red"))
                im.setBackgroundResource(R.drawable.red_selected);
            if(col.equals("Grey"))
                im.setBackgroundResource(R.drawable.grey_selected);
            if(col.equals("White"))
                im.setBackgroundResource(R.drawable.white_selected);
            if(col.equals("Maroon"))
                im.setBackgroundResource(R.drawable.maroon_selected);
            }
        else
            im.setVisibility(View.GONE);


        setTitle(" ");

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
