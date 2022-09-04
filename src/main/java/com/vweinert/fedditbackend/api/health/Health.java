package com.vweinert.fedditbackend.api.health;

import java.net.InetAddress;
import java.net.UnknownHostException;

import lombok.Data;

@Data
public class Health {
    private String hostname;

    public Health() throws UnknownHostException{
        this.hostname = InetAddress.getLocalHost().getHostName().toString();
    }
}
