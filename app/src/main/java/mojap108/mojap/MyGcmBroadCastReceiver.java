package mojap108.mojap;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by gollaba on 7/22/15.
 */

public class MyGcmBroadCastReceiver extends BroadcastReceiver {

    private static final String GCM_RECEIVE_INTENT = "com.google.android.c2dm.intent.RECEIVE";
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "received from gcm");
        if (intent.getAction().equals(GCM_RECEIVE_INTENT)) {
            Bundle bundle = intent.getExtras();
            String alert = null;
            String payload = null;
            alert = bundle.getString("message");
            payload = bundle.getString("payload");
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            Intent notificationIntent = new Intent(context,  MoJapMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            notificationIntent.putExtra("message", payload);
            notificationIntent.putExtra("alert", alert);

            PendingIntent resultIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.app_icon_resize1)
                    .setContentTitle("Mojap")
                    .setContentText(alert);
            mBuilder.setContentIntent(resultIntent);
            mBuilder.setSound(alarmSound);
            mBuilder.setColor(context.getResources().getColor(android.R.color.holo_orange_dark));
            notificationManager.notify(1, mBuilder.build());
        }
    }

}
