package buildorder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import home.medico.com.medicohome.R;

public class CustomList2 extends ArrayAdapter<String>{
	private final Activity context;
	private final String[] web;
	private boolean isSpeakButtonLongPressed ;
	private final Integer[] imageId;
	public CustomList2(Activity context,String[] web, Integer[] imageId) {
			super(context, R.layout.message, web);
			this.context = context;
			this.web = web;
			this.imageId = imageId;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.message, null, true);
		Typeface custom_font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf");
		TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
		txtTitle.setTypeface(custom_font);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
		txtTitle.setText(web[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;
	}
}