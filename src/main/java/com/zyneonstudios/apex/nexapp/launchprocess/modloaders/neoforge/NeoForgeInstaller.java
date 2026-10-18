package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.neoforge;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.ModloaderInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.vanilla.VanillaInstaller;
import fr.flowarg.flowupdater.versions.neoforge.NeoForgeVersion;
import fr.flowarg.flowupdater.versions.neoforge.NeoForgeVersionBuilder;

import java.io.File;
import java.nio.file.Path;

public class NeoForgeInstaller extends VanillaInstaller implements ModloaderInstaller {

    private NeoForgeVersionBuilder neoforgeBuilder;
    private NeoForgeVersion neoforgeVersion;
    private String modloaderVersion;

    public NeoForgeInstaller(File installDir) {
        this(installDir.getAbsolutePath());
    }

    public NeoForgeInstaller(Path installPath) {
        this(installPath.toAbsolutePath().toString());
    }

    public NeoForgeInstaller(String installPathString) {
        super(installPathString);
    }

    public NeoForgeVersionBuilder getNeoForgeBuilder() {
        return neoforgeBuilder;
    }

    public NeoForgeVersion getNeoForgeVersion() {
        return neoforgeVersion;
    }

    @Override
    public String getModloaderVersion() {
        return modloaderVersion;
    }

    public NeoForgeInstaller withNeoForgeBuilder(NeoForgeVersionBuilder neoforgeBuilder) {
        this.neoforgeBuilder = neoforgeBuilder;
        return this;
    }

    public NeoForgeInstaller withNeoForgeVersion(NeoForgeVersion neoforgeVersion) {
        this.neoforgeVersion = neoforgeVersion;
        return this;
    }

    @Override
    public NeoForgeInstaller withModloaderVersion(String modloaderVersion) {
        this.modloaderVersion = modloaderVersion;
        return this;
    }

    @Override
    public boolean install() {
        if(modloaderVersion != null && !modloaderVersion.isBlank()) {
            boolean vanillaInstall = super.install();
            if (vanillaInstall) {
                if(verify()) {
                    try {
                        withUpdater(getBuilder().withModLoaderVersion(neoforgeVersion).build());
                        getUpdater().update(getPath());
                        return true;
                    } catch (Exception e) {
                        Main.getLogger().err("[NeoForge Installer] Failed to install neoforge version ("+neoforgeVersion+") with vanilla version ("+getMinecraftVersion()+") at path ("+getPathString()+") with error: "+e.getMessage());
                    }
                }
            } else {
                Main.getLogger().err("[NeoForge Installer] Failed to install vanilla version " + getMinecraftVersion() + " in " + getPathString() + ". Aborting NeoForge installation.");
            }
        } else {
            Main.getLogger().err("[NeoForge Installer] NeoForge modloader version is not specified. Aborting NeoForge installation.");
        }
        return false;
    }

    private boolean verify() {
        try {
            if(neoforgeVersion == null) {
                if(neoforgeBuilder == null) {
                    neoforgeBuilder = new NeoForgeVersionBuilder().withNeoForgeVersion(modloaderVersion);
                }
                neoforgeVersion = neoforgeBuilder.build();
            }
            return true;
        } catch (Exception e) {
            Main.getLogger().err("[NeoForge Installer] Failed to build & verify neoforge installer. [path: \""+getPathString()+"\", vanilla: \""+getMinecraftVersion()+"\", neoforge: \""+neoforgeVersion+"\", error: \""+e.getMessage()+"\"]");
        }
        return false;
    }
}
