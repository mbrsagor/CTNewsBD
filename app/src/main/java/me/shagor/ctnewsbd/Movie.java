package me.shagor.ctnewsbd;

/**
 * Created by shagor on 7/15/2017.
 */
public class Movie {
    private String id,title, thumbnailUrl,date;

    public Movie() {
    }

    public Movie(String id,String name, String thumbnailUrl, String date) {
        this.id = id;
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.date = date;
    }
    public String getId() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}