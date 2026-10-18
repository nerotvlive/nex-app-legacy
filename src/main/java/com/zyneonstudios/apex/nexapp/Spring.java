package com.zyneonstudios.apex.nexapp;

import com.zyneonstudios.nexus.utilities.system.OperatingSystem;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
public class Spring {

    /**
     * Starts the embedded web server for the application.
     *
     * @param args Command-line arguments passed to the application.
     */
    protected static void startWebServer(String[] args) {
        Main.getLogger().log("Initializing springboot server...");
        if(OperatingSystem.getType().equals(OperatingSystem.Type.macOS)) {
            startWebServerAsync(args);
            return;
        }
        Main.getLogger().deb("Initializing springboot server on \""+OperatingSystem.getName()+"-"+OperatingSystem.getVersion()+"-"+OperatingSystem.getArchitecture()+"\" ("+OperatingSystem.getType()+")...");
        if (Main.getPort() > 65535) {
            throw new RuntimeException("Port range exceeded, cannot launch application web server. Try to restart your computer or stopping port using applications and try again.");
        }
        try {
            // Configure and start the Spring Boot web server.
            new SpringApplicationBuilder(Spring.class)
                    .headless(false)
                    .properties("logging.level.root=WARN", "logging.pattern.console=", "server.port=" + Main.getPort())
                    .run(args);
        } catch (Exception e) {
            // Increment the port and retry if the initial port is in use.
            Main.setPort(Main.getPort()+1);
            startWebServer(args);
        }
    }

    private static void startWebServerAsync(String[] args) {
        Main.getLogger().deb("Initializing springboot server async on \""+OperatingSystem.getName()+"-"+OperatingSystem.getVersion()+"-"+OperatingSystem.getArchitecture()+"\" ("+OperatingSystem.getType()+")...");
        if (Main.getPort() > 65535) {
            throw new RuntimeException("Port range exceeded...");
        }
        CompletableFuture.runAsync(()->{
            Main.getLogger().deb("Initializing web server async...");
            try {
                new SpringApplicationBuilder(Spring.class)
                        .headless(false)
                        .properties("logging.level.root=WARN", "logging.pattern.console=", "server.port=" + Main.getPort())
                        .run(args);
            } catch (Exception e) {
                Main.setPort(Main.getPort()+1);
                startWebServerAsync(args);
            }
        });
    }
}
