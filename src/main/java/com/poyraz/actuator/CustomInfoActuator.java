package com.poyraz.actuator;

import org.springframework.boot.actuate.endpoint.annotation.*;
import org.springframework.stereotype.Component;

@Endpoint(id = "custom-info")
@Component
public class CustomInfoActuator {
    private Long startTime = System.currentTimeMillis();
    private boolean displayUpTime = true;

    @ReadOperation
    public String getUptime() {
        if (displayUpTime) {
            long uptimeMillis = System.currentTimeMillis() - startTime;
            long seconds = (uptimeMillis / 1000) % 60;
            long minutes = (uptimeMillis / 1000) / 60;
            return String.format("Application is up for %d minutes and %d seconds.", minutes, seconds);
        } else {
            return "Uptime tracking is disabled.";
        }
    }

    @WriteOperation
    public String toggleUptimeTracking(@Selector String action) {
        if ("enable".equalsIgnoreCase(action)) {
            displayUpTime = true;
            return "Uptime tracking enabled.";
        } else if ("disable".equalsIgnoreCase(action)) {
            displayUpTime = false;
            return "Uptime tracking disabled.";
        } else {
            return "Invalid action. Use 'enable' or 'disable'.";
        }
    }

    @DeleteOperation
    public void resetUptimeTracking() {
        startTime = System.currentTimeMillis();
    }
}
