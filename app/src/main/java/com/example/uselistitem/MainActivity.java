package com.example.uselistitem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    float rate = 0f;
    String title ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title=getIntent().getStringExtra("huobi");
        rate=getIntent().getFloatExtra("huilv",0f);
        Log.i("TAG", title+rate);
        ((TextView)findViewById(R.id.huobi)).setText(title);
        ((TextView)findViewById(R.id.huilv)).setText(rate+"");
    }
    //处理汇率转换事件
    public void change(View v){
        EditText edit=(EditText) findViewById(R.id.rmb);
        TextView res=(TextView)findViewById(R.id.xianshi);
        Log.i("main","onClick Btn");
        String strInputRMB=edit.getText().toString();
        if (strInputRMB.length() == 0){
            res.setText("请输入金额！");
            return;
        }
        double inputRMB=Float.parseFloat(strInputRMB);
        res.setText(String.format("结果为"+"%.2f",inputRMB*inputRMB));
    }
}