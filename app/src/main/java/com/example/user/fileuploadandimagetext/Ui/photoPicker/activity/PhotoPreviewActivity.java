/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
//          佛曰:
//                  写字楼里写字间，写字间里程序员；
//                  程序人员写程序，又拿程序换酒钱。
//                  酒醒只在网上坐，酒醉还来网下眠；
//                  酒醉酒醒日复日，网上网下年复年。
//                  但愿老死电脑间，不愿鞠躬老板前；
//                  奔驰宝马贵者趣，公交自行程序员。
//                  别人笑我忒疯癫，我笑自己命太贱；

package com.example.user.fileuploadandimagetext.Ui.photoPicker.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fileuploadandimagetext.R;
import com.example.user.fileuploadandimagetext.Ui.Utils.log.LogUtil;
import com.example.user.fileuploadandimagetext.Ui.config.UI;
import com.example.user.fileuploadandimagetext.Ui.photoPicker.adapter.PhotoPagerAdapter;
import com.example.user.fileuploadandimagetext.Ui.photoPicker.intent.PhotoPreviewIntent;
import com.example.user.fileuploadandimagetext.Ui.photoPicker.widget.ViewPagerFixed;

import java.util.ArrayList;

/**
 * Created by foamtrace on 2015/8/25.
 */
public class PhotoPreviewActivity extends UI implements PhotoPagerAdapter.PhotoViewClickListener, ViewPager.OnPageChangeListener {

    public static final String EXTRA_PHOTOS = "extra_photos";
    public static final String EXTRA_CURRENT_ITEM = "extra_current_item";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "preview_result";
    /**
     * 预览请求状态码
     */
    public static final int REQUEST_PREVIEW = 99;
    private ArrayList<String> oldpaths = new ArrayList<>();//存储旧的地址
    private ArrayList<String> paths;
    private ViewPagerFixed mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private int currentItem = 0;
    private Button btn_udo;//撤销按钮

    private TextView tv_title, text;
    private Button btn_send_image;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);
        getData();
        initView();
        initData();
        setOnClick();
        updateActionBarTitle();
    }

    /**
     * 开其预览界面
     * @param activity 从那个页面进入这个页面
     * @param list 图片地址的集合
     * @param requestCode 请求码
     */
    public static void start(Activity activity ,ArrayList<String> list,int requestCode){
        PhotoPreviewIntent intent = new PhotoPreviewIntent(activity);
        intent.setCurrentItem(0);
        intent.setPhotoPaths(list);
        activity.startActivityForResult(intent, requestCode);
    }
    public void initView() {
        mViewPager = (ViewPagerFixed) findViewById(R.id.vp_photos);
        btn_udo = (Button) findViewById(R.id.btn_udo);
        text = (TextView) findViewById(R.id.text);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(R.string.preview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        btn_send_image = (Button) findViewById(R.id.btn_send_image);
        btn_send_image.setVisibility(View.VISIBLE);
        btn_send_image.setText("删除");
        btn_udo.setText("完成");
    }

    public void initData() {
        mPagerAdapter = new PhotoPagerAdapter(this, paths);
        mPagerAdapter.setPhotoViewClickListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.setOffscreenPageLimit(5);
    }

    public void getData() {
        paths = new ArrayList<>();
        ArrayList<String> pathArr = getIntent().getStringArrayListExtra(EXTRA_PHOTOS);
        if (pathArr != null) {
            paths.addAll(pathArr);
        }
        oldpaths.addAll(paths);
        currentItem = getIntent().getIntExtra(EXTRA_CURRENT_ITEM, 0);
    }

    public void setOnClick() {
        mViewPager.addOnPageChangeListener(this);
        btn_send_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == android.R.id.home){
                    onBackPressed();
                    return;
                }
                final int index = mViewPager.getCurrentItem();
                final String deletedPath =  paths.get(index);
                if(paths.size() <= 1){
                    // 最后一张照片弹出删除提示
                    // show confirm dialog
                    new AlertDialog.Builder(PhotoPreviewActivity.this)
                            .setTitle(R.string.confirm_to_delete)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    paths.remove(index);
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }else{
                    paths.remove(index);
                    mPagerAdapter.notifyDataSetChanged();
                    btn_udo.setVisibility(View.VISIBLE);
//                    Toast.makeText(PhotoPreviewActivity.this, R.string.deleted_a_photo, Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_udo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        if (paths.size() > 0 && paths.size() <oldpaths.size()) {
//                            paths.add(index, deletedPath);
//                            Log.d("slj","走这个....paths:"+paths.size() + "----oldpaths:"+oldpaths.size() +"---deletedPath:"+deletedPath+"----index:"+index);
//                        } else if (paths.size() ==oldpaths.size()){
//                            btn_udo.setVisibility(View.GONE);
//                        }
//                        mPagerAdapter.notifyDataSetChanged();
//                        mViewPager.setCurrentItem(index, true);
                onBackPressed();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                onBackPressed();
            }
        });
    }

    @Override
    public void OnPhotoTapListener(View view, float v, float v1) {
        onBackPressed();
    }

    public void updateActionBarTitle() {
        text.setText(getString(R.string.image_index, mViewPager.getCurrentItem() + 1, paths.size()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, paths);
        LogUtil.d("slj","paths---:"+paths.size() + "-----list:"+paths);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        updateActionBarTitle();
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
