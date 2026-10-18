package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.quilt;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftVersionUtils;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.ModloaderLauncher;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.fabric.FabricLauncher;
import com.zyneonstudios.apex.nexapp.utilities.JavaUtilities;
import com.zyneonstudios.nexus.utilities.system.OperatingSystem;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;

import java.io.File;
import java.nio.file.Path;

public class QuiltLauncher extends FabricLauncher implements ModloaderLauncher {

    boolean running = false;
    private Process gameProcess;
    private String modloaderVersion;
    private final AuthInfos authInfos;

    public QuiltLauncher(File runDir, AuthInfos authInfos) {
        this(runDir.getAbsolutePath(), authInfos);
    }

    public QuiltLauncher(Path runPath, AuthInfos authInfos) {
        this(runPath.toAbsolutePath().toString(), authInfos);
    }

    public QuiltLauncher(String runPathString, AuthInfos authInfos) {
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
    public QuiltLauncher withModloaderVersion(String modloaderVersion) {
        this.modloaderVersion = modloaderVersion;
        return this;
    }

    @Override
    public boolean launch() {
        if(getInstaller() == null) {
            QuiltInstaller installer = new QuiltInstaller(getRunDir());
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
                JavaUtilities.setJava(MinecraftVersionUtils.getType(getMinecraftVersion()));
                try {
                    this.gameProcess = getFramework().launch(getMinecraftVersion(), modloaderVersion, NoFramework.ModLoader.QUILT);
                    getPostLaunchHook().run();
                    this.gameProcess.onExit().thenRun(() -> {
                        getPostCloseHook().run();
                        gameProcess = null;
                        running = false;
                    });
                    return true;
                } catch (Exception e) {
                    Main.getLogger().err("[Minecraft Launcher] Could not launch quilt "+modloaderVersion+" with vanilla " + getMinecraftVersion() + " in " + getInstaller().getPathString() + ": " + e.getMessage());
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