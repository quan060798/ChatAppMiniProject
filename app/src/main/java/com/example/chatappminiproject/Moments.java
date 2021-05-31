package com.example.chatappminiproject;

public class Moments {

    private String description;
    private String momentid;
    private String momentimageurl;
    private String poster;
    private String uploadtime;

    public Moments(String description, String momentid, String momentimageurl, String poster, String uploadtime) {
        this.description = description;
        this.momentid = momentid;
        this.momentimageurl = momentimageurl;
        this.poster = poster;
        this.uploadtime = uploadtime;
    }

    public Moments(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMomentid() {
        return momentid;
    }

    public void setMomentid(String momentid) {
        this.momentid = momentid;
    }

    public String getMomentimageurl() {
        return momentimageurl;
    }

    public void setMomentimageurl(String momentimageurl) {
        this.momentimageurl = momentimageurl;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getUploadtime() {
        return uploadtime;
    }

    public void setUploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }
}
