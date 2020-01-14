package com.example.lab;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button button;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 화면출력 layout xml 등록 view 객체 생성 완료
        setContentView(R.layout.activity_main);

        // 필요 view 객체 획득
        button = findViewById(R.id.button);
        editText = findViewById(R.id.edit);

        // 유저 이벤트 처리..
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String data = editText.getText().toString();
        Log.d("dong", data);
    }
}
