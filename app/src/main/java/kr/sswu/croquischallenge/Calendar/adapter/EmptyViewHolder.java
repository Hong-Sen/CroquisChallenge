package kr.sswu.croquischallenge.Calendar.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kr.sswu.croquischallenge.Calendar.model.EmptyDay;

public class EmptyViewHolder extends RecyclerView.ViewHolder {


    public EmptyViewHolder(@NonNull View itemView) {
        super(itemView);

        initView(itemView);
    }

    public void initView(View v){

    }

    public void bind(EmptyDay model){


    };
}