package com.rock.xinhuapk;


import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lib.CDate;
import com.lib.CLog;
import com.lib.Image;
import com.lib.Jiami;
import com.lib.Json;
import com.lib.Rock;
import com.lib.RockActivity;
import com.lib.RockFile;
import com.lib.RockHttp;
import com.lib.SqliteClass;
import com.view.ImageViewXinhu;
import com.view.TextViewChat;


import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;



public class ChatActivity extends RockActivity {

    private LinearLayout showviewobj0,upbtnobj;
    private EditText inputobj;
    private ScrollView scrollobj;
    private InputMethodManager mInputMethodManager;

    private ArrayList<View> chatview = new ArrayList<View>();

    private ArrayList<Map<String, String>> chatrecordarr = new ArrayList<Map<String, String>>();//聊天记录

    private Bitmap adminfacebit = null,recefacebit = null;
    private String nowdate = "", recechatname = "", recechatid = "",recechattype = "";

    private ChatReceiver chatReceiver;
    private TextView yybtnobj,mTextView;
    private ImageView mImageView,yuyinbtnobj,jianpanbtnobj,addobj;
    private Button sendbtnobj;

    private int showmaxid	= 0,issendyy=0,ishistory=0,showminid	= 999999999,
            what_sendcont = 3001,what_upfile = 3002,sendnnid = -1,what_scrooto=4001,
            what_downok = 1000,what_downshib=1001,
            what_autoclose=4002,what_scrllo=4003;

    private String emotsString = "[微笑],[撇嘴],[色],[发呆],[得意],[流泪],[害羞],[闭嘴],[睡],[大哭],[尴尬],[发怒],[调皮],[呲牙],[惊讶],[难过],[酷],[冷汗],[抓狂],[吐],[偷笑],[愉快],[白眼],[傲慢],[饥饿],[困],[恐惧],[流汗],[憨笑],[悠闲],[奋斗],[咒骂],[疑问],[嘘],[晕],[疯了],[衰],[骷髅],[敲打],[再见],[擦汗],[抠鼻],[鼓掌],[糗大了],[坏笑],[左哼哼],[右哼哼],[哈欠],[鄙视],[委屈],[快哭了],[阴险],[亲亲],[吓],[可怜],[菜刀],[西瓜],[啤酒],[篮球],[乒乓],[咖啡],[饭],[猪头],[玫瑰],[凋谢],[嘴唇],[爱心],[心碎],[蛋糕],[闪电],[炸弹],[刀],[足球],[瓢虫],[便便],[月亮],[太阳],[礼物],[拥抱],[强],[弱],[握手],[胜利],[抱拳],[勾引],[拳头],[差劲],[爱你],[NO],[OK],[爱情],[飞吻],[跳跳],[发抖],[怄火],[转圈],[磕头],[回头],[跳绳],[投降],[激动],[街舞],[献吻],[左太极],[右太极]";
    private Map<String,String> emotsMap = new HashMap<String, String>(),chatnow;

    private  AudioRecoderUtils mAudioRecoderUtils;
    private PopupWindowFactory mPop;
    private MediaPlayer mediaPlayer = null;

