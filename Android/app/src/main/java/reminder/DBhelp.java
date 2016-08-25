package reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhelp {
    public static final String KEY_NAME = "name";
    public static final String KEY_DAYS = "days";
    public static final String KEY_DOSAGE = "dosage";
    public static final String KEY_TIME = "time";
    public static final String KEY_COLOR = "color";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table notes (_id integer primary key autoincrement, " + "name text not null, days text not null,"
            + " dosage text not null, time text not null, color text not null, message text not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "notes";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }

    public DBhelp(Context ctx) {
        this.mCtx = ctx;
    }

    public DBhelp open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createNote(String name, String days, String dosage, String time, String color, String message) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DAYS, days);
        initialValues.put(KEY_DOSAGE, dosage);
        initialValues.put(KEY_TIME, time);
        initialValues.put(KEY_COLOR, color);
        initialValues.put(KEY_MESSAGE, message);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteNote(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public void deleteAllNote() {
        mDb.delete(DATABASE_TABLE, null, null);
    }

    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_DAYS, KEY_DOSAGE, KEY_TIME, KEY_COLOR, KEY_MESSAGE}, null, null, null, null, null);
    }

    public Cursor fetchNote(long rowId) throws SQLException {
        Cursor mCursor = mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_NAME, KEY_DAYS, KEY_DOSAGE, KEY_TIME, KEY_COLOR, KEY_MESSAGE}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public boolean updateNote(long rowId, String name, String days, String dosage, String time, String color, String message) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_DAYS, days);
        args.put(KEY_DOSAGE, dosage);
        args.put(KEY_TIME, time);
        args.put(KEY_COLOR, color);
        args.put(KEY_MESSAGE, message);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}