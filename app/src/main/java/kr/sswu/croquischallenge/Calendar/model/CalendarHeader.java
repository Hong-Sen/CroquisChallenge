package kr.sswu.croquischallenge.Calendar.model;

import kr.sswu.croquischallenge.Calendar.util.DateUtil;

public class CalendarHeader extends ViewModel {

    String header;
    long mCurrentTime;

    public CalendarHeader() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(long time) {

        String value = DateUtil.getDate(time, DateUtil.YEAR_FORMAT + "년 " + DateUtil.MONTH_FORMAT + "월");
        this.header = value;

    }

    public void setHeader(String header) {

        this.header = header;

    }

}