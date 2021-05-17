package kr.sswu.croquischallenge.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.sswu.croquischallenge.Adapter.FeedAdapter;
import kr.sswu.croquischallenge.Model.FeedModel;
import kr.sswu.croquischallenge.PostImageActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.SearchActivity;
import kr.sswu.croquischallenge.TimerActivity;

public class FeedFragment extends Fragment {

    //toolbar
    private TextView subTitle;
    private ImageView menu, search, timer;

    private TabLayout tabLayout;
    private int num_category = 0;

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

        subTitle = (TextView) view.findViewById(R.id.sub_title);
        menu = view.findViewById(R.id.drawer_menu);
        search = view.findViewById(R.id.toolbar_search);
        timer = view.findViewById(R.id.toolbar_timer);
        fab = view.findViewById(R.id.fab);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);

        tabLayout = (TabLayout) view.findViewById(R.id.tab);

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

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                MenuItem category = navigationView.getCheckedItem();
                int pos = tab.getPosition();

                switch (pos) {
                    case 0:
                        if (num_category == 0)
                            loadFeeds();
                        else if (num_category == 1)
                            searchByCategoryFeeds("Anatomy");
                        else if (num_category == 2)
                            searchByCategoryFeeds("Animal");
                        else if (num_category == 3)
                            searchByCategoryFeeds("Objects");
                        else if (num_category == 4)
                            searchByCategoryFeeds("Scenery");
                        break;

                    case 1:
                        if (num_category == 0)
                            loadByPopularityFeeds();
                        else if (num_category == 1)
                            searchByPopularityByCategoryFeeds("Anatomy");
                        else if (num_category == 2)
                            searchByPopularityByCategoryFeeds("Animal");
                        else if (num_category == 3)
                            searchByPopularityByCategoryFeeds("Objects");
                        else if (num_category == 4)
                            searchByPopularityByCategoryFeeds("Scenery");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //feed upload activity로 이동
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PostImageActivity.class);
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
                        subTitle.setText("");
                        tabLayout.getTabAt(0).select();
                        loadFeeds();
                        num_category = 0;
                        break;
                    case R.id.nav_anatomy:
                        subTitle.setText("Anatomy");
                        tabLayout.getTabAt(0).select();
                        searchByCategoryFeeds("Anatomy");
                        num_category = 1;
                        break;
                    case R.id.nav_animal:
                        subTitle.setText("Animal");
                        tabLayout.getTabAt(0).select();
                        searchByCategoryFeeds("Animal");
                        num_category = 2;
                        break;
                    case R.id.nav_objects:
                        subTitle.setText("Objects");
                        tabLayout.getTabAt(0).select();
                        searchByCategoryFeeds("Objects");
                        num_category = 3;
                        break;
                    case R.id.nav_scenery:
                        subTitle.setText("Scenery");
                        tabLayout.getTabAt(0).select();
                        searchByCategoryFeeds("Scenery");
                        num_category = 4;
                        break;
                }

                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });
        return view;
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

    //전체 피드 - 인기순 정렬
    private void loadByPopularityFeeds() {
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

    //카테고리 - 최신순
    private void searchByCategoryFeeds(String c) {
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

    //카테고리 - 인기순
    private void searchByPopularityByCategoryFeeds(String c) {
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