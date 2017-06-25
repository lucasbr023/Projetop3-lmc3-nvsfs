package com.example.lmc3.projetop3_lmc3_nvsfs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lmc3 on 18/06/2017.
 */

public class PhoneUnlockedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
         if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent mainIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);

            Notification.Builder notification = new Notification.Builder(context)
                    .setSmallIcon(android.R.drawable.btn_star)
                    .setAutoCancel(true)
                    .setContentTitle("Previsão do tempo e modeda convertida")
                    .setContentText("Aqui estará a previsão do tempo do lugar onde a pessoa to no momento");


            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //Ao clicar na notificação, será redirecionado para a MainActivity
            notification.setContentIntent(mainIntent);
            mNotificationManager.notify(0,notification.build());
        }
    }
}
