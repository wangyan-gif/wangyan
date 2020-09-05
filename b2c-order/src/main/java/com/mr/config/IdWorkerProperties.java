package com.mr.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "b2c.worker")
public class IdWorkerProperties {
    private long workerId;  //当前机器ID
    private long datacenterId;  //序列号

    public long getWorkerId(){
        return workerId;
    }
    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }
    public long getDatacenterId() {
        return datacenterId;
    }
    public void setDatacenterId(long datacenterId) {
        this.datacenterId = datacenterId;
    }
}