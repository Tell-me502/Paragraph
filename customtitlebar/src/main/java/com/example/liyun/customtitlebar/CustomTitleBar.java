package com.example.liyun.customtitlebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomTitleBar extends RelativeLayout {
    private Button customLeftBtn,customRightBtn;
    private TextView customTitle;
    public CustomTitleBar(Context context) {
        super(context);
    }

    /**
     * 确定自身view的宽高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 确定与夫布局相对位置，即view或子view的宽高和四个角的位置
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /**
     * 绘制视图
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 在这个构造方法中初始化控件
     * @param context
     * @param attrs
     */
    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化布局和控件
        LayoutInflater.from(context).inflate(R.layout.custom_title_bar,this,true);
        customLeftBtn=findViewById(R.id.custom_left_btn);
        customRightBtn=findViewById(R.id.custom_right_btn);
        customTitle=findViewById(R.id.custom_title);

        //获取属性值数组,将xml设置的属性值绑定
        TypedArray attributes=context.obtainStyledAttributes(attrs,R.styleable.CustomTitleBar);
        Log.i("CustomTitleBar", String.valueOf(attributes!=null));
        //判断属性数组是否为空
        if (attributes!=null){

            //处理titlebar的背景颜色,获取设置的背景颜色，如果没有设置，则默认为绿色
            int titleBarBackground=attributes.getResourceId(R.styleable.CustomTitleBar_title_background_color,Color.GREEN);
            setBackgroundResource(titleBarBackground);//设置titlebar背景颜色

            //从左到右，先处理左边的按钮属性
            //获取是否要显示左边按钮
            boolean customLeftBtnVisible=attributes.getBoolean(R.styleable.CustomTitleBar_left_button_visible,true);
            if (customLeftBtnVisible){
                customLeftBtn.setVisibility(View.VISIBLE);
            }else {
                customLeftBtn.setVisibility(View.GONE);
            }
            //设置左边按钮文字
            String customLeftBtnText=attributes.getString(R.styleable.CustomTitleBar_left_button_text);
            //判断是否有设置文字
            if (!TextUtils.isEmpty(customLeftBtnText)){
                customLeftBtn.setText(customLeftBtnText);
                //然后再设置文字的颜色
                int customLeftBtnTextColor=attributes.getColor(R.styleable.CustomTitleBar_left_button_text_color,Color.WHITE);
                customLeftBtn.setTextColor(customLeftBtnTextColor);
            }else {//如果左边按钮设置的不是文字，那只能设置icon了
                //设置左边图片icon 这里是二选一 要么只能是文字 要么只能是图片
                int customLeftBtnDrawable=attributes.getResourceId(R.styleable.CustomTitleBar_left_button_text_drawable,R.drawable.titlebar_back);
                if (customLeftBtnDrawable!=-1){//判断是否有设置图标
                    customLeftBtn.setBackgroundResource(customLeftBtnDrawable);
                }
            }

            //处理中间的标题TextView
            //先获取标题是否要显示图标icon
            int customTitleDrawable=attributes.getResourceId(R.styleable.CustomTitleBar_title_text_drawable,-1);
            if (customTitleDrawable!=-1){
                customTitle.setBackgroundResource(customTitleDrawable);
            }else{
                //如果没有设置图标，则获取文字标题
                String customTitleText=attributes.getString(R.styleable.CustomTitleBar_title_text);
                if (!TextUtils.isEmpty(customTitleText)){
                    customTitle.setText(customTitleText);
                }
                //设置标题字体颜色
                int customTitleColor=attributes.getColor(R.styleable.CustomTitleBar_title_text_color,Color.WHITE);
                customTitle.setTextColor(customTitleColor);
            }

            //处理右边按钮
            //判断是否显示右边按钮
            boolean customRightBtnVisible=attributes.getBoolean(R.styleable.CustomTitleBar_right_button_visible,true);
            if (customRightBtnVisible){
                customRightBtn.setVisibility(View.VISIBLE);
            }else {
                customRightBtn.setVisibility(View.GONE);
            }

            //设置右边按钮的文字
            String customRightBtnText=attributes.getString(R.styleable.CustomTitleBar_right_button_text);
            if (!TextUtils.isEmpty(customRightBtnText)){
                customRightBtn.setText(customRightBtnText);
                //设置文字字体颜色
                int customRightBtnColor=attributes.getColor(R.styleable.CustomTitleBar_right_button_text_color,Color.WHITE);
                customRightBtn.setTextColor(customRightBtnColor);
            }else {
                //如果没有设置文字，则设置图片，只有这两个选择
                int customRightBtnDrawable=attributes.getResourceId(R.styleable.CustomTitleBar_right_button_text_drawable,R.drawable.titlebar_add);
                if (customRightBtnDrawable!=-1){
                    customRightBtn.setBackgroundResource(customRightBtnDrawable);
                }
            }
            attributes.recycle();//最后得释放内存对象
        }

    }
    //为左右两边的按钮设置点击监听事件

    /**
     * 为左右两边的按钮分别设置点击事件
     * @param onClickListener
     */
    public void setTitleClickListener(OnClickListener onClickListener){
        if (onClickListener!=null){
            customLeftBtn.setOnClickListener(onClickListener);
            customRightBtn.setOnClickListener(onClickListener);
        }
    }

    /**
     * 获得左边按钮
     * @return
     */
    public Button getCustomLeftBtn(){
        return customLeftBtn;
    }

    /**
     * 获得右边按钮
     */
    public Button getCustomRightBtn(){
        return customRightBtn;
    }
    /**
     * 获得标题文本控件
     */
    public TextView getCustomTitle(){
        return customTitle;
    }
}
