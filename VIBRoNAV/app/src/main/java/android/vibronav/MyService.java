package android.vibronav;

/**
 * Created by pravoj01 on 2.4.2017.
 */
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import android.bluetooth.BluetoothSocket;

import java.util.ArrayList;

/**
 * Created by TutorialsPoint7 on 8/23/2016.
 */

public class MyService extends Service {


    //@Nullable
    //@Override
    //public IBinder onBind(Intent intent) {
    //    return null;
    //}

    @Override
    public void onCreate() {
        Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG).show();

        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    IBinder mBinder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MyService getServerInstance() {
            return MyService.this;
        }
    }


    //BT

    public ConnectedThread mConnectedThread;

    public boolean btCancel(){
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

    //
    //ArrayList<Model> modelList;
    //CustomListAdapter adapter;
    //ListView list;

    private BroadcastReceiver onNotice = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // String pack = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            //int id = intent.getIntExtra("icon",0);

            String cmd = intent.getStringExtra("cmd");
            Log.i("MyService cmd", cmd);
            if(cmd.equals("right")){
                btSend("R");
                /*//Log.i("cmd", "SEND");
                if(mConnectedThread != null) {
                    if(mConnectedThread.isAlive())  {
                        mConnectedThread.write("0");
                    }
                }*/
                //mServer.btSend("0");
            }
            if(cmd.equals("left")){
                btSend("L");
            }

            Context remotePackageContext = null;
            try {
//                remotePackageContext = getApplicationContext().createPackageContext(pack, 0);
//                Drawable icon = remotePackageContext.getResources().getDrawable(id);
//                if(icon !=null) {
//                    ((ImageView) findViewById(R.id.imageView)).setBackground(icon);
//                }
                byte[] byteArray = intent.getByteArrayExtra("icon");
                Bitmap bmp = null;
                if (byteArray != null) {
                    bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                }
                Model model = new Model();
                model.setName(title + " " + text);
                model.setImage(bmp);


                //if (modelList != null) {
                //    modelList.add(model);
                //    adapter.notifyDataSetChanged();
                //} else {
                //    modelList = new ArrayList<Model>();
                //    modelList.add(model);
                //    adapter = new CustomListAdapter(getApplicationContext(), modelList);
                //    list = (ListView) findViewById(R.id.list);
                //    list.setAdapter(adapter);
                //}

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}