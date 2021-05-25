package kr.sswu.croquischallenge.login.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import kr.sswu.croquischallenge.MainActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.RegisterActivity;
import kr.sswu.croquischallenge.SplashActivity;
import kr.sswu.croquischallenge.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

    private TextInputEditText edit_email, edit_pw;
    private Button btn_signIn;
    private TextView txt_recoverPw,txt_create, txt_message;

    private ProgressDialog progressDialog;

    //view binding
    private ActivityLoginBinding binding;

    //naver sign in
     /*
    CallbackManager callbackManager;
    OAuthLogin mOAuthLoginModule;
    Context mContext;
    TextView userNameTxt;
    private Button naverLoginBtn;
*/

    //google sign in
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // splash
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

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

        edit_email = (TextInputEditText) findViewById(R.id.edit_email);
        edit_pw = (TextInputEditText) findViewById(R.id.edit_pw);
        txt_recoverPw = (TextView) findViewById(R.id.txt_recoverPw);
        btn_signIn = (Button) findViewById(R.id.btn_signIn);
        txt_create = (TextView) findViewById(R.id.txt_create);
        txt_message = (TextView) findViewById(R.id.txt_message);

        txt_recoverPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoverPasswordDialog();
            }
        });

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString().trim();
                String pw = edit_pw.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    edit_email.setError("Invalid Email");
                    edit_email.setFocusable(true);
                } else {
                    loginUser(email, pw);
                }
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

    /*    binding.googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });*/

        txt_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        progressDialog = new ProgressDialog(this);
    }

    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        builder.setMessage("Enter your user account's verified email and we will send you a password reset link.");

        LinearLayout linearLayout = new LinearLayout(this);

        EditText edit_email = new EditText(this);
        edit_email.setPadding(20, 20, 20, 20);
        edit_email.setHint("Enter your email address");
        edit_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        edit_email.setMinEms(30);

        linearLayout.addView(edit_email);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email = edit_email.getText().toString().trim();
                beginRecovery(email);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void beginRecovery(String email) {
        progressDialog.setMessage("Sending email..");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.show();
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String pw) {
        progressDialog.setMessage("Sign in..");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.d(TAG, "signInWithEmail : success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Log.w(TAG, "signInWithEmail : failure", task.getException());
                        //    Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                txt_message.setText(e.getMessage());
            //    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Log.d(TAG, "Already logged in");

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
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
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                String email = user.getEmail();
                                String uid = user.getUid();

                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("email", email);
                                hashMap.put("uid", uid);
                                hashMap.put("name", "");

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference reference = database.getReference("Users");
                                reference.child(uid).setValue(hashMap);
                            }

                            Toast.makeText(getApplicationContext(), "" + user.getEmail(), Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login Failed..", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
