package reminder;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

import home.medico.com.medicohome.R;

public class Displaydays extends Dialog implements View.OnClickListener, OnCheckedChangeListener{

	Context c;
	String res;
	Button ok;
	RadioButton mon,tue,wed,thu,fri,sat,sun ; 
	int mond,tues,wedn,thur,frid,satu,sund;
	public Displaydays(Context context) {
		
		super(context);
		c = context;
	}

	 public interface EditNameDialogListener {
	        void onFinishEditDialog(String inputText);
	    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_displaydays);
		//getActionBar().hide();
		setTitle("Select Days");
		mond=tues=wedn=thur=frid=satu=sund=0;
		ok = (Button) findViewById(R.id.ok);
		mon = (RadioButton) findViewById(R.id.mon);
		tue = (RadioButton) findViewById(R.id.tue);
		wed = (RadioButton) findViewById(R.id.wed);
		thu = (RadioButton) findViewById(R.id.thur);
		fri = (RadioButton) findViewById(R.id.fri);
		sat = (RadioButton) findViewById(R.id.sat);
		sun = (RadioButton) findViewById(R.id.sun);
		
		mon.setOnCheckedChangeListener(this);
		tue.setOnCheckedChangeListener(this);
		wed.setOnCheckedChangeListener(this);
		thu.setOnCheckedChangeListener(this);
		fri.setOnCheckedChangeListener(this);
		sat.setOnCheckedChangeListener(this);
		sun.setOnCheckedChangeListener(this);
		
		mon.setOnClickListener(this);
		tue.setOnClickListener(this);
		wed.setOnClickListener(this);
		thu.setOnClickListener(this);
		fri.setOnClickListener(this);
		sat.setOnClickListener(this);
		sun.setOnClickListener(this);
		
		ok.setOnClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.displaydays, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == ok.getId()){
			AddMed activity = (AddMed) c;
			createString();
            activity.onFinishEditDialog(res);
			this.dismiss();
		}
		
	}

	private void createString() {
		// TODO Auto-generated method stub
		res = " ";
		if(mond == 1)
			res += "Monday ";
		if(tues == 1)
			res += "Tuesday ";
		if(wedn == 1)
			res += "Wednesday ";
		if(thur == 1)
			res += "Thursday ";
		if(frid == 1)
			res += "Friday ";
		if(satu == 1)
			res += "Saturday ";
		if(sund == 1)
			res += "Sunday ";
		
			
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
		
		
		if(mon.isChecked())
			mond=1;
		else mond=0;
		if(tue.isChecked())
			tues=1;
		else tues=0;
		if(wed.isChecked())
			wedn=1;
		else wedn=0;
		if(thu.isChecked())
			thur=1;
		else thur=0;
		if(fri.isChecked())
			frid=1;
		else frid=0;
		if(sat.isChecked())
			satu=1;
		else satu=0;
		if(sun.isChecked())
			sund=1;
		else sund=0;
		
	}
}
