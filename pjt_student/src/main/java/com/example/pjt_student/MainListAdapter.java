package com.example.pjt_student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class MainListAdapter extends ArrayAdapter<StudentVO> {
    private Context context;
    private ArrayList<StudentVO> datas; // activity가 전달하는 항목 집합 데이터..
    int resId; // activity가 전달하는 항목 layout xml 정보


    public MainListAdapter(Context context, int resId, ArrayList<StudentVO> datas) {
        super(context, resId);
        this.context = context;
        this.datas = datas;
        this.resId = resId;
    }

    // 항목 갯수를 판단하기 위해서 자동 호출
    @Override
    public int getCount() {
        return datas.size();
    }

    // 한 항목이 화면에 보일 때 마다 항목 구성을 위해 호출됨
    // 한 항목을 구성하기 위한 알고리즘!! (Not Loop!!)
    // 성능이슈 고려해서 만들어야함
    // position : 항목 idnex
    // View : 최종 리턴하는 view 부터 그 하위 계층 view들이 해당 항목에 출력
    // convertView : 해당 항목을 구성하기 위해서 재사용할 수 있는 view가 없다.. 그럼 null
    // 그 항목을 구성하기 위해서 리턴시킨 view로 화면 찍고 내부적으로 메모리에 유지했다가
    // 그 다음 재사용 가능한 곳에 전달해 준다.
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resId, null);

            MainListWrapper wrapper = new MainListWrapper(convertView);
            // 모든 view에는 개발자 임의 데이터(non-visible) 저장 가능
            // 그 view가 메모리에 지속만 된다면 언제든지 획득 가능
            convertView.setTag(wrapper); // setTag (key, value) 로 여러건 key 값으로 식별 가능

        }

        MainListWrapper wrapper = (MainListWrapper)convertView.getTag();

        ImageView studentImageView = wrapper.studentimageView;
        TextView nameView = wrapper.nameView;
        final ImageView phoneView = wrapper.phoneView; // 로컬 변수에 무조건 final로 되어 있는건 inner 클래스에서 접근하기 위해

        final StudentVO vo = datas.get(position);

        // 왜 커스텀을 할수밖에 없었는지에 대한 로직이 들어감
        nameView.setText(vo.name);

        // OOM 문제 발생한다.
        if(vo.photo != null && !vo.photo.equals("")) {
            // 작업 옵션 지정이 가능하다.
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 10; // 10분의 1로 줄여서 로딩한다.
            Bitmap bitmap = BitmapFactory.decodeFile(vo.photo);
            if(bitmap != null) { // 유저가 지웠을 수도 있어서
                studentImageView.setImageBitmap(bitmap);
            }
        } else {
            // 사진 업로드한거 없으면 기본 이미지
            studentImageView.setImageDrawable(ResourcesCompat.getDrawable(
                    context.getResources(), R.drawable.ic_student_small, null
            ));
        }

        studentImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogRoot = inflater.inflate(R.layout.dialog_student_image, null);
                ImageView dialogImageView = dialogRoot.findViewById(R.id.dialog_image);

                if(vo.photo != null && !vo.photo.equals("")) {
                    // 작업 옵션 지정이 가능하다.
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 10; // 10분의 1로 줄여서 로딩한다.
                    Bitmap bitmap = BitmapFactory.decodeFile(vo.photo);
                    if(bitmap != null) { // 유저가 지웠을 수도 있어서
                        dialogImageView.setImageBitmap(bitmap);
                    }
                } else {
                    // 사진 업로드한거 없으면 기본 이미지
                    dialogImageView.setImageDrawable(ResourcesCompat.getDrawable(
                            context.getResources(), R.drawable.ic_student_large, null
                    ));
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogRoot);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vo.phone != null && !vo.phone.equals("")) {
                    // Call App Activity 를 인텐트로 날려
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + vo.phone));
                    context.startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(context, R.string.main_list_phone_error, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return convertView;
    }
}
