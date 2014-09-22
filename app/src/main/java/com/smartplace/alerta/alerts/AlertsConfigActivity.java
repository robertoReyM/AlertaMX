package com.smartplace.alerta.alerts;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;


import com.smartplace.alerta.MemoryServices;
import com.smartplace.alerta.R;

import java.util.ArrayList;

public class AlertsConfigActivity extends Activity {

    LinearLayout mLayoutStates;
    ArrayList<CheckBox> mCheckboxStates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts_config);
        getActionBar().setTitle(getResources().getString(R.string.action_settings));
        getActionBar().setIcon(R.drawable.ic_main_alerts);
        getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.main_alerts)));
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mLayoutStates = (LinearLayout)findViewById(R.id.layout_states);
        Switch swNotifications = (Switch)findViewById(R.id.sw_notifcations);
        mCheckboxStates = new ArrayList<CheckBox>();

        Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        ((TextView)findViewById(R.id.header_states)).setTypeface(titleFont);
        ((TextView)findViewById(R.id.header_notifications)).setTypeface(titleFont);
        swNotifications.setTypeface(titleFont);

        String[] selectedStates = MemoryServices.getConfigStates(getBaseContext()).split(",");
        String[] states = getResources().getStringArray(R.array.states_array);
        String[] statesId = getResources().getStringArray(R.array.states_id_array);
        int selectedCount = 0;
        int statesCount = 0;
        for(String state:states){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,0,10,0);
            LinearLayout layoutItem = new LinearLayout(getBaseContext());
            layoutItem.setLayoutParams(params);
            layoutItem.setOrientation(LinearLayout.HORIZONTAL);

            TextView txtState = new TextView(getBaseContext());
            txtState.setText(state);
            txtState.setPadding(10,0,10,0);
            txtState.setTypeface(titleFont);
            txtState.setTextColor(getResources().getColor(android.R.color.black));
            txtState.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 1f));
            layoutItem.addView(txtState);

            CheckBox checkboxState = new CheckBox(getBaseContext());
            checkboxState.setText("");
            checkboxState.setTypeface(titleFont);
            checkboxState.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 4f));
            checkboxState.setGravity(Gravity.CENTER);

            if((selectedCount<selectedStates.length)&& statesId[statesCount].equals(selectedStates[selectedCount])){
                checkboxState.setChecked(true);
                selectedCount++;
            }else{
                checkboxState.setChecked(false);
            }
            mCheckboxStates.add(checkboxState);
            layoutItem.addView(checkboxState);

            mLayoutStates.addView(layoutItem);
            statesCount++;
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
            return true;
        }
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause(){
        super.onPause();
        int checkboxCount= 0;
        String[] statesId = getResources().getStringArray(R.array.states_id_array);
        String selectedStatesId = "";
        for(CheckBox checkBox:mCheckboxStates){
            if(checkBox.isChecked()){
                if(selectedStatesId.equals("")){
                    //First time just add state id
                    selectedStatesId+=statesId[checkboxCount];
                }else{
                    //Addd state id plus ","
                    selectedStatesId+=","+statesId[checkboxCount];
                }
            }
            checkboxCount++;
        }
        MemoryServices.setConfigStates(getBaseContext(),selectedStatesId);
    }
}
