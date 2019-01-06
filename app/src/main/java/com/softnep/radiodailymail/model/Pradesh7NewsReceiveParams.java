package com.softnep.radiodailymail.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by ADMIN on 2017-11-09.
 */

public class Pradesh7NewsReceiveParams {

    @SerializedName("news-39")
    private List<News39Bean> news39;

    public List<News39Bean> getNews39() {
        return news39;
    }

    public void setNews39(List<News39Bean> news39) {
        this.news39 = news39;
    }

    @Table(name = "Pradesh7News")
    public static class News39Bean extends Model implements Serializable{

        /**
         * news_id : 199
         * title : प्रहरीको फरार सूचीमा रहेका रेशम चौधरी दोब्बर मतान्तरसहित विजयी
         * image : https://radiodailymail.com/images/newsManagement/08a3ed526717a715849672a67e065d8e.jpg
         * description : <p>धनगढी, मंसीर २६ –<br />
         कैलाली क्षेत्र नम्बर १ को प्रतिनिधिसभा सदस्य तर्फ प्रहरीको फरार सूचीमा रहेका रेशमलाल चौधरी भारी मतान्तरसहित विजयी हुनु भएको छ। टिकापुर घटनामा प्रहरीले खोजी सूचीमा राखेपछि फरार चौधरीले वारेसमार्फत राष्ट्रिय जनता पार्टीबाट उम्मेदवारी दिनु भएको थियो। चौधरी ३४ हजार ३४१ मतसहित विजयी हुनु भएको हो। <br />
         उहाँका निकटतम प्रतिस्पर्धी एमालेकी मदनकुमारी शाहले १३ हजार ४०६ मतमात्र प्राप्त गर्नु भयो । </p>

         <p>प्रदेशतर्फ १ मा राजपाका कृष्णबहादुर चौधरी १२ हजार १८४ मतसहित विजयी हुनु भएको छ । २ मा कांग्रेसका रण बहादुर रावत ९ हजार ४३४ मतसहित विजयी हुनु भएको छ।</p>
         * date : 2017-12-12 05:18:43
         * api_link : https://api.radiodailymail.com/3459IaYYku12qPq792S/news-detail/199
         * web_link : https://radiodailymail.com/news/199
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
