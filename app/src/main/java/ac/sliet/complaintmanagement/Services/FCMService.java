package ac.sliet.complaintmanagement.Services;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import ac.sliet.complaintmanagement.Common.Common;
import ac.sliet.complaintmanagement.R;
import ac.sliet.complaintmanagement.SplashActivity;
import ac.sliet.complaintmanagement.UI.MainActivity;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {
            Map<String, String> dataRecv = remoteMessage.getData();

            if (null != dataRecv) {
                Intent intent = new Intent(this, SplashActivity.class);
                intent.putExtra(Common.IS_OPENED_FROM_NOTIFICATION, true);
                intent.putExtra(Common.COMPLAINT_ID_FROM_NOTIFICATION, dataRecv.get(Common.NOTI_CID));

                int status = -1;

                try {
                    if (dataRecv.containsKey(Common.NOTI_STATUS)) {

                        status = Integer.parseInt(dataRecv.get(Common.NOTI_STATUS));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                generateNotification(this, new Random().nextInt(), dataRecv.get(Common.NOTI_TITLE), dataRecv.get(Common.NOTI_CONTENT), status, intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Common.updateToken(this);
    }

    private void generateNotification(Context context, int id, String title, String content, int status, Intent intent) {
        PendingIntent pendingIntent = null;

        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String NOTIFICATION_CHANNEL_ID = "SLIET_COMPLAINANT";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Updates", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Local Kart #madeinindia  \\uD83C\\uDDEE\\uD83C\\uDDF3 "); //\u1F1EE\u1F1F3
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);

            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(false);

            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title).setContentText(content).setAutoCancel(true);


        builder.setSmallIcon(R.drawable.ic_sliet_logo_black);


        builder.setPriority(NotificationManager.IMPORTANCE_HIGH).setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content)).setColor(Color.parseColor("#FFFFFFFF"));
        //.setSubText(context.getResources().getString(R.string.madeinindia))

        if (status != -1) {

            if (status == 1)
            {
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_accepted));
                System.out.println("status inside if = "+status);

            }
            if (status == 2) {
                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.noti_attend));

            }

        }
        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(id, notification);

    }
}
