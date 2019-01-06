package com.softnep.radiodailymail.model;

import java.util.List;

/**
 * Created by ADMIN on 2017-11-30.
 */

public class NotificationMessageReceiveParams {


    private List<NewsBean> news;

    public List<NewsBean> getNews() {
        return news;
    }

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    public static class NewsBean {
        /**
         * news_id : 138
         * title : कञ्चनपुरमा बम फेला, नेपाली सेनाद्वारा निष्कृय
         * image : https://radiodailymail.com/images/newsManagement/b8c0f144b119620e65279283de9e3b4e.jpg
         * description :
         * date : 2017-11-30 05:56:54
         * web_link : https://radiodailymail.com/news/138
         */

        private String news_id;
        private String title;
        private String image;
        private String description;
        private String date;
        private String web_link;

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

        public String getWeb_link() {
            return web_link;
        }

        public void setWeb_link(String web_link) {
            this.web_link = web_link;
        }
    }
}
