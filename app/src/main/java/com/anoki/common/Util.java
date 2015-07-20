package com.anoki.common;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Prayer;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PortUnreachableException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-07-07.
 */
public class Util {





    private static Gson gson = new Gson();

    public static <T>  T rest(String path, String method, Object in, Class<T> classOfT){
        return gson.fromJson(rest(path,method,in), classOfT);
    }

    public static <T>  T rest(String path, String method, Object in, Type classOfT){
        return gson.fromJson(rest(path,method,in), classOfT);
    }


    public static String rest(String path, String method, Object in){

        final String USER_AGENT = "Mozilla/5.0";
        StringBuffer response = new StringBuffer();


        String url = "http://anoki.co.kr/anoki/rest/"+path;
        try {
            URL  obj = new URL(url);


            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("Content-Type", "application/json");


            String urlParameters = gson.toJson(in);

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending "+method+" request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }

            reader.close();

            return response.toString();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }



    public static Bitmap fetchImage( String id )
    {
        try
        {
            URL url;
            url = new URL( "http://anoki.co.kr/anoki/images/"+id );

            HttpURLConnection c = ( HttpURLConnection ) url.openConnection();
            c.setDoInput( true );
            c.connect();
            InputStream is = c.getInputStream();
            Bitmap img;
            img = BitmapFactory.decodeStream(is);
            return img;
        }
        catch ( MalformedURLException e )
        {
        }
        catch ( IOException e )
        {
            Log.d("RemoteImageHandler", "fetchImage IO exception: " + e);
        }
        return null;
    }

    public static String upload(Uri selectedImage, ContentResolver contentResolver, CallBack callBack){
        String id = null;
        InputStream imageStream = null;
        try {
            imageStream = contentResolver.openInputStream(selectedImage);
            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
            id = executeMultipartPost(yourSelectedImage);
            if(!"-1".equals(id)){
                callBack.success(id);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return id;
    }


    public static String executeMultipartPost(Bitmap bm)  {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://anoki.co.kr/anoki/rest/prayer/upload");
            ByteArrayBody bab = new ByteArrayBody(data, "profile.jpg");
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("uploaded", bab);
            reqEntity.addPart("photoCaption", new StringBody("profile"));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            System.out.println("Response: " + s);
            return s.toString();
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
        }

        return null;
    }

    public static int dpToPixel(Context mContext,int yourdpmeasure){
        Resources r = mContext.getResources();
        return  (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                yourdpmeasure,
                r.getDisplayMetrics()
        );

    }

    public static void setPicture(String picture,ImageView view,Drawable def){
        if(!"null".equals(picture) && picture!=null && !"0".equals(picture) ) {
            Bitmap bmp = Util.fetchImage(picture+"");
            view.setImageBitmap(bmp);
            view.setAlpha(1.0f);
        }else{
            view.setImageDrawable(def);
            view.setAlpha(.5f);
        }
    }


    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }


}
