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
import ac.sliet.complaintmanagement.UI.MainActivity;

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        try {
            Map<String,String> dataRecv= remoteMessage.getData();

            if (null!=dataRecv)
            {  Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("NOTI", true);
//                     Sy
                System.out.println("CID = "+dataRecv.get(Common.NOTI_CID));
             generateNotification(this,new Random().nextInt(),dataRecv.get(Common.NOTI_TITLE),dataRecv.get(Common.NOTI_CONTENT),intent);
            }
//            {
//
//                switch (Objects.requireNonNull(dataRecv.get(Common.NOTI_TITLE))) {
//                    case "Order Cancelled":
//                    case "Order Delivered": {
//                        // the order will be now in current orders  so opening orders history
//
//                        Intent intent = new Intent(this, MainActivity.class);
//                        intent.putExtra(Common.OPEN_ORDERS_HISTORY_FROM_NOTIFICATION, true);
//                        generateNotification(this, new Random().nextInt(), dataRecv.get(Common.NOTI_TITLE), dataRecv.get(Common.NOTI_CONTENT), intent);
//
//                        break;
//                    }
//                    case "Order Status Updated": {
//                        // the order will be now in orders history so opening orders history
//                        Intent intent = new Intent(this, MainActivity.class);
//                        intent.putExtra(Common.OPEN_CURRENT_ORDERS_FROM_NOTIFICATION, true);
//                        generateNotification(this, new Random().nextInt(), dataRecv.get(Common.NOTI_TITLE), dataRecv.get(Common.NOTI_CONTENT), intent);
//                        break;
//                    }
//                    case "Message Reply":
//                    case "New Message": {
//                        Intent intent = new Intent(this, MainActivity.class);
//                        intent.putExtra(Common.OPEN_MY_CHATS_FROM_NOTIFICATION, true);
//
//                        generateNotification(this, new Random().nextInt(), dataRecv.get(Common.NOTI_TITLE), dataRecv.get(Common.NOTI_CONTENT), intent);
//
//                        break;
//                    }
//                    //                case "Update Available":
//                    //                    {
//                    //                        Intent intent ;//= new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.localkart.localkartcustomer"));
//                    //
//                    //                        try {
//                    //                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.localkart.localkartcustomer" ));
//                    //
//                    //                        } catch (android.content.ActivityNotFoundException anfe) {
//                    //                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.localkart.localkartcustomer"));
//                    //                        }
//                    //                    generateNotification(this, new Random().nextInt(), dataRecv.get(Common.NOTI_TITLE), dataRecv.get(Common.NOTI_CONTENT), intent);
//                    //
//                    //                    break;
//                    //                    }
//                    default:
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            // this  is chutiyappa nothing else don't get confused whenever you see after a long time that for what it is being used
//                            Common.showNotification(this, new Random().nextInt(), dataRecv.get(Common.NOTI_TITLE), dataRecv.get(Common.NOTI_CONTENT), null);
//                        }else {
//                            generateNotification(this, new Random().nextInt(), dataRecv.get(Common.NOTI_TITLE), dataRecv.get(Common.NOTI_CONTENT), null);
//                        }
//                        break;
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
      //  Common.updateToken(this,s);
    }

    private void generateNotification(Context context,int id,String title,String content,Intent intent){
        PendingIntent pendingIntent = null;

        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        String NOTIFICATION_CHANNEL_ID = "local_kart";
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


        if (title.contains("New Message") || title.contains("Message Reply")) {

            builder.setSmallIcon(R.drawable.ic_sliet_logo_black);

        }
        else {
            builder.setSmallIcon(R.drawable.ic_sliet_logo_black);
        }

        builder.setPriority(NotificationManager.IMPORTANCE_HIGH).setAutoCancel(true)
                //.setSubText(context.getResources().getString(R.string.madeinindia))

                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.attention))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(content)).setColor(Color.parseColor("#FFFFFFFF"));

        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(id, notification);

    }
}
