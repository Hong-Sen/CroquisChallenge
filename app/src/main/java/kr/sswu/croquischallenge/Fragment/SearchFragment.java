package kr.sswu.croquischallenge.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import kr.sswu.croquischallenge.MainActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.TimerActivity;
import com.google.android.material.tabs.TabLayout;


public class SearchFragment extends Fragment {

    ImageView timer;
    TabLayout tabLayout;
    WebView webView;
    WebSettings webSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        timer = (ImageView)view.findViewById(R.id.toolbar_timer);
        tabLayout = (TabLayout)view.findViewById(R.id.webView_tab);
        webView = (WebView) view.findViewById(R.id.webView_container);
        webView.setWebViewClient(new WebViewClient());

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //default page
        webView.loadUrl("https://line-of-action.com/");

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
                int position = tab.getPosition();
                Fragment selectedFragment = null;
                if(position == 0)
                    selectedFragment = new WebView1Fragment();
                else if(position == 1)
                    selectedFragment = new WebView2Fragment();
                else if(position == 2)
                    selectedFragment = new WebView3Fragment();
                else if(position == 3)
                    selectedFragment = new WebView4Fragment();
                ((MainActivity) getActivity()).replaceFragment(R.id.webView_container, selectedFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }
}