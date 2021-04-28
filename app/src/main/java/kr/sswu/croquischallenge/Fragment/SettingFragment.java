package kr.sswu.croquischallenge.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import kr.sswu.croquischallenge.MainActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.likeActivity;
import kr.sswu.croquischallenge.login.UserProfileConstants;
import kr.sswu.croquischallenge.login.activity.LoginActivity;

public class SettingFragment extends Fragment {

    Button btnLogout;
    Button btnRemove;
    Button btnList;
    TextView imt;
    TextView myAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 닉네임 shared preference로 부터 가져옴
        // 값이 없으면 로그인 안했다는 뜻이므로 "로그인 필요" 표시
        SharedPreferences preference = getActivity().getSharedPreferences(UserProfileConstants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        String username =  preference.getString(UserProfileConstants.USER_NAME, "로그인 필요");
        myAccount = (TextView) view.findViewById(R.id.myAccount);
        myAccount.setText(
                getString(R.string.text1, username)
        );

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

                        Log.d("FCM Log", "FCM 토큰: " + token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();

                    }
                });

        //좋아요 리스트
        btnList = (Button) view.findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), likeActivity.class);
                startActivity(intent);
            }
        });


        //로그아웃
        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preference.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        //서비스 탈퇴
        btnRemove = (Button) view.findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "서비스 탈퇴 완료", Toast.LENGTH_SHORT).show();
            }
        });

        imt = (TextView) view.findViewById(R.id.imt);
        imt.setText(" 개발자 성신여자대학교 정시공 에이틴");

        return view;
    }
}