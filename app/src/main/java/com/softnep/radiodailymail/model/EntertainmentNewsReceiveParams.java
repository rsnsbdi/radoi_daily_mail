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

public class EntertainmentNewsReceiveParams {


    @SerializedName("news-28")
    private List<News28Bean> news28;

    public List<News28Bean> getNews28() {
        return news28;
    }

    public void setNews28(List<News28Bean> news28) {
        this.news28 = news28;
    }

    @Table(name = "EntertainmentNews")
    public static class News28Bean extends Model implements Serializable{
        /**
         * news_id : 201
         * title : डीजे मार्समेलोले धोका दिएपछि दरबारमार्गको नाइटक्लबमा तोडफोड
         * image : https://radiodailymail.com/images/newsManagement/9a0fd482f2080ff1d869516e6c4550fe.jpg
         * description : <p style="text-align:justify">२८ मंसिर, काठमाडौं । चर्चित अन्तर्राष्ट्रिय डीजे मार्समेलोले धोका दिएपछि उनका समर्थकहरुले काठमाडौंको दरबारमार्गमा रहेको नाइट क्लब डेजाभूमा बुधबार मध्यराती तोडफोड गरेका छन् ।</p>

         <p style="text-align:justify">मार्समेलो आउने भनी प्रतिब्यक्ति ४ हजार रुपैयाँसम्म लिएर आयोजकले हजारौं दर्शकलाई टिकट बेचेका थिए ।</p>

         <p style="text-align:justify">तर, राती अबेरसम्म पनि डीजे मार्समेलो क्लबमा देखिएनन् । राती १ बजेतिरमात्र आयोजकले स्वास्थ्यका कारण उनी नआउने जानकारी दिएपछि आक्रोशित दर्शकले क्लबमा तोडफोड गरेका थिए ।</p>

         <p style="text-align:justify">दरबारमार्ग प्रहरी वृत्तका प्रमुख डीएसपी तिलक भारतीले भने, ‘डीजे नआएपछि आक्रोसित भिडले तोडफोड गरेको हो । क्लबमा केही क्षति भएको छ ।’</p>

         <p style="text-align:justify">बिहान ५ बजेतिर मार्समेलोले आफ्नो फेसबुक पेजमा स्वास्थ्यका कारण आफु काठमाडौंमा उपस्थित हुन नसकेको भन्दै क्षमा मागेका छन् । डाक्टरले आफूलाई आराम गर्न निर्देशन दिएका कारण आफू उपस्थीत हुन नसकेको उनको भनाइ छ ।</p>

         <p style="text-align:justify">तर, उनी आउन नसक्ने सूचना किन बेलैमा दिइएन भन्नेबारे न त आयोजकले नै मुख खोलेका छन् नत डीजे मार्समेलो नै यसबारे बोलेका छन् ।</p>

         <p style="text-align:justify">मार्समेलो पछिल्ला बर्षहरुमा निकै लोकप्रिय बनेका अमेरिकी डीजे हुन् । डीजेइङ गर्दा उनको टाउकोमा मार्समेलो क्याप लगाइरहेकै हुन्छन् । उनको वास्तविक नाम भने अहिलेसम्म खुलाइएको छैन् ।</p>
         * date : 2017-12-14 04:46:19
         * api_link : https://api.radiodailymail.com/3459IaYYku12qPq792S/news-detail/201
         * web_link : https://radiodailymail.com/news/201
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
