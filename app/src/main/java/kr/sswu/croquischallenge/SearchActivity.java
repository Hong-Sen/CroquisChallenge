package kr.sswu.croquischallenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.sswu.croquischallenge.Adapter.FeedAdapter;
import kr.sswu.croquischallenge.Model.FeedModel;

public class SearchActivity extends AppCompatActivity {

    private ImageView back, timer;
    private SearchView searchView;

    //feed view
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private ArrayList<FeedModel> feedList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = (ImageView) findViewById(R.id.toolbar_back);
        timer = (ImageView) findViewById(R.id.toolbar_timer);
        searchView = (SearchView) findViewById(R.id.searchView);

        recyclerView = findViewById(R.id.feed_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        feedList = new ArrayList<>();

        loadFeeds();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //timer-activity로 이동
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TimerActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!TextUtils.isEmpty(s))
                    searchFeeds(s);
                else
                    loadFeeds();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (!TextUtils.isEmpty(s))
                    searchFeeds(s);
                else
                    loadFeeds();
                return false;
            }
        });

    }

    //전체 피드 - 최신순
    private void loadFeeds() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedList.clear();

                for (DataSnapshot d : snapshot.getChildren()) {
                    String fid = d.child("fid").getValue().toString();
                    String img = d.child("image").getValue().toString();
                    String ref = d.child("ref").getValue().toString();
                    String name = d.child("uName").getValue().toString();
                    String email = d.child("email").getValue().toString();
                    String title = d.child("title").getValue().toString();
                    String description = d.child("description").getValue().toString();
                    String date = d.child("date").getValue().toString();
                    String category = d.child("category").getValue().toString();
                    String upload_time = d.child("upload_time").getValue().toString();
                    String likes = d.child("likes").getValue().toString();

                    FeedModel feedModel = new FeedModel(fid, img, ref, name, email, title, description, date, category, upload_time, likes);

                    feedList.add(feedModel);
                    adapter = new FeedAdapter(getApplicationContext(), feedList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //피드 검색
    private void searchFeeds(String c) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
        ref.orderByChild("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedList.clear();

                for (DataSnapshot d : snapshot.getChildren()) {
                    String fid = d.child("fid").getValue().toString();
                    String img = d.child("image").getValue().toString();
                    String ref = d.child("ref").getValue().toString();
                    String name = d.child("uName").getValue().toString();
                    String email = d.child("email").getValue().toString();
                    String title = d.child("title").getValue().toString();
                    String description = d.child("description").getValue().toString();
                    String date = d.child("date").getValue().toString();
                    String category = d.child("category").getValue().toString();
                    String upload_time = d.child("upload_time").getValue().toString();
                    String likes = d.child("likes").getValue().toString();

                    FeedModel feedModel = new FeedModel(fid, img, ref, name, email, title, description, date, category, upload_time, likes);

                    if (feedModel.getName().contains(c) || feedModel.getEmail().contains(c) || feedModel.getTitle().contains(c) || feedModel.getDescription().contains(c))
                        feedList.add(feedModel);

                    adapter = new FeedAdapter(getApplicationContext(), feedList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}