package kr.sswu.croquischallenge.Calendar.model;

import java.util.Calendar;

import kr.sswu.croquischallenge.Calendar.util.DateUtil;

public class Day extends ViewModel {

    String day;

    public Day() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setCalendar(Calendar calendar){

        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);

    }
}