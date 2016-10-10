package cn.com.cardinfo.forward.client;

import java.util.Date;

/**
 * Created by Zale on 16/9/25.
 */
public class TcpShortClient{
    private String id;
    private int port;
    private String ip;
    private int weight;
    private Date lastTime;

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}
