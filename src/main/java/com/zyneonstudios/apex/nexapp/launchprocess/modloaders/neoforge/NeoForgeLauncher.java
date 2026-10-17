package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.neoforge;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.vanilla.VanillaLauncher;
import com.zyneonstudios.apex.nexapp.utilities.JavaUtilities;
import com.zyneonstudios.nexus.utilities.system.OperatingSystem;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import live.nerotv.aminecraftlauncher.launcher.MinecraftVersion;

import java.io.File;
import java.nio.file.Path;

public class NeoForgeLauncher extends VanillaLauncher {

    boolean running = false;
    private Process gameProcess;
    private String modloaderVersion;

    public NeoForgeLauncher(File runDir) {
        this(runDir.getAbsolutePath());
    }

    public NeoForgeLauncher(Path runPath) {
        this(runPath.toAbsolutePath().toString());
    }

    public NeoForgeLauncher(String runPathString) {
        super(runPathString);
    }

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
            if (getInstaller().install()) {
                if(getFramework() == null) {
                    withFramework(new NoFramework(getRunPath(), new AuthInfos("TestUser","", "uniqueid"), GameFolder.FLOW_UPDATER));
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
                    this.gameProcess.onExit().thenRun(() -> running = false);
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