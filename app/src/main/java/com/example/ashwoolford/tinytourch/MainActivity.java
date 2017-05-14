package com.example.ashwoolford.tinytourch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageButton onButton;
    TextView mTextView;
    float BatteryTemp;
    IntentFilter intentfilter;
    int counter = 0;
    ImageView mImageView;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    int notification_id;
    int notification_id1;
    RemoteViews remoteViews;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      onButton = (ImageButton) findViewById(R.id.imageButton);
        mTextView = (TextView) findViewById(R.id.batTemp);
        mImageView = (ImageView) findViewById(R.id.imageView);



        String[] perms = { "android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};

        int permsRequestCode = 200;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, permsRequestCode);
        }
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        MainActivity.this.registerReceiver(mBroadcastReceiver,intentfilter);

        //

        context = this;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        remoteViews = new RemoteViews(context.getPackageName(),R.layout.custom_layout);

        remoteViews.setTextViewText(R.id.ntextView,"Notification");
        notification_id = (int) System.currentTimeMillis();
        notification_id1 = (int) System.currentTimeMillis();
        Intent intent = new Intent("button_clicked");
        Intent intent1 = new Intent("off");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("id",notification_id);
        intent1.putExtra("id1",notification_id1);

        PendingIntent p_button_intent = PendingIntent.getBroadcast(context,123,intent,0);
        PendingIntent p_button_intent1 = PendingIntent.getBroadcast(context,124,intent1,0);
        remoteViews.setOnClickPendingIntent(R.id.nbutton,p_button_intent);
        remoteViews.setOnClickPendingIntent(R.id.nbuttonoff,p_button_intent1);





        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter = counter+1;
                int newNumber =(counter%2);
                if(!(newNumber ==0)){
                    Camera camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    camera.startPreview();
                }
                else{
                    Camera camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters()   ;
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    camera.release();
                }


            }

        });

    }
    public void notification(View view){
//
        Intent notification_intent = new Intent(context,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);
        builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setContent(remoteViews);
        notificationManager.notify(notification_id,builder.build());

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            BatteryTemp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;

            mTextView.setText(BatteryTemp +" "+ (char) 0x00B0 +"C");
        }
    };



}
