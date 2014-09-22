package com.smartplace.alerta;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.smartplace.alerta.cap.CapFeed;
import com.smartplace.alerta.cap.CapXmlParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roberto on 12/07/2014.
 */
public class WebServices {

    private static final int CONNECTION_TIMEOUT = 10000;

    public static final String SERVER_URL = "http://ec2-54-68-158-184.us-west-2.compute.amazonaws.com";
    private static final String PATH_REGISTER = "/user/auth/register";
    private static final String PATH_LOGIN = "/user/auth/login/pushToken";
    private static final String PATH_ADD_FAMILY_MEMBER = "/add/family/member";
    private static final String PATH_GET_FAMILY_MEMBER = "/get/family/members";
    private static final String PATH_SEND_ALERT = "/send/alert";

    public static void userRegister(final String mobile, final String name,final String type,
                                     final String pushToken, final Handler handler)
    {

        /*Run new thread to perform users Info query*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                    /*Set post information to perform query*/
                HttpClient client = new DefaultHttpClient(httpParameters);
                    /*set post's URL*/
                HttpPost post = new HttpPost(SERVER_URL +PATH_REGISTER);
                    /*set post's header*/
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    /*Create new NameValue pair list*/
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                    /*set parameters*/
                params.add(new BasicNameValuePair("mobile", mobile));
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("type", type));
                params.add(new BasicNameValuePair("pushToken", pushToken));
                try {
                        /*set post value*/
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                        /*Execute post*/
                    HttpResponse httpResponse = client.execute(post);
                        /*get post's entity*/
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("mobile", mobile);
                    bundle.putString("name", name);
                    bundle.putString("type", type);
                    bundle.putString("pushToken",pushToken);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("mobile", mobile);
                    bundle.putString("name", name);
                    bundle.putString("type", type);
                    bundle.putString("pushToken",pushToken);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        /*start new thread*/
        thread.start();


    }
    public static void userLogin(final String mobile,final String password, final Handler handler)
    {

        /*Run new thread to perform users Info query*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                    /*Set post information to perform query*/
                HttpClient client = new DefaultHttpClient(httpParameters);
                    /*set post's URL*/
                HttpPost post = new HttpPost(SERVER_URL +PATH_LOGIN);
                    /*set post's header*/
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    /*Create new NameValue pair list*/
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                    /*set parameters*/
                params.add(new BasicNameValuePair("mobile", mobile));
                params.add(new BasicNameValuePair("pass",password));
                try {
                        /*set post value*/
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                        /*Execute post*/
                    HttpResponse httpResponse = client.execute(post);
                        /*get post's entity*/
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("mobile",mobile);
                    bundle.putString("pass", password);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("mobile",mobile);
                    bundle.putString("pass", password);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        /*start new thread*/
        thread.start();


    }
    public static void addFamilyMember(final String userID,final String mobile, final Handler handler)
    {

        /*Run new thread to perform users Info query*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                    /*Set post information to perform query*/
                HttpClient client = new DefaultHttpClient(httpParameters);
                    /*set post's URL*/
                HttpPost post = new HttpPost(SERVER_URL +PATH_ADD_FAMILY_MEMBER);
                    /*set post's header*/
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    /*Create new NameValue pair list*/
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                    /*set parameters*/
                params.add(new BasicNameValuePair("mobile", mobile));
                params.add(new BasicNameValuePair("userID",userID));
                try {
                        /*set post value*/
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                        /*Execute post*/
                    HttpResponse httpResponse = client.execute(post);
                        /*get post's entity*/
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("userID", userID);
                    bundle.putString("mobile",mobile);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("userID", userID);
                    bundle.putString("mobile",mobile);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        /*start new thread*/
        thread.start();


    }
    public static void getFamilyMembers(final String userID, final Handler handler)
    {

        /*Run new thread to perform users Info query*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                    /*Set post information to perform query*/
                HttpClient client = new DefaultHttpClient(httpParameters);
                    /*set post's URL*/
                HttpPost post = new HttpPost(SERVER_URL +PATH_GET_FAMILY_MEMBER);
                    /*set post's header*/
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    /*Create new NameValue pair list*/
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                    /*set parameters*/
                params.add(new BasicNameValuePair("userID",userID));
                try {
                        /*set post value*/
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                        /*Execute post*/
                    HttpResponse httpResponse = client.execute(post);
                        /*get post's entity*/
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("userID", userID);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("userID", userID);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        /*start new thread*/
        thread.start();


    }
    public static void sendAlert(final String userID,final String type,final String lat,final String lng, final Handler handler)
    {

        /*Run new thread to perform users Info query*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                    /*Set post information to perform query*/
                HttpClient client = new DefaultHttpClient(httpParameters);
                    /*set post's URL*/
                HttpPost post = new HttpPost(SERVER_URL +PATH_SEND_ALERT);
                    /*set post's header*/
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                    /*Create new NameValue pair list*/
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                    /*set parameters*/
                params.add(new BasicNameValuePair("userID",userID));
                params.add(new BasicNameValuePair("type",type));
                params.add(new BasicNameValuePair("lat",lat));
                params.add(new BasicNameValuePair("lng",lng));
                try {
                        /*set post value*/
                    post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {

                        /*Execute post*/
                    HttpResponse httpResponse = client.execute(post);
                        /*get post's entity*/
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);

                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("userID", userID);
                    bundle.putString("type",type);
                    bundle.putString("lat",lat);
                    bundle.putString("lng",lng);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("userID", userID);
                    bundle.putString("type",type);
                    bundle.putString("lat",lat);
                    bundle.putString("lng",lng);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        /*start new thread*/
        thread.start();


    }


    public static void getCAP(final String capName,final String capUrl,final Handler handler)
    {

        /*Run new thread to perform users Info query*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                    /*Set post information to perform query*/
                HttpClient client = new DefaultHttpClient(httpParameters);
                    /*set post's URL*/
                HttpGet get = new HttpGet(capUrl);

                try {

                        /*Execute post*/
                    HttpResponse httpResponse = client.execute(get);
                        /*get post's entity*/
                    HttpEntity entity = httpResponse.getEntity();
                    String jsonCapFeed = null;
                    try {
                        CapFeed capFeed = CapXmlParser.parseCapFeed(entity.getContent());
                        jsonCapFeed = new Gson().toJson(capFeed);
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    }

                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", jsonCapFeed);
                    bundle.putString("name", capName);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        /*start new thread*/
        thread.start();


    }
    public static void getGeoJson(final String capName,final String atlasUrl,final Handler handler)
    {

        /*Run new thread to perform users Info query*/
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                // The default value is zero, that means the timeout is not used.
                int timeoutConnection = CONNECTION_TIMEOUT;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

                    /*Set post information to perform query*/
                HttpClient client = new DefaultHttpClient(httpParameters);
                    /*set post's URL*/
                HttpGet get = new HttpGet(atlasUrl);

                try {

                        /*Execute post*/
                    HttpResponse httpResponse = client.execute(get);
                        /*get post's entity*/
                    HttpEntity entity = httpResponse.getEntity();

                    String response = EntityUtils.toString(entity);
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    bundle.putString("name", capName);
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                } catch (IOException e) {
                        /*Set message to send to the handler*/
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", "no connection");
                    bundle.putString("name", capName);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        });

        /*start new thread*/
        thread.start();


    }
    private static String inputStreamToString(InputStream is) throws IOException{
        String line = "";
        StringBuilder total = new StringBuilder();

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        // Read response until the end
        while ((line = rd.readLine()) != null) {
            total.append(line);
        }

        // Return full string
        return total.toString();
    }

}
