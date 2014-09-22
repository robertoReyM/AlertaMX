package com.smartplace.alerta.init;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.smartplace.alerta.MainActivity;
import com.smartplace.alerta.MemoryServices;
import com.smartplace.alerta.R;

public class LauncherActivity extends Activity {

    // used to know if the back button was pressed in the splash screen activity and avoid opening the next activity
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 1000; // 500 miliseconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Register receivers for push notifications
        registerReceivers();

        //Create and start push manager
        PushManager pushManager = PushManager.getInstance(this);

        //Start push manager, this will count app open for Pushwoosh stats as well
        try {
            pushManager.onStartup(this);
        }
        catch(Exception e)
        {
            //push notifications are not available or AndroidManifest.xml is not configured properly
        }

        //Register for push!
        pushManager.registerForPushNotifications();

        checkMessage(getIntent());


        getActionBar().hide();
        setContentView(R.layout.activity_launcher);

        final Button btnHowItWorks = (Button)findViewById(R.id.btn_how_it_works);
        final TextView txtSkip = (TextView)findViewById(R.id.txt_skip);


        Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        ((TextView)findViewById(R.id.txt_launcher)).setTypeface(titleFont);
        ((TextView)findViewById(R.id.txt_app_name_1)).setTypeface(titleFont);
        ((TextView)findViewById(R.id.txt_app_name_2)).setTypeface(titleFont);
        btnHowItWorks.setTypeface(titleFont);
        txtSkip.setTypeface(titleFont);


        YoYo.with(Techniques.FadeInRight).duration(1000).playOn(findViewById(R.id.txt_app_name_1));
        YoYo.with(Techniques.FadeInLeft).duration(1000).playOn(findViewById(R.id.txt_app_name_2));
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(btnHowItWorks);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(txtSkip);

        if(MemoryServices.isFirstTime(getBaseContext())){
            btnHowItWorks.setVisibility(View.VISIBLE);
            txtSkip.setVisibility(View.VISIBLE);
        }else{
            btnHowItWorks.setVisibility(View.INVISIBLE);
            txtSkip.setVisibility(View.INVISIBLE);
        }

        btnHowItWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to guide activity
                Intent intent = new Intent(LauncherActivity.this, GuideActivity.class);
                        /*delete activity from back stack*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                LauncherActivity.this.startActivity(intent);
                finish();
                MemoryServices.setFirstTime(getBaseContext(),false);

                YoYo.with(Techniques.FadeOut).duration(500).playOn(btnHowItWorks);
                YoYo.with(Techniques.FadeOut).duration(500).playOn(txtSkip);
            }
        });
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.FadeOut).duration(500).playOn(btnHowItWorks);
                YoYo.with(Techniques.FadeOut).duration(500).playOn(txtSkip);
                //Go to main activity
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        /*delete activity from back stack*/
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                LauncherActivity.this.startActivity(intent);
                finish();
                MemoryServices.setFirstTime(getBaseContext(),false);
            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //Re-register receivers on resume
        registerReceivers();

        if(MemoryServices.isFirstTime(getBaseContext())) {
        }else{
            Handler handler = new Handler();

            // run a thread after 2 seconds to start the home screen
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    // make sure we close the splash screen so the user won't come back when it presses back key
                    if (!mIsBackButtonPressed) {
                        // start the home screen if the back button wasn't pressed already
                        //Go to main activity
                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        /*delete activity from back stack*/
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        LauncherActivity.this.startActivity(intent);
                    }
                    //delete activity from back stack
                    finish();
                }

            }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();

        //Unregister receivers on pause
        unregisterReceivers();
    }

    @Override
    public void onBackPressed() {

        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }
    //Registration receiver
    BroadcastReceiver mBroadcastReceiver = new RegisterBroadcastReceiver()
    {
        @Override
        public void onRegisterActionReceive(Context context, Intent intent)
        {
            checkMessage(intent);
        }
    };


    //Push message receiver
    private BroadcastReceiver mReceiver = new BasePushMessageReceiver()
    {
        @Override
        protected void onMessageReceive(Intent intent)
        {
            //JSON_DATA_KEY contains JSON payload of push notification.
            showMessage("push message is " + intent.getExtras().getString(JSON_DATA_KEY));
        }
    };

    //Registration of the receivers
    public void registerReceivers()
    {
        IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");

        registerReceiver(mReceiver, intentFilter);

        registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));
    }

    public void unregisterReceivers()
    {
        //Unregister receivers on pause
        try
        {
            unregisterReceiver(mReceiver);
        }
        catch (Exception e)
        {
            // pass.
        }

        try
        {
            unregisterReceiver(mBroadcastReceiver);
        }
        catch (Exception e)
        {
            //pass through
        }
    }
    private void checkMessage(Intent intent)
    {
        if (null != intent)
        {
            if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
            {
                showMessage("push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
            }
            else if (intent.hasExtra(PushManager.REGISTER_EVENT))
            {
                showMessage("register");
                Log.v("alerta", PushManager.getPushToken(getBaseContext()));
                MemoryServices.setPushToken(getBaseContext(), PushManager.getPushToken(getBaseContext()));
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
            {
                showMessage("unregister");
            }
            else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
            {
                showMessage("register error");
            }
            else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
            {
                showMessage("unregister error");
            }

            resetIntentValues();
        }
    }

    /**
     * Will check main Activity intent and if it contains any PushWoosh data, will clear it
     */
    private void resetIntentValues()
    {
        Intent mainAppIntent = getIntent();

        if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
        }
        else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
        {
            mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
        }

        setIntent(mainAppIntent);
    }

    private void showMessage(String message)
    {
        // Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);

        checkMessage(intent);
    }
}
