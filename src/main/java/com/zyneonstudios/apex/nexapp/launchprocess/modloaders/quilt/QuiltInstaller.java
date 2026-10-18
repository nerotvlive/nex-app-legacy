package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.quilt;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.ModloaderInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.vanilla.VanillaInstaller;
import fr.flowarg.flowupdater.versions.fabric.QuiltVersion;
import fr.flowarg.flowupdater.versions.fabric.QuiltVersionBuilder;

import java.io.File;
import java.nio.file.Path;

public class QuiltInstaller extends VanillaInstaller implements ModloaderInstaller {

    private QuiltVersionBuilder quiltBuilder;
    private QuiltVersion quiltVersion;
    private String modloaderVersion;

    public QuiltInstaller(File installDir) {
        this(installDir.getAbsolutePath());
    }

    public QuiltInstaller(Path installPath) {
        this(installPath.toAbsolutePath().toString());
    }

    public QuiltInstaller(String installPathString) {
        super(installPathString);
    }

    public QuiltVersionBuilder getQuiltBuilder() {
        return quiltBuilder;
    }

    public QuiltVersion getQuiltVersion() {
        return quiltVersion;
    }

    @Override
    public String getModloaderVersion() {
        return modloaderVersion;
    }

    public QuiltInstaller withQuiltBuilder(QuiltVersionBuilder quiltBuilder) {
        this.quiltBuilder = quiltBuilder;
        return this;
    }

    public QuiltInstaller withQuiltVersion(QuiltVersion quiltVersion) {
        this.quiltVersion = quiltVersion;
        return this;
    }

    @Override
    public QuiltInstaller withModloaderVersion(String modloaderVersion) {
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
                        withUpdater(getBuilder().withModLoaderVersion(quiltVersion).build());
                        getUpdater().update(getPath());
                        return true;
                    } catch (Exception e) {
                        Main.getLogger().err("[Quilt Installer] Failed to install quilt version ("+quiltVersion+") with vanilla version ("+getMinecraftVersion()+") at path ("+getPathString()+") with error: "+e.getMessage());
                    }
                }
            } else {
                Main.getLogger().err("[Quilt Installer] Failed to install vanilla version " + getMinecraftVersion() + " in " + getPathString() + ". Aborting Quilt installation.");
            }
        } else {
            Main.getLogger().err("[Quilt Installer] Quilt modloader version is not specified. Aborting Quilt installation.");
        }
        return false;
    }

    private boolean verify() {
        try {
            if(quiltVersion == null) {
                if(quiltBuilder == null) {
                    quiltBuilder = new QuiltVersionBuilder().withQuiltVersion(modloaderVersion);
                }
                quiltVersion = quiltBuilder.build();
            }
            return true;
        } catch (Exception e) {
            Main.getLogger().err("[Quilt Installer] Failed to build & verify quilt installer. [path: \""+getPathString()+"\", vanilla: \""+getMinecraftVersion()+"\", quilt: \""+quiltVersion+"\", error: \""+e.getMessage()+"\"]");
        }
        return false;
    }
}
