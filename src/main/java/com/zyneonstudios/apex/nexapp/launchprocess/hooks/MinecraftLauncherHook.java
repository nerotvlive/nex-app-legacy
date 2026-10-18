package com.zyneonstudios.apex.nexapp.launchprocess.hooks;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftLauncher;
import com.zyneonstudios.apex.nexapp.main.NEXApplication;
import com.zyneonstudios.apex.nexapp.search.zyndex.local.LocalInstance;

public abstract class MinecraftLauncherHook {

    private final MinecraftLauncher launcher;
    private LocalInstance instance;

    public MinecraftLauncherHook(MinecraftLauncher launcher) {
        this.launcher = launcher;
        this.instance = null;
    }

    public MinecraftLauncherHook(MinecraftLauncher launcher, LocalInstance instance) {
        this.launcher = launcher;
        this.instance = instance;
    }

    public MinecraftLauncher getLauncher() {
        return this.launcher;
    }

    public LocalInstance getInstance() {
        return this.instance;
    }

    public void setInstance(LocalInstance instance) {
        this.instance = instance;
    }

    public abstract void run();

    public static MinecraftLauncherHook getPreLaunchHook(MinecraftLauncher launcher) {
        return new MinecraftLauncherHook(launcher) {
            @Override
            public void run() {
                NEXApplication.getInstance().getApplicationFrame().executeJavaScript("document.getElementById(\"launch-button\").innerHTML = \"STARTING...\";");
            }
        };
    }

    public static MinecraftLauncherHook getPostLaunchHook(MinecraftLauncher launcher) {
        return new MinecraftLauncherHook(launcher) {
            @Override
            public void run() {
                NEXApplication.getInstance().getApplicationFrame().executeJavaScript("document.getElementById(\"launch-button\").innerHTML = \"<i class='bi bi-check-lg'></i> RUNNING\";");
                if(NEXApplication.getInstance().getLocalSettings().minimizeApp()) {
                    NEXApplication.getInstance().getApplicationFrame().minimize();
                }
            }
        };
    }

    public static MinecraftLauncherHook getGameCloseHook(MinecraftLauncher launcher) {
        return new MinecraftLauncherHook(launcher) {
            @Override
            public void run() {
                NEXApplication.getInstance().getInstanceManager().removeRunningInstance(launcher.getGameProcess());
                if(NEXApplication.getInstance().getApplicationFrame().getURL().contains("page=library")) {
                    NEXApplication.getInstance().getApplicationFrame().reload();
                }
                if(NEXApplication.getInstance().getLocalSettings().minimizeApp()) {
                    NEXApplication.getInstance().getApplicationFrame().setVisible(true);
                }
            }
        };
    }

    public static MinecraftLauncherHook getInstancePreLaunchHook(MinecraftLauncher launcher, LocalInstance instance) {
        return new MinecraftLauncherHook(launcher, instance) {
            @Override
            public void run() {
                NEXApplication.getInstance().getApplicationFrame().executeJavaScript("document.getElementById(\"launch-button\").innerHTML = \"STARTING...\";");
                for(String cmd: getInstance().getPreLaunchHook()) {
                    if(cmd.startsWith("napp ")) {
                        NEXApplication.getInstance().getConsoleHandler().runCommand(cmd.replaceFirst("napp ", ""));
                    } else {
                        runCommand(cmd);
                    }
                }
            }
        };
    }

    public static MinecraftLauncherHook getInstancePostLaunchHook(MinecraftLauncher launcher, LocalInstance instance) {
        return new MinecraftLauncherHook(launcher, instance) {
            @Override
            public void run() {
                NEXApplication.getInstance().getApplicationFrame().executeJavaScript("document.getElementById(\"launch-button\").innerHTML = \"<i class='bi bi-check-lg'></i> RUNNING\";");
                if(NEXApplication.getInstance().getLocalSettings().minimizeApp()) {
                    NEXApplication.getInstance().getApplicationFrame().minimize();
                }
                for(String cmd: getInstance().getOnLaunchHook()) {
                    if(cmd.startsWith("napp ")) {
                        NEXApplication.getInstance().getConsoleHandler().runCommand(cmd.replaceFirst("napp ", ""));
                    } else {
                        runCommand(cmd);
                    }
                }
            }
        };
    }

    public static MinecraftLauncherHook getInstanceGameCloseHook(MinecraftLauncher launcher, LocalInstance instance) {
        return new MinecraftLauncherHook(launcher, instance) {
            @Override
            public void run() {
                NEXApplication.getInstance().getInstanceManager().removeRunningInstance(launcher.getGameProcess());
                if(NEXApplication.getInstance().getApplicationFrame().getURL().contains("page=library")) {
                    NEXApplication.getInstance().getApplicationFrame().reload();
                }
                if(NEXApplication.getInstance().getLocalSettings().minimizeApp()) {
                    NEXApplication.getInstance().getApplicationFrame().setVisible(true);
                }
                for(String cmd: getInstance().getOnExitHook()) {
                    if(cmd.startsWith("napp ")) {
                        NEXApplication.getInstance().getConsoleHandler().runCommand(cmd.replaceFirst("napp ", ""));
                    } else {
                        runCommand(cmd);
                    }
                }
            }
        };
    }

    private static void runCommand(String cmd) {
        /*try {
            Process process;
            String fullCommand;
            if (OperatingSystem.getType().equals(OperatingSystem.Type.Windows)) {
                fullCommand = String.format("cmd.exe /c start " + cmd, NexusApplication.getInstance().getWorkingDir());
            } else {
                fullCommand = String.format("/bin/sh -c nohup " + cmd + " &", NexusApplication.getInstance().getWorkingDir());
            }
            process = Runtime.getRuntime().exec(fullCommand);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        Main.getLogger().err("External command execution is disabled for security reasons: " + cmd);
    }
}
