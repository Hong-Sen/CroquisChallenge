package kr.sswu.croquischallenge.Model;

public class FeedModel {
    private String image, date;

    public FeedModel() { }

    public FeedModel(String image, String date) {
        this.image = image;
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
