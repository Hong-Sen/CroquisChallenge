package kr.sswu.croquischallenge.Model;

public class FeedModel {
    private String imageUrl;
 //   private String category;
    //

    public FeedModel() { }

    public FeedModel(String imageUrl, String category){
        this.imageUrl = imageUrl;
     //   this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

 /*   public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    } */
}
