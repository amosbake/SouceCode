package yanhao.com.soucecode.wiget.jazzviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import yanhao.com.soucecode.R;
import yanhao.com.soucecode.utils.LogUtils;

/**
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-08-12
 * Time: 14:50
 */
public class JazzyViewPager extends ViewPager {
    private static final String TAG = "JazzyViewPager";
    private boolean mEnabled = true;
    private boolean mFadeEnabled = false;
    private boolean mOutlineEnabled = false;
    public static int sOutlineColor = Color.WHITE;
    private TransitionEffect mEffect = TransitionEffect.Standard;

    private SparseIntArray mObjs = new SparseIntArray();

    public static final float SCALE_MAX = 0.5f;
    public static final float ZOOM_MAX = 0.5f;
    public static final float ROT_MAX = 15.0f;

    public enum TransitionEffect {
        Standard, Tablet, CubeIn, CubeOut, FlipVertical, FlipHorizontal, Stack, ZoomIn, ZoomOut, RotateUp, RotateDown, Accordion
    }

    private static final boolean API_11;
    static {
        API_11= Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB;
    }
    public JazzyViewPager(Context context) {
        this(context, null);
    }

    public JazzyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClipChildren(false);
        //get declare-style
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.JazzyViewPager);
        int effect=ta.getInt(R.styleable.JazzyViewPager_style,0);
        String[] transitions=getResources().getStringArray(R.array.jazzy_effects);
        setTransitionEffect(TransitionEffect.valueOf(transitions[effect]));
        setFadeEnabled(ta.getBoolean(R.styleable.JazzyViewPager_fadeEnable, false));
        setOutlineEnabled(ta.getBoolean(R.styleable.JazzyViewPager_outlineEnable, false));
        setOutlineColor(ta.getColor(R.styleable.JazzyViewPager_outlinColor,sOutlineColor));
        switch (mEffect){
            case Stack:
            case ZoomOut:
                setFadeEnabled(true);
        }
        ta.recycle();
    }
    public void setTransitionEffect(TransitionEffect effect) {
        mEffect = effect;
//		reset();
    }

    public void setPagingEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public void setFadeEnabled(boolean enabled) {
        mFadeEnabled = enabled;
    }

    public boolean getFadeEnabled() {
        return mFadeEnabled;
    }

    public void setOutlineEnabled(boolean enabled) {
        mOutlineEnabled = enabled;
        wrapWithOutlines();
    }

    public void setOutlineColor(int color) {
        sOutlineColor = color;
    }

    private void wrapWithOutlines() {
        for (int i=0;i<getChildCount();i++){
            View v=getChildAt(i);
            if (!(v instanceof OutlineContainer)){
                removeView(v);
                super.addView(wrapChild(v),i);
            }
        }
    }

    private View wrapChild(View v) {
        if (!mOutlineEnabled||v instanceof OutlineContainer){
            return v;
        }
        OutlineContainer out=new OutlineContainer(getContext());
        out.setLayoutParams(generateDefaultLayoutParams());
        v.setLayoutParams(new OutlineContainer.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        out.addView(v);
        return out;
    }

    public void addView(View child){
        super.addView(wrapChild(child));
    }
    public void addView(View child, int index) {
        super.addView(wrapChild(child), index);
    }

    public void addView(View child, LayoutParams params) {
        super.addView(wrapChild(child), params);
    }

    public void addView(View child, int width, int height) {
        super.addView(wrapChild(child), width, height);
    }

    public void addView(View child, int index, LayoutParams params) {
        super.addView(wrapChild(child), index, params);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mEnabled?super.onInterceptTouchEvent(ev):false;
    }

    private State mState= State.IDLE;
    private int oldPage;

    private View mLeft;
    private View mRight;
    private float mRot;
    private float mTrans;
    private float mScale;
    /**viewpager scroll state**/
    private enum State {
        IDLE, GOING_LEFT, GOING_RIGHT
    }

    private void logState(View v,String title){
        LogUtils.v(TAG,title+":ROT(");
    }

}
