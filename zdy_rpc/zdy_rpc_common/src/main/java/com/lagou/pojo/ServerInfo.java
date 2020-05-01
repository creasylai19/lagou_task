package com.lagou.pojo;

import java.util.Date;

//{lastInvokeTime:yyyy-mm-dd HH24:mi:ss,lastInvokeTime:milliseconds}
public class ServerInfo implements Comparable<ServerInfo>{
    private Date lastInvokeTime;
    private Integer lastInvokeDuration;

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

    public ServerInfo(Date lastInvokeTime, Integer lastInvokeDuration) {
        this.lastInvokeTime = lastInvokeTime;
        this.lastInvokeDuration = lastInvokeDuration;
    }

    public ServerInfo() {
    }

    @Override
    public String toString() {
        return "ServerInfo{" +
                "lastInvokeTime=" + lastInvokeTime +
                ", lastInvokeDuration=" + lastInvokeDuration +
                '}';
    }

    @Override
    public int compareTo(ServerInfo o) {
        Date currentDate = new Date();
        currentDate.setTime(currentDate.getTime()-5*1000);
        if( o.lastInvokeTime.before(currentDate) ){
            o.lastInvokeDuration = Integer.MAX_VALUE;
        }
        if( this.lastInvokeTime.before(currentDate) ){
            this.lastInvokeDuration = Integer.MAX_VALUE;
        }
        return this.lastInvokeDuration.compareTo(o.lastInvokeDuration);
    }
}
