package com.example.pjt_student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
//    private Button testBtn;
    private ImageView addBtn;
    private ListView listView;
    private ArrayList<StudentVO> datas;


    private double initTime;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testBtn = findViewById(R.id.main_test_btn);

        addBtn = findViewById(R.id.main_btn);
        listView = findViewById(R.id.main_list);

//        testBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from tb_student order by name", null);
        datas = new ArrayList<>();
        while (cursor.moveToNext()) {
            StudentVO vo = new StudentVO();
            vo.id = cursor.getInt(0);
            vo.name = cursor.getString(1);
            vo.email = cursor.getString(2);
            vo.phone = cursor.getString(3);
            vo.photo = cursor.getString(4);
            vo.memo = cursor.getString(5);
            datas.add(vo);
        }
        db.close();
        MainListAdapter adapter = new MainListAdapter(this, R.layout.main_list_item, datas);
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("id", datas.get(position).id);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if(v==addBtn) {
            Intent intent = new Intent(this, AddActivity.class);
            startActivity(intent);
        }
    }

    // 만약 back button 이벤트만을 목적으로 한다면 onBackPressed() 함수를 이용할 수도 있음
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - initTime > 3000) {
                Toast toast = Toast.makeText(this, R.string.main_back_end, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                finish();
            }
            initTime = System.currentTimeMillis();
            return true;
        }

        // 특정 키만 여기서 처리하고 나머지는 너네 원래 처리하던데로 처리해
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // SearchView 객체 획득.. 등록된 MenuItem 객체를 통해서..
        MenuItem menuItem = menu.findItem(R.id.menu_main_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        // 검색어를 입력하세요 라는 문자열을 힌트 문자열로 제공
        searchView.setQueryHint(getResources().getString(R.string.main_search_hint));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(queryListener);
        return super.onCreateOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener queryListener = new SearchView.OnQueryTextListener() {
        // 유저가 검색 했을 때 이벤트
        @Override
        public boolean onQueryTextSubmit(String query) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
            Log.d("dong", query);
            return false;
        }

        // 유저가 검색어 한자한자 입력시마다 호출됨 보통 추천 단어를 처리하기 위해
        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };
}
