package kr.sswu.croquischallenge.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import kr.sswu.croquischallenge.Calendar.adapter.CalendarAdapter;
import kr.sswu.croquischallenge.Calendar.util.Keys;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.TimerActivity;

import static android.content.ContentValues.TAG;

public class CalendarFragment extends Fragment {

    ImageView timer;
    public int mCenterPosition;
    private long mCurrentTime;
    public ArrayList<Object> mCalendarList = new ArrayList<>();

    public TextView textView;
    public RecyclerView recyclerView;
    private CalendarAdapter mAdapter;
    private StaggeredGridLayoutManager manager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);
        timer = view.findViewById(R.id.toolbar_timer);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TimerActivity.class);
                startActivity(intent);
            }
        });

        initView(view);

        initSet();

        setRecycler();

        return view;
    }

    public void initView(View v){

        textView = (TextView)v.findViewById(R.id.item_header_title);
        recyclerView = (RecyclerView)v.findViewById(R.id.calendar);

    }

    public void initSet(){

        initCalendarList();

    }

    public void initCalendarList() {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

    private void setRecycler() {

        if (mCalendarList == null) {
            Log.w(TAG, "No Query, not initializing RecyclerView");
        }

        manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);

        mAdapter = new CalendarAdapter(mCalendarList);

        mAdapter.setCalendarList(mCalendarList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            recyclerView.scrollToPosition(mCenterPosition);
        }
    }

    public void setCalendarList(GregorianCalendar cal) {

        ArrayList<Object> calendarList = new ArrayList<>();

        for (int i = -300; i < 300; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1);
                if (i == 0) {
                    mCenterPosition = calendarList.size();
                }

                // 타이틀
                calendarList.add(calendar.getTimeInMillis());

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //빈칸은 해당 월에 시작하는 요일 -1
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

                // EMPTY 생성
                for (int j = 0; j < dayOfWeek; j++) {
                    calendarList.add(Keys.EMPTY);
                }
                for (int j = 1; j <= max; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
                }

                // TODO : 결과 값 넣을 때 여기에 하기

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mCalendarList = calendarList;
    }
}