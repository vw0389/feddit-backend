package com.vweinert.fedditbackend.api.health;

import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthAPI {
    

    @GetMapping("/health")
    Health getHealth() throws UnknownHostException{
        return new Health();
    }
}
