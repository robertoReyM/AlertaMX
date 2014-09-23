package com.smartplace.alerta;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.IntentSender;
import android.location.Location;
import android.support.v4.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplace.alerta.Utilities.TransparentProgressDialog;
import com.smartplace.alerta.alerts.AlertsConfigActivity;
import com.smartplace.alerta.alerts.AlertsFragment;
import com.smartplace.alerta.atlas.AtlasFragment;
import com.smartplace.alerta.family.FamilyFragment;
import com.smartplace.alerta.family.members.FamilyInfo;
import com.smartplace.alerta.family.members.FamilyMember;
import com.smartplace.alerta.tips.TipsFragment;


public class MainActivity extends FragmentActivity  implements TransparentProgressDialog.OnProgressDialogListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{

    // Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    MainPagerAdapter mMainPagerAdapter;
    ViewPager mViewPager;
    Fragment mCurrentFragment;
    private LocationClient mLocationClient;
    // Global variable to hold the current location
    Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ActionBar actionBar = getActionBar();

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mMainPagerAdapter =
                new MainPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mMainPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // When the tab is selected, switch to the
                // corresponding page in the ViewPager.
                mViewPager.setCurrentItem(tab.getPosition());
                switch(tab.getPosition()){
                    case Constants.TAB_ALERTS:
                        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_alerts)));
                        getActionBar().setTitle(getResources().getString(R.string.title_alerts));
                        break;
                    case Constants.TAB_ATLAS:
                        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_atlas)));
                        getActionBar().setTitle(getResources().getString(R.string.title_atlas));
                        break;
                    case Constants.TAB_FAMILY:
                        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_family)));
                        getActionBar().setTitle(getResources().getString(R.string.title_my_family));
                        break;
                    case Constants.TAB_TIPS:
                        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_tips)));
                        getActionBar().setTitle(getResources().getString(R.string.title_tutorials));
                        break;
                }
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };


        TypedArray mainTypedArray = getResources().obtainTypedArray(R.array.main_icons_array);
        // Add 4 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 4; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setIcon(mainTypedArray.getDrawable(i))
                            .setTabListener(tabListener));
        }

                /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        if(servicesConnected()) {
            mLocationClient = new LocationClient(this, this, this);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            Intent i = new Intent(getBaseContext(), AlertsConfigActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDialogCancelled() {
        if(mCurrentFragment instanceof FamilyFragment){
            ((FamilyFragment) mCurrentFragment).onDialogCancelled();
        }else if(mCurrentFragment instanceof AlertsFragment){
            ((AlertsFragment) mCurrentFragment).onDialogCancelled();
        }
    }
    /*
         * Called when the Activity becomes visible.
         */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
    // Since this is an object collection, use a FragmentStatePagerAdapter,
// and NOT a FragmentPagerAdapter.
    public class MainPagerAdapter extends FragmentStatePagerAdapter {

        public MainPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
                switch (i) {
                    case Constants.TAB_ALERTS:
                        mCurrentFragment = AlertsFragment.newInstance();
                        break;
                    case Constants.TAB_ATLAS:
                        mCurrentFragment = AtlasFragment.newInstance();
                        break;
                    case Constants.TAB_FAMILY:
                        mCurrentFragment = FamilyFragment.newInstance();
                        break;
                    case Constants.TAB_TIPS:
                        mCurrentFragment = TipsFragment.newInstance();
                        break;
                }
            return mCurrentFragment;

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }

    }

    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case CONNECTION_FAILURE_RESOLUTION_REQUEST :
            /*
             * If the result code is Activity.RESULT_OK, try
             * to connect again
             */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                        break;
                }
        }
    }
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode,
                    this,
                    CONNECTION_FAILURE_RESOLUTION_REQUEST);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                // Create a new DialogFragment for the error dialog
                ErrorDialogFragment errorFragment =
                        new ErrorDialogFragment();
                // Set the dialog in the DialogFragment
                errorFragment.setDialog(errorDialog);
                // Show the error dialog in the DialogFragment
                errorFragment.show(getSupportFragmentManager(),
                        "Location Updates");
            }
        }
        return  false;
    }
    /*
        * Called by Location Services when the request to connect the
        * client finishes successfully. At this point, you can
        * request the current location or start periodic updates
        */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
        //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

        mCurrentLocation = mLocationClient.getLastLocation();
        MemoryServices.setUserLocation(getBaseContext(),String.valueOf(mCurrentLocation.getLatitude())+","+String.valueOf(mCurrentLocation.getLongitude()));


    }
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
       // Toast.makeText(this, "Disconnected. Please re-connect.",
       //         Toast.LENGTH_SHORT).show();
    }
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }
}
