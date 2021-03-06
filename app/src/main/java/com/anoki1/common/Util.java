package com.anoki1.common;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.VideoView;

import com.anoki1.fragment.PrayerImageFragment;
import com.anoki1.R;
import com.anoki1.pojo.User;
import com.anoki1.prayer.ZoomInActivity;
import com.anoki1.pojo.Media;
import com.anoki1.pojo.Prayer;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apmem.tools.layouts.FlowLayout;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-07-07.
 */
public class Util {


    private static Gson gson = new Gson();

    public static <T> void  backGroundRest(final String path,final String method,final Object in, final CallBack<Void> callback) {

        new RestTask<Void>().execute(
                new Task() {

                    @Override
                    public  Void execute() {
                        rest(path,method,in);
                        return null;
                    }
                },
                callback);

    }



    public static <T> void  backGroundRest(final String path,final String method,final Object in, final Type classOfT, final CallBack<T> callback) {

        new RestTask<T>().execute(
                new Task() {

                    @Override
                    public  T execute() {
                        return Util.rest(path,method,in,classOfT);
                    }
                },
                callback);

    }


    public static <T> T rest(String path, String method, Object in, Class<T> classOfT) {
        return gson.fromJson(rest(path, method, in), classOfT);
    }

    public static <T> T rest(String path, String method, Object in, Type classOfT) {
        return gson.fromJson(rest(path, method, in), classOfT);
    }


    public static String rest(String path, String method, Object in) {

        final String USER_AGENT = "Mozilla/5.0";
        StringBuffer response = new StringBuffer();


        String url = "http://anoki.co.kr/anoki/rest/" + path;
        try {
            URL obj = new URL(url);


            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "utf-8");
            con.setRequestProperty("Content-Type", "application/json");


            String urlParameters = gson.toJson(in);

            // Send post request
            con.setDoOutput(true);
            BufferedOutputStream bos = new BufferedOutputStream(con.getOutputStream());
            bos.write(urlParameters.getBytes("UTF-8"));
            bos.flush();
            bos.close();


            int responseCode = con.getResponseCode();
            System.out.println("\nSending " + method + " request to URL : " + url);
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


    public static Bitmap fetchImage(String id){
        return fetchImage(id,null);
    }

    public static Bitmap fetchImage(String id,ImageView view) {

        if("0".equals(id) || "-1".equals(id)) {
            return null;
        }

        try {

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;

            if(view != null && view.getLayoutParams() !=null) {
                int photoWidth = bmOptions.outWidth;
                int photoHeight = bmOptions.outHeight;

                if(view.getLayoutParams().width == 0 || view.getLayoutParams().height == 0) return null;
                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoWidth / view.getLayoutParams().width, photoHeight / view.getLayoutParams().height);

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;
            }

            URL url;
            url = new URL("http://anoki.co.kr/anoki/images/" + id);

            HttpURLConnection c = (HttpURLConnection) url.openConnection();


            //   Value of stale indicates how old the cache content can be
            //   before using it. Here the value is 1 hour(in secs)
            c.addRequestProperty("Cache-Control", "max-stale=" + 60 * 60);

            c.setUseCaches(true);

            c.setDoInput(true);
            c.connect();
            InputStream is = c.getInputStream();
            Bitmap img;
            img = BitmapFactory.decodeStream(is);
            return img;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            Log.d("RemoteImageHandler", "fetchImage IO exception: " + e);
        }
        return null;
    }

    public static String upload(Uri selectedImage, ContentResolver contentResolver, PictureCallBack callBack) {
        String id = null;
        InputStream imageStream = null;
        try {
            imageStream = contentResolver.openInputStream(selectedImage);



            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(imageStream, null,options);

            // Only scale if we need to
            // (16384 buffer for img processing)
            Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);

            final int height = options.outHeight;
            final int width = options.outWidth;

            if(options.outHeight * options.outWidth  >= 786432){
                // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                int inSampleSize = 1;



                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) * (halfWidth / inSampleSize)  >= 786432) {
                    inSampleSize *= 2;
                }

