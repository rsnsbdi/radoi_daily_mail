package com.softnep.radiodailymail.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by ADMIN on 2017-12-15.
 */

public class TokenSaveParams {

    List<DeviceTokenList> deviceTokenLists;

    public List<DeviceTokenList> getDeviceTokenLists() {
        return deviceTokenLists;
    }

    public void setDeviceTokenLists(List<DeviceTokenList> deviceTokenLists) {
        this.deviceTokenLists = deviceTokenLists;
    }

    @Table(name = "DeviceTokenList")
    public static class DeviceTokenList extends Model {

        @Column(name = "device_token")
        private String device_token;

        public String getDevice_token() {
            return device_token;
        }

        public void setDevice_token(String device_token) {
            this.device_token = device_token;
        }
    }
}
