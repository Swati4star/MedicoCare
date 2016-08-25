package home.medico.com.medicohome;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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

public class CustomList extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] obid,or,add;
	public CustomList(Activity context, String[] order, String[] address, String[] oid){
		super(context, R.layout.message, order);
		this.context = context;
		or = order;
		add= address;
		obid=oid;

	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();


		Log.e("bfdbs" ,or[position] + "\n"+add[position]);

		View rowView= inflater.inflate(R.layout.message2, null, true);
		TextView t = (TextView) rowView.findViewById(R.id.textView1 );
		String a[]=or[position].split(",");

		String s=" ";
		for(int i=0;i<a.length;i++)
		{
			if(a[i].contains("null"))
				break;
			s = s+ a[i];
		}
		t.setText(s);

		TextView t2 = (TextView) rowView.findViewById(R.id.textView2 );
		t2.setText(add[position] + obid[position]);

		return rowView;
	}



}