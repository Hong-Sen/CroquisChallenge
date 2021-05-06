package kr.sswu.croquischallenge.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.TimerActivity;


public class AddFragment extends Fragment {

    ImageView timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_add, container, false);

        timer = view.findViewById(R.id.toolbar_timer);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimerActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}