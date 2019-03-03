import java.io.Serializable;

public class Device implements Serializable {

private String type;
private String ssid;
private String mac;
private long chnl;
private long rssi;
private long occurances;

public Device(String type, String ssid, String mac, long chnl, long rssi, long occurances) {
    this.type = type;
    this.ssid = ssid;
    this.mac = mac;
    this.chnl = chnl;
    this.rssi = rssi;
    this.occurances = occurances;
}

public Device() {}

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

public long getChnl() {
    return chnl;
}

public void setChnl(long chnl) {
    this.chnl = chnl;
}

public long getRssi() {
    return rssi;
}

public void setRssi(long rssi) {
    this.rssi = rssi;
}


    public long getOccurances() {
        return occurances;
    }

    public void setOccurances(long occurances) {
        this.occurances = occurances;
    }

}

