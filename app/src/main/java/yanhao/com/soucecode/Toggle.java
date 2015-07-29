package yanhao.com.soucecode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

/**
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-07-29
 * Time: 11:03
 */
public class Toggle extends View {
    private static final String TAG = "Toggle";
    private static final String KEY_OPEN = "open";
    private static final String KEY_STATE = "state";
    private int mNomX;//目前滑块的x位置
    private int mSmoothDuration = 500;
    private boolean isOpen;//是否为打开状态
    private Drawable mOpenDrawable;//打开状态的图片
    private Drawable mCloseDrawable;//关闭状态的图片
    private Scroller mScroller;

    public Toggle(Context context) {
        this(context, null);
    }

    public Toggle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Toggle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context, new LinearInterpolator());
        //获取xml定义图片
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.toggle, defStyleAttr, 0);
        mOpenDrawable = a.getDrawable(R.styleable.toggle_toggle_drawable_open);
        mCloseDrawable = a.getDrawable(R.styleable.toggle_toggle_drawable_close);
        a.recycle();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_OPEN, isOpen);
        bundle.putParcelable(KEY_STATE, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isOpen = bundle.getBoolean(KEY_OPEN);
            state = bundle.getParcelable(KEY_STATE);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        /**获取背景的宽高**/
        Drawable background = getBackground();
        // 获取宽度
        int _Width = Math.max(background.getIntrinsicWidth(), 50);
        // 获取高度
        int _Height = Math.max(background.getIntrinsicHeight(), 20);

        // 父布局指定大小
        if (MeasureSpec.EXACTLY == widthMode) {
            _Width = widthSize;
        } else if (MeasureSpec.AT_MOST == widthMode) {
            _Width = Math.min(_Width, widthSize);
        }
        setMeasuredDimension(_Width, _Height);
        // 如果“关闭” 则滑块的位置为当前view宽度-关闭图片宽度
        if (!isOpen) {
            mNomX = getMeasuredWidth() - mCloseDrawable.getIntrinsicWidth();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 根据isOpen获取当前要绘制的drawable
        Drawable _Drawable = isOpen ? mOpenDrawable : mCloseDrawable;
        // clip bounds
        _Drawable.setBounds(mNomX, 0, mNomX + mOpenDrawable.getIntrinsicWidth(), getMeasuredHeight());
        // draw on the canvas
        _Drawable.draw(canvas);
    }

    @Override
    public void computeScroll() {
        mScroller.abortAnimation();
        // open -> close
        if (isOpen) {
            mScroller.startScroll(0, 0, getMeasuredWidth() - mOpenDrawable.getIntrinsicWidth(), 0, mSmoothDuration);
        } else {
            mScroller.startScroll(getMeasuredWidth() - mCloseDrawable.getIntrinsicWidth(), 0, getMeasuredWidth(), 0, mSmoothDuration);
        }
        postInvalidate();
    }
    /**
     * 设置Scroller的Interpolator
     * @param interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        mScroller = new Scroller(getContext(), interpolator);
    }

    /**
     * 设置动画完成的时间间隔
     * @param duration
     */
    public void setSmoothDuration(int duration) {
        mSmoothDuration = duration;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        isOpen = true;
        mNomX = 0;
        postInvalidate();
    }

    public void close() {
        isOpen = false;
        mNomX = getMeasuredWidth() - mCloseDrawable.getIntrinsicWidth();
        postInvalidate();
    }

}
