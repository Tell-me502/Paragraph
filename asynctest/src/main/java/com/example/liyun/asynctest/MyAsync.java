package com.example.liyun.asynctest;

import android.os.AsyncTask;

/**
 * 创建AsyncTask子类
 * 注：
 * a. 继承AsyncTask类
 * b. 为3个泛型参数指定类型；若不使用，可用java.lang.Void类型代替
 *    此处指定为：输入参数 = String类型、执行进度 = Integer类型、执行结果 = String类型
 *  c. 根据需求，在AsyncTask子类内实现核心方法
 *
 */
public class MyAsync extends AsyncTask<String,Integer,String> {
    public MainActivity main=new MainActivity();

    /**
     * 执行 线程任务前的操作
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        main.getmLoadingText().setText("加载中！");
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
        main.getmPbLoading().setProgress(values[0]);
        main.getmLoadingText().setText("Loading"+values[0]+"%");
    }


    /**
     * 接收线程任务执行的结果，将线程返回的结果显示更新到UI上
     * @param s
     */
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        main.getmLoadingText().setText(s);
    }

    /**
     * 将异步任务设置为：取消状态
     * 注意：取消并不是销毁此异步线程。
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        main.getmLoadingText().setText("已取消！");
        main.getmPbLoading().setProgress(0);
    }
}
