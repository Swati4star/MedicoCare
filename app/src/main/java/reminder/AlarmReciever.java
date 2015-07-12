package reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;

import home.medico.com.medicohome.R;

/**
 * Created by kamlesh kumar garg on 05-07-2015.
 */
public class AlarmReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager mNM;
        mNM = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.logo, "Test Alarm",
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, profile.class), 0);
        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(context, "Medicohome Reminder", "Time to take Medicine", contentIntent);


        SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(context);
        String alarms = getAlarms.getString("ringtone", null);


        if(alarms!=null)
        notification.sound = Uri.parse(alarms);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE; // Send the notification.
        // We use a layout id because it is a unique number. We use it later to cancel.


        mNM.notify(R.string.Remind, notification);

    }
}
