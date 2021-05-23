package kr.sswu.croquischallenge;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ShowPhotoCalendarActivity extends AppCompatActivity {

    private String uid, date;

    private ImageView close, imageView;
    private TextView cal_date, memo;

    String image, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_show_photo);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        date = getIntent().getStringExtra("date");
        image = getIntent().getStringExtra("image");
        description = getIntent().getStringExtra("description");

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

        try {
            Picasso.get().load(image).into(imageView);
        } catch (Exception e) {

        }

        memo.setText(description);
    }

}