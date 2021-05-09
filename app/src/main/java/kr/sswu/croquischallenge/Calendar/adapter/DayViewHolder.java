package kr.sswu.croquischallenge.Calendar.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

import kr.sswu.croquischallenge.Calendar.util.DateUtil;
import kr.sswu.croquischallenge.PostActivity;
import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.Calendar.model.Day;

public class DayViewHolder extends RecyclerView.ViewHolder {

    TextView itemDay;

    public DayViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);

    }

    public void initView(View v){

        itemDay = (TextView)v.findViewById(R.id.item_day);

    }

    public void bind(Day model){

        //Log.i("today","today!");

        // 일자 값 가져오기
        String day = ((Day)model).getDay();

        // 일자 값 View에 보이게하기
        itemDay.setText(day);

    };
}