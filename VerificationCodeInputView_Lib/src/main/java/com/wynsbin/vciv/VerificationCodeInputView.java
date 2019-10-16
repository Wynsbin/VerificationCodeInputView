package com.wynsbin.vciv;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description : 验证码输入框
 * <p>
 * 支持粘贴功能
 *
 * @author WSoban
 * @date 2019/10/9
 */
public class VerificationCodeInputView extends RelativeLayout {

    private Context mContext;
    private OnInputListener onInputListener;

    private LinearLayout mLinearLayout;
    private RelativeLayout[] mRelativeLayouts;
    private TextView[] mTextViews;
    private View[] mUnderLineViews;
    private View[] mCursorViews;
    private EditText mEditText;
    private PopupWindow mPopupWindow;
    private ValueAnimator valueAnimator;

    private List<String> mCodes = new ArrayList<>();

    /**
     * 输入框数量
     */
    private int mEtNumber;

    /**
     * 输入框类型
     */
    private VCInputType mEtInputType;

    /**
     * 输入框的宽度
     */
    private int mEtWidth;

    /**
     * 输入框的高度
     */
    private int mEtHeight;

    /**
     * 文字颜色
     */
    private int mEtTextColor;

    /**
     * 文字大小
     */
    private float mEtTextSize;

    /**
     * 输入框间距
     */
    private int mEtSpacing;

    /**
     * 平分后的间距
     */
    private int mEtBisectSpacing;

    /**
     * 判断是否平分,默认平分
     */
    private boolean isBisect;

    /**
     * 输入框宽度
     */
    private int mViewWidth;

    /**
     * 下划线默认颜色,焦点颜色,高度,是否展示
     */
    private int mEtUnderLineDefaultColor;
    private int mEtUnderLineFocusColor;
    private int mEtUnderLineHeight;
    private boolean mEtUnderLineShow;

    /**
     * 光标宽高,颜色
     */
    private int mEtCursorWidth;
    private int mEtCursorHeight;
    private int mEtCursorColor;
    /**
     * 输入框的背景色、焦点背景色、是否有焦点背景色
     */
    private int mEtBackground;
    private int mEtFocusBackground;
    private boolean isFocusBackgroud;

    public enum VCInputType {
        /**
         * 数字类型
         */
        NUMBER,
        /**
         * 数字密码
         */
        NUMBERPASSWORD,
        /**
         * 文字
         */
        TEXT,
        /**
         * 文字密码
         */
        TEXTPASSWORD,
    }

    public VerificationCodeInputView(Context context) {
        super(context);
        init(context, null);
    }

