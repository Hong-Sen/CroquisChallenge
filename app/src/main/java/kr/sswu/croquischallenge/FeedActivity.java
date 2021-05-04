package kr.sswu.croquischallenge;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FeedActivity extends AppCompatActivity {

    ImageView imageView;
    TextView title, description, date, uTime;//, category;
    ImageButton like, like_stroke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        imageView = (ImageView) findViewById(R.id.imageView);
        title = (TextView) findViewById(R.id.fTitle);
        description = (TextView) findViewById(R.id.fDescription);
        date = (TextView) findViewById(R.id.fDate);
        uTime = (TextView) findViewById(R.id.uTime);
        //    category = (TextView) findViewById(R.id.fCategory);
        like = (ImageButton) findViewById(R.id.like);
        like_stroke = (ImageButton) findViewById(R.id.like_stroke);

        String img = getIntent().getStringExtra("image");
        String ttl = getIntent().getStringExtra("title");
        String des = getIntent().getStringExtra("description");
        String dat = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("uTime");
        //    String cat = getIntent().getStringExtra("category");

        try {
            Picasso.get().load(img).into(imageView);
        } catch (Exception e) {

        }

        if (ttl.contentEquals(""))
            title.setVisibility(View.GONE);
        else
            title.setText(ttl);

        if (des.contentEquals(""))
            description.setVisibility(View.GONE);
        else
            description.setText(des);

        if (dat.contentEquals("Date")) {
            date.setVisibility(View.GONE);
        } else {
            date.setText(dat);
        }

    /*     if(cat.contentEquals(""))
             category.setVisibility(View.GONE);
         else
             category.setText(cat); */

        uTime.setText(time);

        like_stroke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like_stroke.setVisibility(View.GONE);
                like.setVisibility(View.VISIBLE);
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setVisibility(View.GONE);
                like_stroke.setVisibility(View.VISIBLE);
            }
        });
    }
}