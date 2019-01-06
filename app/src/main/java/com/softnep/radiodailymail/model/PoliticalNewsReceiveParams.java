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

public class PoliticalNewsReceiveParams {


    @SerializedName("news-26")
    private List<News26Bean> news26;

    public List<News26Bean> getNews26() {
        return news26;
    }

    public void setNews26(List<News26Bean> news26) {
        this.news26 = news26;
    }

    @Table(name = "PoliticalNews")
    public static class News26Bean extends Model implements Serializable{
        /**
         * news_id : 135
         * title : रविन्द्र मिश्रले भने- जित पक्का भइसक्यो, मतान्तर कति भन्ने प्रश्न मात्र बाँकी छ
         * image : https://radiodailymail.com/images/newsManagement/564b6121f14768ffb5ee19a992925392.jpg
         * description : <p style="text-align:justify">1३ मंसिर, काठमाडौं । विवेकशील साझा पार्टीका काठमाडौंमा १ का उम्मेदवार रविन्द्र मिश्रले आफ्नो जित सुनिश्चित भइसकेको दाबी गरेका छन् ।</p>

         <p style="text-align:justify"><br />
         बुधबार बबलमहल क्षेत्रमा घरदैलो गरेका विवेकशील साझाका उम्मेदवार मिश्रले बाटोबाटो गल्लीगल्लीमा मतदाताले देखाएको उत्साहले जित पक्का भएको बताए । जितप्रति आफू ढुक्क रहेको भन्दै उनले भने, ‘हामी जित्छौं भन्नेमा ढुक्क भइसक्यौं, मतान्तर कति ठूलो हुन्छ भन्ने प्रश्न मात्र बाँकी हो ।’काठमाडौं १ मा मिश्रसँगै कांग्रेसका प्रकाशमान सिंह र वाम गठबन्धनका अनिल शर्मा उम्मेदवार छन् । सिंह र शर्माले पनि आफू जित्ने दाबी गरेका छन् ।</p>
         * date : 2017-11-30 04:00:58
         * api_link : https://api.radiodailymail.com/3459IaYYku12qPq792S/news-detail/135
         * web_link : https://radiodailymail.com/news/135
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