    public VerificationCodeInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public VerificationCodeInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeInputView);
        mEtNumber = typedArray.getInteger(R.styleable.VerificationCodeInputView_vciv_et_number, 4);
        int inputType = typedArray.getInt(R.styleable.VerificationCodeInputView_vciv_et_inputType, VCInputType.NUMBER.ordinal());
        mEtInputType = VCInputType.values()[inputType];
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_width, DensityUtils.dp2px(context, 40));
        mEtHeight = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_height, DensityUtils.dp2px(context, 40));
        mEtTextColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_text_color, Color.BLACK);
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_text_size, DensityUtils.sp2px(context, 14));
        mEtBackground = typedArray.getResourceId(R.styleable.VerificationCodeInputView_vciv_et_background, -1);
        if (mEtBackground < 0) {
            mEtBackground = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_background, Color.WHITE);
        }
        isFocusBackgroud = typedArray.hasValue(R.styleable.VerificationCodeInputView_vciv_et_foucs_background);
        mEtFocusBackground = typedArray.getResourceId(R.styleable.VerificationCodeInputView_vciv_et_foucs_background, -1);
        if (mEtFocusBackground < 0) {
            mEtFocusBackground = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_foucs_background, Color.WHITE);
        }
        isBisect = typedArray.hasValue(R.styleable.VerificationCodeInputView_vciv_et_spacing);
        if (isBisect) {
            mEtSpacing = typedArray.getDimensionPixelSize(R.styleable.VerificationCodeInputView_vciv_et_spacing, 0);
        }
        mEtCursorWidth = typedArray.getDimensionPixelOffset(R.styleable.VerificationCodeInputView_vciv_et_cursor_width, DensityUtils.dp2px(context, 2));
        mEtCursorHeight = typedArray.getDimensionPixelOffset(R.styleable.VerificationCodeInputView_vciv_et_cursor_height, DensityUtils.dp2px(context, 30));
        mEtCursorColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_cursor_color, Color.parseColor("#C3C3C3"));
        mEtUnderLineHeight = typedArray.getDimensionPixelOffset(R.styleable.VerificationCodeInputView_vciv_et_underline_height, DensityUtils.dp2px(context, 1));
        mEtUnderLineDefaultColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_underline_default_color, Color.parseColor("#F0F0F0"));
        mEtUnderLineFocusColor = typedArray.getColor(R.styleable.VerificationCodeInputView_vciv_et_underline_focus_color, Color.parseColor("#C3C3C3"));
        mEtUnderLineShow = typedArray.getBoolean(R.styleable.VerificationCodeInputView_vciv_et_underline_show, false);
        initView();
        typedArray.recycle();
    }

    private void initView() {
        mRelativeLayouts = new RelativeLayout[mEtNumber];
        mTextViews = new TextView[mEtNumber];
        mUnderLineViews = new View[mEtNumber];
        mCursorViews = new View[mEtNumber];

        mLinearLayout = new LinearLayout(mContext);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mLinearLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        for (int i = 0; i < mEtNumber; i++) {
            RelativeLayout relativeLayout = new RelativeLayout(mContext);
            relativeLayout.setLayoutParams(getEtLayoutParams(i));
            setEtBackground(relativeLayout, mEtBackground);
            mRelativeLayouts[i] = relativeLayout;

            TextView textView = new TextView(mContext);
            initTextView(textView);
            relativeLayout.addView(textView);
            mTextViews[i] = textView;

            View cursorView = new View(mContext);
            initCursorView(cursorView);
            relativeLayout.addView(cursorView);
            mCursorViews[i] = cursorView;

            if (mEtUnderLineShow) {
                View underLineView = new View(mContext);
                initUnderLineView(underLineView);
                relativeLayout.addView(underLineView);
                mUnderLineViews[i] = underLineView;
            }
            mLinearLayout.addView(relativeLayout);
        }
        addView(mLinearLayout);
        mEditText = new EditText(mContext);
        initEdittext(mEditText);
        addView(mEditText);
        setCursorColor();
    }

    private void initTextView(TextView textView) {
        RelativeLayout.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        textView.setLayoutParams(lp);
        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(mEtTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEtTextSize);
        setInputType(textView);
        textView.setPadding(0, 0, 0, 0);
    }

    private void initCursorView(View view) {
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(mEtCursorWidth, mEtCursorHeight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        view.setLayoutParams(layoutParams);
    }

    private void initUnderLineView(View view) {
        RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mEtUnderLineHeight);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(mEtUnderLineDefaultColor);
    }

    private void initEdittext(EditText editText) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_TOP, mLinearLayout.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, mLinearLayout.getId());
        editText.setLayoutParams(layoutParams);
        setInputType(editText);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setTextColor(Color.TRANSPARENT);
        editText.setCursorVisible(false);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable != null && editable.length() > 0) {
                    mEditText.setText("");
                    setCode(editable.toString());
                }
            }
        });
        // 监听验证码删除按键
        editText.setOnKeyListener((view, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN && mCodes.size() > 0) {
                mCodes.remove(mCodes.size() - 1);
                showCode();
                return true;
            }
            return false;
        });
        editText.setOnLongClickListener(v -> {
            showPaste();
            return false;
        });
        getEtFocus(editText);
    }

    private void initPopupWindow() {
        mPopupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView tv = new TextView(mContext);
        tv.setText("粘贴");
        tv.setTextSize(14.0f);
        tv.setTextColor(Color.BLACK);
        tv.setBackgroundResource(R.drawable.vciv_paste_bg);
        tv.setPadding(30, 10, 30, 10);
        tv.setOnClickListener(v -> {
            setCode(getClipboardString());
            mPopupWindow.dismiss();
        });
        mPopupWindow.setContentView(tv);
        mPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);// 设置菜单的宽度
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 获取焦点
        mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
        mPopupWindow.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        //设置点击隐藏popwindow
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        mPopupWindow.setBackgroundDrawable(dw);
    }

    private void setEtBackground(RelativeLayout rl, int background) {
        if (background > 0) {
            rl.setBackgroundResource(background);
        } else {
            rl.setBackgroundColor(background);
        }
    }

    private String getClipboardString() {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        //获取剪贴板中第一条数据
        if (clipboardManager != null && clipboardManager.hasPrimaryClip() && clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item itemAt = clipboardManager.getPrimaryClip().getItemAt(0);
            if (!(itemAt == null || TextUtils.isEmpty(itemAt.getText()))) {
                return itemAt.getText().toString();
            }
        }
        return null;
    }

    private LinearLayout.LayoutParams getEtLayoutParams(int i) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mEtWidth, mEtHeight);
        int spacing;
        if (!isBisect) {
            spacing = mEtBisectSpacing / 2;
        } else {
            spacing = mEtSpacing / 2;
            //如果大于最大平分数，将设为最大值
            if (mEtSpacing > mEtBisectSpacing) {
                spacing = mEtBisectSpacing / 2;
            }
        }
        if (i == 0) {
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = spacing;
        } else if (i == mEtNumber - 1) {
            layoutParams.leftMargin = spacing;
            layoutParams.rightMargin = 0;
        } else {
            layoutParams.leftMargin = spacing;
            layoutParams.rightMargin = spacing;
        }
        return layoutParams;
    }

    private void setInputType(TextView textView) {
        switch (mEtInputType) {
            case NUMBERPASSWORD:
                textView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                textView.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                break;
            case TEXT:
                textView.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case TEXTPASSWORD:
                textView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                textView.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                break;
            default:
                textView.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }
    }

    /**
     * 展示自定义的粘贴板
     */
    private void showPaste() {
        //去除输入框为数字模式，但粘贴板不是数字模式
        if ((mEtInputType == VCInputType.NUMBER || mEtInputType == VCInputType.NUMBERPASSWORD) && !isNumeric(getClipboardString())) {
            return;
        }
        if (!TextUtils.isEmpty(getClipboardString())) {
            if (mPopupWindow == null) {
                initPopupWindow();
            }
            mPopupWindow.showAsDropDown(mTextViews[0], 0, 20);
            SoftInputUtils.hideSoftInput((Activity) getContext());
        }
    }

    /**
     * 判断粘贴板上的是不是数字
     *
     * @param str
     * @return
     */
    private boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private void setCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return;
        }
        for (int i = 0; i < code.length(); i++) {
            if (mCodes.size() < mEtNumber) {
                mCodes.add(String.valueOf(code.charAt(i)));
            }
        }
        showCode();
    }

    private void showCode() {
        for (int i = 0; i < mEtNumber; i++) {
            TextView textView = mTextViews[i];
            if (mCodes.size() > i) {
                textView.setText(mCodes.get(i));
            } else {
                textView.setText("");
            }
        }
        setCursorColor();//设置高亮跟光标颜色
        setCallBack();//回调
    }

    /**
     * 设置焦点输入框底部线、光标颜色、背景色
     */
    private void setCursorColor() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        for (int i = 0; i < mEtNumber; i++) {
            View cursorView = mCursorViews[i];
            cursorView.setBackgroundColor(Color.TRANSPARENT);

            if (mEtUnderLineShow) {
                View underLineView = mUnderLineViews[i];
                underLineView.setBackgroundColor(mEtUnderLineDefaultColor);
            }
            if (isFocusBackgroud) {
                setEtBackground(mRelativeLayouts[i], mEtBackground);
            }
        }
        if (mCodes.size() < mEtNumber) {
            setCursorView(mCursorViews[mCodes.size()]);
            if (mEtUnderLineShow) {
                mUnderLineViews[mCodes.size()].setBackgroundColor(mEtUnderLineFocusColor);
            }
            if (isFocusBackgroud) {
                setEtBackground(mRelativeLayouts[mCodes.size()], mEtFocusBackground);
            }
        }
    }

    /**
     * 设置焦点色变换动画
     *
     * @param view
     */
    private void setCursorView(View view) {
        this.valueAnimator = ObjectAnimator.ofInt(view, "backgroundColor", mEtCursorColor, android.R.color.transparent);
        this.valueAnimator.setDuration(1500);
        this.valueAnimator.setRepeatCount(-1);
        this.valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        this.valueAnimator.setEvaluator((fraction, startValue, endValue) -> fraction <= 0.5f ? startValue : endValue);
        this.valueAnimator.start();
    }

    private void setCallBack() {
        if (onInputListener == null) {
            return;
        }
        if (mCodes.size() == mEtNumber) {
            onInputListener.onComplete(getCode());
        } else {
            onInputListener.onInput();
        }
    }

    /**
     * 获得验证码
     *
     * @return 验证码
     */
    private String getCode() {
        StringBuilder sb = new StringBuilder();
        for (String code : mCodes) {
            sb.append(code);
        }
        return sb.toString();
    }

    /**
     * 清空验证码
     */
    public void clearCode() {
        mCodes.clear();
        showCode();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        updateETMargin();
    }

    private void updateETMargin() {
        //平分Margin，把第一个TextView跟最后一个TextView的间距同设为平分
        mEtBisectSpacing = (mViewWidth - mEtNumber * mEtWidth) / (mEtNumber - 1);
        for (int i = 0; i < mEtNumber; i++) {
            mLinearLayout.getChildAt(i).setLayoutParams(getEtLayoutParams(i));
        }
    }

    private void getEtFocus(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        SoftInputUtils.showSoftInput(getContext(), editText);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        SoftInputUtils.hideSoftInput((Activity) getContext());
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
    }

    //定义回调
    public interface OnInputListener {
        void onComplete(String code);

        void onInput();
    }

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }
}
