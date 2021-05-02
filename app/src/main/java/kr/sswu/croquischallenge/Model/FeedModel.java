package kr.sswu.croquischallenge.Model;

public class FeedModel {
    private String upload_time, image, title, description, date, category;

    public FeedModel() { }

    public FeedModel(String upload_time, String image, String title, String description, String date, String category) {
        this.upload_time = upload_time;
        this.image = image;
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
