package kr.sswu.croquischallenge.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.sswu.croquischallenge.Adapter.FeedAdapter;
import kr.sswu.croquischallenge.Model.FeedModel;
import kr.sswu.croquischallenge.PostActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.TimerActivity;

public class FeedFragment extends Fragment {

    //toolbar
    private ImageView menu, timer;

    //category menu
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    //feed upload btn
    private FloatingActionButton fab;

    //feed view
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private ArrayList<FeedModel> feedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        menu = view.findViewById(R.id.drawer_menu);
        timer = view.findViewById(R.id.toolbar_timer);
        fab = view.findViewById(R.id.fab);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);


        recyclerView = view.findViewById(R.id.feed_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        feedList = new ArrayList<>();
        loadFeeds();

        // drawer navigation open
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(navigationView);
            }
        });

        //timer-activity로 이동
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimerActivity.class);
                startActivity(intent);
            }
        });

        //feed upload activity로 이동
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
            }
        });

        // open category fragment
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_feed:
                        loadFeeds();
                        break;
                    case R.id.nav_anatomy:
                        searchFeeds("Anatomy");
                        break;
                    case R.id.nav_animal:
                        searchFeeds("Animal");
                        break;
                    case R.id.nav_objects:
                        searchFeeds("Objects");
                        break;
                    case R.id.nav_scenery:
                        searchFeeds("Scenery");
                        break;
                }

                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });
        return view;
    }

    private void loadFeeds() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedList.clear();

                for (DataSnapshot d : snapshot.getChildren()) {
                    String img = d.child("image").getValue().toString();
                    String name = d.child("uName").getValue().toString();
                    String email = d.child("email").getValue().toString();
                    String title = d.child("title").getValue().toString();
                    String description = d.child("description").getValue().toString();
                    String date = d.child("date").getValue().toString();
                    String category = d.child("category").getValue().toString();
                    String upload_time = d.child("upload_time").getValue().toString();

                    FeedModel feedModel = new FeedModel(img, name, email, title, description, date, category, upload_time);

                    feedList.add(feedModel);
                    adapter = new FeedAdapter(getActivity(), feedList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchFeeds(String c) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Feeds");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedList.clear();

                for (DataSnapshot d : snapshot.getChildren()) {
                    String img = d.child("image").getValue().toString();
                    String name = d.child("uName").getValue().toString();
                    String email = d.child("email").getValue().toString();
                    String title = d.child("title").getValue().toString();
                    String description = d.child("description").getValue().toString();
                    String date = d.child("date").getValue().toString();
                    String category = d.child("category").getValue().toString();
                    String upload_time = d.child("upload_time").getValue().toString();

                    FeedModel feedModel = new FeedModel(img, name, email, title, description, date, category, upload_time);

                    if (feedModel.getCategory().contains(c))
                        feedList.add(feedModel);

                    adapter = new FeedAdapter(getActivity(), feedList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}