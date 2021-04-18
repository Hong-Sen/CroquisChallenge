package kr.sswu.croquischallenge.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import kr.sswu.croquischallenge.Adapter.FeedAdapter;
import kr.sswu.croquischallenge.MainActivity;
import kr.sswu.croquischallenge.Model.Feed;
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
    private RecyclerView recyclerView;
    private ArrayList<Feed> list;
    private FeedAdapter adapter;
 //   private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Image");

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
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        list = new ArrayList<>();
        adapter = new FeedAdapter(getContext(), list);
        /*
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Feed feed = dataSnapshot.getValue(Feed.class);
                    list.add(feed);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                        selectedFragment = new FeedFragment();
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
}
