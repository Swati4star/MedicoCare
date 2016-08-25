package reminder;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;


import java.util.Calendar;

import home.medico.com.medicohome.R;


public class AddMed extends AppCompatActivity implements Displaydays.EditNameDialogListener, View.OnClickListener {

    int DAYS = 0;
    Button blue,dblue,dgreen,green,maroon,orange,purple,red,white,orange2,purple2,grey;
    int Blue,Dblue,Dgreen,Green,Maroon,Orange,Purple,Red,White,Orange2,Purple2,Grey;
    AutoCompleteTextView medname;
    RadioButton all,selected;
    TextView displaydays;
    EditText message;
    NumberPicker numberPicker;
    TimePicker timep;
    int dosage;
    String extramessage,mediname,days,time;

    String ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        getSupportActionBar().setTitle("Add New Medicine");
        getSupportActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        numberPicker = (NumberPicker) findViewById(R.id.numberPicker1);
        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
        timep = (TimePicker) findViewById(R.id.timePicker1);
        medname = (AutoCompleteTextView) findViewById(R.id.medname);
        message = (EditText) findViewById(R.id.extramessage);
        Blue=Dblue=Dgreen=Green=Maroon=Orange=Purple=Red=White=Orange2=Purple2=Grey=0;
        blue = (Button) findViewById(R.id.blue);
        blue.setOnClickListener(this);
        purple2 = (Button) findViewById(R.id.purple2);
        purple2.setOnClickListener(this);
        grey = (Button) findViewById(R.id.grey);
        grey.setOnClickListener(this);
        dblue = (Button) findViewById(R.id.dblue);
        dblue.setOnClickListener(this);
        green = (Button) findViewById(R.id.green);
        green.setOnClickListener(this);
        dgreen = (Button) findViewById(R.id.dgreen);
        dgreen.setOnClickListener(this);
        maroon = (Button) findViewById(R.id.maroon);
        maroon.setOnClickListener(this);
        orange = (Button) findViewById(R.id.orange);
        orange.setOnClickListener(this);
        purple = (Button) findViewById(R.id.purple);
        purple.setOnClickListener(this);
        red = (Button) findViewById(R.id.red);
        red.setOnClickListener(this);
        white = (Button) findViewById(R.id.white);
        white.setOnClickListener(this);
        orange2 = (Button) findViewById(R.id.orange2);
        orange2.setOnClickListener(this);

        all = (RadioButton) findViewById(R.id.all);
        selected = (RadioButton) findViewById(R.id.some);
        displaydays = (TextView) findViewById(R.id.selecteddays);


