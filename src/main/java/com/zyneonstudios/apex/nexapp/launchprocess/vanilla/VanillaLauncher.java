package com.zyneonstudios.apex.nexapp.launchprocess.vanilla;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftLauncher;
import com.zyneonstudios.apex.nexapp.utilities.JavaUtilities;
import com.zyneonstudios.nexus.utilities.system.OperatingSystem;
import fr.flowarg.openlauncherlib.NoFramework;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.minecraft.GameFolder;
import live.nerotv.aminecraftlauncher.launcher.MinecraftVersion;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class VanillaLauncher implements MinecraftLauncher {

    private int minMemory = 1024;
    private int maxMemory = 2048;
    private File runDir;
    private Path runPath;
    private String runPathString;
    private String minecraftVersion;
    private String[] args;
    private String[] envArgs;
    private VanillaInstaller installer;
    private NoFramework framework;
    private Process gameProcess;
    private boolean running = false;

    public VanillaLauncher(File runDir) {
        this(runDir.getAbsolutePath());
    }

    public VanillaLauncher(Path runPath) {
        this(runPath.toAbsolutePath().toFile());
    }

    public VanillaLauncher(String runPathString) {
        if(verifyPath(runPathString)) {

        } else {
            throw new IllegalArgumentException("[Minecraft Launcher] Couldn't verify run directory ("+runPathString+")!");
        }
    }

    private boolean verifyPath(String pathString) {
        if(pathString != null && !pathString.isBlank()) {
            File installDir = new File(pathString);
            if(!installDir.exists()) {
                if(!installDir.mkdirs()) {
                    Main.getLogger().err("[Minecraft Launcher] Could not prepare install directory ("+pathString+")");
                    installDir = null;
                    System.gc();
                    return false;
                }
            }
            installDir = null;
        } else {
            Main.getLogger().err("[Minecraft Launcher] Run path cannot be null or blank!");
            return false;
        }
        if(runDir!=null&&runDir.exists()&&runDir.isDirectory()) {
            if(runDir.listFiles()==null || Objects.requireNonNull(runDir.listFiles()).length == 0) {
                if(!runDir.delete()) {
                    runDir.deleteOnExit();
                }
            }
            runDir = null;
            runPath = null;
            runPathString = null;
            System.gc();
        }
        runPath = Paths.get(pathString).toAbsolutePath();
        runPathString = runPath.toString();
        runDir = new File(runPathString);
        return true;
    }

    @Override
    public VanillaInstaller getInstaller() {
        return installer;
    }

    @Override
    public File getRunDir() {
        return runDir;
    }

    @Override
    public Path getRunPath() {
        return runPath;
    }

    @Override
    public String getRunPathString() {
        return runPathString;
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public String[] getArgs() {
        return args;
    }

    @Override
    public String[] getEnvArgs() {
        return envArgs;
    }

    @Override
    public int getMinMemory() {
        return minMemory;
    }

    @Override
    public int getMaxMemory() {
        return maxMemory;
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
    public NoFramework getFramework() {
        return framework;
    }

    public VanillaLauncher withInstaller(VanillaInstaller installer) {
        this.installer = installer;
        return this;
    }

    @Override @Deprecated
    public VanillaLauncher withInstaller(MinecraftInstaller installer) {
        return withInstaller((VanillaInstaller)installer);
    }

    @Override
    public VanillaLauncher withPath(File file) {
        return withPath(file.getAbsolutePath());
    }

    @Override
    public VanillaLauncher withPath(Path path) {
        return withPath(path.toAbsolutePath().toString());
    }

    @Override
    public VanillaLauncher withPath(String pathString) {
        if(verifyPath(pathString)) {
            return this;
        }
        return null;
    }

    @Override
    public VanillaLauncher withMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
        return this;
    }

    @Override
    public VanillaLauncher withArgs(String... args) {
        this.args = args;
        return this;
    }

    @Override
    public VanillaLauncher withEnvArgs(String... envArgs) {
        this.envArgs = envArgs;
        return this;
    }

    @Override
    public VanillaLauncher withFramework(NoFramework framework) {
        this.framework = framework;
        return this;
    }

    @Override
    public VanillaLauncher withMinMemory(int minMemory) {
        this.minMemory = minMemory;
        return this;
    }

    @Override
    public VanillaLauncher withMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
        return this;
    }

    @Override
    public boolean launch() {
        if(installer == null) {
            installer = new VanillaInstaller(runDir).withMinecraftVersion(minecraftVersion);
        }
        if(!running) {
            running = true;
            if (installer.install()) {
                if(framework == null) {
                    framework = new NoFramework(runPath, new AuthInfos("TestUser","", "uniqueid"), GameFolder.FLOW_UPDATER);
                    this.framework.getAdditionalVmArgs().add("-Xms" + minMemory + "M");
                    this.framework.getAdditionalVmArgs().add("-Xmx" + maxMemory + "M");
                    if (OperatingSystem.getType() == OperatingSystem.Type.macOS) {
                        this.framework.getAdditionalVmArgs().add("-XstartOnFirstThread");
                    }
                    if (args != null) {
                        for (String arg : args) {
                            this.framework.getAdditionalVmArgs().add(arg);
                        }
                    }
                    if (envArgs != null) {
                        for (String arg : envArgs) {
                            this.framework.getAdditionalArgs().add(arg);
                        }
                    }
                }
                JavaUtilities.setJava(MinecraftVersion.getType(minecraftVersion));
                try {
                    this.gameProcess = this.framework.launch(minecraftVersion, null, NoFramework.ModLoader.VANILLA);
                    this.gameProcess.onExit().thenRun(() -> running = false);
                    return true;
                } catch (Exception e) {
                    Main.getLogger().err("[Minecraft Launcher] Could not launch vanilla " + minecraftVersion + " in " + installer.getPathString() + ": " + e.getMessage());
                    throw new RuntimeException(e);
                }
            } else {
                Main.getLogger().err("[Minecraft Launcher] Could not install vanilla " + installer.getMinecraftVersion() + " in " + installer.getPathString());
            }
        } else {
            Main.getLogger().err("[Minecraft Launcher] The game is already running");
        }
        return false;
    }
}