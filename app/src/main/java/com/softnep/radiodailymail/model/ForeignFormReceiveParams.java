package com.softnep.radiodailymail.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by ADMIN on 2017-11-15.
 */

public class ForeignFormReceiveParams {

    private List<ResultBean> Result;

    public List<ResultBean> getResult() {
        return Result;
    }

    public void setResult(List<ResultBean> Result) {
        this.Result = Result;
    }

    @Table(name = "Feedback")
    public static class ResultBean extends Model {
        /**
         * Status : Data Submission Unsuccessful
         * Message : Could not read POST data
         */

        @Column(name = "Status")
        private String Status;

        @Column(name = "Message")
        private String Message;

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }
    }
}
