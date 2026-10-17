package com.zyneonstudios.apex.nexapp.launchprocess;

import fr.flowarg.openlauncherlib.NoFramework;

import java.io.File;
import java.nio.file.Path;

public interface MinecraftLauncher {

    MinecraftInstaller getInstaller();
    File getRunDir();
    Path getRunPath();
    String getRunPathString();
    String getMinecraftVersion();
    String[] getArgs();
    String[] getEnvArgs();
    int getMinMemory();
    int getMaxMemory();
    boolean isRunning();
    Process getGameProcess();
    NoFramework getFramework();
    MinecraftLauncher withInstaller(MinecraftInstaller installer);
    MinecraftLauncher withPath(File file);
    MinecraftLauncher withPath(Path path);
    MinecraftLauncher withPath(String pathString);
    MinecraftLauncher withMinecraftVersion(String minecraftVersion);
    MinecraftLauncher withArgs(String... args);
    MinecraftLauncher withEnvArgs(String... envArgs);
    MinecraftLauncher withFramework(NoFramework framework);
    MinecraftLauncher withMinMemory(int minMemory);
    MinecraftLauncher withMaxMemory(int maxMemory);
    boolean launch();
}