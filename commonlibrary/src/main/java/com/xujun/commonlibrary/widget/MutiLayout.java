package com.xujun.commonlibrary.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xujun.commonlibrary.R;

/**
 * @ explain:
 * @ author：xujun on 2016/10/27 20:07
 * @ email：gdutxiaoxu@163.com
 */
public class MutiLayout extends FrameLayout {

    public static final int STATE_NOONE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;

    private ImageView mIvEmpty;
    private TextView mTvEmpty;
    private int mEmptyIconId;
    private String mEmptyText;
    private Context mContext;
    private String mErrorText;
    private int mErrorIconId;

    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View emptyView;// 空界面
    private View successView;// 加载成功的界面

    int state = STATE_NOONE;

    OnClickListener mRetryListener;

    public MutiLayout(Context context) {
        this(context, null, 0);
    }

    public MutiLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MutiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttr(attrs);

    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.empty);
        mEmptyText = typedArray.getString(R.styleable.empty_emptyText);
        mEmptyIconId = typedArray.getResourceId(R.styleable.empty_emptyIcon, -1);
        mErrorText = typedArray.getString(R.styleable.error_errorIcon);
        mErrorIconId = typedArray.getResourceId(R.styleable.error_errorIcon, -1);
        typedArray.recycle();

    }

    @TargetApi(21)
    public MutiLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public enum LoadResult {
        loading(1), error(2), empty(3), success(4);

        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


    public void setOnRetryListener(OnClickListener onClickListener){
        mRetryListener=onClickListener;
    }

    public void show(LoadResult loadResult) {
        state = loadResult.getValue();
        showPage();
    }

    // 根据不同的状态显示不同的界面
    private void showPage() {
        if (loadingView == null) {
            loadingView = createLoadingView();

        }
        loadingView.setVisibility(state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        if (errorView == null) {
            errorView = createErrorView();
        }
        errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                : View.INVISIBLE);
        if (emptyView == null) {
            emptyView = createEmptyView();
        }
        emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                : View.INVISIBLE);

    }

    /* 创建了空的界面 */
    private View createEmptyView() {
        View view = View.inflate(mContext, R.layout.loadpage_empty,
                null);
        TextView tv=(TextView)view.findViewById(R.id.tv_empty);
        ImageView imageView=(ImageView)view.findViewById(R.id.iv_empty);
        if (TextUtils.isEmpty(mEmptyText)) {
            tv.setText(mEmptyText);
        }

        if(mEmptyIconId!=-1){
            imageView.setImageResource(mEmptyIconId);
        }
        return view;
    }

    /* 创建了错误界面 */
    private View createErrorView() {
        View view = View.inflate(mContext, R.layout.loadpage_error,
                null);
        Button page_bt = (Button) view.findViewById(R.id.page_bt);
        ImageView iv=(ImageView)view.findViewById(R.id.page_iv);
        if(!TextUtils.isEmpty(mEmptyText)){
            page_bt.setText(mErrorText);
        }

        if(mErrorIconId!=-1){
            iv.setImageResource(mErrorIconId);
        }

        page_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mRetryListener!=null){
                    mRetryListener.onClick(v);
                }
            }
        });
        return view;
    }

    /* 创建加载中的界面 */
    private View createLoadingView() {
        View view = View.inflate(mContext,
                R.layout.loadpage_loading, null);
        return view;
    }


}