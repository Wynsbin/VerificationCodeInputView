package com.wynsbin.vciv;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

/**
 * Description : 修改密码字符串改变的等待时间
 *
 * @author WSoban
 * @date 2019/9/3
 */
public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence mSource;

        public PasswordCharSequence(CharSequence source) {
            mSource = source; // Store char sequence
        }

        @Override
        public char charAt(int index) {
            return '•'; // This is the important part
        }

        @Override
        public int length() {
            return mSource.length(); // Return default
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return mSource.subSequence(start, end); // Return default
        }
    }
}
