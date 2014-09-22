package com.smartplace.alerta.alerts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartplace.alerta.Constants;
import com.smartplace.alerta.R;
import com.smartplace.alerta.Utilities.Miscellaneous;
import com.smartplace.alerta.cap.CapAlertArea;
import com.smartplace.alerta.cap.CapEntry;
import com.smartplace.alerta.cap.CapParameter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Roberto on 13/07/2014.
 */
public class AlertsListAdapter extends BaseAdapter{
    /**
     * Objects to reference xml
     */
    public ArrayList listData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    private DisplayImageOptions defaultOptions;

    public AlertsListAdapter(Context context, ArrayList<CapEntry> capEntries) {

        listData = capEntries;
        mLayoutInflater = LayoutInflater.from(context);
        mContext =context;


        //configure image loader
        defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.map_icon)
                .resetViewBeforeLoading(false).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(defaultOptions).build();
        ImageLoader.getInstance().init(config);

    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder;
        if(convertView==null){
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.list_item_alert,null);
            holder.imagePreview = (ImageView) convertView.findViewById(R.id.image_map);
            holder.alertInfoHeadline = (TextView)convertView.findViewById(R.id.alert_info_headline);
            holder.alertInfoArea = (TextView)convertView.findViewById(R.id.alert_info_area);
            holder.alertInfoProbability = (TextView)convertView.findViewById(R.id.alert_info_probability);
            holder.alertInfoSeverity = (TextView)convertView.findViewById(R.id.alert_info_severity);
            holder.alertInfoUrgency = (TextView)convertView.findViewById(R.id.alert_info_urgency);
            holder.alertSent = (TextView)convertView.findViewById(R.id.alert_info_sent);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }

        Typeface titleFont= Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSansLight.ttf");
        holder.alertInfoHeadline.setTypeface(titleFont);
        holder.alertInfoArea.setTypeface(titleFont);
        holder.alertInfoUrgency.setTypeface(titleFont);
        holder.alertInfoSeverity.setTypeface(titleFont);
        holder.alertInfoProbability.setTypeface(titleFont);
        holder.alertSent.setTypeface(titleFont);

        final CapEntry capEntry = (CapEntry)listData.get(position);
        try {
            //Set headline
            holder.alertInfoHeadline.setText(capEntry.getContent().getAlert().getInfo().get(0).getHeadline());
            String sentDateTime= capEntry.getContent().getAlert().getSent();
            String[]infoDateTime = sentDateTime.split("T");
            String[]infoDate = infoDateTime[0].split("-");
            String[]infoTime = infoDateTime[1].split(":");
            String[] months = mContext.getResources().getStringArray(R.array.months_array);
            holder.alertSent.setText("Publicado a las "+infoTime[0]+":"+infoTime[1]+ " hrs del " + infoDate[2] + " de " + months[Integer.valueOf(infoDate[1])-1]);

           /* //set severity
            String severity = capEntry.getContent().getAlert().getInfo().get(0).getSeverity();
            if(severity.equals("Extreme")){
                holder.alertInfoSeverity.setText("Extraordinaria amenaza de vida o propiedad");
                holder.alertImageSeverity.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_severity_extreme));
            } else if(severity.equals("Severe")){
                holder.alertInfoSeverity.setText("Severa amenaza de vida o propiedad");
                holder.alertImageSeverity.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_severity_severe));
            } else if(severity.equals("Moderate")){
                holder.alertInfoSeverity.setText("Posible amenaza de vida o propiedad");
                holder.alertImageSeverity.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_severity_moderate));
            }else if(severity.equals("Minor")){
                holder.alertInfoSeverity.setText("Minima amenaza de vida o propiedad");
                holder.alertImageSeverity.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_severity_minor));
            }else{
                holder.alertInfoSeverity.setText("Nivel de severidad desconocido");
                holder.alertImageSeverity.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_severity_unknown));
            }*/

            //Get event projection
            for(CapParameter parameter :capEntry.getContent().getAlert().getInfo().get(0).getParameter()){
                if(parameter.getValueName().equals("trayectoria")){
                    holder.alertInfoArea.setText(parameter.getValue());
                }

            }
/*
            //Get information area
            String area = capEntry.getContent().getAlert().getInfo().get(0).getArea().get(0).getAreaDesc();
            if (area != null) {
                String[] affectedStates = area.split(",");
                String[] stateIds = mContext.getResources().getStringArray(R.array.states_id_array);
                String[] states = mContext.getResources().getStringArray(R.array.states_array);
                String areaDescription = "Estados afectados: ";
                for(String affectedState : affectedStates){
                    for(int i = 0; i<stateIds.length;i++){
                        if(stateIds[i].equals(affectedState)){
                            areaDescription+=states[i]+", ";
                            i = stateIds.length;
                        }
                    }
                }
                holder.alertInfoArea.setText(areaDescription);
            }else {
                holder.alertInfoArea.setText("");
            }
*/
            //Get image map url
            String imageMapUrl = Constants.MAPS_STATIC_IMAGE_PREFIX;
            ArrayList<CapAlertArea> capAreas = capEntry.getContent().getAlert().getInfo().get(0).getArea();
            for(int i = 0;i<capAreas.size();i++){
                CapAlertArea capAlertArea = capAreas.get(i);
                if(capAlertArea.getPolygon()!=null){
                    for(int i2 = 0;i2<capAlertArea.getPolygon().size();i2++){
                        String polygon = capAlertArea.getPolygon().get(i2);
                        polygon = polygon.replace(" ","|");
                        if((imageMapUrl.length()+polygon.length())< Constants.MAX_URL_SIZE){
                            imageMapUrl+= "|"+polygon;
                        }
                    }
                }
                if(capAlertArea.getCircle()!=null){
                    for(int i2 = 0;i2<capAlertArea.getCircle().size();i2++){
                        String circle = capAlertArea.getCircle().get(i2);
                        circle = circle.replace(" ",",");
                        String[]circleInfo = circle.split(",");
                        String polygon = Miscellaneous.getCirclePolygon(Double.valueOf(circleInfo[0]),Double.valueOf(circleInfo[1]),Double.valueOf(circleInfo[2]),10);
                        if((imageMapUrl.length()+polygon.length())< Constants.MAX_URL_SIZE) {
                            imageMapUrl += polygon;
                        }
                    }
                }
            }

            //Check for map in eeprom otherwise download it
            File Dir = new File (Constants.IMAGES_PATH);
            String fname = capEntry.getContent().getAlert().getIdentifier() +".png";
            File file = new File (Dir, fname);
            if(file.exists()){
                holder.imagePreview.setImageBitmap(
                        BitmapFactory.decodeFile(Constants.IMAGES_PATH + File.separator+fname));
            }else {
                ImageLoader.getInstance().displayImage(
                        imageMapUrl, holder.imagePreview, defaultOptions,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {}
                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {}
                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                //member id is the last string on the uri
                                Miscellaneous.saveImage(bitmap, Constants.IMAGES_PATH, capEntry.getContent().getAlert().getIdentifier());
                            }
                            @Override
                            public void onLoadingCancelled(String s, View view) {}
                 });
            }

        }catch (NullPointerException e){

        }
        return convertView;
    }
    static class Holder {
        //TextView alertIdentifier;
        //TextView alertSender;
        TextView alertSent;
        //TextView alertStatus;
        //TextView alertMsgType;
        //TextView alertScope;
        //TextView alertInfoCategory;
        //TextView alertInfoEvent;
        //TextView alertInfoUrgency;
        //TextView alertInfoCertainty;
        //TextView alertInfoSenderName;
        TextView alertInfoHeadline;
        TextView alertInfoProbability;
        TextView alertInfoSeverity;
        TextView alertInfoUrgency;
        TextView alertInfoArea;
        ImageView imagePreview;
        //MapView mapView;
        //GoogleMap map;
    }
}
