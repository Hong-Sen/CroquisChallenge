package kr.sswu.croquischallenge.login.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.CallbackManager;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.sswu.croquischallenge.MainActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.login.UserProfileConstants;

public class LoginActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    OAuthLogin mOAuthLoginModule;
    Context mContext;
    TextView userNameTxt;
    private Button naverLoginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences preference = getSharedPreferences(UserProfileConstants.PREFERENCE_KEY, Context.MODE_PRIVATE);
        Boolean isLoggedIn = preference.getBoolean(UserProfileConstants.IS_LOGGED_IN,false);
        if(isLoggedIn){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            userNameTxt = (TextView) findViewById(R.id.userNameTxt);

            naverLoginBtn = (Button) findViewById(R.id.naver_login_button);
            mContext = getApplicationContext();

            naverLoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOAuthLoginModule = OAuthLogin.getInstance();
                    mOAuthLoginModule.init(
                            mContext
                            , getString(R.string.naver_client_id)
                            , getString(R.string.naver_client_secret)
                            , getString(R.string.naver_client_name)
                    );
                    @SuppressLint("HandlerLeak")
                    OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
                        @Override
                        public void run(boolean success) {
                            if (success) {
                                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                                String tokenType = mOAuthLoginModule.getTokenType(mContext);

                                Log.i("LoginData", "accessToken : " + accessToken);
                                Log.i("LoginData", "refreshToken : " + refreshToken);
                                Log.i("LoginData", "expiresAt : " + expiresAt);
                                Log.i("LoginData", "tokenType : " + tokenType);

                                Log.e("Naver_SESSION", "네이버 로그인 성공");

                                // 프로필 정보 가져오기
                                String apiURL = "https://openapi.naver.com/v1/nid/me";
                                String token = "Bearer " + accessToken;
                                Map<String, String> requestHeaders = new HashMap<>();
                                requestHeaders.put("Authorization", token);
                                AndroidNetworking.get(apiURL)
                                        .addHeaders(requestHeaders)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                //{
                                                // "resultcode":"00",
                                                // "message":"success",
                                                // "response":{
                                                // "id":"",
                                                // "email":"",
                                                // "name":"홍세은"}
                                                // }

                                                try {
                                                    SharedPreferences.Editor editor = preference.edit();
                                                    editor.putString(UserProfileConstants.USER_NAME, response.getJSONObject("response").getString("name"));
                                                    editor.putBoolean(UserProfileConstants.IS_LOGGED_IN,true);
                                                    editor.apply();
                                                } catch (JSONException e) {
                                                    Log.d("Login", response.keys().toString());
                                                    e.printStackTrace();
                                                }

                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                Log.d("Login", response.toString());
                                            }

                                            @Override
                                            public void onError(ANError anError) {

                                            }
                                        });
                            } else {
                                String errorCode = mOAuthLoginModule
                                        .getLastErrorCode(mContext).getCode();
                                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                                Toast.makeText(mContext, "errorCode:" + errorCode
                                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                            }
                        }

                        ;
                    };
                    mOAuthLoginModule.startOauthLoginActivity(LoginActivity.this, mOAuthLoginHandler);
                }
            });
        }
    }
}
