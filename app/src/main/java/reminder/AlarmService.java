package reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> 8c13fe976855a2811ca66d9ee032ecce5b2c5a7c

import java.util.Calendar;

/**
<<<<<<< HEAD
 * Created by Swati garg on 05-07-2015.
=======
 * Created by kamlesh kumar garg on 05-07-2015.
>>>>>>> 8c13fe976855a2811ca66d9ee032ecce5b2c5a7c
 */
public class AlarmService {
    private Context context;
    private PendingIntent mAlarmSender;
<<<<<<< HEAD
    int m;
    public AlarmService(Context context,long id) {
        this.context = context;
        int m=(int)id;
        this.m=m;
         }

    public PendingIntent startAlarm(Calendar c, String days ){
        //Set the alarm to 10 seconds from now
        long firstTime = c.getTimeInMillis();
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        m=m*100;
        int i=0;
        String d = days.toLowerCase();
        if(d.contains("everyday")) {
            while(i<100) {
                m=m+i;
                mAlarmSender = PendingIntent.getBroadcast(context, m, new Intent(context, AlarmReciever.class), 0);
                Log.e("setting",c+" ");
                am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);
                c.add(Calendar.SECOND, 86400);
                firstTime = c.getTimeInMillis();
                i++;
            }
        }else {
            int ar[] = {-1,0, 0, 0, 0, 0, 0, 0};
            //sun,mon,tue,wed,thur,fri,sat,sun
            Log.e("jhbbik","oi"+ " "+d);
            if(d.contains("monday"))
                ar[1]=1;
            if(d.contains("tuesday"))
                ar[2]=1;
            if(d.contains("wednesday"))
                ar[3]=1;
            if(d.contains("thursday"))
                ar[4]=1;
            if(d.contains("friday"))
                ar[5]=1;
            if(d.contains("saturday"))
                ar[6]=1;
            if(d.contains("sunday"))
                ar[7]=1;


            while(i<100) {
                m=m+i;
                mAlarmSender = PendingIntent.getBroadcast(context, m, new Intent(context, AlarmReciever.class), 0);
                int dow = c.get(Calendar.DAY_OF_WEEK);
                if(ar[dow]==1) {
                    am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);
                }c.add(Calendar.SECOND, 86400);
                firstTime = c.getTimeInMillis();
                i++;
            }

        }
       // mAlarmPendingIntent = PendingIntent.getActivity(this, requestCode, intent, flags);
        return mAlarmSender;
    }
    public void  cancel(long id){
        m=(int)id;
        m=m*100;
        int i=0;
        while(i<100) {

            m=m+i;
            mAlarmSender = PendingIntent.getBroadcast(context, m, new Intent(context, AlarmReciever.class), 0);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            am.cancel(mAlarmSender);
            i++;
        }
=======
    public AlarmService(Context context) {
        this.context = context;
        mAlarmSender = PendingIntent.getBroadcast(context, 0, new Intent(context, AlarmReciever.class), 0);
    }

    public void startAlarm(){
        //Set the alarm to 10 seconds from now
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND, 10);
        long firstTime = c.getTimeInMillis();
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, firstTime, mAlarmSender);

       // mAlarmPendingIntent = PendingIntent.getActivity(this, requestCode, intent, flags);
//this.getAlarmManager().cancel(mAlarmPendingIntent);
>>>>>>> 8c13fe976855a2811ca66d9ee032ecce5b2c5a7c
    }
}