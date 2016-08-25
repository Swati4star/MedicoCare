package buildorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import home.medico.com.medicohome.R;


public class MyCart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        Intent it = getIntent();
        String[] values = it.getStringArrayExtra("Mednames");
        ArrayList<Integer> qty = it.getIntegerArrayListExtra("qty");
        int in = it.getIntExtra("num", 0);
        Object[] x = qty.toArray();

        setTitle(" My Cart ");
        TextView t = (TextView) findViewById(R.id.textView1);
        for (int i = 0; i < in; i++) {
            t.append(values[i] + "   " + x[i].toString() + "  \n");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
