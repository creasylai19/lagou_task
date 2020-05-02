package com.lagou.pojo;

import java.util.Date;

//{lastInvokeTime:yyyy-mm-dd HH24:mi:ss,lastInvokeTime:milliseconds}
public class ServerInfo implements Comparable<ServerInfo>{
    private Date lastInvokeTime;
    private Integer lastInvokeDuration;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    private String instance;

    public Date getLastInvokeTime() {
        return lastInvokeTime;
    }

    public void setLastInvokeTime(Date lastInvokeTime) {
        this.lastInvokeTime = lastInvokeTime;
    }

    public Integer getLastInvokeDuration() {
        return lastInvokeDuration;
    }

    public void setLastInvokeDuration(Integer lastInvokeDuration) {
        this.lastInvokeDuration = lastInvokeDuration;
    }

    public ServerInfo(Date lastInvokeTime, Integer lastInvokeDuration, String instance) {
        this.lastInvokeTime = lastInvokeTime;
        this.lastInvokeDuration = lastInvokeDuration;
        this.instance = instance;
    }

    public ServerInfo() {
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "lastInvokeTime=" + lastInvokeTime +
                ", lastInvokeDuration=" + lastInvokeDuration +
                ", instance='" + instance + '\'' +
                '}';
    }

    @Override
    public int compareTo(ServerInfo o) {
        Date currentDate = new Date();
        currentDate.setTime(currentDate.getTime()-5*1000);
        if( o.lastInvokeTime.before(currentDate) ){
            o.lastInvokeDuration = 0;//如果是5秒之前调用，则设置为0，优先级最高
        }
        if( this.lastInvokeTime.before(currentDate) ){
            this.lastInvokeDuration = 0;
        }
        return this.lastInvokeDuration.compareTo(o.lastInvokeDuration);
    }
}
