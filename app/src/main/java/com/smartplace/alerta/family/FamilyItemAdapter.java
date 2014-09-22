package com.smartplace.alerta.family;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.smartplace.alerta.Constants;
import com.smartplace.alerta.R;
import com.smartplace.alerta.family.members.FamilyMember;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Roberto on 21/10/13.
 */
public class FamilyItemAdapter extends BaseAdapter {

    public ArrayList listData;
    Context mContext;
    private DisplayImageOptions defaultOptions;
    private LayoutInflater layoutInflater;

    public FamilyItemAdapter(Context context, ArrayList listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        mContext =context;
        //configure image loader
        defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_main_family)
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

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_family, null);
            holder = new ViewHolder();
            holder.userImage = (ImageView)convertView.findViewById(R.id.image_people);
            holder.imageStatus = (ImageView)convertView.findViewById(R.id.image_status);
            holder.userName = (TextView) convertView.findViewById(R.id.name_people);
            holder.statusUpdate = (TextView)convertView.findViewById(R.id.txt_status_update);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final FamilyMember familyItem = (FamilyMember)listData.get(position);
        int type = Integer.valueOf(familyItem.getType());
        switch (type){
            case 1:
                holder.userImage.setImageResource(R.drawable.ic_man);
                break;
            case 2:
                holder.userImage.setImageResource(R.drawable.ic_woman);
                break;
            case 3:
                holder.userImage.setImageResource(R.drawable.ic_children);
                break;
            case 4:
                holder.userImage.setImageResource(R.drawable.ic_pregnant);
                break;
            case 5:
                holder.userImage.setImageResource(R.drawable.ic_handicap);
                break;
        }
        int status = Integer.valueOf(familyItem.getStatus());
        switch (status){
            case 1:
                holder.imageStatus.setImageResource(R.drawable.ic_status_trapped);
                break;
            case 2:
                holder.imageStatus.setImageResource(R.drawable.ic_status_ok);
                break;
            case 3:
                holder.imageStatus.setImageResource(R.drawable.ic_status_medical_aid);
                break;
        }
        holder.userName.setText(familyItem.getName());
        if(familyItem.getTimestamp()!=null){
            String[]infoDateTime = familyItem.getTimestamp().split(" ");
            String[] infoDate = infoDateTime[0].split("-");
            String[] infoTime = infoDateTime[1].split(":");
            String[] months = mContext.getResources().getStringArray(R.array.months_array);

            holder.statusUpdate.setText(mContext.getString(R.string.updated) +infoTime[0]+":"+infoTime[1]+ " hrs del " + infoDate[2] + " de " + months[Integer.valueOf(infoDate[1])-1] );
        }else{
            holder.statusUpdate.setText(mContext.getString(R.string.updated) + mContext.getString(R.string.not_available));
        }
        Typeface titleFont= Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSansLight.ttf");
        holder.userName.setTypeface(titleFont);
        holder.statusUpdate.setTypeface(titleFont);

        return convertView;
    }

    static class ViewHolder {

        ImageView userImage;
        TextView userName;
        ImageView imageStatus;
        TextView statusUpdate;


    }

}
