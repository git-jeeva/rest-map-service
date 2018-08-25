package com.map.controller;

import com.map.service.MapService;
import com.map.util.Constants;
import com.map.util.Messages;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entry point for REST calls to access MapService
 */
@RestController
@Api(description = "Operations pertaining to Map Service")
public class MapController {

    @Autowired
    Messages messages;

    @Autowired
    MapService mapService;

    /**
     * Checks if the given cities are connected
     *
     * @param src  origin city
     * @param dest destination city
     * @return boolean true if connected, false otherwise
     */
    @ApiOperation(value = "Checks if the given cities are connected")
    @RequestMapping(value = "/connected", method = RequestMethod.GET)
    public String checkConnection(@RequestParam(value = "origin") String src, @RequestParam(value = "destination") String dest) {
        final String messageCode;

        if (src.trim().isEmpty()) {
            messageCode = Constants.ERROR_MISSING_ORIGIN;
        } else if (dest.trim().isEmpty()) {
            messageCode = Constants.ERROR_MISSING_DESTINATION;
        } else if (src.trim().equalsIgnoreCase(dest.trim())) {
            messageCode = Constants.ERROR_DIFFERENT_CITIES_REQUIRED;
        } else {
            boolean connected = mapService.hasBFSPath(src, dest);
            messageCode = connected ? Constants.CONNECTION_FOUND : Constants.CONNECTION_NOT_FOUND;
        }

        return messages.get(messageCode);
    }

    /**
     * Gets all the connected cities from the given city
     *
     * @param src origin city
     * @return String names of connected cities delimited by hyphen
     */
    @ApiOperation(value = "Gets all the connected cities from the given city")
    @RequestMapping(value = "/connections", method = RequestMethod.GET)
    public String getConnections(@RequestParam(value = "origin") String src) {
        final String response;

        if (src.trim().isEmpty()) {
            response = messages.get(Constants.ERROR_MISSING_ORIGIN);
        } else {
            response = mapService.bfs(src);
        }

        return response;
    }
}
