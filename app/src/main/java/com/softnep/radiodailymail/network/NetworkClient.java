package com.softnep.radiodailymail.network;



import com.softnep.radiodailymail.model.AboutUsReceiveParams;
import com.softnep.radiodailymail.model.EconomicNewsReceiveParams;
import com.softnep.radiodailymail.model.EntertainmentNewsReceiveParams;
import com.softnep.radiodailymail.model.ForeignFormReceiveParams;
import com.softnep.radiodailymail.model.ForeignFormSendParams;
import com.softnep.radiodailymail.model.InternationalNewsReceiveParams;
import com.softnep.radiodailymail.model.NationalNewsReceiveParams;
import com.softnep.radiodailymail.model.NotificationReceiveParams;
import com.softnep.radiodailymail.model.NotificationSendParams;
import com.softnep.radiodailymail.model.PoliticalNewsReceiveParams;
import com.softnep.radiodailymail.model.Pradesh7NewsReceiveParams;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ADMIN on 2017-11-07.
 */

public interface NetworkClient {

    @GET("category/39/{page}")
    Call<Pradesh7NewsReceiveParams> getPradesh7News(@Path("page") int page);

    @GET("category/37/{page}")
    Call<NationalNewsReceiveParams> getNationalNews(@Path("page") int page);

    @GET("category/36/{page}")
    Call<InternationalNewsReceiveParams> getInternationaNews(@Path("page") int page);

    @GET("category/26/{page}")
    Call<PoliticalNewsReceiveParams> getPoliticalNews(@Path("page") int page);

    @GET("category/28/{page}")
    Call<EntertainmentNewsReceiveParams> getEntertainmentNews(@Path("page") int page);

    @GET("category/23/{page}")
    Call<EconomicNewsReceiveParams> getEconomicNews(@Path("page") int page);

    @POST("feedback")
    Call<ForeignFormReceiveParams> submitForm(@Body ForeignFormSendParams foreignFormSendParams);

    @POST("about")
    Call<AboutUsReceiveParams> getAboutUs();

    @POST("subscribe")
    Call<NotificationReceiveParams> sendNotification(@Body NotificationSendParams notificationSendParams);

    @GET("news-detail/{news_id}")
    Call<NationalNewsReceiveParams> getNotificationNews(@Path("news_id") int news_id);
}
