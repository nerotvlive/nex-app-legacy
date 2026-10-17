package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.fabric;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.vanilla.VanillaInstaller;
import fr.flowarg.flowupdater.versions.fabric.FabricVersion;
import fr.flowarg.flowupdater.versions.fabric.FabricVersionBuilder;

import java.io.File;
import java.nio.file.Path;

public class FabricInstaller extends VanillaInstaller implements MinecraftInstaller {

    private FabricVersionBuilder fabricBuilder;
    private FabricVersion fabricVersion;
    private String modloaderVersion;

    public FabricInstaller(File installDir) {
        this(installDir.getAbsolutePath());
    }

    public FabricInstaller(Path installPath) {
        this(installPath.toAbsolutePath().toString());
    }

    public FabricInstaller(String installPathString) {
        super(installPathString);
    }

    public FabricVersionBuilder getFabricBuilder() {
        return fabricBuilder;
    }

    public FabricVersion getFabricVersion() {
        return fabricVersion;
    }

    public String getModloaderVersion() {
        return modloaderVersion;
    }

    public FabricInstaller withFabricBuilder(FabricVersionBuilder fabricBuilder) {
        this.fabricBuilder = fabricBuilder;
        return this;
    }

    public FabricInstaller withFabricVersion(FabricVersion fabricVersion) {
        this.fabricVersion = fabricVersion;
        return this;
    }

    public FabricInstaller withModloaderVersion(String modloaderVersion) {
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
                        withInstaller(getBuilder().withModLoaderVersion(fabricVersion).build());
                        getInstaller().update(getPath());
                        return true;
                    } catch (Exception e) {
                        Main.getLogger().err("[Fabric Installer] Failed to install fabric version ("+fabricVersion+") with vanilla version ("+getMinecraftVersion()+") at path ("+getPathString()+") with error: "+e.getMessage());
                    }
                }
            } else {
                Main.getLogger().err("[Fabric Installer] Failed to install vanilla version " + getMinecraftVersion() + " in " + getPathString() + ". Aborting Fabric installation.");
            }
        } else {
            Main.getLogger().err("[Fabric Installer] Fabric modloader version is not specified. Aborting Fabric installation.");
        }
        return false;
    }

    private boolean verify() {
        try {
            if(fabricVersion == null) {
                if(fabricBuilder == null) {
                    fabricBuilder = new FabricVersionBuilder().withFabricVersion(modloaderVersion);
                }
                fabricVersion = fabricBuilder.build();
            }
            return true;
        } catch (Exception e) {
            Main.getLogger().err("[Fabric Installer] Failed to build & verify fabric installer. [path: \""+getPathString()+"\", vanilla: \""+getMinecraftVersion()+"\", fabric: \""+fabricVersion+"\", error: \""+e.getMessage()+"\"]");
        }
        return false;
    }
}
