package com.map.controller;

import com.map.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Custom error controller to display simple error messages based on status codes
 */
@Api(description = "Custom error handler")
@RestController
public class CustomErrorController implements ErrorController {
    /**
     * Returns a simple error message based on the status code of a request
     *
     * @param request {@link HttpServletRequest}
     * @return String error message
     */
    @ApiOperation(value = "Returns a simple error message based on the status code of a request")
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        final Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        final Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        return String.format(Constants.ERROR_DISPLAY_FORMAT, statusCode(status), message.toString());
    }

    private Integer statusCode(Object status) {
        return Integer.valueOf(status.toString());
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
