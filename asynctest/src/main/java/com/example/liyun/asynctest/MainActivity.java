package com.example.liyun.asynctest;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MyAsync mTask;

    private Button start,cancel;
    public TextView mLoadingText;
    public ProgressBar mPbLoading;

    /**
     * 创建AsyncTask子类
     * 注：
     * a. 继承AsyncTask类
     * b. 为3个泛型参数指定类型；若不使用，可用java.lang.Void类型代替
     *    此处指定为：输入参数 = String类型、执行进度 = Integer类型、执行结果 = String类型
     *  c. 根据需求，在AsyncTask子类内实现核心方法
     *方法执行顺序：execute()->onPreExecute()->doInBackground()->publishProgress(count)->
     *              onPostExecute(s)->onProgressUpdate(values)->onCancelled()
     */
    public class MyAsync extends AsyncTask<String,Integer,String> {

        /**
         * 执行 线程任务前的操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingText.setText("加载中！");
            //执行前显示提示
        }


        /**
         * 接收参数，执行任务中的耗时操作，返回 线程任务执行的结果
         *此处通过计算从而模拟“加载进度”的情况
         * @param strings
         * @return
         */
        @Override
        protected String doInBackground(String... strings) {

            try {
                int count=0;
                int length=1;
                while (count<99) {
                    count += length;
                    //可调用publishProgress（）获取进度, 之后将执行onProgressUpdate（）更新UI显示进度
                    publishProgress(count);
                    //模拟耗时操作
                    Thread.sleep(30);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            return "加载完毕！";
        }


        /**
         * 在主线程显示异步线程任务执行的进度
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mPbLoading.setProgress(values[0]);
            mLoadingText.setText("Loading"+values[0]+"%");
        }


        /**
         * 接收线程任务执行的结果，将线程返回的结果显示更新到UI上
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLoadingText.setText(s);
        }

        /**
         * 将异步任务设置为：取消状态
         * 注意：取消并不是销毁此异步线程。
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            mLoadingText.setText("已取消！");
            mPbLoading.setProgress(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=findViewById(R.id.btn_start);
        cancel=findViewById(R.id.btn_cancel);
        mLoadingText=findViewById(R.id.tv_loading);
        mPbLoading=findViewById(R.id.pb_loading);

        /**
         * AsyncTask子类的实例必须在UI线程中创建
         */
        mTask=new MyAsync();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 手动调用execute(Params... params) 从而执行异步线程任务
                 * a. 必须在UI线程中调用
                 *  b. 同一个AsyncTask实例对象只能执行1次，若执行第2次将会抛出异常
                 *  c. 执行任务中，系统会自动调用AsyncTask的一系列方法：onPreExecute() 、doInBackground()、onProgressUpdate() 、onPostExecute()
                 *   d. 不能手动调用上述方法
                 */
                mTask.execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTask.cancel(true);
            }
        });
    }

    public TextView getmLoadingText() {
        return mLoadingText;
    }

    public ProgressBar getmPbLoading() {
        return mPbLoading;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask!=null){
            mTask.onCancelled();
        }
    }
}
