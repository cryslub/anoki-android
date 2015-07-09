package com.anoki.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
            System.out.println("\nSending 'POST' request to URL : " + url);
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
            Log.d( "RemoteImageHandler", "fetchImage IO exception: " + e );
        }
        return null;
    }



    ///test

}
