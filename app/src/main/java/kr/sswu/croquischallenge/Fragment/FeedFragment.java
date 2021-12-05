package kr.sswu.croquischallenge.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.google.android.material.tabs.TabLayout;
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
    private TextView subTitle;
    private ImageView subCategory;
    private ImageView menu, timer;

    private SearchView searchView;
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

        subCategory = (ImageView) view.findViewById(R.id.sub_category);
        subTitle = (TextView) view.findViewById(R.id.sub_title);
        menu = view.findViewById(R.id.drawer_menu);

        timer = view.findViewById(R.id.toolbar_timer);
        fab = view.findViewById(R.id.fab);
        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);

        searchView = (SearchView) view.findViewById(R.id.searchView);
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

        //timer-activity로 이동
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimerActivity.class);
                startActivity(intent);
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabLayout.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tabLayout.setVisibility(View.VISIBLE);
                tabLayout.getTabAt(0).select();
                return false;
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
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
            }
        });

        // open category fragment
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                tabLayout.setVisibility(View.VISIBLE);
                searchView.setVisibility(View.INVISIBLE);
                searchView.onActionViewCollapsed();

                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_feed:
                        subCategory.setVisibility(View.GONE);
                        subTitle.setText("");
                        searchView.setVisibility(View.VISIBLE);
                        tabLayout.getTabAt(0).select();
                        loadFeeds();
                        num_category = 0;
                        break;
                    case R.id.nav_anatomy:
                        subCategory.setImageResource(R.drawable.anatomy_fill);
                        subTitle.setText("Anatomy");
                        tabLayout.getTabAt(0).select();
                        searchByCategoryFeeds("Anatomy");
                        num_category = 1;
                        break;
                    case R.id.nav_animal:
                        subCategory.setImageResource(R.drawable.animal_fill);
                        subTitle.setText("Animal");
                        tabLayout.getTabAt(0).select();
                        searchByCategoryFeeds("Animal");
                        num_category = 2;
                        break;
                    case R.id.nav_objects:
                        subCategory.setImageResource(R.drawable.objects_fill);
                        subTitle.setText("Objects");
                        tabLayout.getTabAt(0).select();
                        searchByCategoryFeeds("Objects");
                        num_category = 3;
                        break;
                    case R.id.nav_scenery:
                        subCategory.setImageResource(R.drawable.scenery_fill);
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
                if (tabLayout.getSelectedTabPosition() == 0) {
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
                if (tabLayout.getSelectedTabPosition() == 1) {
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
                if (tabLayout.getSelectedTabPosition() == 0) {
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
                if (tabLayout.getSelectedTabPosition() == 1) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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

                    adapter = new FeedAdapter(getContext(), feedList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}