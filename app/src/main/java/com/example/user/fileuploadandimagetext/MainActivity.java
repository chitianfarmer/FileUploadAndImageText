package com.example.user.fileuploadandimagetext;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.fileuploadandimagetext.Ui.Utils.media.BitmapUtil;
import com.example.user.fileuploadandimagetext.Ui.Utils.media.ImageGetUtils;
import com.example.user.fileuploadandimagetext.Ui.config.RequestCode;
import com.example.user.fileuploadandimagetext.Ui.config.UI;
import com.example.user.fileuploadandimagetext.Ui.photoPicker.activity.PhotoPickerActivity;
import com.example.user.fileuploadandimagetext.Ui.file.browser.FileBrowserActivity;
import com.example.user.fileuploadandimagetext.permission.MPermission;
import com.example.user.fileuploadandimagetext.permission.annotation.OnMPermissionDenied;
import com.example.user.fileuploadandimagetext.permission.annotation.OnMPermissionGranted;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoView;

public class MainActivity extends UI implements View.OnClickListener {
    private Button btn_send_file,btn_send_post, btn_get_imagePath;
    private TextView tv_image_path, tv_file_path, tv_title, tv_post_result;
    private ImageView iv_back;
    PhotoView iv_newestimage;
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private int tempTimes = 1;
    private ArrayList<String> selectList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestBasicPermission();
        initView();
        setOnClick();
    }

    private void setOnClick() {
        btn_send_file.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btn_send_post.setOnClickListener(this);
        btn_get_imagePath.setOnClickListener(this);
    }

    private void initView() {
        btn_send_file = (Button) findViewById(R.id.btn_send_file);
        btn_get_imagePath = (Button) findViewById(R.id.btn_get_imagePath);

        tv_image_path = (TextView) findViewById(R.id.tv_image_path);
        tv_file_path = (TextView) findViewById(R.id.tv_file_path);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("文件及图片路径获取");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        findViewById(R.id.view_temp).setVisibility(View.GONE);
        iv_back.setVisibility(View.GONE);
        btn_send_post = (Button) findViewById(R.id.btn_send_post);
        tv_post_result = (TextView) findViewById(R.id.tv_post_result);
        iv_newestimage = (PhotoView) findViewById(R.id.iv_newestimage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_file:
                chooseFile();
                break;
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_get_imagePath:
                //打开多图选择
                PhotoPickerActivity.startActivity(MainActivity.this,selectList,RequestCode.GET_LOCAL_IMAGE);
                break;
            case R.id.btn_send_post:
                if (tempTimes == 1) {
                    btn_send_post.setText("显示最老照片");
                    String mPath = ImageGetUtils.getNewestImagePath(MainActivity.this);
                    tv_post_result.setText("最新照片地址为:" + mPath);
                    //得到原图片
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    if (height > 200 || width > 200) {
                        if (height > width) {
                            height = height / 2;
                        } else {
                            width = width / 2;
                        }
                    }
                    //得到缩略图
                    bitmap = BitmapUtil.resizeBitmap(bitmap, width, height);
                    iv_newestimage.setImageBitmap(bitmap);
                    tempTimes++;
                } else {
                    btn_send_post.setText("显示最新照片");
                    String mPath = ImageGetUtils.getOldImagePath(MainActivity.this);
                    tv_post_result.setText("最老照片地址为:" + mPath);
                    //得到原图片
                    Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                    int height = bitmap.getHeight();
                    int width = bitmap.getWidth();
                    if (height > 200 || width > 200) {
                        if (height > width) {
                            height = height / 2;
                        } else {
                            width = width / 2;
                        }
                    }
                    //得到缩略图
                    bitmap = BitmapUtil.resizeBitmap(bitmap, width, height);
                    iv_newestimage.setImageBitmap(bitmap);
                    tempTimes--;
                }
                break;
        }
    }

    private void chooseFile() {
        FileBrowserActivity.startActivityForResult(MainActivity.this, RequestCode.GET_LOCAL_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCode.GET_LOCAL_FILE) {
                String path = data.getStringExtra(FileBrowserActivity.EXTRA_DATA_PATH);
                Toast.makeText(this, "文件所在路径:" + path, Toast.LENGTH_SHORT).show();
                tv_file_path.setText("文件所在路径:" + path);
            } else if (requestCode == RequestCode.PICK_IMAGE) {
                if (data == null) {
                    return;
                }
                Bundle extras = data.getExtras();
                if (extras != null) {
                    ArrayList<String> images = (ArrayList<String>) extras.getSerializable("images");
                    Log.d("slj","返回:"+images);
                    if (images != null || images.size() != 0) {
                        String s = images.get(0);
                        tv_image_path.setText("多张图片所在地址:" + s);
                    }else {
                        tv_image_path.setText("多张图片所在地址:未获取到");
                    }
                }else{
                    tv_image_path.setText("多张图片所在地址:未获取到");
                }
            } else if (requestCode == RequestCode.GET_LOCAL_IMAGE){
                ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                Log.d("slj","返回:"+list);
                if (list != null || list.size() != 0) {
                    String s = list.get(0);
                    tv_image_path.setText("多张图片所在地址:" + s);
                }else {
                    tv_image_path.setText("多张图片所在地址:未获取到");
                }
            }else{
                tv_image_path.setText("多张图片所在地址:未获取到");
            }
        }
    }

    /**
     * 基本权限管理
     */
    private void requestBasicPermission() {
        /**
         *   Manifest.permission.RECORD_AUDIO,
         Manifest.permission.ACCESS_COARSE_LOCATION,
         Manifest.permission.ACCESS_FINE_LOCATION
         */
        MPermission.with(MainActivity.this)
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE
                )
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }
}
