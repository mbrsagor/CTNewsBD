package me.shagor.ctnewsbd;

/**
  Created by shagor on 7/24/2017.
 */

public class FeathareConfig {
    private String first_text,first_title,first_date,second_tilte,second_date,third_title,third_date,
            fourth_title,fourth_date,thumbnails,left_thumb,right_thumb;

    public FeathareConfig() {

    }

    public FeathareConfig(String first_text, String first_title, String first_date, String second_tilte,
                          String second_date, String third_title, String third_date, String fourth_title,
                          String fourth_date, String thumbnails,String left_thumb,String right_thumb)
    {
        this.first_text = first_text;
        this.first_title = first_title;
        this.first_date = first_date;
        this.second_tilte = second_tilte;
        this.second_date = second_date;
        this.third_title = third_title;
        this.third_date = third_date;
        this.fourth_title = fourth_title;
        this.fourth_date = fourth_date;
        this.thumbnails = thumbnails;
        this.left_thumb = left_thumb;
        this.right_thumb = right_thumb;
    }



    public String getFirst_text() {
        return first_text;
    }

    public void setFirst_text(String first_text) {
        this.first_text = first_text;
    }

    public String getFirst_title() {
        return first_title;
    }

    public void setFirst_title(String first_title) {
        this.first_title = first_title;
    }

    public String getFirst_date() {
        return first_date;
    }

    public void setFirst_date(String first_date) {
        this.first_date = first_date;
    }

    public String getSecond_tilte() {
        return second_tilte;
    }

    public void setSecond_tilte(String second_tilte) {
        this.second_tilte = second_tilte;
    }

    public String getSecond_date() {
        return second_date;
    }

    public void setSecond_date(String second_date) {
        this.second_date = second_date;
    }

    public String getThird_title() {
        return third_title;
    }

    public void setThird_title(String third_title) {
        this.third_title = third_title;
    }

    public String getThird_date() {
        return third_date;
    }

    public void setThird_date(String third_date) {
        this.third_date = third_date;
    }

    public String getFourth_title() {
        return fourth_title;
    }

    public void setFourth_title(String fourth_title) {
        this.fourth_title = fourth_title;
    }

    public String getFourth_date() {
        return fourth_date;
    }

    public void setFourth_date(String fourth_date) {
        this.fourth_date = fourth_date;
    }


    public String getThumbnailUrl() {
        return thumbnails;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnails = thumbnailUrl;
    }

    public String getLeft_thumb() {
        return left_thumb;
    }

    public void setLeft_thumb(String left_thumb) {
        this.left_thumb = left_thumb;
    }

    public String getRight_thumb() {
        return right_thumb;
    }

    public void setRight_thumb(String right_thumb) {
        this.right_thumb = right_thumb;
    }

}
