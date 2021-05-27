package kr.sswu.croquischallenge.Model;

public class DaysInMonthModel {
    private String fid, monthYear, day, image, description;

    public DaysInMonthModel() {
    }


    public DaysInMonthModel(String fid, String monthYear, String day, String image, String description) {
        this.monthYear = monthYear;
        this.day = day;
        this.image = image;
        this.description = description;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFid() {
        return fid;
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
