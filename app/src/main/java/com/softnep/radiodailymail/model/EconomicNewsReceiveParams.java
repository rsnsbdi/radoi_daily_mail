package com.softnep.radiodailymail.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ADMIN on 2017-11-09.
 */

public class EconomicNewsReceiveParams {


    @SerializedName("news-23")
    private List<News23Bean> news23;

    public List<News23Bean> getNews23() {
        return news23;
    }

    public void setNews23(List<News23Bean> news23) {
        this.news23 = news23;
    }

    @Table(name = "EconomicNews")
    public static class News23Bean extends Model implements Serializable{
        /**
         * news_id : 200
         * title : सुन सातामा ११ सयले घट्यो
         * image : https://radiodailymail.com/images/newsManagement/1d2e59b92b3a6bba63d7c5ff4276909a.jpg
         * description : <p>मंसीर २६ –<br />
         नेपाली बजारमा सुनको मुल्य ओरोलो लाग्ने क्रम जारी छ।</p>

         <p>एक सातामा सुनको मुल्य तोलामा १ हजार १ सय रुपैयाँ घटेको छ।</p>

         <p>एक साता अघि प्रतितोला सुनको मुल्य ५५ हजार २ सय थियो ।</p>

         <p>आज भने सुनको मुल्य ५४ हजार १ सय कायम भएको छ। हिजो सुनको मुद्यय प्रतितोला ५४ हजार ३ सय थियो। त्यस्तै चाँदी प्रतितोला ७ सय १५ रुपैयाँमा कारोबार भइरहेको सुनचाँदी व्यवसायी महासंघले जनाएको छ।</p>
         * date : 2017-12-12 07:00:05
         * api_link : https://api.radiodailymail.com/3459IaYYku12qPq792S/news-detail/200
         * web_link : https://radiodailymail.com/news/200
         */

        @Column(name = "news_id")
        private String news_id;

        @Column(name = "title")
        private String title;

        @Column(name = "image")
        private String image;

        @Column(name = "description")
        private String description;

        @Column(name = "news_date")
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
