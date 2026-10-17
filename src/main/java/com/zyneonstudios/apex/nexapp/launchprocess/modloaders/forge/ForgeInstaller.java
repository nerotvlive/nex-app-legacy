package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.forge;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.vanilla.VanillaInstaller;
import fr.flowarg.flowupdater.versions.forge.ForgeVersion;
import fr.flowarg.flowupdater.versions.forge.ForgeVersionBuilder;

import java.io.File;
import java.nio.file.Path;

public class ForgeInstaller extends VanillaInstaller implements MinecraftInstaller {

    private ForgeVersionBuilder forgeBuilder;
    private ForgeVersion forgeVersion;
    private String modloaderVersion;

    public ForgeInstaller(File installDir) {
        this(installDir.getAbsolutePath());
    }

    public ForgeInstaller(Path installPath) {
        this(installPath.toAbsolutePath().toString());
    }

    public ForgeInstaller(String installPathString) {
        super(installPathString);
    }

    public ForgeVersionBuilder getForgeBuilder() {
        return forgeBuilder;
    }

    public ForgeVersion getForgeVersion() {
        return forgeVersion;
    }

    public String getModloaderVersion() {
        return modloaderVersion;
    }

    public ForgeInstaller withForgeBuilder(ForgeVersionBuilder forgeBuilder) {
        this.forgeBuilder = forgeBuilder;
        return this;
    }

    public ForgeInstaller withForgeVersion(ForgeVersion forgeVersion) {
        this.forgeVersion = forgeVersion;
        return this;
    }

    public ForgeInstaller withModloaderVersion(String modloaderVersion) {
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
                        withInstaller(getBuilder().withModLoaderVersion(forgeVersion).build());
                        getInstaller().update(getPath());
                        return true;
                    } catch (Exception e) {
                        Main.getLogger().err("[Forge Installer] Failed to install forge version ("+forgeVersion+") with vanilla version ("+getMinecraftVersion()+") at path ("+getPathString()+") with error: "+e.getMessage());
                    }
                }
            } else {
                Main.getLogger().err("[Forge Installer] Failed to install vanilla version " + getMinecraftVersion() + " in " + getPathString() + ". Aborting Forge installation.");
            }
        } else {
            Main.getLogger().err("[Forge Installer] Forge modloader version is not specified. Aborting Forge installation.");
        }
        return false;
    }

    private boolean verify() {
        try {
            if(forgeVersion == null) {
                if(forgeBuilder == null) {
                    forgeBuilder = new ForgeVersionBuilder().withForgeVersion(modloaderVersion);
                }
                forgeVersion = forgeBuilder.build();
            }
            return true;
        } catch (Exception e) {
            Main.getLogger().err("[Forge Installer] Failed to build & verify forge installer. [path: \""+getPathString()+"\", vanilla: \""+getMinecraftVersion()+"\", forge: \""+forgeVersion+"\", error: \""+e.getMessage()+"\"]");
        }
        return false;
    }
}
