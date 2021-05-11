package kr.sswu.croquischallenge;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FeedActivity extends AppCompatActivity {

    ImageView back,imageView;
    TextView name, title, date, description, txtLike;//, category;
    ImageButton like, like_stroke, imageSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        back = (ImageView) findViewById(R.id.btn_back);
        imageView = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.uName);
        title = (TextView) findViewById(R.id.fTitle);
        date = (TextView) findViewById(R.id.fDate);
        txtLike = (TextView) findViewById(R.id.txt_like);
        description = (TextView) findViewById(R.id.fDescription);
        like = (ImageButton) findViewById(R.id.like_fill);
        like_stroke = (ImageButton) findViewById(R.id.like_stroke);
        imageSrc = (ImageButton) findViewById(R.id.btn_src);

        String img = getIntent().getStringExtra("image");
        String ref = getIntent().getStringExtra("ref");
        String uName = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String ttl = getIntent().getStringExtra("title");
        String des = getIntent().getStringExtra("description");
        String dat = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("uTime");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        if (dat.contentEquals("Date"))
            date.setVisibility(View.GONE);
        else
            date.setText(dat);

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

        imageSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ref.equals("")) {
                    ImageView img = new ImageView(FeedActivity.this);
                    try {
                        Picasso.get().load(ref).into(img);
                    } catch (Exception e) {

                    }
                    img.setPadding(70, 30, 70, 30);
                    androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(FeedActivity.this);
                    builder.setTitle("References");
                    builder.setView(img);
                    builder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                } else {
                    Toast.makeText(getApplicationContext(), "No reference image", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}