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
    TextView name, title, description, txtLike;//, category;
    ImageButton like, like_stroke, imageSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        imageView = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.uName);
        title = (TextView) findViewById(R.id.fTitle);
        txtLike = (TextView) findViewById(R.id.txt_like);
        description = (TextView) findViewById(R.id.fDescription);
        like = (ImageButton) findViewById(R.id.like_fill);
        like_stroke = (ImageButton) findViewById(R.id.like_stroke);
        imageSrc = (ImageButton) findViewById(R.id.btn_src);

        String img = getIntent().getStringExtra("image");
        String uName = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String ttl = getIntent().getStringExtra("title");
        String des = getIntent().getStringExtra("description");
        String dat = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("uTime");

        try {
            Picasso.get().load(img).into(imageView);
        } catch (Exception e) {

        }

        if (uName.contentEquals(""))
            name.setText(email);
        else
            name.setText(uName);

        if (ttl.contentEquals(""))
            title.setVisibility(View.GONE);
        else
            title.setText(ttl);

        if (des.contentEquals(""))
            description.setVisibility(View.GONE);
        else
            description.setText(des);

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