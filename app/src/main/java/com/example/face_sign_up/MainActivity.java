package com.example.face_sign_up;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

//import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

public class MainActivity extends AppCompatActivity {
        private ArrayList<String> list1 = new ArrayList<String>();
        private ArrayList<String> list2 = new ArrayList<String>();
        private  MyDatabaseHelper dbHelper;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            dbHelper = new MyDatabaseHelper(this,"facesign.db",null,1);//绑定数据库

            load();
        }
        public  void load(){
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("Class",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    String str=cursor.getString(cursor.getColumnIndex("studentid"));
                    int flag=cursor.getInt(cursor.getColumnIndex("attend"));
                    if(flag==1){
                        list2.add(str);
                    }
                    else{
                        list1.add(str);
                    }
                }while (cursor.moveToNext());
            }
//                cursor.close();
            //显示listview
            ListView listview=(ListView)findViewById(R.id.listview1);
            ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list1);
            listview.setAdapter(adapter);

            ListView listview2=(ListView)findViewById(R.id.listview2);
            ArrayAdapter<String> adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list2);
            listview2.setAdapter(adapter2);
        }
        public void btn_reset(View view) {
                //数据库操作，清空签到数据
            String[] up=new String[1];
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            //清空操作
//            db.delete("Class",null,null);
//            Facepp.faceset_remove();

            Cursor cursor = db.query("Class",null,null,null,null,null,null);
            if(cursor.moveToFirst()){
                do{
                    String str=cursor.getString(cursor.getColumnIndex("studentid"));
                    up[0]=str;
                    try{
                        ContentValues values = new ContentValues();
                        values.put("attend", 0);
                        db.update("Class", values, "studentid = ?", up);
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }
                }while (cursor.moveToNext());
            }

        }
        public void btn_add(View view){
                //TODO 这里面放所需要执行的程序代码
                Intent intent = new Intent();
                intent.setClass(this, AddActivity.class);
                startActivity(intent);
                this.finish();
        }


        public void btn_identify(View view){
                //TODO 这里面放所需要执行的程序代码
                Intent intent = new Intent();
                intent.setClass(this, CameraActivity.class);
                startActivity(intent);
                this.finish();
        }



//        public  void faceset_creat(){
//                String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/create";
//                HashMap<String, String> map = new HashMap<>();
//                HashMap<String, byte[]> byteMap = new HashMap<>();
//                map.put("api_key",key );
//                map.put("api_secret", secret);
//                map.put("outer_id", "class");
//                try{
//                        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
//                        StrictMode.setThreadPolicy(policy);
//
//                        byte[] bacd = post(url, map, byteMap);
//                        String str = new String(bacd);
//                        System.out.println(str);
//                        //System.out.println("11111111111111111");
//                }catch (Exception e) {System.out.println(e);
//                        e.printStackTrace();
//                }
//        }







}

