package com.softnep.radiodailymail.model;

import com.activeandroid.annotation.Column;

/**
 * Created by ADMIN on 2017-11-27.
 */

public class NotificationSendParams {

    /**
     * device_id : e50n9q2Q8XE:APA91bGKJPEG9KEJhjvOAiAcoJ4H-fv4OZFLrfECEYRuQptdEOus6w69OUyBv-IqtvpfOj9AMDX2dwULqJvB9qvPMFSi0aeSN_QBV62l-RTEY-loCvfEzYIADRmIcThSTGZhO7G2r14s
     * category_id : 0
     */

   // @Column(name = "device_id")
    private String device_id;

  //  @Column(name = "category_id")
    private String category_id;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