                options.inSampleSize = inSampleSize;
            }

            // Do the actual decoding
            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[512];
            imageStream = contentResolver.openInputStream(selectedImage);

            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream,null, options);



            id = executeMultipartPost(yourSelectedImage);
            if (!"-1".equals(id)) {
                callBack.success(id);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return id;
    }

    public static String uploadBitmap(Bitmap yourSelectedImage, PictureCallBack callBack) {
        return uploadBitmap(yourSelectedImage, callBack, "I");
    }

    public static String uploadBitmap(Bitmap yourSelectedImage, PictureCallBack callBack, String type) {
        String id = null;
        id = executeMultipartPost(yourSelectedImage, type);
        if (!"-1".equals(id)) {
            callBack.success(id);
        }

        return id;
    }

    public static String executeMultipartPost(Bitmap bm) {
        return executeMultipartPost(bm, "I");
    }

    public static String executeMultipartPost(Bitmap bm, String type) {
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
            reqEntity.addPart("type", new StringBody(type));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
         //   System.out.println("Response: " + s);
            return s.toString();
        } catch (Exception e) {
            // handle exception here
           // Log.e(e.getClass().getName(), e.getMessage());
        }

        return null;
    }

    public static int dpToPixel(Context mContext, int yourdpmeasure) {
        Resources r = mContext.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                yourdpmeasure,
                r.getDisplayMetrics()
        );

    }

    public static Bitmap setPicture(String picture, ImageView view) {
        return setPicture(picture, view, null);
    }

    public static Bitmap setPicture(String picture, ImageView view, Drawable def) {
        if (!"null".equals(picture) && picture != null && !"0".equals(picture)) {
            Bitmap bmp = Util.fetchImage(picture + "",view);
            new DownloadImageTask(view).execute(picture);
            return bmp;
        } else {
            if (def != null) {
                view.setImageDrawable(def);
//                view.setAlpha(.5f);
            }
        }

        return null;
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, String> {


        private ImageView view;
        private Bitmap bmp;

        public DownloadImageTask(ImageView view){
            this.view = view;
        }

        @Override
        protected String doInBackground(String... params) {
            bmp = fetchImage(params[0]);
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            view.setImageBitmap(bmp);
            view.setAlpha(1.0f);
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


    public static Intent inviteIntent(Prayer prayer) {

        String list = "";
        for (String phone : prayer.phone) {
            list += phone + ";";
        }
        list = list.substring(0, list.length() - 1);

        Intent intentsms = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + list));
        intentsms.putExtra("sms_body", Global.me.name + "님이 기도어플 아노키로 초대하셨습니다. 아래를 누르시면 " + Global.me.name + "님과 친구가 됩니다. \n\n http://anoki.co.kr/anoki/invite.jsp");

        return intentsms;
    }

    public static String makePhoneNumber(String country, String number) {

        if(number.length()>=11) {

            return "+" + country + " " + number.substring(0, 3) + "-" + number.substring(3, 7) + "-" + number.substring(7, 11);
        }else{
            return number;
        }
    }

    public static void styleTab(Context context, TabHost myTabHost) {
        int height = Util.dpToPixel(context, 40);

        for (int i = 0; i < myTabHost.getTabWidget().getChildCount(); i++) {
            ((ImageView) ((LinearLayout) myTabHost.getTabWidget().getChildTabViewAt(i)).getChildAt(0)).setMaxHeight(height);
            myTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = height;
            TextView tv = (TextView) myTabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
            tv.setTextSize(18);

        }
    }

    public static void addMedia(Activity activity, String id) {
        ViewGroup flowLayout = (ViewGroup) activity.findViewById(R.id.media_list);

        //ImageView imageView = new ImageView(activity);
        // / // Bitmap bmp = Util.fetchImage(id,imageView);
      //  imageView.setImageBitmap(bmp);


        int size = Util.dpToPixel(activity, 80);
        int margin = Util.dpToPixel(activity, 5);
//                    FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
//                    layoutParams.setMargins(margin, margin, margin, margin);

//                            imageView.setLayoutParams(layoutParams);
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        //                  flowLayout.addView(imageView, layoutParams);


        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
        layoutParams.setMargins(margin, margin, margin, margin);

        LinearLayout rowLayout = new LinearLayout(activity);

        FragmentManager fragMan = activity.getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        rowLayout.setId(Integer.parseInt(id));

// add rowLayout to the root layout somewhere here

        PrayerImageFragment imageFragment = new PrayerImageFragment();
      //  imageFragment.setBmp(bmp);
        imageFragment.setId(id);
//                    imageFragment.setUri(mUrls[i]);
        fragTransaction.add(rowLayout.getId(), imageFragment, "fragment" + id);
        fragTransaction.commit();


        flowLayout.addView(rowLayout, layoutParams);

    }

    public static void setMediaView(View itemView, Media media){
        setMediaView(itemView, media,false,0);
    }
    public static void setMediaView(View itemView, Media media,boolean resize,int width) {
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
        VideoView videoView = (VideoView) itemView.findViewById(R.id.video);


        if ("I".equals(media.type)) {
            Bitmap bmp = Util.setPicture(media.id, imageView, null);
            if(bmp!= null && resize) {

            }
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);


        } else {

            String path = "http://anoki.co.kr/anoki/images/video/" + media.id;
            Uri uri = Uri.parse(path);

            System.out.println(path);

            videoView.setVideoURI(uri);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.setLooping(true);
                    mp.start();
                }
            });

            videoView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);


        }

    }

    public static void zoom(Media media, Activity activity) {
        Intent intent = new Intent(activity, ZoomInActivity.class);
        intent.putExtra("media", media);
        activity.startActivity(intent);
    }

    public static String getProductId(int dalant){
        switch (dalant){
            case 1000:
                return "com.anoki.one.thousand";
            case 2000:
                return "com.anoki.two.thousand";
            case 4000:
                return "com.anoki.four.thousand";
            case 6000:
                return "com.anoki.six.thousand";
            case 7000:
                return "com.anoki.seven.thousand";
            case 10000:
                return "com.anoki.ten.thousand";
            case 12000:
                return "com.anoki.twelve.thousand";
            case 20000:
                return "com.anoki.twenty.thousand";
            case 21000:
                return "com.anoki.twenty.one.thousand";
            case 24000:
                return "com.anoki.twenty.four.thousand";
            case 40000:
                return "com.anoki.fourty.thousand";
            case 42000:
                return "com.anoki.fourty.two.thousand";
            case 60000:
                return "com.anoki.sixty.thousand";
            case 70000:
                return "com.anoki.seventy.thousand";
            case 120000:
                return "com.anoki.one.hundred.twenty.thousand";
            case 200000:
                return "com.anoki.two.hundred.thousand";


        }
        return "";
    }



    public static int getLimit(int dalant){
        switch (dalant){
            case 0:
                return 30;
            case 2000:
                return 50;
            case 4000:
                return 100;
            case 7000:
                return 500;
            case 10000:
                return 1000;
            case 20000:
                return -1;
        }

        return 0;
    }

    public static void setRegId(final User user,final Context context){
        final String PROJECT_NUMBER = "100224718871";

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {

                    InstanceID instanceID = InstanceID.getInstance(context);

                    String default_senderId = context.getString(R.string.gcm_defaultSenderId);
                    // GCM 기본 scope는 "GCM"이다.
                    String scope = GoogleCloudMessaging.INSTANCE_ID_SCOPE;
                    // Instance ID에 해당하는 토큰을 생성하여 가져온다.
                    user.regid = instanceID.getToken(default_senderId, scope, null);


                    Util.rest("user","PUT",user);

                } catch (IOException ex) {
                    Log.i("GCM",ex.getMessage());
                    ex.printStackTrace();
                }

                return user.regid;


            }
        }.execute(null, null, null);
    }

    public static String alarmText(String type, String name1, String name2){
        String text = "";
        switch (type){
            case"F":
                text = "짝짝짝, " + name1 + "님과 친구가 되었습니다.";
                break;
            case"R":
                text =  name2 + "님이 기도응답글을 올렸습니다. 확인해보세요.";
                break;
            case"Q":
                text =  name1 + "님이 기도를 요청하였습니다.";
                break;
            case"S":
                text = name1 + "님의 기도글에 " + name2 + "님이 댓글을 남겼습니다. 확인해보세요.";
                break;
            case"G":
                text =  name1 + "에 새 글이 업데이트 되었습니다. 확인해보세요.";
                break;

        }

        return text;
    }

}