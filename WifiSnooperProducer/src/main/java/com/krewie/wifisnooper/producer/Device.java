package com.krewie.wifisnooper.producer;

import java.io.Serializable;

public class Device implements Serializable {

    private String type;
    private String ssid;
    private String mac;
    private String chnl;
    private String rssi;

    public Device(String type, String ssid, String mac, String chnl, String rssi) {
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

    public String getChnl() {
        return chnl;
    }

    public void setChnl(String chnl) {
        this.chnl = chnl;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }
}
