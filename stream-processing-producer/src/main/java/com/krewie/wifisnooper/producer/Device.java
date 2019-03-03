package com.krewie.wifisnooper.producer;

import java.io.Serializable;

public class Device implements Serializable {

    private String type;
    private String ssid;
    private String mac;
    private Integer chnl;
    private Integer rssi;

    public Device(String type, String ssid, String mac, Integer chnl, Integer rssi) {
        this.type = type;
        this.ssid = ssid;
        this.mac = mac;
        this.chnl = chnl;
        this.rssi = rssi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getChnl() {
        return chnl;
    }

    public void setChnl(Integer chnl) {
        this.chnl = chnl;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }
}