        all.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(all.isChecked()){
                    selected.setChecked(false);
                    displaydays.setText("EveryDay");
                }
            }
        });
        selected.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(selected.isChecked()){
                    all.setChecked(false);
                    Displaydays d = new Displaydays(AddMed.this);
                    d.show();
                    //startActivityForResult(i, DAYS);
                }
            }
        });
        medname = (AutoCompleteTextView) findViewById(R.id.medname);
        String[] mednames = getResources().getStringArray(R.array.medicines);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mednames);
        medname.setAdapter(adapter);



         Intent i = getIntent();
        if(i.getStringExtra(DBhelp.KEY_NAME)!=null) {
            medname.setText(i.getStringExtra(DBhelp.KEY_NAME) + " ");
            displaydays.setText(i.getStringExtra(DBhelp.KEY_DAYS) + " ");
            numberPicker.setValue(Integer.parseInt(i.getStringExtra(DBhelp.KEY_DOSAGE)));
            message.setText(i.getStringExtra(DBhelp.KEY_MESSAGE) + " ");
            ids = i.getStringExtra(DBhelp.KEY_ROWID);
        }

    }

    @Override
    public void onFinishEditDialog(String inputText) {
        displaydays.setText(inputText);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_med, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();

        if (id == R.id.yes) {

            extramessage = message.getText().toString();
            dosage = numberPicker.getValue();
            mediname = medname.getText().toString();
            days = displaydays.getText().toString();
            time = timep.getCurrentHour().toString() + " : " + timep.getCurrentMinute().toString();
            Log.e("fdbssfbv",""+mediname+" "+dosage+" "+days+" "+time);
            if(mediname.equals(""))
                Toast.makeText(this, "Add Medicine Name", Toast.LENGTH_SHORT).show();
            else if(days.equals(""))
                Toast.makeText(this, "Add Days", Toast.LENGTH_SHORT).show();
            else if(dosage==0)
                Toast.makeText(this, "Add Dosage", Toast.LENGTH_SHORT).show();
            else {
                DBhelp d = new DBhelp(this);
                d.open();
                String color=" ";
                if(Blue==1)
                    color="Blue";
                if(Dblue==1)
                    color="Dblue";
                if(Dgreen==1)
                    color="Dgeen";
                if(Green==1)
                    color="Green";
                if(Maroon==1)
                    color="Maroon";
                if(Orange==1)
                    color="Orange";
                if(Orange2==1)
                    color="Orange2";
                if(Purple==1)
                    color="Purple";
                if(Purple2==1)
                    color="Purple2";
                if(Red==1)
                    color="Red";
                if(Grey==1)
                    color="Grey";
                if(White==1)
                    color="White";
                Long i;

                if (ids == null) {
                    i =  d.createNote(mediname, days, "" + dosage, time, color, extramessage);
                }else {
                    i = Long.parseLong(ids);
                    d.updateNote(Long.parseLong(ids), mediname, days, "" + dosage, time, color, extramessage);
                }Toast.makeText(this, "Medicine added", Toast.LENGTH_SHORT).show();

                Long t = System.currentTimeMillis();
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(t);
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                AlarmService a = new AlarmService(getApplicationContext(),i);
                c.set(mYear, mMonth, mDay, timep.getCurrentHour(), timep.getCurrentMinute());
                a.startAlarm(c,days);
                finish();
            }
            if(mediname==null)
                Toast.makeText(this, "Add Medicine Name", Toast.LENGTH_SHORT).show();
            else {
                DBhelp d = new DBhelp(this);
                d.open();
                if(ids==null)
                d.createNote(mediname, days, "" + dosage, time, " COLOR", extramessage);
                else
                d.updateNote(Long.parseLong(ids),mediname, days, "" + dosage, time, " COLOR", extramessage);
                Toast.makeText(this, "Medicine added", Toast.LENGTH_SHORT).show();
                finish();
            }


            AlarmService a = new AlarmService(getApplicationContext(),0);
            Calendar c = Calendar.getInstance();
            c.set(2015,7,16,timep.getCurrentHour(),timep.getCurrentMinute());
            a.startAlarm();

            return true;
        }
        else
        if(id == R.id.no){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void clearall()
    {
        blue.setBackgroundResource(R.drawable.blue);
        dblue.setBackgroundResource(R.drawable.dblue);
        dgreen.setBackgroundResource(R.drawable.dgreen);
        green.setBackgroundResource(R.drawable.green);
        maroon.setBackgroundResource(R.drawable.maroon);
        orange.setBackgroundResource(R.drawable.orange);
        purple.setBackgroundResource(R.drawable.purple);
        red.setBackgroundResource(R.drawable.red);
        white.setBackgroundResource(R.drawable.white);
        orange2.setBackgroundResource(R.drawable.orange2);
        grey.setBackgroundResource(R.drawable.grey);
        purple2.setBackgroundResource(R.drawable.purple2);
        Blue=Dblue=Dgreen=Green=Maroon=Orange=Purple=Red=Grey=Purple2=White=Orange2=0;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch(v.getId())
        {
            case R.id.blue :  if(Blue == 0)
            {
                clearall();
                Blue=1;
                blue.setBackgroundResource(R.drawable.blue_selected);

            }
            else
            {
                Blue=0;
                blue.setBackgroundResource(R.drawable.blue);
            }break;
            case R.id.dblue :  if(Dblue == 0)
            {
                clearall();
                Dblue=1;
                dblue.setBackgroundResource(R.drawable.dblue_selected);

            }
            else
            {
                Dblue=0;
                dblue.setBackgroundResource(R.drawable.dblue);
            }break;
            case R.id.dgreen :  if(Dgreen == 0)
            {
                clearall();
                Dgreen=1;
                dgreen.setBackgroundResource(R.drawable.dgreen_selected);

            }
            else
            {
                Dgreen=0;
                dgreen.setBackgroundResource(R.drawable.dgreen);
            }break;
            case R.id.green :  if(Green == 0)
            {
                clearall();
                Green=1;
                green.setBackgroundResource(R.drawable.green_selected);

            }
            else
            {
                Green=0;
                green.setBackgroundResource(R.drawable.green);
            }break;
            case R.id.maroon :  if(Maroon == 0)
            {
                clearall();
                Maroon=1;
                maroon.setBackgroundResource(R.drawable.maroon_selected);

            }
            else
            {
                Maroon=0;
                maroon.setBackgroundResource(R.drawable.maroon);
            }break;
            case R.id.orange :  if(Orange == 0)
            {
                clearall();
                Orange=1;
                orange.setBackgroundResource(R.drawable.orange_selected);

            }
            else
            {
                Orange=0;
                orange.setBackgroundResource(R.drawable.orange);
            }break;
            case R.id.purple :  if(Purple == 0)
            {
                clearall();
                Purple=1;
                purple.setBackgroundResource(R.drawable.purple_selected);

            }
            else
            {
                Purple=0;
                purple.setBackgroundResource(R.drawable.purple);
            }break;
            case R.id.red :  if(Red == 0)
            {
                clearall();
                Red=1;
                red.setBackgroundResource(R.drawable.red_selected);

            }
            else
            {
                Red=0;
                red.setBackgroundResource(R.drawable.red);
            }break;
            case R.id.orange2 :  if(Orange2 == 0)
            {
                clearall();
                Orange2=1;
                orange2.setBackgroundResource(R.drawable.orange2_selected);

            }
            else
            {
                Orange2=0;
                orange2.setBackgroundResource(R.drawable.orange2);
            }break;
            case R.id.white :  if(White == 0)
            {
                clearall();
                White=1;
                white.setBackgroundResource(R.drawable.white_selected);

            }
            else
            {
                White=0;
                white.setBackgroundResource(R.drawable.white);
            }break;
            case R.id.purple2 :  if(Purple2 == 0)
            {
                clearall();
                Purple2=1;
                purple2.setBackgroundResource(R.drawable.purple2_selected);

            }
            else
            {
                Purple2=0;
                purple2.setBackgroundResource(R.drawable.purple2);
            }break;
            case R.id.grey :  if(Grey == 0)
            {
                clearall();
                Grey=1;
                grey.setBackgroundResource(R.drawable.grey_selected);

            }
            else
            {
                Grey=0;
                grey.setBackgroundResource(R.drawable.grey);
            }break;
        }

    }
}
