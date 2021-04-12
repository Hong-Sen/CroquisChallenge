package kr.sswu.croquischallenge.Fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.likeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class SettingFragment extends Fragment {

    Button btnLogout;
    Button btnRemove;
    Button btnList;
    TextView imt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //fcm cloudmessage token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();

                        Log.d("FCM Log", "FCM 토큰: "+ token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();

                    }
                });

        //좋아요 리스트
        btnList = (Button)view.findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), likeActivity.class);
                startActivity(intent);
            }
        });


        //로그아웃
        btnLogout = (Button)view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show();
            }
        });


        //서비스 탈퇴
        btnRemove = (Button)view.findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "서비스 탈퇴 완료", Toast.LENGTH_SHORT).show();
            }
        });

        imt = (TextView)view.findViewById(R.id.imt);
        imt.setText(" 개발자 성신여자대학교 정시공 에이틴");

        return view;
    }
}