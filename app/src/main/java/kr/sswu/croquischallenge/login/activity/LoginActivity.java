package kr.sswu.croquischallenge.login.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nhn.android.naverlogin.OAuthLogin;

import kr.sswu.croquischallenge.MainActivity;
import kr.sswu.croquischallenge.R;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private static final String TAG = "";
    GoogleSignInClient mGoogleSignInClient;

    CallbackManager callbackManager;
    OAuthLogin mOAuthLoginModule;
    Context mContext;
    TextView userNameTxt;
    private Button naverLoginBtn;

    private FirebaseAuth firebaseAuth;
    private SignInButton googleLoginBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
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
         */

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        firebaseAuth = FirebaseAuth.getInstance();

        googleLoginBtn = (SignInButton) findViewById(R.id.google_login_btn);
        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //user email
                            Toast.makeText(getApplicationContext(), user.getEmail().toString(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
             //   Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
