package com.smartplace.alerta;

import android.os.Environment;

/**
 * Created by Roberto on 30/06/2014.
 */
public class Constants {
    public static final String IMAGES_PATH = Environment.getExternalStorageDirectory().toString() + "/Android/data/com.smartplace.alerta/Images";

    /**
     * Please replace this with a valid API key which is enabled for the
     * YouTube Data API v3 service. Go to the
     * <a href=”https://code.google.com/apis/console/“>Google APIs Console</a> to
     * register a new developer key.
     */
    public static final String DEFAULT_ADMIN_INFO = "{\"caps\":[],\"atlas\":[]}";
    public static final String DEFAULT_CAP_INFO = "{\"title\":\"\",\"id\":\"\",\"rights\":\"\",\"updated\":\"\",\"entry\":[]}";
    public static final String DEFAULT_ATLAS_INFO = "{\"type\":\"FeatureCollection\",\"features\":[]}";
    public static final String DEFAULT_FAMILY_INFO = "{\"familyMembers\":[]}";
    public static final String DEFAULT_USER_INFO = "{\"name\":\"\",\"type\":\"\",\"status\":\"\",\"mobile\":\"\",\"timestamp\":\"\",\"lat\":\"\",\"lng\":\"\"}";
    public static final String DEVELOPER_YOUTUBE_KEY = "AIzaSyDiZlb-NIIIGfzQEMYTwrB-I75TuggTGsM";
    public static final String THUMBNAIL_YOUTUBE_IMAGE = "http://img.youtube.com/vi/";
    public static final String THUMBNAIL_YOUTUBE_IMAGE_2 = "/0.jpg";


    public static final String MAPS_STATIC_IMAGE_PREFIX = "http://maps.googleapis.com/maps/api/staticmap?size=600x300&path=fillcolor:0xAA000033|color:0xFFFFFF00|weight:5";
    public static final int MAX_URL_SIZE = 2048;

    public static final int TAB_ALERTS = 0;
    public static final int TAB_ATLAS = 1;
    public static final int TAB_FAMILY = 2;
    public static final int TAB_TIPS = 3;

    public static final int ITEMS_PER_PAGE = 10;

    public static final String DEFAULT_CONFIG_STATES = "Ags,BC,BCS,Camp,Chis,Chih,Coah,Col,DF,Dgo,Gto,Gro,Hgo,Jal,Mex,Mich,Mor,Nay,NL,Oax,Pue,Qro,Q Roo,SLP,Sin,Son,Tab,Tamps,Tlax,Ver,Yuc,Zac";

}
