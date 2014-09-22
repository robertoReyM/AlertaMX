package com.smartplace.alerta;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Roberto on 31/07/2014.
 */
public class MemoryServices {

    public static void setAdminInfo(Context context,String capInfo)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("ADMIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("INFO", capInfo);
        editor.apply();
    }

    public static String getAdminInfo(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("ADMIN", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("INFO", Constants.DEFAULT_ADMIN_INFO);
    }
    public static String getPushToken(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("ADMIN", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("PUSH_TOKEN", Constants.DEFAULT_ADMIN_INFO);
    }
    public static void setPushToken(Context context,String pushToken)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("ADMIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("PUSH_TOKEN", pushToken);
        editor.apply();
    }
    public static void setCAPInfo(Context context,String capInfo,String capName)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("CAP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("CAP_INFO_"+capName, capInfo);
        editor.apply();
    }
    public static String getCAPInfo(Context context,String capName)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("CAP", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("CAP_INFO_"+capName, Constants.DEFAULT_CAP_INFO);
    }
    public static void setAtlasInfo(Context context,String atlasInfo,String name)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("ATLAS",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("ATLAS_INFO_"+name, atlasInfo);
        editor.apply();
    }
    public static String getAtlasInfo(Context context,String name)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("ATLAS", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("ATLAS_INFO_"+name, Constants.DEFAULT_ATLAS_INFO);
    }
    public static void setConfigStates(Context context,String states)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("CONFIG",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("STATES", states);
        editor.apply();
    }

    public static String getConfigStates(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("STATES", Constants.DEFAULT_CONFIG_STATES);
    }
    public static String getUserID(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("USER_ID","");
    }
    public static void setUserID(Context context,String pushToken)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("USER_ID", pushToken);
        editor.apply();
    }
    public static String getUserLocation(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("LOCATION","");
    }
    public static void setUserLocation(Context context,String location)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("LOCATION", location);
        editor.apply();
    }
    public static String getUserInfo(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("INFO",Constants.DEFAULT_USER_INFO);
    }
    public static void setUserInfo(Context context,String info)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("INFO", info);
        editor.apply();
    }
    public static String getFamilyInfo(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        return mySharedPreferences.getString("FAMILY_MEMBERS",Constants.DEFAULT_FAMILY_INFO);
    }
    public static void setFamilyInfo(Context context,String familyInfo)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("USER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("FAMILY_MEMBERS", familyInfo);
        editor.apply();
    }
    public static boolean isFirstTime(Context context)
    {
        SharedPreferences mySharedPreferences = context.getSharedPreferences("ADMIN", Context.MODE_PRIVATE);
        return mySharedPreferences.getBoolean("FIRST_TIME",true);
    }
    public static void setFirstTime(Context context,boolean firstTime)
    {
        SharedPreferences mySharedPreferences =
                context.getSharedPreferences("ADMIN",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putBoolean("FIRST_TIME", firstTime);
        editor.apply();
    }
}
