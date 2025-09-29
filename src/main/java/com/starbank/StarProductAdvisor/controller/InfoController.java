package com.starbank.StarProductAdvisor.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/management")
public class InfoController {

    @Value("${info.app.name:unknown}")
    private String appName;

    @Value("${info.app.version:unknown}")
    private String appVersion;

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("name", appName);
        info.put("version", appVersion);
        return info;
    }
}
