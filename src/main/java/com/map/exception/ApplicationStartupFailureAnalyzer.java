package com.map.exception;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * Invoked in case of application startup errors and gracefully displays the error and recommended action on the console
 * instead of stack traces (Scenario: Missing or renamed city.txt file in the jar)
 */
public class ApplicationStartupFailureAnalyzer extends AbstractFailureAnalyzer<MapException> {
    @Override
    protected FailureAnalysis analyze(Throwable throwable, MapException cause) {
        String description = "Application startup failed, [Reason]: " + cause.getMessage();
        String action = "Please check startup configuration files";
        return new FailureAnalysis(description, action, cause);
    }
}
