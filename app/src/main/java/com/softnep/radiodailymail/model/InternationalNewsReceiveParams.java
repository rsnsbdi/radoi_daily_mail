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

public class InternationalNewsReceiveParams {


    @SerializedName("news-36")
    private List<News36Bean> news36;

    public List<News36Bean> getNews36() {
        return news36;
    }

    public void setNews36(List<News36Bean> news36) {
        this.news36 = news36;
    }

    @Table(name = "InternationalNews")
    public static class News36Bean extends Model implements Serializable{
        /**
         * news_id : 202
         * title : नेप्से परिसूचकमा फेरि दोहोरो अंकले वृद्धि
         * image : https://radiodailymail.com/images/newsManagement/66816c4bfc9a1f3783b26dcba4465238.jpg
         * description : <p style="text-align:justify">२७ मंसिर, काठमाडौं । नेप्से परिसूचक बुधवार दोहोरो अंकले बढेर १५२० विन्दुमा आइपुगेको छ ।  लामो समयदेखि अस्थिर बन्दै गएको बजारमा ट्रेडर्स (दैनिक करोबारी) हरुले अल्पकालिन लाभ लिने गरि रणनीतिहरु बनाउदै गएका छन् । दुई/चार दिनको अन्तरालमा नेप्से दोहोरो अंकले बढ्ने र पुन :  घट्ने क्रमले यसको संकेत गर्छ ।</p>

         <p style="text-align:justify">बजारमा बाम गठबन्धनको प्रभाव, बैंक तथा वित्तीय संस्थामा लगानी योग्य पूँजी बढेको वा घटेको, ब्रोकरलाई राजस्व तिर्न भनिएको जस्ता समाचारहरु प्रभाव भएसँगै यसको प्रभावलाई रणनीति बनाउदै छोटो अवधिमै सेयर किनबेच गर्ने प्रवृत्ति देखिएको छ ।</p>

         <p style="text-align:justify">बजारले ठोस गति लिन नसक्दा र बेलोबेलामा दोहोरो अंकले नै तलमाथि हुने क्रममा यसकैको लाभ लिने गरि लगानीकर्ताहरुले लगानी रणनीति बनाइरहेको देखिन्छ ।</p>

         <p style="text-align:justify">बजारमा प्राविधिक पक्षको पनि आंशिक प्रभाव देखिदै आएको छ । केही दिन अघि नेप्से डब्लु आकृती बनाएर केही समय बढेको पनि देखियो । रेखा चित्रको आधारमा देखिएको यसअघिको टेवा विन्दु १४९० देखि १५१३ मा नेप्से पुगेर फर्किने प्रयासहरु पनि देखिएको छ । बुधवार पनि नेप्से १५१० बाट नेप्से दोहोरो अंकले बढेको छ ।</p>

         <p style="text-align:justify">कारोबार रकममा भने ४९ करोडमा सिमित भएको छ । कारोबार रकम यसअघि जस्तै उच्च नभएपनि लगानी भने केही वृद्धि भएको देखिन्छ । यस दिन सबै समूहगत परिसूचकमा पनि वृद्धि भएको छ । कतिपय समूहगत परिसूचकमा दोहोरो अंकले नै वृद्धि भएको छ ।</p>

         <p style="text-align:justify">समस्याग्रस्तबाट मुक्त भएका कुवेर मर्चेन्ट र ललितपुर फाइनान्सको यस दिन करिब १० प्रतिशतले मूल्य बढेर सेयर कारोबार भएको छ । कम्पनीहरुले ३५ दिन भित्र नेपाल राष्ट्र बैंकमा चुक्ता पूँजी वृद्धिको योजना पेश गर्नुपर्ने भएपछि पनि लगानीकर्ताहरुमा लाभांशको आश देखिएको छ ।</p>
         * date : 2017-12-14 05:01:29
         * api_link : https://api.radiodailymail.com/3459IaYYku12qPq792S/news-detail/202
         * web_link : https://radiodailymail.com/news/202
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
