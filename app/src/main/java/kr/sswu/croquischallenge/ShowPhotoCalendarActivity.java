package kr.sswu.croquischallenge;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ShowPhotoCalendarActivity extends AppCompatActivity {

    Button close;
    ImageView imageView;
    TextView memo;
    private long date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_show_photo);

        date = getIntent().getLongExtra("date", 0L);
        close = (Button) findViewById(R.id.btn_close);
        imageView = (ImageView) findViewById(R.id.iv_show_day_photo);
        memo = (TextView) findViewById(R.id.tv_memo);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences settings = getSharedPreferences("calendar", 0);
        imageView.setImageURI(Uri.parse(settings.getString("" + date, "")));
    }

}