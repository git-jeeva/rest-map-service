package com.map.controller;

import com.map.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MapController {

    @Autowired
    MapService mapService;

    @RequestMapping(value = "/connected", method = RequestMethod.GET)
    public String checkConnection(@RequestParam(value="origin") String src, @RequestParam(value="destination") String dest) {
        String message = "no";
        if(src.trim().isEmpty()) {
            message = "Please provide a city for origin";
        } else if(dest.trim().isEmpty()) {
            message = "Please provide a city for destination";
        } else if(src.trim().equalsIgnoreCase(dest.trim())) {
            message = "Please provide different cities for origin and destination";
        } else {
            boolean connected = mapService.hasBFSPath(src, dest);
            System.out.println(src + " -- " + dest + " -> " + connected);
            message = connected ? "yes" : "no";
        }

        return message;
    }
}
