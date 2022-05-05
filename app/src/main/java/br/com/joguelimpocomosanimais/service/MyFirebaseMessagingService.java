package br.com.joguelimpocomosanimais.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.com.joguelimpocomosanimais.Activity.Animais;
import br.com.joguelimpocomosanimais.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage notificacao) {
        if (notificacao.getNotification() != null) {
            String titulo = notificacao.getNotification().getTitle();
            String corpo = notificacao.getNotification().getBody();
            enviarNotifica(titulo, corpo);

        }

    }

    private void enviarNotifica(String titulo, String corpo) {
        // configuração
        String canal = getString(R.string.default_notification_channel_id);
        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Intent intent = new Intent(this, Animais.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        // cria a notificação
        NotificationCompat.Builder notifica = new NotificationCompat.Builder(this, canal)
                .setContentTitle(titulo)
                .setContentText(corpo)
                .setSmallIcon(R.drawable.send)
                .setSound(som)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

// recupera notificação

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chanel = new NotificationChannel(canal, "canal", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(chanel);


        }
        ;


        // envia notificação
        notificationManager.notify(0, notifica.build());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        // salvar token



    }






}
