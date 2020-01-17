package com.example.pjt_student;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


// 유저가 tab 화면을 조정하는 순간을 잡아서 필요한 순간 html 로딩하겠다
public class DetailActivity extends AppCompatActivity implements TabHost.OnTabChangeListener, View.OnClickListener {


    private ImageView studentImageView;
    private TextView nameView;
    private TextView phoneView;
    private TextView emailView;
    private TabHost host;

    private TextView addScoreView;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btnAdd, btnBack;

    private MyView scoreView;

    private ListView listView;
    private SimpleAdapter adapter;
    private ArrayList<HashMap<String, String>> scoreList; // SimpleAdpater 에 넘기는 데이터

    private WebView webView;

    private int studentId = 1; // select where 조건

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // 자신을 실행시킨 intent 획득
        Intent intent = getIntent();
        studentId = intent.getIntExtra("id", 1);

        initData();
        initTab();
        initAddScore();
        initSpannable();
        initList();
        initWebView();
    }

    private void initData() {
        studentImageView = findViewById(R.id.detail_student_image);
        nameView = findViewById(R.id.detail_name);
        phoneView = findViewById(R.id.detail_phone);
        emailView = findViewById(R.id.detail_email);
        scoreView = findViewById(R.id.detail_score);
        studentImageView.setOnClickListener(this);
        // TODO: 동적 데이터 받아서 init 처리

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        // Cursor : select 된 row의 집합객체, cursor를 움직여서 row를 선택하고 선택된 row의 column data 추출
        Cursor cursor = db.rawQuery("select * from tb_student where _id=?",
                new String[]{String.valueOf(studentId)});

        String photo = null;

        if (cursor.moveToFirst()) {
            nameView.setText(cursor.getString(1));
            emailView.setText(cursor.getString(2));
            phoneView.setText(cursor.getString(3));
            photo = cursor.getString(4);

            int score = 0;
            Cursor scoreCursor = db.rawQuery("select score from tb_score where student_id=? order by date desc limit 1",
                    new String[]{String.valueOf(studentId)});

            if (scoreCursor.moveToFirst()) {
                score = scoreCursor.getInt(0);
            }
            scoreView.setScore(score);
        }
        db.close();

        initStudentImage(photo);
    }

    private void initTab() {
        host = findViewById(R.id.host);
        host.setOnTabChangedListener(this);
        // 하위에 TabWidget 과 FrameLayout 선언되어 있을거다 선언된데로 초기화 해라
        host.setup();

        // Tab 화면이 Tab Button 과 그 Button 눌렀을 때 나올 content 가 TabSpec 객체로 묶여서 등록되는 구조
        // 이벤트 처리 없이도 그 버튼 누르면 그 content 가 나온다.
        // 매개변수 문자열은 개발자가 준 현 tab 을 식별하기 위한 식별자값
        // 유저가 알아서 button 클릭으로 화면 조정이 잘 되지만, 코드에서 어떤 tab 화면을 유저가 연거지? 를 판단할 때 사용
        TabHost.TabSpec spec = host.newTabSpec("tab1");
        spec.setIndicator("score"); // button
        spec.setContent(R.id.detail_score_list); // content
        host.addTab(spec);

        spec = host.newTabSpec("tab2");
        spec.setIndicator("chart"); // button
        spec.setContent(R.id.detail_score_chart); // content
        host.addTab(spec);

        spec = host.newTabSpec("tab3");
        spec.setIndicator("add"); // button
        spec.setContent(R.id.detail_score_add); // content
        host.addTab(spec);

        spec = host.newTabSpec("tab4");
        spec.setIndicator("memo"); // button
        spec.setContent(R.id.detail_memo); // content
        host.addTab(spec);
    }

    private void initAddScore() {
        btn0 = (Button) findViewById(R.id.key_0);
        btn1 = (Button) findViewById(R.id.key_1);
        btn2 = (Button) findViewById(R.id.key_2);
        btn3 = (Button) findViewById(R.id.key_3);
        btn4 = (Button) findViewById(R.id.key_4);
        btn5 = (Button) findViewById(R.id.key_5);
        btn6 = (Button) findViewById(R.id.key_6);
        btn7 = (Button) findViewById(R.id.key_7);
        btn8 = (Button) findViewById(R.id.key_8);
        btn9 = (Button) findViewById(R.id.key_9);
        btnBack = (Button) findViewById(R.id.key_back);
        btnAdd = (Button) findViewById(R.id.key_add);

        addScoreView = (TextView) findViewById(R.id.key_edit);

        btn0.setOnClickListener(addScoreListener);
        btn1.setOnClickListener(addScoreListener);
        btn2.setOnClickListener(addScoreListener);
        btn3.setOnClickListener(addScoreListener);
        btn4.setOnClickListener(addScoreListener);
        btn5.setOnClickListener(addScoreListener);
        btn6.setOnClickListener(addScoreListener);
        btn7.setOnClickListener(addScoreListener);
        btn8.setOnClickListener(addScoreListener);
        btn9.setOnClickListener(addScoreListener);
        btnBack.setOnClickListener(addScoreListener);
        btnAdd.setOnClickListener(addScoreListener);
    }

    View.OnClickListener addScoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnAdd) {
                String score = addScoreView.getText().toString();
                long date = System.currentTimeMillis();

                DBHelper helper = new DBHelper(DetailActivity.this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("insert into tb_score (student_id, date, score) values (?, ?, ?)",
                        new String[]{String.valueOf(studentId), String.valueOf(date), score});
                db.close();

                // 화면은?? 보통 추가 후 목록 화면으로 감
                // 코드에서 자동으로 특정 tab 화면으로 이동해야함
                host.setCurrentTab(0);
                addScoreView.setText("0");

                scoreView.setScore(Integer.parseInt(score));

                HashMap<String, String> map = new HashMap<>();
                map.put("score", score);
                Date d = new Date(date);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                map.put("date", sd.format(d));
                scoreList.add(0, map); // 가장 첫번째 데이터에 추가
                // 항목 추가 제거는 데이터 추가제거후 adapter에게 반영 명령만 (이미 어댑터는 만들어져 있으니)
                adapter.notifyDataSetChanged();

            } else if (v == btnBack) {
                String score = addScoreView.getText().toString();
                if (score.length() == 1) {
                    addScoreView.setText("0");
                } else {
                    String newScore = score.substring(0, score.length() - 1);
                    addScoreView.setText(newScore);
                }
            } else {
                Button btn = (Button) v;
                String txt = btn.getText().toString();
                String score = addScoreView.getText().toString();
                if (score.equals("0")) {
                    addScoreView.setText(txt);
                } else {
                    String newScore = score + txt;
                    int intScore = Integer.parseInt(newScore);
                    if (intScore > 100) {
                        Toast toast = Toast.makeText(DetailActivity.this, R.string.read_add_score_over_score, Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        addScoreView.setText(newScore);
                    }
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 원래 여기서 add add 해서 메뉴 구성해야 하는데 xml 로 리소스 외부화 시켰기 때문에 inflate로
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initSpannable() {
        TextView spanView = findViewById(R.id.spanView);
        TextView htmlView = findViewById(R.id.htmlView);

        // ForegroundColorSpan 등은 이 span 이 적용되어 그 문자열이 그 UI 로 나오면 끝
        // URLSpan 의 경우는 이 span 이 적용되어 문자열이 링크 모양으로 나온다가 끝이 아니라
        // User 의 클릭 이벤트 처리는?
        // 이벤트 로직을 포함한 URLSpan 의 서브 클래스로 적용

        URLSpan urlSpan = new URLSpan("") {
            @Override
            public void onClick(View widget) {
                Toast toast = Toast.makeText(DetailActivity.this, "more click..", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        // text data 와 ui 정보 넣기 위해
        String data = spanView.getText().toString();
        Spannable spannable = (Spannable) spanView.getText();

        int pos = data.indexOf("EXID");
        while (pos > -1) {
            spannable.setSpan(new ForegroundColorSpan(Color.RED), pos, pos + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            pos = data.indexOf("EXID", pos + 1);
        }

        pos = data.indexOf("more");
        spannable.setSpan(urlSpan, pos, pos + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 아래 설정 없이는 이벤트 발생 안함
        spanView.setMovementMethod(LinkMovementMethod.getInstance());

        String html = "<font color='blue'>HANI</font><br/><img src='myImage'/>";

        // fromHtml 함수가 문자열의 html tag 위치에 Span 클래스 적용
        // <img> 태그 위치에 이미지 출력만 해준다 이미지 준비는 우리가 해야함. 브라우저가 아니기 때문에
        // tagHandler 는 특정 태그는 개발자 알고리즘으로 핸들링 하겠다면 TagHandler 서브 클래스 정의
        htmlView.setText(Html.fromHtml(html, new MyImageGetter(), null));
    }

    class MyImageGetter implements Html.ImageGetter {
        // fromHtml 함수가 <img> 태그 하나당 이미지 획득 목적으로 한번씩 호출, img 태그 여러개면 여러번 호출
        // 매개변수가 <img> 태그의 src 값을 전달 받음
        // 최종 리턴되는 Drawable 객체로 표현되는 이미지가 <img> 위치에
        @Override
        public Drawable getDrawable(String source) {
            if (source.equals("myImage")) {
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.hani_1, null);
                // 아래 설정하지 않으면 화면에 이미지 안나옴 설정하지 않으면 width, height가 제로로 됨
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                return drawable;
            }
            // null 리턴하면 에러는 안나는데 이미지가 없다는 얘기
            return null;
        }
    }

    private void initList() {
        listView = findViewById(R.id.detail_score_list);
        scoreList = new ArrayList<>();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select score, date from tb_score where student_id=? order by date desc",
                new String[]{String.valueOf(studentId)});
        while (cursor.moveToNext()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("score", cursor.getString(0));
            Date d = new Date(Long.parseLong(cursor.getString(1)));
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            map.put("date", sd.format(d));
            scoreList.add(map);
        }

        db.close();
        adapter = new SimpleAdapter(
                this,
                // 집합 데이터
                scoreList,
                // 항목 구성 layout xml 정보
                R.layout.read_list_item,
                // 한항목의 데이터가 Map 이다. Map 에서 데이터 추출 key 값
                new String[]{"score", "date"},
                // 하나의 항목에 view 가 여러개다.. 어느 view 인지 view id 값
                new int[]{R.id.read_list_score, R.id.read_list_date});

        listView.setAdapter(adapter);
    }

    private void initWebView() {
        webView = findViewById(R.id.detail_score_chart);
        // WebView 설정
        WebSettings settings = webView.getSettings();
        // WebView 의 js engine 이 기본으로 꺼져있다.
        settings.setJavaScriptEnabled(true);

        // WebView 의 js 가 할수 없는 일을 java 에 작성하고 js 에서 필요한 순간 java 의 함수를 호출해서 연동하는 구조!!!! (Hybrid)
        // java에서 공개한 객체의 함수만 호출 가능 (보안때메)
        // android는 개발자 임의 단어, 공개하는 객체의 js 객체명
        // 이름 바꾸어 여러개 객체 공개 가능
        webView.addJavascriptInterface(new JavascriptTest(), "android");

    }

    // POJO
    public class JavascriptTest {
        // 이 객체가 js에 공개되었다고 하더라도 아래의 어노테이션이 추가된 함수만 호출 가능
        @JavascriptInterface
        public String getWebData() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[");
            if (scoreList.size() <= 10) {
                int j = 0;
                for (int i = scoreList.size(); i > 0; i--) {
                    buffer.append("[" + j +",");
                    buffer.append(scoreList.get(i-1).get("score"));
                    buffer.append("]");
                    if(i>1) buffer.append(",");
                    j++;
                }
            } else {
                int j = 0;
                for (int i = 10; i > 0; i--) {
                    buffer.append("[" + j +",");
                    buffer.append(scoreList.get(i-1).get("score"));
                    buffer.append("]");
                    if(i>1) buffer.append(",");
                    j++;
                }
            }
            buffer.append("]");
            Log.d("dong", buffer.toString());
            return buffer.toString();
        }
    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId == "tab2") {
            // WebView 에 html 로딩.. html, css, js 는 resource 대상이 아님. 그래서 asset
            webView.loadUrl("file:///android_asset/test.html");
        }
    }

    // menu event...
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String sendData = scoreList.get(0).get("score") + " " + scoreList.get(0).get("date");

        int id = item.getItemId();
        if(id == R.id.menu_detail_sms) {
            // sms app 의 발송 activity 연동
            String phone = phoneView.getText().toString();
            if(phone != null && !phone.equals("")) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + phone));
                intent.putExtra("sms_body", sendData);
                startActivity(intent);
            }
        }else if(id == R.id.menu_detail_email) {
            String email = emailView.getText().toString();
            if (email != null && !email.equals("")) {
                String url = "mailto:" + email + "?subject=score&body="+sendData; // GET 방식처럼
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse(url));
                try {
                    // email app 이 없을 수도 있다는 가정하에
                    // intent 정보로 실행될 activity가 없으면 에러난다
                    startActivity(intent);
                } catch (Exception e) {
                    Toast toast = Toast.makeText(this, "no email app", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initStudentImage(String path) {
        if(path != null && !path.equals("")) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            Log.d("dong","###### in");
            if(bitmap != null) {
                Log.d("dong","###### inin");

                studentImageView.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // Intent 로 Gallery App 목록 화면
        // 결과 되돌려 받아서 처리
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    // requestCode : intent 를 발생시킨 곳에서, 그 intent 를 구분하기 위해서 준 개발자 임의 식별자
    // resultCode : intent에 의해 실행된 곳에서 결과를 되돌리기 전에 요청을 어떻게 처리해서 되돌린거다 지정 (예를 들면, 사진을 선택했어 안했어 이런거)
    // data :
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode==RESULT_OK) {
            // gallery app 에서 사진을 선택해서 되돌아 와도 사진 경로가 넘어오지 않는다.
            // 선택한 사진을 식별하는 식별자 값만 넘어온다 url 형태로.. url 맨 마지막 값이 식별자
            // 식별자를 조건으로 해서 다시 gallery app 에게 구체적으로 원하는 데이터를 요구
            // 외부앱에게 데이터를 요구하는 거다
            // 앱과 앱간의 데이터 ContentProvider 이용해서
            Uri uri = data.getData();
            String columns[] = {MediaStore.Images.Media.DATA}; // 사진 경로
            Cursor cursor = getContentResolver().query(uri, columns, null, null, null);
            cursor.moveToFirst();
            String path = cursor.getString(0);

            Log.d("dong", "path......." + path);

            if (path != null) {
                DBHelper helper = new DBHelper(this);
                SQLiteDatabase db = helper.getWritableDatabase();
                db.execSQL("update tb_student set photo=? where _id=?", new String[]{path, String.valueOf(studentId)});
                db.close();

                initStudentImage(path);
            }
        }
    }
}
