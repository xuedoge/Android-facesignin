package com.example.face_sign_up;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.util.Iterator;
import java.util.List;



public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private  MyDatabaseHelper dbHelper;
    private int cameraId = 1;//声明cameraId属性，ID为1调用前置摄像头，为0调用后置摄像头。此处因有特殊需要故调用前置摄像头

    private Camera.PictureCallback mpictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                Bitmap bm=byte2bitmap(data);
                bm=compress();
                String faceid = Facepp.detect(bm);

                System.out.println(faceid);

                String result_face = Facepp.search(faceid);
                String userid=Facepp.face_get_uid(result_face);
                System.out.println(userid);
                System.out.println("ok");


                if(userid!=null){
                    //更新数据库，已签到为1 ， 未签到为0
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    String[] up = new String[1];
                    up[0]=userid;
                    values.put("attend",1);
                    db.update("Class",values,"studentid = ?",up);

                    Toast showToast=Toast.makeText(CameraActivity.this, "success", Toast.LENGTH_LONG);
                    showToast.setGravity(Gravity.CENTER, 0, 0);
                    showToast.show();

                    onPause();
                    onResume();
                }
            } catch (Exception e) {
                Toast showToast=Toast.makeText(CameraActivity.this, "fail", Toast.LENGTH_LONG);
                showToast.setGravity(Gravity.CENTER, 0, 0);
                showToast.show();
                System.out.println("333");
                e.printStackTrace();
            }
        }
    };
    public Bitmap compress() {
        //ImageView imageView=findViewById(R.id.imageView);
        String imagePath = "/sdcard/emp.png";
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        int width= options.outWidth;
        int inSampleSize = 1; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if(minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
            float ratio = (float)minLen / 100.0f; // 计算像素压缩比例
            inSampleSize = (int)ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件

        Matrix matrix=new Matrix();//新建一个矩阵对象
        matrix.setRotate(270);//矩阵旋转操作让照片可以正对着你。但是还存在一个左右对称的问题
        bm=Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);

        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //imageView.setImageBitmap(bm);

        //Bitmap bitmap = ((BitmapDrawable) imageView.getBackground()).getBitmap();

        return  bm;

    }

    public byte[] compressBitmap(byte[] buffer) {
        if (buffer == null || buffer.length == 0) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        try { // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
        int outWidth = options.outWidth;
        int scale = outWidth / 2;
        if (scale > 1) {
            options.inSampleSize = scale; options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
            baos = new ByteArrayOutputStream(); bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            return baos.toByteArray(); }
            else { return buffer; } }
            catch (Exception e) { return null; }
            catch (OutOfMemoryError e) { return null; }
            finally { try { if (baos != null) { baos.close(); } }
            catch (IOException e) { return null; }
        }
    }

    public Bitmap byte2bitmap(byte[] data){
        String path = "/sdcard/emp.png";
        Bitmap bitmap=null;
        File tempfile=new File(path);//新建一个文件对象tempfile，并保存在某路径中
        try {
            FileOutputStream fos = new FileOutputStream(tempfile);
            fos.write(data);//将照片放入文件中
            fos.close();//关闭文件
            //ImageView imageview=findViewById(R.id.imageView);
            FileInputStream fis=new FileInputStream(path);//通过path把照片读到文件输入流中
            bitmap=BitmapFactory.decodeStream(fis);//将输入流解码为bitmap
            Matrix matrix=new Matrix();//新建一个矩阵对象
            matrix.setRotate(270);//矩阵旋转操作让照片可以正对着你。但是还存在一个左右对称的问题
            bitmap=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            //将位图展示在imageview上
            //imageview.setImageBitmap(bitmap);
        }catch (IOException e){
            System.out.println(e);
            e.printStackTrace();
        }
        return bitmap;
    }
    public void btn_signup(View view){
        //TODO 这里面放所需要执行的程序代码
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mPreview = findViewById(R.id.preview);//初始化预览界面
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this); //点击预览界面聚焦
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });


        dbHelper = new MyDatabaseHelper(this,"facesign.db",null,1);
    }
    //定义“拍照”方法
    public void capture(View view) {
        System.out.println("111");
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);//设置照片格式
        parameters.setPreviewSize(100, 100);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO); //摄像头聚焦
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {

                    System.out.println("222");
                    mCamera.takePicture(null, null, mpictureCallback);

            }
        });
    }
    //activity生命周期在onResume是界面应是显示状态
    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            //如果此时摄像头值仍为空
            mCamera = getCamera();//则通过getCamera()方法开启摄像头
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);//开启预览界面
            }
        }
    }

    //activity暂停的时候释放摄像头
    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    //onResume()中提到的开启摄像头的方法
    private Camera getCamera() {
        Camera camera;//声明局部变量camera
        try {
            camera = Camera.open(cameraId);
        }//根据cameraId的设置打开前置摄像头
        catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }
        return camera;
    }

    //开启预览界面
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);//如果没有这行你看到的预览界面就会是水平的
            camera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//定义释放摄像头的方法
    private void releaseCamera(){
        if(mCamera!=null){//如果摄像头还未释放，则执行下面代码
            mCamera.stopPreview();//1.首先停止预览
            mCamera.setPreviewCallback(null);//2.预览返回值为null
            mCamera.release(); //3.释放摄像头
            mCamera=null;//4.摄像头对象值为null
            }
    }

    //定义新建预览界面的方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera,mHolder);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();//如果预览界面改变，则首先停止预览界面
        setStartPreview(mCamera,mHolder);//调整再重新打开预览界面
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();//预览界面销毁则释放相机
    }


}