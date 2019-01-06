package com.softnep.radiodailymail.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by ADMIN on 2017-11-27.
 */

public class NotificationReceiveParams {

    private List<MessageBean> Message;

    public List<MessageBean> getMessage() {
        return Message;
    }

    public void setMessage(List<MessageBean> Message) {
        this.Message = Message;
    }

    @Table(name = "Message")
    public static class MessageBean extends Model{
        /**
         * Result : Operation Successful
         */

        @Column(name = "result")
        private String Result;

        public String getResult() {
            return Result;
        }

        public void setResult(String Result) {
            this.Result = Result;
        }
    }
}
