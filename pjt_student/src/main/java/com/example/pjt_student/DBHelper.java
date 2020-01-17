package com.example.pjt_student;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// app을 위한 db 관리 코드 추상화
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // studentdb -> db file 명 하나의 file에 여러가지 table 저장 가능
        // 원한다면 동적 데이터 처리해서 하나의 app 내에 여러 db file 가능
        // 상위 클래스에 db version 정보를 넘겼다
        super(context, "studentdb", null, 1);
    }

    // app install 후 helper 가 최초로 이용되는 순간 한번 자동 호출
    @Override
    public void onCreate(SQLiteDatabase db) {
        String studentSql = "create table tb_student (_id integer primary key autoincrement, name not null, email, phone, photo, memo)";
        String scoreSql = "create table tb_score (_id integer primary key autoincrement, student_id not null, date, score)";
        db.execSQL(studentSql);
        db.execSQL(scoreSql);
    }

    // 상위 클래스에 전달한 db version 정보가 변겨오딜대 마다..
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table tb_student");
        db.execSQL("drop table tb_score");
        onCreate(db);
    }
}
