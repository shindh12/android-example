package com.example.pjt_student;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {
    private Context context;
    private int score;
    private int color;

    // view를 activity java 에서 직접 생성해서 화면 구성한다면??
    // ==> 생성자는 하나만
    // view를 layout xml 에 등록해서 화면 구성한다면??
    // ==> 개발자 코드로 객체가 생성되는게 아니라 상황에 따라 호출되는 생성자가 다름
    // 그래서 layout xml 등로게 의한 화면 구성이 가능하게 하려면 생성자 3개 모두 정의해야함
    public MyView(Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    // xml 에 있는 속성값을 참조하려고 전달받음
    public MyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init(attributeSet);
    }

    public MyView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        this.context = context;
        init(attributeSet);
    }

    private void init(AttributeSet attributeSet) {
        if (attributeSet != null) {
            // activity 에서 지정한 속성 값 획득
            TypedArray array = context.obtainStyledAttributes(attributeSet, R.styleable.AAA);
            color = array.getColor(R.styleable.AAA_scoreColor, Color.YELLOW);
        }
    }

    // activity 에서 score 전달 목적으로 호출
    public void setScore(int score) {
        this.score = score;
        // 새로운 score 전달되면 그림 다시 그려야함
        invalidate(); // 자동으로 onDraw() 함수 다시 호출
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 화면 지우고 다시 그려야함
        canvas.drawColor(Color.alpha(Color.CYAN));

        RectF rectF = new RectF(15, 15, 70, 70);
        // 그리기 속성
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        paint.setAntiAlias(true);

        //기본원 회색으로 360도 그려내고
        canvas.drawArc(rectF, 0, 360, false, paint);
        float endAngle = (360 * score) / 100;
        // score 그리기

        paint.setColor(color);
        // 0도가 동쪽이고 북쪽은 -90
        canvas.drawArc(rectF, -90, endAngle, false, paint);
    }
}
