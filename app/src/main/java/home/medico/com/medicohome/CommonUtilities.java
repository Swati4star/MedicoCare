package home.medico.com.medicohome;

/**
 * Created by kamlesh kumar garg on 13-06-2015.
 */import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    static final String TAG = "YOLO";

    static final String DISPLAY_MESSAGE_ACTION = "MESSAGE";

    static final String EXTRA_MESSAGE = "message";

    static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}