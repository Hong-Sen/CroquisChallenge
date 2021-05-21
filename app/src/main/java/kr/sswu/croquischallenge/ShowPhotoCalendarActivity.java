package kr.sswu.croquischallenge;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ShowPhotoCalendarActivity extends AppCompatActivity {

    private String uid, date;

    private ImageView close, imageView;
    private TextView cal_date, memo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_show_photo);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        date = getIntent().getStringExtra("date");

        cal_date = (TextView) findViewById(R.id.cal_date);
        close = (ImageView) findViewById(R.id.btn_close);
        imageView = (ImageView) findViewById(R.id.iv_show_day_photo);
        memo = (TextView) findViewById(R.id.tv_memo);

        cal_date.setText(date);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences settings = getSharedPreferences("calendar", 0);
        imageView.setImageURI(Uri.parse(settings.getString(uid + date + "image", "")));
        memo.setText(settings.getString(uid + date + "text",""));
    }

}