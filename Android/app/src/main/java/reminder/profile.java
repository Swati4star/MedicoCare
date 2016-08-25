package reminder;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import home.medico.com.medicohome.R;


public class profile extends ListActivity {

    private DBhelp mDbHelper;
    private Cursor mNotesCursor;

    Button add;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        setContentView(R.layout.activity_profile);
        SharedPreferences sharedPreferences2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name2 = sharedPreferences2.getString("name", null);
        getActionBar().setTitle(name2);
        getActionBar().setIcon(R.drawable.girl);


        mDbHelper = new DBhelp(this);
        mDbHelper.open();
        fillData();
        registerForContextMenu(getListView());

        add = (Button) findViewById(R.id.addMed);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(profile.this, AddMed.class);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                startActivity(i);

            }
        });


        registerForContextMenu(getListView());
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
            menu.setHeaderTitle("Choose an Option");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        boolean refresh = false;

        switch (item.getItemId()) {
            case R.id.menu_edit:
                Cursor c1 = mNotesCursor;
                c1.moveToPosition(info.position);
                Intent i = new Intent(this, AddMed.class);
                i.putExtra(DBhelp.KEY_ROWID, info.id);
                i.putExtra(DBhelp.KEY_NAME, c1.getString(c1.getColumnIndexOrThrow(DBhelp.KEY_NAME)));
                i.putExtra(DBhelp.KEY_DAYS, c1.getString(c1.getColumnIndexOrThrow(DBhelp.KEY_DAYS)));
                i.putExtra(DBhelp.KEY_DOSAGE, c1.getString(c1.getColumnIndexOrThrow(DBhelp.KEY_DOSAGE)));
                i.putExtra(DBhelp.KEY_TIME, c1.getString(c1.getColumnIndexOrThrow(DBhelp.KEY_TIME)));
                i.putExtra(DBhelp.KEY_COLOR, c1.getString(c1.getColumnIndexOrThrow(DBhelp.KEY_COLOR)));
                i.putExtra(DBhelp.KEY_MESSAGE, c1.getString(c1.getColumnIndexOrThrow(DBhelp.KEY_MESSAGE)));
                startActivity(i);
                break;

            case R.id.menu_delete:
                long id = info.id;
                AlarmService a = new AlarmService(getApplicationContext(), id);
                a.cancel(id);
                DBhelp d = new DBhelp(profile.this);
                d.open();
                d.deleteNote(info.id);
                refresh = true;
                break;

            case R.id.menu_view:
                Cursor c = mNotesCursor;
                c.moveToPosition(info.position);
                Intent i2 = new Intent(this, DisplayMed.class);
                i2.putExtra(DBhelp.KEY_ROWID, info.id);
                i2.putExtra(DBhelp.KEY_NAME, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_NAME)));
                i2.putExtra(DBhelp.KEY_DAYS, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_DAYS)));
                i2.putExtra(DBhelp.KEY_DOSAGE, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_DOSAGE)));
                i2.putExtra(DBhelp.KEY_TIME, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_TIME)));
                i2.putExtra(DBhelp.KEY_COLOR, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_COLOR)));
                i2.putExtra(DBhelp.KEY_MESSAGE, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_MESSAGE)));
                startActivity(i2);
                break;
        }
        if (refresh) {
            SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
            adapter.getCursor().requery();
            adapter.notifyDataSetChanged();
        }

        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        openContextMenu(v);
    }


    private void fillData() {
        // Get all of the rows from the database and create the item list
        mNotesCursor = mDbHelper.fetchAllNotes();
        startManagingCursor(mNotesCursor);
        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] Mednamefrom = new String[]{DBhelp.KEY_NAME};
        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] MedNameto = new int[]{R.id.text1};
        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter notes = new SimpleCursorAdapter(this, R.layout.notes_row, mNotesCursor, Mednamefrom, MedNameto);
        setListAdapter(notes);
    }


   /* @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = mNotesCursor;
        c.moveToPosition(position);
       Intent i = new Intent(this, DisplayMed.class);
        i.putExtra(DBhelp.KEY_ROWID, id);
        i.putExtra(DBhelp.KEY_NAME, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_NAME)));
        i.putExtra(DBhelp.KEY_DAYS, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_DAYS)));
        i.putExtra(DBhelp.KEY_DOSAGE, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_DOSAGE)));
        i.putExtra(DBhelp.KEY_TIME, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_TIME)));
        i.putExtra(DBhelp.KEY_COLOR, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_COLOR)));
        i.putExtra(DBhelp.KEY_MESSAGE, c.getString(c.getColumnIndexOrThrow(DBhelp.KEY_MESSAGE)));
        startActivity(i);
    }*/


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // TODO Auto-generated method stub
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
