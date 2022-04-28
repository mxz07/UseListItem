package com.example.uselistitem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MainActivityList extends ListActivity implements Runnable, AdapterView.OnItemClickListener {


    private String TAG = "myList2";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.list_item);
        Thread t = new Thread(this);
        t.start();

        //处理线程消息
        handler = new Handler(Looper.myLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {//msg为线程中的msg
                if(msg.what==5){
                    String str = (String) msg.obj;
                    Log.i("revThreadMsg",str);
                }
                super.handleMessage(msg);
            }
        };
        SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
        //构建hashmap
        ArrayList<HashMap<String,String>>listItems = new ArrayList<HashMap<String, String>>();
        for(int i=0;i<27;i++){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("huobi",sharedPreferences.getString("List"+i,"0").split("---->")[0]);
            map.put("huilv",sharedPreferences.getString("List"+i,"0").split("---->")[1]);
            listItems.add(map);
        }
        /*
        List<String> list1 = new ArrayList<String>();
        for(int i=0;i<27;i++) {
            list1.add(sharedPreferences.getString("List"+i,"0"));
        }
        */
        /*
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                listItems,
                R.layout.list_item,
                new String[]{"huobi","huilv"},
                new int[]{R.id.textView,R.id.textView2}
        );
        setListAdapter(adapter);
         */
        MyAdapter myAdapter = new MyAdapter(this,
                R.layout.list_item,
                listItems);
        this.setListAdapter(myAdapter);

        //获取到控件内容
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void run() {
        Log.i("RUN","run");
        //获取msg对像用于返回主线程
        Message msg = handler.obtainMessage();
        msg.what=5;
        msg.obj = "Hello from run()";
        handler.sendMessage(msg);

        SharedPreferences sharedPreferences = getSharedPreferences("myRate", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();

        String url = "https://www.boc.cn/sourcedb/whpj/index.html";
        List<String> list1 = new ArrayList<String>();
        String Time=sharedPreferences.getString("Time","2022/4/21");
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tables = doc.getElementsByTag("table");
            Element table1 = tables.get(1);
            Elements tds = table1.getElementsByTag("td");
            for(int i=0;i<27;i++) {
                list1.add(tds.get(i * 8).text() + "---->" + tds.get(i * 8 + 5).text());
                Log.i("data",list1.get(i));
                editor.putString("List"+i,list1.get(i));
            }
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG,"onItemClick:parent=" + adapterView);
        Log.i(TAG,"onItemClick:view=" + view);
        Log.i(TAG,"onItemClick:position=" + i);
        Log.i(TAG,"onItemClick:id=" + l);
        //点击行获得相应的数据项，从相应的数据项中取数据（map）
        HashMap<String,String> map = (HashMap<String,String>)getListView().getItemAtPosition(i);
        String titleStr=map.get("huobi");
        String detailStr=map.get("huilv");
        Log.i(TAG, titleStr+detailStr);
        //通过view
        TextView title = (TextView) view.findViewById(R.id.textView);
        TextView detail = (TextView) view.findViewById(R.id.textView2);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG, title2+detail2);

        //跳转页面，打开新的页面传入汇率
        Intent rateCalc = new Intent(this,MainActivity.class);
        rateCalc.putExtra("huobi",titleStr);
        rateCalc.putExtra("huilv",Float.parseFloat(detailStr));
        startActivity(rateCalc);


    }
}
