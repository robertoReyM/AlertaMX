package com.smartplace.alerta.tips;

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

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Roberto on 08/07/2014.
 */
public class TipsListAdapter extends BaseAdapter {

    /**
     * Objects to reference xml
     */
    public ArrayList listData;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private DisplayImageOptions defaultOptions;

    public TipsListAdapter(Context context, ArrayList listData) {

        //save references to local variables
        this.listData = listData;
        mLayoutInflater = LayoutInflater.from(context);
        mContext =context;
        //configure image loader
        defaultOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_main_tutorials)
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
            convertView = mLayoutInflater.inflate(R.layout.list_item_tips,null);
            holder.cardTitle = (TextView)convertView.findViewById(R.id.title_card);
            holder.videoPreview = (ImageView)convertView.findViewById(R.id.video_preview);
            holder.videoContent = (TextView)convertView.findViewById(R.id.video_content);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }

        final TipsItem tipsItem = (TipsItem)listData.get(position);
        File Dir = new File (Constants.IMAGES_PATH);
        String fname = tipsItem.getVideoID() +".png";
        File file = new File (Dir, fname);
        if(file.exists()){
            holder.videoPreview.setImageBitmap(
                    BitmapFactory.decodeFile(Constants.IMAGES_PATH + "/" +
                            tipsItem.getVideoID()+ ".png"));


        }else {
            ImageLoader.getInstance().displayImage(
                    Constants.THUMBNAIL_YOUTUBE_IMAGE +
                            tipsItem.getVideoID() +
                            Constants.THUMBNAIL_YOUTUBE_IMAGE_2,
                    holder.videoPreview, defaultOptions,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            //get member id to save photo
                            String[] dividers = s.split("/");

                        /*Save image*/

                            //member id is the last string on the uri
                            Miscellaneous.saveImage(bitmap, Constants.IMAGES_PATH, dividers[dividers.length - 2]);

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    }
            );
        }
        holder.cardTitle.setText(tipsItem.getVideoTitle());
        holder.videoContent.setText(tipsItem.getVideoContent());
        Typeface titleFont= Typeface.createFromAsset(mContext.getAssets(), "fonts/OpenSansLight.ttf");
        holder.cardTitle.setTypeface(titleFont);
        holder.videoContent.setTypeface(titleFont);

        return convertView;
    }
    static class Holder {
        ImageView videoPreview;
        TextView videoContent;
        TextView cardTitle;
    }



}
