package com.softnep.radiodailymail.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ADMIN on 2017-11-09.
 */

public class NationalNewsReceiveParams {


    @SerializedName("news-37")
    private List<News37Bean> news37;

    public List<News37Bean> getNews37() {
        return news37;
    }

    public void setNews37(List<News37Bean> news37) {
        this.news37 = news37;
    }

    @Table(name = "NationalNews")
    public static class News37Bean extends Model implements Serializable{
        /**
         * id : 96
         * title : à¤¬à¤¾à¤¢à¥€ à¤° à¤¡à¥à¤¬à¤¾à¤¨à¤•à¤¾ à¤•à¤¾à¤°à¤£ à¤•à¥ˆà¤²à¤¾à¤²à¥€à¤®à¤¾ à¤šà¤¾à¤° à¤¸à¤¯ à¤˜à¤° à¤ªà¤°à¤¿à¤µà¤¾à¤° à¤¬à¤¿à¤¸à¥à¤¥à¤¾à¤ªà¤¿à¤¤ , à¤–à¥‹à¤²à¤¾ à¤¤à¤°à¥à¤¨à¥‡ à¤•à¥à¤°à¤®à¤®à¤¾ à¤¡à¥à¤µà¥‡à¤° à¥«à¥« à¤µà¤°à¥à¤·à¥€à¤¯à¤¾ à¤à¤• à¤®à¤¹à¤¿à¤²à¤¾ à¤µà¥‡à¤ªà¤¤à¥à¤¤à¤¾
         * image : ae7f59a1f3d9ff543369cbbda9222d11.jpg
         * description : à¤¬à¤¾à¤¢à¥€ à¤° à¤¡à¥à¤¬à¤¾à¤¨à¤•à¤¾ à¤•à¤¾à¤°à¤£ à¤•à¥ˆà¤²à¤¾à¤²à¥€à¤®à¤¾ à¤šà¤¾à¤° à¤¸à¤¯ à¤˜à¤° à¤ªà¤°à¤¿à¤µà¤¾à¤° à¤¬à¤¿à¤¸à¥à¤¥à¤¾à¤ªà¤¿à¤¤ à¤­à¤à¤•à¤¾ à¤›à¤¨à¥ à¥¤ à¤†à¤‡à¤¤à¤¬à¤¾à¤° à¤¸à¤¾à¤à¤ à¤­à¤œà¤¨à¥€ à¥¯ à¤®à¤¾ à¤–à¥‹à¤²à¤¾ à¤¤à¤°à¥à¤¨à¥‡ à¤•à¥à¤°à¤®à¤®à¤¾ à¥«à¥« à¤µà¤°à¥à¤·à¥€à¤¯à¤¾ à¤à¤• à¤®à¤¹à¤¿à¤²à¤¾ à¤µà¥‡à¤ªà¤¤à¥à¤¤à¤¾ à¤­à¤à¤•à¥€ à¤›à¤¨à¥ à¥¤ à¤Ÿà¤¿à¤•à¤¾à¤ªà¥à¤°, à¤­à¤œà¤¨à¥€ à¤° à¤œà¤¾à¤¨à¤•à¥€ à¤—à¤¾à¤‰à¤à¤ªà¤¾à¤²à¤¿à¤•à¤¾à¤•à¤¾ à¤¬à¤¸à¥à¤¤à¥€ à¤¡à¥à¤¬à¤¾à¤¨à¤²à¥‡ à¤ªà¥à¤°à¤­à¤¾à¤µà¤¿à¤¤ à¤­à¤à¤•à¤¾ à¤›à¤¨à¥ à¥¤
         * date : 1489029259
         * api_link : http://localhost/radio-daily369Iau12qPq792S/news/96
         * web_link : https://radiodailymail.com/news/96
         */

        @Column(name= "news_id")
        private String news_id;

        @Column(name = "title")
        private String title;

        @Column(name = "image")
        private String image;

        @Column(name = "description")
        private String description;

        @Column(name = "date")
        private String date;

        @Column(name = "api_link")
        private String api_link;

        @Column(name = "web_link")
        private String web_link;

        @Column(name = "news_time")
        private long news_time;

        public String getNews_id() {
            return news_id;
        }

        public void setNews_id(String news_id) {
            this.news_id = news_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getApi_link() {
            return api_link;
        }

        public void setApi_link(String api_link) {
            this.api_link = api_link;
        }

        public String getWeb_link() {
            return web_link;
        }

        public void setWeb_link(String web_link) {
            this.web_link = web_link;
        }

        public long getNews_time() {
            return news_time;
        }

        public void setNews_time(long news_time) {
            this.news_time = news_time;
        }
    }
}
