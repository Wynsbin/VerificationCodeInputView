package com.wynsbin.verificationcodeinputview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wynsbin.vciv.VerificationCodeInputView;

public class MainActivity extends AppCompatActivity implements VerificationCodeInputView.OnInputListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerificationCodeInputView verificationCodeInputView1 = findViewById(R.id.vciv_code1);
        VerificationCodeInputView verificationCodeInputView2 = findViewById(R.id.vciv_code2);
        VerificationCodeInputView verificationCodeInputView3 = findViewById(R.id.vciv_code3);
        verificationCodeInputView1.setOnInputListener(this);
        verificationCodeInputView2.setOnInputListener(this);
        verificationCodeInputView3.setOnInputListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(view -> {
            verificationCodeInputView1.clearCode();
            verificationCodeInputView2.clearCode();
            verificationCodeInputView3.clearCode();
        });
    }

    @Override
    public void onComplete(String code) {
        Toast.makeText(MainActivity.this, code, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInput() {

    }
}
