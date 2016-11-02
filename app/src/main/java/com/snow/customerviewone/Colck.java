package com.snow.customerviewone;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Calendar;

import static com.snow.customerviewone.R.styleable.View;

/**
 * Created by snow on 2016/11/1.
 */

public class Colck extends View {


    private float mRadius;//外圆半径
    private float mTextSize;//文字大小
    private float mPadding; //边距
    private float mHourPointerWidth;//时针宽度
    private float mMintuePointerWidth;//分针宽度;
    private float mSecondPointerWidth;//秒针宽度;
    private int mPointRadius;//指针的圆角
    private float mPointEndLength;//指针末尾的长度

    private int mColorLong;//长线的颜色
    private int mColorShort;//短线的颜色
    private int mHourPointerColor;//时针的颜色
    private int mMinutePointerColor;//分针的颜色
    private int mSecondPointerColor;//秒针的颜色

    private Paint mPaint;

    public Colck(Context context) {
        super(context);
    }

    public Colck(Context context, AttributeSet attrs) {
        super(context, attrs);
        obtainStyleAttrs(attrs);
        init();

    }


    private void obtainStyleAttrs(AttributeSet attrs) {
        TypedArray array = null;
        try {

            array = getContext().obtainStyledAttributes(attrs, R.styleable.Colck);
            //边距 dp转换成px
//            mPadding = array.getDimension(R.styleable.Colck_wb_padding,
//                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10,
//                            getResources().getDisplayMetrics()));

//            mPadding = array.getDimension(R.styleable.Colck_wb_padding,
//                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
//                            getResources().getDisplayMetrics()));

            mPadding = array.getDimension(R.styleable.Colck_wb_padding, DptoPx(10));

            //字体大小 sp 转换成 px
//            mTextSize = array.getDimension(R.styleable.Colck_wb_text_size,
//                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
//                            getResources().getDisplayMetrics()));
            mTextSize = array.getDimension(R.styleable.Colck_wb_text_size, SptoPx(26));
            //时针,分针,秒针的宽度
            mHourPointerWidth = array.getDimension(
                    R.styleable.Colck_wb_hour_pointer_width, DptoPx(5));
            mMintuePointerWidth = array.getDimension(
                    R.styleable.Colck_wb_minute_pointer_width, DptoPx(3));
            mSecondPointerWidth = array.getDimension(
                    R.styleable.Colck_wb_second_pointer_width,DptoPx(2));
            //指针末尾的长度;指针的圆角
            mPointRadius = (int) array.getDimension(
                    R.styleable.Colck_wb_pointer_corner_radius, DptoPx(10));
            mPointEndLength = array.getDimension(
                    R.styleable.Colck_wb_pointer_end_length, DptoPx(10));
            //长线,短线的颜色
            mColorLong = array.getColor(
                    R.styleable.Colck_wb_scale_long_color, Color.argb(255, 0, 0, 0));
            mColorShort = array.getColor(
                    R.styleable.Colck_wb_scale_short_color, Color.argb(125, 0, 0, 0));
            //分针,秒针的颜色
            mMinutePointerColor = array.getColor(
                    R.styleable.Colck_wb_minute_pointer_color, Color.BLACK);
            mSecondPointerColor = array.getColor(
                    R.styleable.Colck_wb_second_pointer_color, Color.RED);
            mHourPointerColor = array.getColor(
                    R.styleable.Colck_wb_hour_pointer_color, Color.GRAY);
        } catch (Exception e) {
            //如果出错就用默认值
            mPadding = DptoPx(10);
            mTextSize = SptoPx(18);
            mHourPointerWidth = DptoPx(5);
            mMintuePointerWidth = DptoPx(3);
            mSecondPointerWidth = DptoPx(2);
            mPointEndLength = DptoPx(10);
            mPointRadius = (int) DptoPx(10);

            mColorLong = Color.argb(255, 0, 0, 0);
            mColorShort = Color.argb(125, 0, 0, 0);
            mMinutePointerColor = Color.BLACK;
            mSecondPointerColor = Color.RED;
            mHourPointerColor = Color.GRAY;
        }finally {
            if (array == null) {
                array.recycle();
            }
        }
    }

    //Dp 转 Px
    private float DptoPx(int value) {

//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
//                getResources().getDisplayMetrics());
        //可以将其封装成一个类 进行调用
        return SizeUtil.DptoPx(getContext(), value);
    }

    //Sp 转 Px
    private float SptoPx(int value) {
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,
//                getResources().getDisplayMetrics());
        return SizeUtil.SptoPx(getContext(), value);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

    }

