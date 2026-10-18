package com.zyneonstudios.apex.nexapp.launchprocess;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import java.io.File;
import java.nio.file.Path;

public interface MinecraftInstaller {

    Path getPath();
    String getPathString();
    FlowUpdater.FlowUpdaterBuilder getBuilder();
    VanillaVersion.VanillaVersionBuilder getVanillaBuilder();
    FlowUpdater getUpdater();
    VanillaVersion getVanillaVersion();
    String getMinecraftVersion();
    MinecraftInstaller withPath(File file);
    MinecraftInstaller withPath(Path path);
    MinecraftInstaller withPath(String pathString);
    MinecraftInstaller withBuilder(FlowUpdater.FlowUpdaterBuilder builder);
    MinecraftInstaller withVanillaBuilder(VanillaVersion.VanillaVersionBuilder versionBuilder);
    MinecraftInstaller withUpdater(FlowUpdater installer);
    MinecraftInstaller withVanillaVersion(VanillaVersion vanillaVersion);
    MinecraftInstaller withMinecraftVersion(String version);
    boolean install();
}