package android.vibronav;

import android.app.Notification;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;

import java.io.ByteArrayOutputStream;

import android.net.Uri;
import android.media.Ringtone;
import android.media.RingtoneManager;

import android.os.Binder;
import android.os.IBinder;

public class NotificationService extends NotificationListenerService {

    Context context;
/*
    ConnectedThread mConnectedThread;

    public boolean btCancel(){
        //mConnectedThread = new ConnectedThread(mBTSocket);
        if(mConnectedThread != null)
            mConnectedThread.cancel();

        return true;
    };
    public boolean btStart(BluetoothSocket mBTSocket){
        mConnectedThread = new ConnectedThread(mBTSocket);
        mConnectedThread.start();

        return true;
    };
    public boolean btSend(String data){
        if(mConnectedThread != null) {
            if(mConnectedThread.isAlive())  {
                mConnectedThread.write(data);
            }
        }
        return true;
    };

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public NotificationService getServerInstance() {
            return NotificationService.this;
        }
    }
*/
    ////////////

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pack = sbn.getPackageName();
        String ticker ="";
        if(sbn.getNotification().tickerText !=null) {
            ticker = sbn.getNotification().tickerText.toString();
        }
        Bundle extras = sbn.getNotification().extras;
        String title = extras.getString("android.title");
        String text = extras.getCharSequence("android.text").toString();
        int id1 = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Bitmap id = sbn.getNotification().largeIcon;


        Log.i("Package",pack);
        Log.i("Ticker",ticker);
        Log.i("Title",title);
        Log.i("Text",text);

        Intent msgrcv = new Intent("Msg");
        msgrcv.putExtra("package", pack);
        msgrcv.putExtra("ticker", ticker);
        msgrcv.putExtra("title", title);
        msgrcv.putExtra("text", text);
        msgrcv.putExtra("cmd", "");

        if(pack.equals("com.here.app.maps")) {
            Log.i("PLAY", pack);

            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);

            Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);

            r.play();
            //v.vibrate(500);


            if(text.toLowerCase().contains("left")) {
                v.vibrate(500);
                msgrcv.putExtra("cmd", "left");
            }
            if(text.toLowerCase().contains("right")) {
                //context.mConnectedThread.wr(500);
                msgrcv.putExtra("cmd", "right");
                //btSend("0");
            }

        }


        if(id != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            id.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            msgrcv.putExtra("icon",byteArray);
        }
        LocalBroadcastManager.getInstance(context).sendBroadcast(msgrcv);
        //LocalBroadcastManager.getInstance().sendBroadcast(msgrcv);


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }
}
