package com.zyneonstudios.apex.nexapp.launchprocess.modloaders;

import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftInstaller;

public interface ModloaderInstaller extends MinecraftInstaller {

    String getModloaderVersion();
    ModloaderInstaller withModloaderVersion(String modloaderVersion);
}