# VerificationCodeInputView

[![](https://jitpack.io/v/Wynsbin/VerificationCodeInputView.svg)](https://jitpack.io/#Wynsbin/VerificationCodeInputView)

**验证码输入框**

1、能自定义输入框个数和样式

2、支持长按粘贴或剪切板内容自动填充（数字模式时，如果剪切板第一个条不是数字，长按输入框不会弹出粘贴窗）

## Demo

![](https://github.com/Wynsbin/VerificationCodeInputView/blob/master/gif/GIF.gif)

## Principle

大致是Edittext + n* TextView，然后设置edittext字体跟背景颜色都为透明，隐藏光标

Edittext：监听edittext每次输入一个字符就赋值到对应的TextView上，然后在清空自己

下划线：在TextView下面添加View

光标：这里的每个TextView的焦点光标其实对View设置了ValueAnimator

粘贴：粘贴弹窗是自定义的PopupWindow

## Gradle

Step 1. Add it in your root build.gradle at the end of repositories:

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Step 2. Add the dependency:

```
dependencies {
    implementation 'com.github.Wynsbin:VerificationCodeInputView:1.0.2'
}
```


## How to use

### In layout

```
<com.wynsbin.vciv.VerificationCodeInputView
    android:id="@+id/vciv_code"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="48dp"
    android:gravity="center"
    android:orientation="horizontal"
    app:vciv_et_background="@android:color/white"
    app:vciv_et_foucs_background="@android:color/holo_orange_dark"
    app:vciv_et_cursor_color="@color/colorPrimary"
    app:vciv_et_height="58dp"
    app:vciv_et_inputType="number"
    app:vciv_et_number="6"
    app:vciv_et_text_color="@android:color/black"
    app:vciv_et_text_size="18sp"
    app:vciv_et_underline_default_color="@android:color/holo_green_dark"
    app:vciv_et_underline_focus_color="@android:color/holo_blue_bright"
    app:vciv_et_underline_height="2dp"
    app:vciv_et_underline_show="true"
    app:vciv_et_width="58dp" />
```

### In Java Code

```
VerificationCodeInputView view = findViewById(R.id.vciv_code);
view.setOnInputListener(new VerificationCodeInputView.OnInputListener() {
    @Override
    public void onComplete(String code) {
        Toast.makeText(MainActivity.this, code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInput() {

    }
});

//清除验证码
view.clearCode();
```

## Attributes

|name|describe|format|default|
|:--|:--|:--|:--:|
|vciv_et_number|输入框的数量|integer|4|
|vciv_et_inputType|输入框输入类型|enum|数字模式|
|vciv_et_width|输入框的宽度|dimension|40dp|
|vciv_et_height|输入框的高度|dimension|40dp|
|vciv_et_text_color|输入框文字颜色|color|Color.BLACK|
|vciv_et_text_size|输入框文字大小|dimension|14sp|
|vciv_et_spacing|输入框间距，不输入则代表平分|dimension||
|vciv_et_background|输入框背景色|reference&color|Color.WHITE|
|vciv_et_foucs_background|输入框焦点背景色，不输入代表不设置|reference&color||
|vciv_et_cursor_width|输入框焦点宽度|dimension|2dp|
|vciv_et_cursor_height|输入框焦点高度|dimension|30dp|
|vciv_et_cursor_color|输入框焦点颜色|color|#C3C3C3|
|vciv_et_underline_height|输入框下划线高度|dimension|1dp|
|vciv_et_underline_default_color|输入框无焦点的下划线颜色|color|#F0F0F0|
|vciv_et_underline_focus_color|输入框有焦点的下划线颜色|color|#C3C3C3|
|vciv_et_underline_show|输入框下划线是否展示|boolean|false|


### VCInputType

|name|describe
|:--|:--|
|number|数字模式|
|numberPassword|数字密码模式|
|text|字符模式|
|textPassword|字符密码模式|


### vciv_et_background&vciv_et_foucs_background

1、@drawable/xxx

2、@color/xxx

3、#xxxxxx
