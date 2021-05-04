package kr.sswu.croquischallenge.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;
import kr.sswu.croquischallenge.R;

public class WebView2Fragment extends Fragment {

    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web_view2, container, false);

        webView = view.findViewById(R.id.webView2);
        webView.loadUrl("https://www.characterdesigns.com/");

        return view;
    }
}