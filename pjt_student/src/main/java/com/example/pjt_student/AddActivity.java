package com.example.pjt_student;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameView;
    private EditText emailView;
    private EditText phoneView;
    private EditText memoView;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nameView = findViewById(R.id.add_name);
        emailView = findViewById(R.id.add_email);
        phoneView = findViewById(R.id.add_phone);
        memoView = findViewById(R.id.add_memo);
        addBtn = findViewById(R.id.add_btn);

        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = nameView.getText().toString();
        String email = emailView.getText().toString();
        String phone = phoneView.getText().toString();
        String memo = memoView.getText().toString();

        if (name == null || name.equals("")) {
            // Short 는 3초 Long은 5초 일반적으로 숏 많이함
            Toast toast = Toast.makeText(this, R.string.add_name_null, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_student (name, email, phone, memo) values (?,?,?,?)", new String[]{name, email, phone, memo});
            db.close();

            // 화면은 보통 일반적으로 create 화면이 성공하고 나면 다시 목록화면으로 돌아가는게 좋음
            // MainActivity 로 자동 화면 전환
            // Intent 에 의해 화면 전환 시키지 않고 자신 Activity를 종료 시켜서 자동으로 이전 화면으로 전환
            finish(); // 시스템에 컴포넌트 라이프 사이클 종료시켜 달라고 '의뢰' 하는거
        }
    }
}