    /**
     * 初始化
     * */
    protected void initCreate() {
        setContentView(R.layout.activity_chat);
        TextView view = (TextView) findViewById(R.id.title);
        view.setText(Xinhu.recechatname);
        recechatname    = Xinhu.recechatname;
        recechatid      = Xinhu.recechatid;
        recechattype    = Xinhu.recechattype;

        msgview = (TextView) findViewById(R.id.msg);
        yybtnobj= (TextView) findViewById(R.id.yybtn);
        nowdate = CDate.date();
        findViewById(R.id.back).setOnClickListener(this.OnViewClickListener);
        findViewById(R.id.up).setOnClickListener(this.OnViewClickListener);
        findViewById(R.id.add).setOnClickListener(this.OnViewClickListener);
        findViewById(R.id.sendbtn).setOnClickListener(this.OnViewClickListener);
        findViewById(R.id.yuyinbtn).setOnClickListener(this.OnViewClickListener);
        findViewById(R.id.jianpanbtn).setOnClickListener(this.OnViewClickListener);
        inputobj 	    = (EditText) findViewById(R.id.content);
        sendbtnobj 	    = (Button) findViewById(R.id.sendbtn);
        showviewobj0 	= (LinearLayout) findViewById(R.id.showview0);
        upbtnobj 	    = (LinearLayout) findViewById(R.id.up);
        scrollobj 	    = (ScrollView) findViewById(R.id.scrollviewss);
        yuyinbtnobj 	    = (ImageView) findViewById(R.id.yuyinbtn);
        jianpanbtnobj 	    = (ImageView) findViewById(R.id.jianpanbtn);
        addobj 	            = (ImageView) findViewById(R.id.add);
        showviewobj0.setOnClickListener(this.OnViewClickListener);

        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        //Rock.setStatusBar(this, R.id.headertopzt); //设置沉淀会挡着输入框？

        inputobj.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {  }
            @Override
            public void afterTextChanged(Editable editable) {
                changeinput();
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(Xinhu.ACTION_CHATRECEMESS);
        filter.addAction(Xinhu.ACTION_ALLCLOSE);
        chatReceiver		= new ChatReceiver();
        registerReceiver(chatReceiver, filter);
        initemotss();
        loadrecord(0);


        scrollobj.setOnTouchListener(new ScrollView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP){
                    scrolltiao(false);
                }
                return false;
            }
        });

        mAudioRecoderUtils = new AudioRecoderUtils();
        yybtnobj.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return yybtnonTouch(event);
            }
        });

        //录音回调
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecoderUtils.OnAudioStatusUpdateListener() {

            //录音中....db为声音分贝，time为录音时长
            @Override
            public void onUpdate(double db, long time) {
                //根据分贝值来设置录音时话筒图标的上下波动
                mImageView.getDrawable().setLevel((int) (3000 + 6000 * db / 100));
                mTextView.setText(CDate.long2String(time));
            }

            //录音结束，filePath为保存路径，audioTime为录音时长
            @Override
            public void onStop(String filePath, long audioTime) {
                endluyunjst(filePath, audioTime);
               // Rock.alert(mActivity, "录音保存在：" + filePath);
                mTextView.setText(CDate.long2String(0));
            }
        });
        View vav = Rock.getView(this,R.layout.layout_microphone);
        mPop = new PopupWindowFactory(this,vav);
        //PopupWindow布局文件里面的控件
        mImageView = (ImageView) vav.findViewById(R.id.iv_recording_icon);
        mTextView = (TextView) vav.findViewById(R.id.tv_recording_time);
    }



    private void scrolltiao(Boolean bo)
    {
        if(!bo) {
            RunTimer(what_scrllo, 500);
        }else{
            int y = scrollobj.getScrollY();
            if(y==0 && ishistory>0){
                upbtnobj.setVisibility(View.VISIBLE);
            }else{
                upbtnobj.setVisibility(View.GONE);
            }
        }
    }

    private void scrollback()
    {
        loadrecord(3); //加载历史聊天记录
    }

    /*
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_MOVE){
            fanyeshow(event);
        }
        return super.dispatchTouchEvent(event);
    }

    public void fanyeshow(MotionEvent event){
        int y	= (int)event.getY();

        TextView view = (TextView) findViewById(R.id.title);
        view.setText(""+y+"");
    }*/


    private  void initemotss()
    {
        int oj = 1;
        String[] arr = emotsString.split(",");
        for(String fst:arr){
            emotsMap.put(fst, oj+"");
            oj++;
        }
    }

    private void hidejianpan()
    {
        // 隐藏输入法
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(inputobj.getWindowToken(), 0);
        }
        inputobj.clearFocus();
    }


    protected void ViewClick(View v) {
        int id = v.getId();
        if(id == R.id.back){
            finish();
        }
        if(id == R.id.sendbtn){
            send();
        }
        if(id == R.id.up){
            scrollback();
        }
        if(id == R.id.add){
            showaddmenu();
        }
        if(id == R.id.showview0){
            hidejianpan();
        }

        if(id == R.id.filesendbtn){
            sendfileque();
        }

        if(id == R.id.yuyinbtn){
            fayuyin();
        }
        if(id == R.id.jianpanbtn){
            fayuyin();
        }
    }




    //发送消息
    private void send()
    {
        String cont = inputobj.getText().toString();
        if(Rock.isEmpt(cont) || sendnnid>0 || issendyy==1)return;


        Map<String, String> a = new HashMap<String, String>();
        String conts = Jiami.base64encode(cont), optdt = CDate.getserverdt();
        a.put("sendid", Xinhu.adminid);
        a.put("cont", conts);
        a.put("face", Xinhu.adminmap.get("face"));
        a.put("optdt", optdt);

        sendnnid = addchatlist(a,2);
        inputobj.setText("");
        this.sendconts(conts,optdt,"0");

        scrooltobottom(0);
    }

    //发送消息内容
    private void sendconts(String conss, String optdt, String fid){
        String[] params = new String[]{"cont",conss,"gid",recechatid,"nuid","","type",recechattype,"optdt", optdt, "fileid", fid};
        Xinhu.ajaxpost("reim","sendinfor", params,myhandler, what_sendcont);
    }
    //发送完成回传
    private void sendcontsback(String bstr, int lcode)
    {
        TextView av = (TextView) chatview.get(sendnnid).findViewById(R.id.msg);
        if(lcode==0){
            av.setText("失败");
            av.setTextColor(Color.RED);
        }else{
            //Rock.alert(this,bstr);
            av.setText("已发");
            av.setTextColor(Color.parseColor("#33AA55"));
            Xinhu.recechatsend = "1";
        }
        sendnnid = -1;
    }


    //滚动到最底部
    private void scrooltobottom(int lx)
    {
        if(lx==1){
            scrollobj.scrollTo(0, showviewobj0.getHeight());
        }
        if(lx==0)RunTimer(what_scrooto,100);
    }

    //加到列表中
    private int addchatlist(Map<String, String> a, int lx){
        return addchatlist(a,lx,"text");
    }
    private int addchatlist(Map<String, String> a, int lx, String stype)
    {
        Boolean ismy= Rock.equals(a.get("sendid"), Xinhu.adminid);
        if(lx==1 && ismy)return 0; //如果是接收到信息不加载我发的

        chatrecordarr.add(a);
        int xu = chatrecordarr.size()-1;
        View view;
        Bitmap bitface = null;
        String optdt   = a.get("optdt"),dtstr = "",fileid=a.get("fileid");
        if(fileid==null)fileid = "0";
        int viewid  = R.layout.chat_right_text;


        if(ismy){
            bitface = adminfacebit;
            dtstr   = "我";
        }else{
            viewid  = R.layout.chat_left_text;
            if(Rock.equals(Xinhu.recechattype, Xinhu.CHAT_USERVAL))bitface = recefacebit; //只有是单人聊天才使用头像
            dtstr   = a.get("sendname");
        }
        if(Rock.contain(optdt, nowdate)){
            dtstr+="("+optdt.substring(11)+")";
        }else{
            dtstr+="("+optdt.substring(5,16)+")";
        }

        int slx = 0;
        long yuchang = 0;

        //文件类型的
        String[] frs = {"","","","","","",""};
        String thumbpath = "",downpath="";
        if(!Rock.equals(fileid,"0")) {
            frs = Sqlite.getone(SqliteClass.tn_file,"`id`="+fileid+"", "`id`,`fileext`,`filename`,`filepath`,`thumbpath`,`filesizecn`,`downpath`,`filesize`");
            if(frs!=null){
                if(RockFile.isimg(frs[1])) {
                    if (!ismy) {
                        viewid = R.layout.chat_left_img;
                    } else {
                        viewid = R.layout.chat_right_img;
                    }
                    slx = 1;
                    thumbpath = frs[4];
                }else if(Rock.equals(frs[1], "amr")){
                    if (!ismy) {
                        viewid = R.layout.chat_left_yuyin;
                    } else {
                        viewid = R.layout.chat_right_yuyin;
                    }
                    slx = 3;
                    yuchang = Long.parseLong(frs[7]);
                    chatrecordarr.get(xu).put("downpath", frs[6]);
                    downpath = frs[6];
                }else{
                    if(!ismy){
                        viewid  = R.layout.chat_left_file;
                    }else{
                        viewid  = R.layout.chat_right_file;
                    }
                    slx     = 2;//文件的展示
                }
            }
        }
        boolean sfilebo = Rock.equals(stype, "file");
        boolean syuyinbo = Rock.equals(stype, "yuyin");
        if(sfilebo){
            viewid = R.layout.chat_right_sendfile;
        }

        if(syuyinbo){
            viewid = R.layout.chat_right_yuyin;
            yuchang= Long.parseLong(a.get("filesize"));
        }

        view = Rock.getView(this, viewid);
        ImageView imgv;
        TextView av;

        //设置头像
        if(bitface != null){
            imgv = (ImageView) view.findViewById(R.id.icons);
            imgv.setImageBitmap(bitface);
        }

        //是文字就设置显示内容
        if(slx==0 && !sfilebo && !syuyinbo) {
            TextViewChat avs = (TextViewChat) view.findViewById(R.id.content);
            avs.setTextChat(this,Jiami.base64decode(a.get("cont")), emotsMap);
        }

        final int oi = xu;

        av = (TextView) view.findViewById(R.id.dt);
        av.setText(dtstr);

        av = (TextView) view.findViewById(R.id.xu);
        av.setText(xu+"");

        //发送中显示
        if(lx==2) {
            av = (TextView) view.findViewById(R.id.msg);
            av.setVisibility(View.VISIBLE);

            if(sfilebo){
                av = (TextView) view.findViewById(R.id.fileext);
                av.setText(a.get("fileext"));

                av = (TextView) view.findViewById(R.id.filename);
                av.setText(a.get("filename"));

                av = (TextView) view.findViewById(R.id.filesizecn);
                av.setText(a.get("filesizecn"));
            }
        }

        //语音秒数
        if(yuchang>0){
            av = (TextView) view.findViewById(R.id.yuyintime);
            String st11 = ""+((int)(yuchang/1000))+"";
            av.setText(""+st11+"\"");

            view.findViewById(R.id.content).setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    openyuyin(oi);
                }
            });
            //未读的语音显示红点
            if(!ismy && Rock.isEmpt(downpath)){
                view.findViewById(R.id.msgwei).setVisibility(View.VISIBLE);
            }
        }


        //文件显示
        if(slx==2){
            av = (TextView) view.findViewById(R.id.fileext);
            av.setText(frs[1]);

            av = (TextView) view.findViewById(R.id.filename);
            av.setText(frs[2]);

            av = (TextView) view.findViewById(R.id.filesizecn);
            av.setText(frs[5]);

            view.findViewById(R.id.content).setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    openfileview(oi);
                }
            });
        }


        registerForContextMenu(view);//注册长按事件

        chatview.add(view);
        if(bitface == null) donwfaces(xu, a.get("face"),a.get("sendid")); //下载头像

        //下载发送过来的图片
        if(slx==1 && !Rock.isEmpt(thumbpath)){
            view.findViewById(R.id.content).setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    openimgview(oi);
                }
            });
            ImageViewXinhu imgvs = (ImageViewXinhu) view.findViewById(R.id.content);
            imgvs.setPath(thumbpath);
            chatrecordarr.get(xu).put("thumbpath", thumbpath);
            chatrecordarr.get(xu).put("filepath", frs[3]);
            chatrecordarr.get(xu).put("downpath", frs[6]);
        }

        if(lx==0 || lx==3){
            showviewobj0.addView(view,0);
        }else{
            showviewobj0.addView(view);
        }
        String idss = a.get("id");
        if(idss!=null) {
            int ids = Integer.parseInt(idss);
            if (ids > showmaxid) showmaxid = ids;
            if (ids < showminid) showminid = ids;
        }
        return xu;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        TextView av = (TextView) v.findViewById(R.id.xu);
        int i 		= Integer.parseInt(av.getText().toString());
        chatnow     = chatrecordarr.get(i);

        menu.add(0, 0, Menu.NONE, "复制");
        menu.add(0, 1, Menu.NONE, "查看资料");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==0){
            Rock.copy(this, Jiami.base64decode(chatnow.get("cont")));
            Rock.Toast(this, "已复制");
        }
        if(id==1){
            Xinhu.startActivity(this,UserActivity.class, chatnow.get("sendname"), chatnow.get("sendid"));
        }
        return super.onContextItemSelected(item);
    }


    //下载头像
    private void donwfaces(int i, String face, String sendid)
    {
        Bitmap bitmap = Xinhu.downface(face,myhandler, i);
        if(bitmap != null){
            ImageView imgv = (ImageView)chatview.get(i).findViewById(R.id.icons);
            imgv.setImageBitmap(bitmap);
            if(Rock.equals(sendid, Xinhu.adminid)) {
                adminfacebit = bitmap; //自己头像
            }else{
                recefacebit  = bitmap; //他人的
            }
        }
    }
    private void downface(int i, String str)
    {
        Map<String, String> a = chatrecordarr.get(i);
        Map<String, String> b = Json.getJsonObject(str);
        String nface 	   = RockFile.saveFiles(b.get("result"), b.get("path"));
        if(!Rock.isEmpt(nface)){
            ImageView imgv  = (ImageView)chatview.get(i).findViewById(R.id.icons);
            Bitmap bitmap   = BitmapFactory.decodeFile(nface);
            if(Rock.equals(a.get("sendid"), Xinhu.adminid)) {
                adminfacebit = bitmap; //自己头像
            }else{
                recefacebit  = bitmap;
            }
            imgv.setImageBitmap(bitmap);
        }
    }

    private void addcentmsg(String msg, int lx, int wz)
    {
        View view = Rock.getView(this, R.layout.chat_center);
        TextView av = (TextView) view.findViewById(R.id.content);
        av.setText(msg);
        if(lx==3){
            showviewobj0.addView(view,wz);
        }else {
            showviewobj0.addView(view);
        }
    }

    /**
     * 加载本地的聊天记录
     * lx 0 第一次,1收到消息，2加载历史
     * */
    private void loadrecord(int lx)
    {
        String [] fields= SqliteClass.tn_recordfields;
        String fstr = "",where="",order="order by `id` desc limit 5";
        for(String fstr1:fields)fstr+=",`"+fstr1+"`";
        where=" and `receid`='"+recechatid+"'";
        if(Rock.equals(recechattype, Xinhu.CHAT_USERVAL))where="and ( (`sendid`="+Xinhu.adminid+" and `receid`="+recechatid+") or (`sendid`="+recechatid+" and `receid`="+Xinhu.adminid+"))";
        if(lx==1){
            where+=" and id>"+showmaxid+"";
            order="order by `id`";
        }
        //上拉加载历史记录
        if(lx==3){
            where+=" and id<"+showminid+"";
            order="order by `id` desc limit 10";
        }
        String wheress   = "`type`='"+Xinhu.recechattype+"' "+where+"";
        String [][] rows = Sqlite.getall("select "+fstr.substring(1)+" from "+SqliteClass.tn_chatrecord+" where "+wheress+" "+order+"");
        if(rows!=null) {
            int i, len = rows.length,j=0;
            for(i=0;i<len;i++){
                Map<String, String> a = new HashMap<String, String>();
                j = 0;
                for(String fstr1:fields){
                    a.put(fstr1, rows[i][j]);
                    j++;
                }
                addchatlist(a, lx);
            }
            if(lx==3){
                addcentmsg("------------↑以上是新加载"+len+"条历史记录------------", lx, len);
            }
            if(lx==0){
                addcentmsg("------------↑以上是历史记录------------", lx,0);
            }
            ishistory = Sqlite.rows(SqliteClass.tn_chatrecord, "`id`<"+showminid+" and "+wheress+"");//判断是否还有历史记录
            if(lx<=2)scrooltobottom(0);
            scrolltiao(true);
        }
    }



    //异步回调处理的
    protected  void handleCallback(Message msg, String bstr)
    {
        int what = msg.what,wlcode = msg.arg1;
        if(what==what_autoclose){

        }
        //语言下载
        if(what==what_downok){
            downyuyinok();
            return;
        }

        if(what==what_scrooto){
            scrooltobottom(1);
        }
        if(what==what_scrllo){
            scrolltiao(true);
        }

        //发消息回复
        if(what==what_sendcont) {
            sendcontsback(bstr, wlcode);
        }
        //网络请求失败
        if(what>3000 && what<4000 && wlcode==0){
            msgtishi(bstr,"#ff0000",10);
            sendnnid = -1;//标识没在发送
            return;
        }

        //下载会话列表的头像
        if(what<2000){
            downface(what, bstr);
        }

        //上传文件回传
        if(what==what_upfile){
            sendfileok( bstr);
        }
        //进度条
        if(what==what_upfile+1){
            sendfileprogress(bstr);
        }
    }

    @Override
    protected void onDestroy(){
        unregisterReceiver(chatReceiver);//注销广播
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopmediaPlayer(1);
        RunnableCancel();
        super.onStop();
    }

    //聆听推送过来的消息
    public class ChatReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action   = intent.getAction(),title,content;
            Bundle bundle   = intent.getExtras();
            //Rock.Toast(context, "chataction："+action+"");

            //有收到消息
            if(action.equals(Xinhu.ACTION_CHATRECEMESS)) {
                loadrecord(1);
            }

            //接收到关闭命令
            if(action.equals(Xinhu.ACTION_ALLCLOSE)){
                finish();
            }
        }
    }

    //图片查看器
    private  void openimgview(int oi)
    {
        Map<String, String> a = chatrecordarr.get(oi);
        DownLoad.init(this,Sqlite);
        DownLoad.openimageview(a.get("fileid"));
    }

    //打开文件
    private void openfileview(int oi)
    {
        Map<String, String> a = chatrecordarr.get(oi);
        String fileid     = a.get("fileid");
        DownLoad.openfile(this, fileid);
    }

    //打开语音
    private ImageView payview;
    private Boolean ismybofan,downbool=false;
    private Integer optshov;
    private void openyuyin(int oi)
    {
        if(downbool)return;
        optshov = oi;
        Map<String, String> a = chatrecordarr.get(oi);
        String downpath = a.get("downpath");
        String fileid   = a.get("fileid");
        ismybofan= Rock.equals(a.get("sendid"), Xinhu.adminid);
        if(!ismybofan) {
            chatview.get(oi).findViewById(R.id.msgwei).setVisibility(View.GONE);
        }
        if(!Rock.isEmpt(fileid) && Rock.isEmpt(downpath)){
            downbool = true;
            downyuyin(oi, fileid);//去下载语音
            return;
        }
        bofangamr(oi, downpath);
    }
    private Map<String,String> fMap = null;
    private void downyuyin(int oi, String fileid)
    {
        TextView tv = (TextView)chatview.get(oi).findViewById(R.id.msg);
        tv.setVisibility(View.VISIBLE);
        tv.setText("下载中");
        DownLoad.start(this,Sqlite,fileid, null);
        DownLoad.startdown(new CallBack(){
            public void back(){
                downbool=false;
                myhandler.sendEmptyMessage(what_downok);
            }
        });
    }
    private void downyuyinok()
    {
        fMap    = DownLoad.getfMap();
        String  downpath = fMap.get("downpath");
        TextView tv = (TextView)chatview.get(optshov).findViewById(R.id.msg);
        if(Rock.isEmpt(downpath)){
            tv.setText("无法下载");
        }else {
            tv.setVisibility(View.GONE);
            chatrecordarr.get(optshov).put("downpath", downpath);
            bofangamr(optshov, downpath);
        }
    }
    private void bofangamr(int oi, String downpath)
    {
        if(!Rock.isEmpt(downpath)){
            File file = new File(downpath);
            if (!file.exists()){
                msgtishi("语音文件已被清理了");
                return;
            }
        }else{
            CLog.debug("空路径");
            return;
        }
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
            return;
        }
        payview = (ImageView) chatview.get(oi).findViewById(R.id.yuyinimg);
        mediaPlayer = new MediaPlayer();
        if(mediaPlayer.isPlaying())return;
        try {
            mediaPlayer.setDataSource(downpath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            dingshichuli(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void dingshichuli(final int lxs)
    {
        int lx = lxs;
        if(mediaPlayer==null || !mediaPlayer.isPlaying())lx=2;
        if(lx>0){
            int vieid = R.drawable.yuright0;
            if(!ismybofan)vieid = R.drawable.yuleft1;
            if(lx % 2==0){
                vieid = R.drawable.yuright1;
                if(!ismybofan)vieid = R.drawable.yuleft0;
            }
            payview.setImageResource(vieid);
        }
        //播放结束了
        if(mediaPlayer!=null && !mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        if(mediaPlayer==null)return;
        RunnableTime(new Runnable(){
            @Override
            public void run() {
                dingshichuli(lxs+1);
            }
        }, 500);
    }
    private void stopmediaPlayer(int lx)
    {

        //播放没结束就退出页面
        if(lx==1 && mediaPlayer!=null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }


    private PopupMenu popupMenu = null;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showaddmenu()
    {
        if(popupMenu!=null)popupMenu.dismiss();
        View v 		= (View)findViewById(R.id.add);
        popupMenu 	= new PopupMenu(this, v);
        Menu menu 	= popupMenu.getMenu();
        menu.add(0,0,0,"拍照");
        menu.add(0,1,0,"发文件");
       // menu.add(0,2,0,"定位");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onOptionsSelected(item);
                return false;
            }
        });
        popupMenu.show();
    }

    private void onOptionsSelected(MenuItem item) {
        switch(item.getItemId()) {
            case 0:
                changepaizhao();
                break;
            case 1:
                changefile();
                break;
            case 3:
                fayuyin();
                break;
        }
    }

    private String cappath = "",capfname = "";
    private void changepaizhao()
    {
        if(sendnnid>-1)return;
        // 启动相机
        Intent intent1  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cappath         = RockFile.getDir("capture");
        capfname        = "cap"+CDate.gettime()+".jpg";
        File file       = new File(cappath, capfname);
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file)); //设置图片存储路径
        startActivityForResult(intent1, 2);
    }

    private void changefile()
    {
        if(sendnnid>-1)return;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "选择文件"), 4);
    }

    protected  void ActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            if(requestCode==2){
                String soupath = Image.squashSize(cappath+"/"+capfname+"");//生成简单图片
                showchatfile(soupath);
            }
            if(requestCode==4){
                Uri uri = data.getData();
                String path = RockFile.getChangePath(this, uri);
                if(Rock.isEmpt(path)){
                    msgerror("无法选择文件");
                }else {
                    showchatfile(path);
                }
            }
        }
    }

    private Integer sendfiletype = 0;
    private long audioTimesj  = 0;
    private void endluyunjst(String spath, long audioTime)
    {
        //不到1秒
        if(audioTime<1000){
            msgtishi("录音时间太短");
            File file = new File(spath);
            if (file.exists()) file.delete();
            return;
        }
        cappath         = spath;  //路径
        audioTimesj     = audioTime;
        //CLog.debug("保存在:"+filePath+","+audioTime+"");
        Map<String, String> a = new HashMap<String, String>();
        String conts = Jiami.base64encode("语音"), optdt = CDate.getserverdt();
        a.put("sendid", Xinhu.adminid);
        a.put("cont", conts);
        a.put("face", Xinhu.adminmap.get("face"));
        a.put("optdt", optdt);
        a.put("filepath", spath);
        a.put("downpath", spath);
        a.put("fileext", "amr");
        a.put("filesize", ""+audioTime+"");
        sendnnid = addchatlist(a, 2, "yuyin");
        scrooltobottom(0);
        sendfiletype = 1;
        String upurl = Xinhu.getapiurl("upload","upfileapp");
        RockHttp.upload(upurl+"&filesize="+audioTime+"", cappath , myhandler, what_upfile);
    }

    //显示要发送的文件
    private void showchatfile(String spath)
    {
        cappath               = spath;  //路径
        Map<String, String> a = new HashMap<String, String>();
        String conts = Jiami.base64encode("文件"), optdt = CDate.getserverdt();
        a.put("sendid", Xinhu.adminid);
        a.put("cont", conts);
        a.put("face", Xinhu.adminmap.get("face"));
        a.put("optdt", optdt);
        a.put("filepath", spath);
        a.put("filename", RockFile.getFilename(spath));
        a.put("fileext", RockFile.getExt(spath));
        a.put("filesizecn", RockFile.getFilesizecn(spath));
        sendnnid = addchatlist(a,2, "file");
        scrooltobottom(0);

        View view = chatview.get(sendnnid);
        view.findViewById(R.id.filesendbtn).setOnClickListener(this.OnViewClickListener);

        TextView av = (TextView)view.findViewById(R.id.msg);
        av.setText("确认中");
    }

    //确认发送文件
    private void sendfileque()
    {
        Map<String, String> a = chatrecordarr.get(sendnnid);
        Dialog.confirm(this, "当前文件大小："+a.get("filesizecn")+"是否继续发送");
        Dialog.setTitle("发送确认");
        Dialog.setCancelbtn("不发送",new CallBack(){
            public void back(){
                showviewobj0.removeView(chatview.get(sendnnid));
                chatview.set(sendnnid, null);
                chatrecordarr.set(sendnnid, null);
                sendnnid = -1;
            }
        });
        Dialog.setOkbtn("确认",new CallBack(){
            public void back(){
                sendfileokque();
            }
        });
    }

    //确认发送
    private void sendfileokque()
    {
        View view = chatview.get(sendnnid);
        view.findViewById(R.id.filesendbtn).setVisibility(View.GONE);
        TextView av = (TextView)view.findViewById(R.id.msg);
        av.setText("处理中");
        sendfiletype = 0;
        RockHttp.upload(Xinhu.getapiurl("upload","upfileapp"), cappath , myhandler, what_upfile);
    }
    //发送文件进度条显示
    private void sendfileprogress(String bstr)
    {
        if(sendfiletype!=0)return;
        Map<String, String> b = Json.getJsonObject(bstr);
        View view = chatview.get(sendnnid);
        ProgressBar proobj = (ProgressBar) view.findViewById(R.id.progress);
        proobj.setProgress(Integer.parseInt(b.get("bili")));
    }
    //发送完成
    private void sendfileok(String bstr)
    {
        Map<String, String> b = Json.getJsonObject(bstr);
        DownLoad.init(this, Sqlite);
        DownLoad.savefilerecord(bstr);//保存文件记录
        String ustring = "";
        if(sendfiletype==1)ustring=",`filesize`='"+audioTimesj+"'";//语音时保存
        Sqlite.update(SqliteClass.tn_file,"`downpath`='"+cappath+"'"+ustring+"", "`id`="+b.get("id")+"");
        String conss = "";
        if(RockFile.isimg(b.get("fileext"))){
            conss = "[图片 "+b.get("filesizecn")+"]";
        }else{
            conss = "["+b.get("filename")+" "+b.get("filesizecn")+"]";
        }
        if(sendfiletype==1)conss = "[语音]";
        this.sendconts(Jiami.base64encode(conss),b.get("adddt"),b.get("id"));
    }

    //录入是监听
    private void changeinput()
    {
        String str = inputobj.getText().toString();
        if(Rock.isEmpt(str)){
            sendbtnobj.setVisibility(View.GONE);
            addobj.setVisibility(View.VISIBLE);
        }else{
            sendbtnobj.setVisibility(View.VISIBLE);
            addobj.setVisibility(View.GONE);
        }
    }

    //发语音
    private void fayuyin()
    {
        if(issendyy==0) {
            hidejianpan();
            yybtnobj.setVisibility(View.VISIBLE);
            inputobj.setVisibility(View.GONE);
            yuyinbtnobj.setVisibility(View.GONE);
            jianpanbtnobj.setVisibility(View.VISIBLE);
            yybtnobj.setText(getResources().getString(R.string.yyin_btn));
            issendyy = 1;
        }else{
            yybtnobj.setVisibility(View.GONE);
            inputobj.setVisibility(View.VISIBLE);
            yuyinbtnobj.setVisibility(View.VISIBLE);
            jianpanbtnobj.setVisibility(View.GONE);
            issendyy = 0;
        }
    }
    private Boolean yybtnonTouch(MotionEvent event)
    {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        int evlx = event.getAction();
        if(evlx==MotionEvent.ACTION_DOWN){
            mPop.showAtLocation(rl, Gravity.CENTER,0,0);
            yybtnobj.setText(getResources().getString(R.string.yyin_btns));
            mAudioRecoderUtils.startRecord();
            return true;
        }
        if(evlx==MotionEvent.ACTION_UP || evlx==MotionEvent.ACTION_CANCEL){
            yybtnobj.setText(getResources().getString(R.string.yyin_btn));
            mAudioRecoderUtils.stopRecord();//保存录音
            mPop.dismiss();
        }
        return false;
    }
}
