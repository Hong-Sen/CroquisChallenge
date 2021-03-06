package kr.sswu.croquischallenge.Model;

public class FeedModel {
    private String image, fid, ref, name, email, title, description, date, category, upload_time, likes;

    public FeedModel() {
    }

    public FeedModel(String fid, String image, String ref, String name, String email, String title, String description, String date, String category, String upload_time, String likes) {
        this.fid = fid;
        this.image = image;
        this.ref = ref;
        this.name = name;
        this.email = email;
        this.title = title;
        this.description = description;
        this.date = date;
        this.category = category;
        this.upload_time = upload_time;
        this.likes = likes;
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

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef() {
        return ref;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
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

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getUpload_time() {
        return upload_time;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getLikes() {
        return likes;
    }
}