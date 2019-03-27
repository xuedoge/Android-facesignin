package com.example.face_sign_up;

import android.graphics.Bitmap;
import android.os.StrictMode;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

public class Facepp {
    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private final static String key = "yoYY7MKiuCPA1VjsnTHE7XqdqPujyJwc";
    private final static String secret = "b4p6mAOC4dIrkI8-PyGtKGz6Hqa33nvo";

    private static String boundaryString = getBoundary();
    public  static void face_set(String face_token ,String id){
        String url = "https://api-cn.faceplusplus.com/facepp/v3/face/setuserid";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key",key );
        map.put("api_secret", secret);
        map.put("face_token",face_token);
        map.put("user_id",id);
        System.out.println(id);
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);

            System.out.println(str);
            //System.out.println("11111111111111111");
        }catch (Exception e) {
            //System.out.println(e);
            //Toast showToast=Toast.makeText(MainActivity, "网络连接失败，请检查您的网络", Toast.LENGTH_LONG);
            //showToast.setGravity(Gravity.BOTTOM, 0, 0);
            //showToast.show();

            e.printStackTrace();
        }
    }
    public static void  faceset_remove(){
        String userid=null;

        String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/removeface";

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key",key );
        map.put("api_secret", secret);
        map.put("outer_id","class");
        map.put("face_tokens","RemoveAllFaceTokens");
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);

            System.out.println(str);
            //System.out.println("11111111111111111");
        }catch (Exception e) {
            //System.out.println(e);
//            Toast showToast=Toast.makeText(this, "网络连接失败，请检查您的网络", Toast.LENGTH_LONG);
//            showToast.setGravity(Gravity.BOTTOM, 0, 0);
//            showToast.show();

            e.printStackTrace();
        }
    }
    public  static String face_get_uid(String face_token ){
        String userid=null;

        String url = "https://api-cn.faceplusplus.com/facepp/v3/face/getdetail";

        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key",key );
        map.put("api_secret", secret);
        map.put("face_token",face_token);
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            JSONObject dt = JSON.parseObject(str);

            userid= dt.getString("user_id");

            System.out.println(str);
            //System.out.println("11111111111111111");
        }catch (Exception e) {
            //System.out.println(e);
//            Toast showToast=Toast.makeText(this, "网络连接失败，请检查您的网络", Toast.LENGTH_LONG);
//            showToast.setGravity(Gravity.BOTTOM, 0, 0);
//            showToast.show();

            e.printStackTrace();
        }
        return userid;
    }
    public  static void faceset_add(String face_token){
        String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/addface";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key",key );
        map.put("api_secret", secret);
        map.put("outer_id", "class");
        map.put("face_tokens",face_token);
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            System.out.println(str);
            //System.out.println("11111111111111111");
        }catch (Exception e) {
            //System.out.println(e);
//            Toast showToast=Toast.makeText(this, "网络连接失败，请检查您的网络", Toast.LENGTH_LONG);
//            showToast.setGravity(Gravity.BOTTOM, 0, 0);
//            showToast.show();

            e.printStackTrace();
        }
    }
    public  static String search(String faceid){
//        System.out.println("************************");
//        System.out.println(bm.getByteCount());
//        System.out.println("************************");
        //返回face_token
        float confidence=0;
        float r3=0;
        float r4=0;
        float r5=0;

        String face_token=" ";
        String url = "https://api-cn.faceplusplus.com/facepp/v3/search";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key",key );
        map.put("api_secret", secret);
        map.put("face_token", faceid);
        map.put("outer_id", "class");
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);//主线程访问网络

            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            JSONObject dt = JSON.parseObject(str);

            //String results = dt.getString("results");
            JSONArray results_array = JSON.parseArray(dt.getString("results"));
            JSONObject face = results_array.getJSONObject(0);
            face_token = face.getString("face_token");
            System.out.println("face_token");
            System.out.println(face_token);
            System.out.println("face_token");

            confidence = face.getFloat("confidence") ;
            System.out.println("confidence");
            System.out.println(confidence);
            System.out.println("confidence");

            JSONObject thresholds = JSON.parseObject(dt.getString("thresholds"));
            r3 = thresholds.getFloat("1e-3");
            r4 = thresholds.getFloat("1e-4");
            r5 = thresholds.getFloat("1e-5");
            System.out.println("r");
            System.out.println(r3);
            System.out.println(r4);
            System.out.println(r5);
            System.out.println("r");

        }catch (Exception e) {
//            System.out.println("nmd wsm");
//            System.out.println(e);
//            System.out.println("nmd wsm");
//
            e.printStackTrace();
        }
        if(confidence > r4){
            return face_token;
        }
        else return null;
    }
    public  static String detect(byte[] bm){
        //返回face_token
        //File file = new File("手机存储\\Tencent\\QQ_Images\\4c433d5e451a5499.jpg");
        String face_token=" ";
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key",key );
        map.put("api_secret", secret);
        byteMap.put("image_file", bm);
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);//主线程访问网络

            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            JSONObject dt = JSON.parseObject(str);

            String faces = dt.getString("faces");
            JSONArray faces_array = JSON.parseArray(faces);
            JSONObject face = faces_array.getJSONObject(0);
            face_token = face.getString("face_token");
            System.out.println(face_token);
            //System.out.println("11111111111111111");
        }catch (Exception e) {
            System.out.println(e);
//            Toast showToast=Toast.makeText(this, "网络连接失败，请检查您的网络", Toast.LENGTH_LONG);
//            showToast.setGravity(Gravity.BOTTOM, 0, 0);
//            showToast.show();
            e.printStackTrace();
        }
        return face_token;
    }
    public  static String detect(Bitmap bm){
//        System.out.println("************************");
//        System.out.println(bm.getByteCount());
//        System.out.println("************************");
        //返回face_token
        //File file = new File("手机存储\\Tencent\\QQ_Images\\4c433d5e451a5499.jpg");
        byte[] buff = Bitmap2Bytes(bm);
        String face_token=" ";
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key",key );
        map.put("api_secret", secret);
        byteMap.put("image_file", buff);
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);//主线程访问网络

            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            JSONObject dt = JSON.parseObject(str);

            String faces = dt.getString("faces");
            JSONArray faces_array = JSON.parseArray(faces);
            JSONObject face = faces_array.getJSONObject(0);
            face_token = face.getString("face_token");
//            System.out.println(face_token);
            //System.out.println("11111111111111111");
        }catch (Exception e) {
//            System.out.println("nmd wsm");
//            System.out.println(e);
//            System.out.println("nmd wsm");
//
            e.printStackTrace();
        }
        return face_token;
    }
    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            Iterator fileIter = fileMap.entrySet().iterator();
            while(fileIter.hasNext()){
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            //System.out.println(e);
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
    private static String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }
}
