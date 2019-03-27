package com.example.face_sign_up;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class AddActivity extends AppCompatActivity {

    private  MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        dbHelper = new MyDatabaseHelper(this,"facesign.db",null,1);
    }



    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public void startCamera(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
        }
    }
    public void btn_back(View view) {
        back();
    }
    public void back() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        //this.finish();
    }
    @Override
    protected void onActivityResult(int requestCode ,int resultCode, Intent data){
        super.onActivityResult(requestCode , resultCode ,data);
        String id;
        String faceid;
        EditText et_id;


        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                faceid=Facepp.detect(imageBitmap);
                Facepp.faceset_add(faceid);//将人脸id上传的人脸库set里面

                et_id = findViewById(R.id.et);//student id
                id = et_id.getText().toString();

                //数据库操作，添加id
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("studentid",id);
                values.put("attend",0);
                db.insert("Class",null,values);
                values.clear();

                Facepp.face_set(faceid,id);//将库里面的人脸id和输入的id绑定

                back();
            }
        }
    }

}
