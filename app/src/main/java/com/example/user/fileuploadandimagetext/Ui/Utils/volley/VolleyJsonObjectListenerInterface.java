package com.example.user.fileuploadandimagetext.Ui.Utils.volley;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/7/4.
 */
public abstract class VolleyJsonObjectListenerInterface {
    public Context mContext;
    public static Response.ErrorListener mErrorListener;
    public static Response.Listener<JSONObject> JSONObject;
    public static VolleyJsonObjectListenerInterface volleyJsonListenerInterface;
    public VolleyJsonObjectListenerInterface(){
        Response.Listener<JSONObject> responseObjListener;
        Response.ErrorListener errorObjListener;
    }
    public VolleyJsonObjectListenerInterface(Context context,VolleyJsonObjectListenerInterface volleyJsonListenerInterface) {
        this.mContext = context;
        this.volleyJsonListenerInterface = volleyJsonListenerInterface;
    }
    public VolleyJsonObjectListenerInterface(Context context, Response.Listener<JSONObject> responseObjListener, Response.ErrorListener errorObjListener) {
        this.mContext = context;
        this.mErrorListener = errorObjListener;
        this.JSONObject = responseObjListener;
    }
    // 请求成功时的回调函数
    public abstract void onMySuccessObj(JSONObject result);
    // 请求失败时的回调函数
    public abstract void onMyErrorObj(VolleyError error);
    // 创建请求的事件监听
    public Response.Listener<JSONObject> responseObjListener() {
        JSONObject = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject s) {
                onMySuccessObj(s);
            }
        };
        return JSONObject;
    }
    // 创建请求失败的事件监听
    public Response.ErrorListener errorObjListener() {
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onMyErrorObj(volleyError);
            }
        };
        return mErrorListener;
    }
}
