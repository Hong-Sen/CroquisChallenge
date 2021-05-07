package kr.sswu.croquischallenge.Calendar.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kr.sswu.croquischallenge.R;
import kr.sswu.croquischallenge.Calendar.model.CalendarHeader;

import static kr.sswu.croquischallenge.R.id.item_header_title;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    TextView itemHeaderTitle;

    public HeaderViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);
    }


    public void initView(View v){

        itemHeaderTitle = (TextView)v.findViewById(item_header_title);

    }

    public void bind(CalendarHeader model){

        // 일자 값 가져오기
        String header = ((CalendarHeader)model).getHeader();

        // header에 표시하기, ex : 2018년 8월
        itemHeaderTitle.setText(header);


    };
}