    class NoDetermineSizeException extends Exception {
        public NoDetermineSizeException(String message) {
            super(message);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 1000;


        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST ||
                widthMode == MeasureSpec.UNSPECIFIED ||
                heightMeasureSpec == MeasureSpec.AT_MOST ||
                heightMeasureSpec == MeasureSpec.UNSPECIFIED) {
            try {
                throw new NoDetermineSizeException("宽度高度至少有一个确定的值,不能同时为wrap_content");
            } catch (NoDetermineSizeException e) {
                e.printStackTrace();
            }

        } else {        //至少有一个为确定值,要获取其中的最小值
            if (widthMode == MeasureSpec.EXACTLY) {
                width = Math.min(widthSize, width);
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                width = Math.min(heightSize, width);
            }
        }
        setMeasuredDimension(width,width);
    }

    //表盘圆的半径值与 尾部长度值
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = (Math.min(w, h) - mPadding) / 2;
        mPointEndLength = mRadius / 6;//指针尾部的长度默认为 半径的六分之一;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        paintCircle(canvas);//外圆背景
        paintScale(canvas);//绘制刻度
        //绘制指针
       // paintPointer(canvas);
       // TODO   暂时有缺陷

        canvas.restore();
        postInvalidateDelayed(1000);//间隔1秒钟刷新

    }

    public void paintCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, mRadius, mPaint);
    }

    public void paintScale(Canvas canvas) {
        mPaint.setStrokeWidth(SizeUtil.DptoPx(getContext(), 1));
        int lineWidth = 0;
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                mPaint.setStrokeWidth(SizeUtil.DptoPx(getContext(), (int) 1.5f));
                mPaint.setColor(mColorLong);
                lineWidth = 40;
                //绘制文字
                mPaint.setTextSize(mTextSize);
                String text = ((i / 5) == 0 ? 12 : (i / 5)) + "";
                Rect textBound = new Rect();
                mPaint.getTextBounds(text, 0, text.length(), textBound);
                mPaint.setColor(Color.BLACK);
                canvas.save();
                canvas.translate(0, -mRadius + DptoPx(5) + lineWidth +
                        (textBound.bottom - textBound.top));
                canvas.rotate(-6 * i);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text, -(textBound.right - textBound.left) / 2,
                        textBound.bottom, mPaint);
                canvas.restore();

            } else {
                lineWidth = 30;
                mPaint.setColor(mColorShort);
                mPaint.setStrokeWidth(SizeUtil.DptoPx(getContext(),1));
            }
            canvas.drawLine(0, -mRadius + SizeUtil.DptoPx(getContext(), 10),
                    0, -mRadius + SizeUtil.DptoPx(getContext(), 10) + lineWidth, mPaint);
            canvas.rotate(6);
        }
        canvas.restore();
    }

    //画上指针
    public void paintPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        //时针,分针,秒针旋转的角度
        int roHour = (hour % 12) * 360 / 12;
        int roMinute = minute * 360 / 60;
        int roSecond = second * 360 / 60;
        //时针
        canvas.save();
        canvas.rotate(roHour);
        RectF rectFHour = new RectF(-mHourPointerWidth / 2, -mRadius * 3 / 5,
                mHourPointerWidth / 2, mPointEndLength);

        mPaint.setColor(mHourPointerColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointerWidth);
        canvas.drawRoundRect(rectFHour,mPointRadius,mPointRadius,mPaint);
        canvas.restore();
        //分针
        canvas.save();
        canvas.rotate(roMinute);
        RectF rectFMinute = new RectF(-mMintuePointerWidth / 2, -mRadius * 3.5f / 5,
                mMintuePointerWidth / 2, mPointEndLength);
        mPaint.setColor(mMinutePointerColor);
        mPaint.setStrokeWidth(mMintuePointerWidth);
        canvas.drawRoundRect(rectFMinute,mPointRadius,mPointRadius,mPaint);
        canvas.restore();
        //秒针
        canvas.save();
        canvas.rotate(roSecond);
        RectF rectFSecond = new RectF(-mSecondPointerWidth / 2, -mRadius + 15,
                mSecondPointerWidth / 2, mPointEndLength);
        mPaint.setColor(mSecondPointerColor);
        mPaint.setStrokeWidth(mSecondPointerWidth);
        canvas.drawRoundRect(rectFSecond, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //中心
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSecondPointerColor);
        canvas.drawCircle(0, 0, mSecondPointerWidth * 4, mPaint);

    }
}
