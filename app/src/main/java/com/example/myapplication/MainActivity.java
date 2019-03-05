package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    tv.setText(msg.obj.toString());
                    break;
            }
        }
    };

    // json请求地址
    public String stringUrl = "https://m2.qiushibaike.com/article/list/suggest?page=1";
    private Button btn1, btn2;
    private TextView tv;
    public ByteArrayOutputStream baos;
    public BufferedInputStream bis;
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        tv = findViewById(R.id.tv);
        recyclerView = findViewById(R.id.recyclerView);
        setRecyclerView();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyThread();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyAsyncTask();
            }
        });
    }

    private void setRecyclerView() {
        List mDatas = new ArrayList<String>();
        for (int i = 0; i < 40; i++) {
            mDatas.add("item" + i);
        }
        MyRecyclerAdapter recycleAdapter= new MyRecyclerAdapter(MainActivity.this , mDatas );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置Adapter
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void startMyAsyncTask() {
        new MyAsyncTask(this).execute(stringUrl);
    }

    private void startMyThread() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(stringUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(5000);
                    if (httpURLConnection.getResponseCode() == 200) {
                        InputStream is = httpURLConnection.getInputStream();

                        byte[] buffer = new byte[is.available()];
                        StringBuffer sb = new StringBuffer();
                        while (is.read(buffer) != -1) {
                            sb.append(new String(buffer, "utf-8"));
                        }
                        Message message = new Message();
                        message.what = 1;
                        message.obj = sb;
                        handler.sendMessage(message);
                        Log.e("-----------success", sb.toString());
                        is.close();
                    } else {
                        Log.e("-----------------!200:", String.valueOf(httpURLConnection.getResponseCode()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("-----------------e", e.toString());
                }
            }
        }.start();
    }


    class MyAsyncTask extends AsyncTask<String, Void, byte[]> {

        public Context context;

        public MyAsyncTask(Context context) {
            this.context = context;

        }

        @Override
        protected byte[] doInBackground(String... params) {
            try {
                // 网络请求
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url
                        .openConnection();
                // 缓存
                baos = new ByteArrayOutputStream();
                // 网络请求是否成功
                if (conn.getResponseCode() == 200) {
                    // 获得输入流
                    bis = new BufferedInputStream(conn.getInputStream());

                    // 流的读写
                    int a = 0;
                    byte[] buffer = new byte[1024 * 8];
                    // 循环读取
                    while ((a = bis.read(buffer)) != -1) {
                        baos.write(buffer, 0, a);
                        // 保证读写的完成
                        baos.flush();
                    }
                    Log.d("------------aa:", baos.toString());
                    return baos.toByteArray();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (baos != null) {
                        baos.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return null;
        }

        //业务处理,处理返回的Jason字符串
        @Override
        protected void onPostExecute(byte[] result) {
            // 先判断，返回结果为不为空
            if (result != null) {
                // 先把result转换成String
                try {
                    String data = new String(result, "utf-8");
                    Log.e("-------------aa:", data);

                    // 调用解析工具去解析
                    List<Map<String, String>> list = Tools
                            .jsonStringToList(data);
                    Log.e("-------------bb:", list.toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

}

