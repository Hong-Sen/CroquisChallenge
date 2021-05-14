package kr.sswu.croquischallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class likeActivity extends Activity {

    //feed view
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private ArrayList<FeedModel> likedList;
    private String uid;

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        recyclerView = findViewById(R.id.posts_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        likedList = new ArrayList<>();
        loadLikedPosts();

        back = (ImageView) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadLikedPosts() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Likes");
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likedList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String likedFid = ds.getRef().getKey();

                    getFeeds(likedFid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFeeds(String likedFid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
        ref.orderByChild("fid").equalTo(likedFid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String fid = ds.child("fid").getValue().toString();
                            String img = ds.child("image").getValue().toString();
                            String ref = ds.child("ref").getValue().toString();
                            String name = ds.child("uName").getValue().toString();
                            String email = ds.child("email").getValue().toString();
                            String title = ds.child("title").getValue().toString();
                            String description = ds.child("description").getValue().toString();
                            String date = ds.child("date").getValue().toString();
                            String category = ds.child("category").getValue().toString();
                            String upload_time = ds.child("upload_time").getValue().toString();
                            String likes = ds.child("likes").getValue().toString();

                            FeedModel feed = new FeedModel(fid, img, ref, name, email, title, description, date, category, upload_time, likes);

                            likedList.add(feed);
                        }

                        adapter = new FeedAdapter(likeActivity.this, likedList);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
