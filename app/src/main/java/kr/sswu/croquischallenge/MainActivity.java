package kr.sswu.croquischallenge;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import com.kakao.util.helper.Utility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import kr.sswu.croquischallenge.login.activity.LoginActivity;


public class MainActivity extends AppCompatActivity {
//    private static volatile MainActivity instance = null;
//
//    public static MainActivity getInstance() {
//        if (instance == null) {
//            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
//        }
//        return instance;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        instance = this;
//        findViewById(R.id.btn_login).setOnClickListener(v -> {
//            startActivity(new Intent(this, LoginActivity.class));
//        });


    }

}