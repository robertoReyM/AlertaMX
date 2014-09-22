package com.smartplace.alerta.alerts;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.filippudak.ProgressPieView.ProgressPieView;
import com.google.gson.Gson;
import com.smartplace.alerta.Constants;
import com.smartplace.alerta.R;
import com.smartplace.alerta.cap.CapEntry;
import com.smartplace.alerta.cap.CapParameter;

import java.io.File;

public class AlertDetailedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_detailed);
        getActionBar().hide();


        TextView txtHeaderTitle = (TextView)findViewById(R.id.header_title);
        TextView txtHeaderTitle2 = (TextView)findViewById(R.id.header_title2);
        ImageView imageMap = (ImageView) findViewById(R.id.image_map);
        TextView txtPublished = (TextView)findViewById(R.id.txt_published);
        TextView txtExpires = (TextView)findViewById(R.id.txt_expires);
        TextView txtUrgency = (TextView)findViewById(R.id.alert_info_urgency);
        TextView txtSeverity = (TextView)findViewById(R.id.alert_info_severity);
        TextView txtCertainty = (TextView)findViewById(R.id.alert_info_probability);
        TextView txtTrayectory = (TextView)findViewById(R.id.txt_alert_location);
        TextView txtStates = (TextView)findViewById(R.id.txt_alert_states);
        TextView txtDescription = (TextView)findViewById(R.id.txt_alert_description);
        TextView txtInstruction = (TextView)findViewById(R.id.txt_alert_instruction);
        TextView txtSenderName = (TextView)findViewById(R.id.txt_alert_sender_name);
        TextView txtSender = (TextView)findViewById(R.id.txt_alert_sender);
        ImageView imageBack = (ImageView)findViewById(R.id.image_back);

        Typeface titleFont= Typeface.createFromAsset(getAssets(), "fonts/OpenSansLight.ttf");
        ((TextView)findViewById(R.id.header_expires)).setTypeface(titleFont);
        ((TextView)findViewById(R.id.header_published)).setTypeface(titleFont);
        txtPublished.setTypeface(titleFont);
        txtExpires.setTypeface(titleFont);
        txtHeaderTitle.setTypeface(titleFont);
        txtHeaderTitle2.setTypeface(titleFont);
        txtUrgency.setTypeface(titleFont);
        txtSeverity.setTypeface(titleFont);
        txtCertainty.setTypeface(titleFont);
        ((TextView)findViewById(R.id.header_location)).setTypeface(titleFont);
        txtTrayectory.setTypeface(titleFont);
        txtStates.setTypeface(titleFont);
        ((TextView)findViewById(R.id.header_description)).setTypeface(titleFont);
        txtDescription.setTypeface(titleFont);
        ((TextView)findViewById(R.id.header_instruction)).setTypeface(titleFont);
        txtInstruction.setTypeface(titleFont);
        ((TextView)findViewById(R.id.header_source)).setTypeface(titleFont);
        txtSenderName.setTypeface(titleFont);
        txtSender.setTypeface(titleFont);

        String jsonEntry = getIntent().getStringExtra("entry");


        if(jsonEntry!=null){
            CapEntry capEntry = new Gson().fromJson(jsonEntry,CapEntry.class);
            txtHeaderTitle.setText(capEntry.getTitle());
            txtHeaderTitle2.setText(capEntry.getTitle());
            File Dir = new File (Constants.IMAGES_PATH);
            String fname = capEntry.getContent().getAlert().getIdentifier() +".png";
            File file = new File (Dir, fname);
            if(file.exists()){
                imageMap.setImageBitmap(
                        BitmapFactory.decodeFile(Constants.IMAGES_PATH + File.separator + fname));


            }

            try {
                String published = capEntry.getContent().getAlert().getInfo().get(0).getEffective();
                String[]infoDateTime = published.split("T");
                String[]infoDate = infoDateTime[0].split("-");
                String[]infoTime = infoDateTime[1].split(":");
                String[] months = getResources().getStringArray(R.array.months_array);
                txtPublished.setText(infoTime[0] + ":" + infoTime[1] + " hrs del " + infoDate[2] + " de " + months[Integer.valueOf(infoDate[1]) - 1]);

            }catch (NullPointerException w){

            }
            try {
                String expires = capEntry.getContent().getAlert().getInfo().get(0).getExpires();
                String[]infoDateTime = expires.split("T");
                String[]infoDate = infoDateTime[0].split("-");
                String[]infoTime = infoDateTime[1].split(":");
                String[] months = getResources().getStringArray(R.array.months_array);
                txtExpires.setText(infoTime[0]+":"+infoTime[1]+ " hrs del " + infoDate[2] + " de " + months[Integer.valueOf(infoDate[1])-1]);
            }catch (NullPointerException w){

            }
            try {
                //Get event projection
                for(CapParameter parameter :capEntry.getContent().getAlert().getInfo().get(0).getParameter()){
                    if(parameter.getValueName().equals("trayectoria")){
                        txtTrayectory.setText(parameter.getValue());
                    }

                }
            }catch (NullPointerException w){

            }
            try {
                //Get information area
                String area = capEntry.getContent().getAlert().getInfo().get(0).getArea().get(0).getAreaDesc();
                if (area != null) {
                    String[] affectedStates = area.split(",");
                    String[] stateIds = getResources().getStringArray(R.array.states_id_array);
                    String[] states = getResources().getStringArray(R.array.states_array);
                    String areaDescription = "";
                    for(String affectedState : affectedStates){
                        for(int i = 0; i<stateIds.length;i++){
                            if(stateIds[i].equals(affectedState)){
                                if(areaDescription.equals("")){
                                    areaDescription+=states[i];
                                }else{
                                    areaDescription+=", "+states[i];
                                }
                                i = stateIds.length;
                            }
                        }
                    }
                    txtStates.setText(areaDescription);
                }else {
                    txtStates.setText("");
                }
            }catch (NullPointerException w){

            }
            try {
               String description = capEntry.getContent().getAlert().getInfo().get(0).getDescription();
                txtDescription.setText(description);
            }catch (NullPointerException w){

            }
            try {
               String instruction = capEntry.getContent().getAlert().getInfo().get(0).getInstruction();
                txtInstruction.setText(instruction);
            }catch (NullPointerException w){

            }
            try {
                String senderName = capEntry.getContent().getAlert().getInfo().get(0).getSenderName();
                txtSenderName.setText(senderName);
            }catch (NullPointerException w){

            }
            try {
                String sender = capEntry.getContent().getAlert().getSender();
                txtSender.setText(sender);
            }catch (NullPointerException w){

            }
            try {

                String certainty = capEntry.getContent().getAlert().getInfo().get(0).getCertainty();
                if(certainty.equals("Observed")){
                } else if(certainty.equals("Likely")){
                } else if(certainty.equals("Posible")){
                }else if(certainty.equals("Unlikely")){
                }else{
                }
            }catch (NullPointerException w){

            }
            try {
                String urgency = capEntry.getContent().getAlert().getInfo().get(0).getUrgency();
                if(urgency.equals("Immediate")){
                    //txtUrgency.setText("Urgencia:\nInmediata");
                    //progressUrgency.animateProgressFill(100);
                } else if(urgency.equals("Expected")){
                    //txtUrgency.setText("Urgencia:\nEsperada");
                    //progressUrgency.animateProgressFill(75);
                } else if(urgency.equals("Future")){
                    //txtUrgency.setText("Urgencia:\nFutura");
                    //progressUrgency.animateProgressFill(50);
                }else if(urgency.equals("Past")){
                    //txtUrgency.setText("Urgencia:\nPasada");
                    //progressUrgency.animateProgressFill(25);
                }else{
                    //txtUrgency.setText("Urgencia:\nDesconocida");
                    //progressUrgency.setText("?");
                    //progressUrgency.animateProgressFill(0);
                }
            }catch (NullPointerException w){

            }
            try {

                String severity = capEntry.getContent().getAlert().getInfo().get(0).getSeverity();
                if(severity.equals("Extreme")){
                    //txtSeverity.setText("Severidad:\nExtraordinaria");
                    //progressSeverity.animateProgressFill(100);
                } else if(severity.equals("Severe")){
                    //txtSeverity.setText("Severidad:\nSevera");
                    //progressSeverity.animateProgressFill(75);
                } else if(severity.equals("Moderate")){
                    //txtSeverity.setText("Severidad:\nPosible");
                    //progressSeverity.animateProgressFill(50);
                }else if(severity.equals("Minor")){
                    //txtSeverity.setText("Severidad:\nMinima");
                    //progressSeverity.animateProgressFill(25);
                }else{
                    //txtSeverity.setText("Severidad:\nDesconocida");
                    //progressSeverity.setText("?");
                    //progressSeverity.animateProgressFill(0);
                }
            }catch (NullPointerException w){

            }
        }
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
        return super.onOptionsItemSelected(item);
    }
}
