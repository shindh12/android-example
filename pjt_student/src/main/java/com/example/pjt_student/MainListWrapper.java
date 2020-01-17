package com.example.pjt_student;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

// findViewById 의 성능 이슈.. 한번은 find 한다. find한 view 를 메모리에 저장했다가
// 그 다음번 이용 시 find 없이 그대로 획득해서 사용하자
// 한 항목에 find 대상이 되는 view 가 여러개라면 하나하나 따로 저장/획득이 힘들어서 묶어서 퉁으로
public class MainListWrapper {
    // 항목에 찍히는 모든 view를 선언할 필요 없다. find 대상이 되는 애들만..
    ImageView studentimageView;
    TextView nameView;
    ImageView phoneView;

    // 한번은 find 해야 한다. find 하기 위해서는 view 계층의 root 객체 있어야 한다.
    // layout 초기화 하는 역활이 adapter 이므로 adapter 에 있다. adapter 가 전달할거다

    // 가정1 : view가 필요한 곳은 adapter 이다. adapter 에서 find 하지 않고
    // 이 wrapper 가 가지는 view를 그대로 획득해서 사용한다는 가정
    // 가정2 : adapter에서 이 wrapper 객체를 메모리에 지속시킨다는 가정
    public MainListWrapper(View root) {
        studentimageView = root.findViewById(R.id.main_item_student_image);
        nameView = root.findViewById(R.id.main_item_name);
        phoneView = root.findViewById(R.id.main_item_contact);
    }
}
