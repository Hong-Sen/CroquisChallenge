package kr.sswu.croquischallenge.Model;

public class Feed {
    private String imageUrl;
    private String category;
    //

    public Feed() { }

    public Feed(String imageUrl, String category){
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
