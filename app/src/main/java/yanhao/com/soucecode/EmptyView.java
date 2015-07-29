package yanhao.com.soucecode;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * Author: yanhao(amosbake@gmail.com)
 * Date : 2015-07-29
 * Time: 09:43
 */
public class EmptyView extends RelativeLayout {
    private static final String TAG = "EmptyView";
    private String mText;//数据为空时的提醒文本
    private String mLoadingText;//加载中的提醒文本
    private TextView mTextView;//显示提醒文本
    private Button mButton;//用户的刷新按钮
    private View mBindView;//绑定的view
    private LayoutInflater mInflater;

    public EmptyView(Context context) {
        this(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.empty_view, defStyleAttr, 0);
        mText = ta.getString(R.styleable.empty_view_empty_view_text);
        mLoadingText = ta.getString(R.styleable.empty_view_empty_loading_text);
        String _ButtonText = ta.getString(R.styleable.empty_view_empty_button_text);
        ta.recycle();
        init(context, _ButtonText);
    }

    /**
     * 初始化方法
     *
     * @param buttonText 按钮文本
     */
    private void init(Context context, String buttonText) {
        if (TextUtils.isEmpty(mText)) {
            mText = "暂无数据";
        }
        if (TextUtils.isEmpty(buttonText)) {
            buttonText = "重试";
        }
        if (TextUtils.isEmpty(mLoadingText)) {
            mLoadingText = "加载中";
        }

        View.inflate(context, R.layout.view_empty_view, this);
        mTextView = (TextView) this.findViewById(R.id.empty_text);
        mButton = (Button) this.findViewById(R.id.empty_button);
        mTextView.setText(mText);
        mButton.setText(buttonText);
    }

    /**
     * 显示加载中的文本
     */
    public void loading() {
        if (mBindView != null) {
            mBindView.setVisibility(GONE);
            setVisibility(VISIBLE);
            mButton.setVisibility(INVISIBLE);
            mTextView.setText(mLoadingText);
        }
    }

    /**
     * 显示加载成功
     */
    public void success() {
        setVisibility(GONE);
        if (mBindView != null) {
            mBindView.setVisibility(VISIBLE);
        }
    }

    /**
     * 无数据显示
     */
    public void empty() {
        if (mBindView != null) {
            mBindView.setVisibility(GONE);
            setVisibility(VISIBLE);
            mButton.setVisibility(VISIBLE);
            mTextView.setText(mText);
        }
    }

    /**
     * 绑定需要显示的控件
     *
     * @param view
     */
    public void bindView(View view) {
        mBindView = view;
    }

    /**
     * 利用反射设置按钮点击事件
     * @param base 一般为activity
     * @param method  调用的方法
     * @param paramters 方法的参数
     */
    public void buttonClick(final Object base, final String method, final Object... paramters) {
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int _Length = paramters.length;
                Class<?>[] paramsTypes=new Class[_Length];
                for (int i=0;i<_Length;i++){
                    paramsTypes[i]=paramters.getClass();
                }
                try {
                    Method m=base.getClass().getDeclaredMethod(method,paramsTypes);
                    m.setAccessible(true);
                    m.invoke(base,paramsTypes);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
