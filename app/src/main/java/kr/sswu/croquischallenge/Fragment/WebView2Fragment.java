package kr.sswu.croquischallenge.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import kr.sswu.croquischallenge.R;


public class WebView2Fragment extends Fragment {

    WebView webView;
    WebSettings webSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_web_view2, container, false);

        webView = view.findViewById(R.id.webView2);
        webView.loadUrl("https://vimeo.com/croquiscafe");

        return view;
    }
}