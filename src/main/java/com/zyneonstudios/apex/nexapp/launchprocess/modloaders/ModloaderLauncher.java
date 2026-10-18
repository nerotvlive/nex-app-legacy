package com.zyneonstudios.apex.nexapp.launchprocess.modloaders;

import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftLauncher;

public interface ModloaderLauncher extends MinecraftLauncher {

    String getModloaderVersion();
    ModloaderLauncher withModloaderVersion(String modloaderVersion);
}