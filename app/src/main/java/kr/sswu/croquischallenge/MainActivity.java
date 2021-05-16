package kr.sswu.croquischallenge;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kr.sswu.croquischallenge.Fragment.AddFragment;
import kr.sswu.croquischallenge.Fragment.CalendarFragment;
import kr.sswu.croquischallenge.Fragment.FeedFragment;
import kr.sswu.croquischallenge.Fragment.SearchFragment;
import kr.sswu.croquischallenge.Fragment.SettingFragment;

public class MainActivity extends AppCompatActivity {

    static MainActivity instance;

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        // splash
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_feed:
                        selectedFragment = new FeedFragment();
                        break;
                    case R.id.bottom_search:
                        selectedFragment = new SearchFragment();
                        break;
                    case R.id.bottom_calendar:
                        selectedFragment = new CalendarFragment();
                        break;
//                    case R.id.bottom_add:
//                        selectedFragment = new AddFragment();
//                        break;
                    case R.id.bottom_setting:
                        selectedFragment = new SettingFragment();
                        break;
                }

                if (selectedFragment != null) {
                    replaceFragment(R.id.fragment_container, selectedFragment);
                }

                return true;
            }
        });

        replaceFragment(R.id.fragment_container, new FeedFragment());
    }

    //open new fragment func (activity -> fragment : feedFragment에서 사용)
    public void replaceFragment(int container, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment).commit();
    }
}