package com.rock.xinhuapk;


import com.lib.A;
import com.lib.Rock;
import com.lib.RockActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;


import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;

import java.io.File;
import java.util.Map;

public class WebActivity extends RockActivity {

    protected WebView webview;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessage6;
    private AndroidJs Androidjs;
    private int what_location = 5;
    private String backfun = "";
    private Map<String,String> backmap;


    public void gotoUrl(WebView web, String url)
    {
        webview = web;
        initweb();
        webview.loadUrl(url);
    }

    protected  void onChangeTitle(String title){

    }

    //查看当前url地址
    protected String getUrl()
    {
        String url = webview.getUrl();
        String params="&token="+Xinhu.admintoken+"&adminid="+Xinhu.adminid+"";
        url     = url.replace(params,"");
        return url;
    }

    protected  void handleCallback(Message msg, String bs){
        int what = msg.what;
        //定位回传
        if(what==what_location){
            String bstr = "{latitude:"+backmap.get("latitude")+",longitude:"+backmap.get("longitude")+",accuracy:"+backmap.get("accuracy")+",address:\""+backmap.get("address")+"\",provider:\""+backmap.get("provider")+"\"}";
            webview.loadUrl("javascript:"+backfun+"("+bstr+")");
        }
    }

    //停止定位
    protected void stopLocation()
    {
        Androidjs.stopLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocation();
    }

    @SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
    @JavascriptInterface
    private void initweb()
    {
        //避免输入法界面弹出后遮挡输入光标的问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        webview.setBackgroundColor(Color.argb(0, 0, 0, 0));//设置透明背景
        webview.setDrawingCacheEnabled(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true); //开启缓存
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setAllowFileAccess(true);	//可以访问文件权限
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath()+Xinhu.CACAHE_DIRNAME;
        webSettings.setAppCachePath(appCachePath);
        webSettings.setUserAgentString(webSettings.getUserAgentString()+";XINHUAPP ANDROID V"+ A.VERSION+"");

        //设置定位回传处理
        Androidjs   = new AndroidJs(this, Sqlite){
            public void Locationback(String fun, Map<String,String> a){
                backfun = fun; backmap = a;
                myhandler.sendEmptyMessage(what_location);
            }
        };
        webview.addJavascriptInterface(Androidjs, "appxinhu");

        webview.setWebViewClient(new WebViewClient(){
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                dialogXinhu.show();
            }
            public void onPageFinished(WebView view, String url){
                dialogXinhu.dismiss();
            }
        });
        webview.setWebChromeClient(new WebChromeClient(){
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openfilecheck(uploadMsg);
            }
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                openfilecheck(uploadMsg);
            }
            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                openfilecheck(uploadMsg);
            }

            @Override
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessage6 = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                startActivityForResult(Intent.createChooser(i, "文件选择"), 3);
                return true;
            }

            //标题改变时
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                onChangeTitle(title);
            }

            /**
             * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
             */
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("对话框").setMessage(message).setPositiveButton("确定", null);

                // 不需要绑定按键事件
                // 屏蔽keycode等于84之类的按键
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode,KeyEvent event) {
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;
                // return super.onJsAlert(view, url, message, result);
            }
            public boolean onJsBeforeUnload(WebView view, String url,String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }
        });
    }

    //选择文件的
    private void openfilecheck(ValueCallback<Uri> uploadMsg)
    {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        this.startActivityForResult(Intent.createChooser(i, "文件选择"), 2);
    }


    protected  void ActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==2) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        if(requestCode==3) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadMessage6 != null) {
                if (result != null) {
                    mUploadMessage6.onReceiveValue(new Uri[]{result});
                }else{
                    mUploadMessage6.onReceiveValue(new Uri[]{});
                }
                mUploadMessage6 = null;
            }
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();// 返回前一个页面
            return true;
        }
        this.finish();
        return super.onKeyDown(keyCode, event);
    }
}