package com.rock.xinhuapk;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import com.lib.CLog;
import com.lib.Jiami;
import com.lib.Json;
import com.lib.Rock;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;


/**
 * 长连接服务使用websocket原理
 * */
public class WebSocketService extends Service {

    private Context mContext    = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mContext        = this.getApplicationContext();
        if(!Xinhu.WEBSOCKET_BOOL){
            Bundle bundle   = intent.getExtras();
            String config   = bundle.getString(Xinhu.SERVICETYPE_SOCKET);
            startWebsocket(config);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startWebsocket(String config)
    {
        if(Rock.isEmpt(config))return;
        Map<String,String> ret = Json.getJsonObject(config);
        String wsurl = ret.get("wsurl");
        final  String recid = ret.get("recid");
        if(Rock.isEmpt(wsurl) || Rock.isEmpt(recid))return;//没有配置服务端


        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(Jiami.base64decode(wsurl))
                .build();
        okHttpClient.newWebSocket(request, new WebSocketListener() {
            ExecutorService writeExecutor = Executors.newSingleThreadExecutor();
            WebSocket webSocket = null;

            @Override
            public void onOpen(final WebSocket webSocket, final Response response) {
                this.webSocket = webSocket;
                //建立连接成功后，发送消息给服务器端
                writeExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Xinhu.WEBSOCKET_BOOL = true;
                        webSocket.send("{\"from\":\""+recid+"_android\",\"adminid\":\""+Xinhu.adminid+"\",\"atype\":\"connect\",\"sendname\":\""+Xinhu.adminname+"\"}");
                    }
                });
            }

            @Override
            public void onMessage(final WebSocket webSocket, String text) {

                CLog.debug(text);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                CLog.debug("websocket,onClosed");
                Xinhu.WEBSOCKET_BOOL = false;
                super.onClosed(webSocket, code, reason);
                writeExecutor.shutdown();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Xinhu.WEBSOCKET_BOOL = false;
                CLog.debug("websocket,onFailure");
                super.onFailure(webSocket, t, response);
            }
        });
    }
}
