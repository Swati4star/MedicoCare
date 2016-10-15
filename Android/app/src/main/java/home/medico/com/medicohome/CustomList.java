package home.medico.com.medicohome;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] obid, or, add;

    public CustomList(Activity context, String[] order, String[] address, String[] oid) {
        super(context, R.layout.message, order);
        this.context = context;
        or = order;
        add = address;
        obid = oid;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.message2, parent, true);
        TextView t = (TextView) rowView.findViewById(R.id.textView1);
        String a[] = or[position].split(",");

        String s = " ";
        for (int i = 0; i < a.length; i++) {
            if (a[i].contains("null"))
                break;
            s = s + a[i];
        }
        t.setText(s);

        TextView t2 = (TextView) rowView.findViewById(R.id.textView2);
        t2.setText(add[position] + obid[position]);

        return rowView;
    }


}