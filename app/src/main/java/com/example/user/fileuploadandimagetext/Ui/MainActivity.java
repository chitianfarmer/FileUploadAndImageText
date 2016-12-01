package com.example.user.fileuploadandimagetext.Ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fileuploadandimagetext.R;
import com.example.user.fileuploadandimagetext.Ui.picker.activity.PickImageActivity;
import com.example.user.fileuploadandimagetext.demo.Extras;
import com.example.user.fileuploadandimagetext.demo.RequestCode;
import com.example.user.fileuploadandimagetext.file.browser.FileBrowserActivity;
import com.example.user.fileuploadandimagetext.permission.MPermission;
import com.example.user.fileuploadandimagetext.permission.annotation.OnMPermissionDenied;
import com.example.user.fileuploadandimagetext.permission.annotation.OnMPermissionGranted;

public class MainActivity extends UI implements View.OnClickListener{
    private Button btn_send_file,btn_send_image;
    private TextView tv_image_path,tv_file_path,tv_title;
    private ImageView iv_back;
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
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
        btn_send_image.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        btn_send_file = (Button) findViewById(R.id.btn_send_file);
        btn_send_image = (Button) findViewById(R.id.btn_send_image);
        tv_image_path = (TextView) findViewById(R.id.tv_image_path);
        tv_file_path = (TextView) findViewById(R.id.tv_file_path);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("文件及图片路径获取");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        findViewById(R.id.view_temp).setVisibility(View.GONE);
        iv_back.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_send_file:
                chooseFile();
            break;
            case R.id.btn_send_image:
                chosseImage();
                break;
            case  R.id.iv_back:
                finish();
            break;
        }
    }
    private void chooseFile() {
        FileBrowserActivity.startActivityForResult(MainActivity.this, RequestCode.GET_LOCAL_FILE);
    }
    private void chosseImage() {
        int from = PickImageActivity.FROM_LOCAL;
          PickImageActivity.start(MainActivity.this,RequestCode.GET_LOCAL_IMAGE, from, "", false,9, false, true, 30, 30);
//        PickImageActivity.start(MainActivity.this, RequestCode.GET_LOCAL_IMAGE, from, "");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){

        if (requestCode == RequestCode.GET_LOCAL_FILE) {
            String path = data.getStringExtra(FileBrowserActivity.EXTRA_DATA_PATH);
            tv_file_path.setText("文件所在路径:" + path);
            }  else if (requestCode == RequestCode.GET_LOCAL_IMAGE){
            if (data == null) {
                return;
             }
            final String photoPath = data.getStringExtra(Extras.EXTRA_FILE_PATH);
            if (TextUtils.isEmpty(photoPath)) {
                return;
            }
            tv_image_path.setText("图片所在地址:"+photoPath);
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
    public void onBasicPermissionSuccess(){
        Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
    }

}
