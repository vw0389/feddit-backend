package com.vweinert.fedditbackend.controller;

import java.net.UnknownHostException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vweinert.fedditbackend.controller.customHealths.Health;


@RequestMapping("/api")
@RestController
public class HealthController {
    @GetMapping("/health")
    Health getHealth() throws UnknownHostException{
        return new Health();
    }
}
