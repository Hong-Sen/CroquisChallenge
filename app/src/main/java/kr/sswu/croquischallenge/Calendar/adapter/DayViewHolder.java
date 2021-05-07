package kr.sswu.croquischallenge.Calendar.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.Calendar.model.Day;

public class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder

    TextView itemDay;

    public DayViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);

    }

    public void initView(View v){

        itemDay = (TextView)v.findViewById(R.id.item_day);

    }

    public void bind(Day model){

        // 일자 값 가져오기
        String day = ((Day)model).getDay();

        // 일자 값 View에 보이게하기
        itemDay.setText(day);

    };
}