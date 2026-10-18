package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.neoforge;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.ModloaderLauncher;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.forge.ForgeLauncher;
import com.zyneonstudios.apex.nexapp.utilities.JavaUtilities;
import com.zyneonstudios.nexus.utilities.system.OperatingSystem;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import live.nerotv.aminecraftlauncher.launcher.MinecraftVersion;

import java.io.File;
import java.nio.file.Path;

public class NeoForgeLauncher extends ForgeLauncher implements ModloaderLauncher {

    boolean running = false;
    private Process gameProcess;
    private String modloaderVersion;
    private final AuthInfos authInfos;

    public NeoForgeLauncher(File runDir, AuthInfos authInfos) {
        this(runDir.getAbsolutePath(), authInfos);
    }

    public NeoForgeLauncher(Path runPath, AuthInfos authInfos) {
        this(runPath.toAbsolutePath().toString(), authInfos);
    }

    public NeoForgeLauncher(String runPathString, AuthInfos authInfos) {
        super(runPathString, authInfos);
        this.authInfos = authInfos;
    }

    @Override
    public String getModloaderVersion() {
        return modloaderVersion;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public Process getGameProcess() {
        return gameProcess;
    }

    @Override
    public NeoForgeLauncher withModloaderVersion(String modloaderVersion) {
        this.modloaderVersion = modloaderVersion;
        return this;
    }

    @Override
    public boolean launch() {
        if(getInstaller() == null) {
            NeoForgeInstaller installer = new NeoForgeInstaller(getRunDir());
            installer.withMinecraftVersion(getMinecraftVersion());
            installer.withModloaderVersion(modloaderVersion);
            withInstaller(installer);
        }
        if(!running) {
            running = true;
            getPreLaunchHook().run();
            if (getInstaller().install()) {
                if(getFramework() == null) {
                    withFramework(new NoFramework(getRunPath(), authInfos, GameFolder.FLOW_UPDATER));
                    getFramework().getAdditionalVmArgs().add("-Xms" + getMinMemory() + "M");
                    getFramework().getAdditionalVmArgs().add("-Xmx" + getMaxMemory() + "M");
                    if (OperatingSystem.getType() == OperatingSystem.Type.macOS) {
                        getFramework().getAdditionalVmArgs().add("-XstartOnFirstThread");
                    }
                    if (getArgs() != null) {
                        for (String arg : getArgs()) {
                            getFramework().getAdditionalVmArgs().add(arg);
                        }
                    }
                    if (getEnvArgs() != null) {
                        for (String arg : getEnvArgs()) {
                            getFramework().getAdditionalArgs().add(arg);
                        }
                    }
                }
                JavaUtilities.setJava(MinecraftVersion.getType(getMinecraftVersion()));
                try {
                    this.gameProcess = getFramework().launch(getMinecraftVersion(), modloaderVersion, NoFramework.ModLoader.NEO_FORGE);
                    getPostLaunchHook().run();
                    this.gameProcess.onExit().thenRun(() -> {
                        getPostCloseHook().run();
                        gameProcess = null;
                        running = false;
                    });
                    return true;
                } catch (Exception e) {
                    Main.getLogger().err("[Minecraft Launcher] Could not launch neoforge "+modloaderVersion+" with vanilla " + getMinecraftVersion() + " in " + getInstaller().getPathString() + ": " + e.getMessage());
                    throw new RuntimeException(e);
                }
            } else {
                Main.getLogger().err("[Minecraft Launcher] Could not install vanilla " + getInstaller().getMinecraftVersion() + " in " + getInstaller().getPathString());
            }
        } else {
            Main.getLogger().err("[Minecraft Launcher] The game is already running");
        }
        return false;
    }
}