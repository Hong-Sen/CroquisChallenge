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
import kr.sswu.croquischallenge.MainActivity;
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
    private Fragment selectedFragment = null;

    //feed upload btn
    private FloatingActionButton fab;

    //feed view
    private RecyclerView recyclerView;
    private FeedAdapter adapter;
    private ArrayList<FeedModel> feedList;

    // private FirestoreRecyclerAdapter<FeedModel, FeedViewHolder> adapter;
    //  private RecyclerView.LayoutManager layoutManager;
    //  private ArrayList<FeedModel> arrayList;
    //private FirebaseFirestore firestore;


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
        /*
        adapter = new FeedAdapter(getContext(), feedList);

        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        firestore.collection("feeds").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d : list) {
                                FeedModel feed = d.toObject(FeedModel.class);
                                feed.setImageUrl(d.getString("feedId"));
                                feedList.add(feed);
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                */

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
                        selectedFragment = new AllFragment();
                        break;
                    case R.id.nav_anatomy:
                        selectedFragment = new AnatomyFragment();
                        break;
                    case R.id.nav_animal:
                        selectedFragment = new AnimalFragment();
                        break;
                    case R.id.nav_objects:
                        selectedFragment = new ObjectsFragment();
                        break;
                    case R.id.nav_scenery:
                        selectedFragment = new SceneryFragment();
                        break;
                }
                if (selectedFragment != null) {
                    ((MainActivity) getActivity()).replaceFragment(R.id.nav_host_fragment, selectedFragment);
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
                    String time = d.child("date").getValue().toString();
                    FeedModel feedModel = new FeedModel(img, time);

                    feedList.add(feedModel);

                    adapter = new FeedAdapter(getActivity(), feedList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

