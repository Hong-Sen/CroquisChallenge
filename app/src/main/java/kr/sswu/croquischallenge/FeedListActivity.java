package kr.sswu.croquischallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.sswu.croquischallenge.Adapter.FeedAdapter;
import kr.sswu.croquischallenge.Adapter.PostAdapter;
import kr.sswu.croquischallenge.Model.FeedModel;

public class FeedListActivity extends AppCompatActivity {

    //feed view
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private ArrayList<FeedModel> feedList;
    private String uid;

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_list);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        recyclerView = findViewById(R.id.posts_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        feedList = new ArrayList<>();
        loadMyPosts();

        back = (ImageView) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadMyPosts() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");

        Query query = ref.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String img = ds.child("image").getValue().toString();
                    String name = ds.child("uName").getValue().toString();
                    String email = ds.child("email").getValue().toString();
                    String title = ds.child("title").getValue().toString();
                    String description = ds.child("description").getValue().toString();
                    String date = ds.child("date").getValue().toString();
                    String category = ds.child("category").getValue().toString();
                    String upload_time = ds.child("upload_time").getValue().toString();

                    FeedModel feedModel = new FeedModel(img, name, email, title, description, date, category, upload_time);

                    feedList.add(feedModel);

                    adapter = new PostAdapter(getApplicationContext(), feedList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}