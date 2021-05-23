package kr.sswu.croquischallenge.Model;

public class CalendarModel {
    private String uid, fid, monthYear, day, date, image, description;

    public CalendarModel() {
    }


    public CalendarModel(String uid, String fid, String monthYear, String day, String date, String image, String description) {
        this.uid = uid;
        this.fid = fid;
        this.monthYear = monthYear;
        this.day = day;
        this.date = date;
        this.image = image;
        this.description = description;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public String getMonthYear() {
        return monthYear;
    }
}
