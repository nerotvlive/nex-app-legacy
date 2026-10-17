package com.zyneonstudios.apex.nexapp.launchprocess.vanilla;

import com.zyneonstudios.apex.nexapp.Main;
import com.zyneonstudios.apex.nexapp.launchprocess.MinecraftInstaller;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.versions.VanillaVersion;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class VanillaInstaller implements MinecraftInstaller {

    private FlowUpdater.FlowUpdaterBuilder builder;
    private VanillaVersion.VanillaVersionBuilder vanillaBuilder;

    private FlowUpdater installer;
    private VanillaVersion vanillaVersion;

    private String minecraftVersion;

    private String pathString;
    private Path path;

    public VanillaInstaller(File installDir) {
        this(installDir.getAbsolutePath());
    }

    public VanillaInstaller(Path installPath) {
        this(installPath.toAbsolutePath().toFile());
    }

    public VanillaInstaller(String installPathString) {
        if(!verifyPath(installPathString)) {
            throw new IllegalArgumentException("[Vanilla Installer] Failed to verify installation path ("+installPathString+")");
        }
        this.pathString = installPathString;
        this.path = Paths.get(installPathString);
        minecraftVersion = null;
        builder = new FlowUpdater.FlowUpdaterBuilder();
        vanillaBuilder = new VanillaVersion.VanillaVersionBuilder();
    }

    private boolean verifyPath(String installPathString) {
        if(installPathString != null && !installPathString.isBlank()) {
            File installDir = new File(installPathString);
            if(!installDir.exists()) {
                if(!installDir.mkdirs()) {
                    Main.getLogger().err("[Vanilla Installer] Could not create install directory ("+installPathString+")");
                    installDir = null;
                    System.gc();
                    return false;
                }
            }
            installDir = null;
        } else {
            Main.getLogger().err("[Vanilla Installer] Install path cannot be null or blank!");
            return false;
        }
        if(path != null && pathString != null && !pathString.isBlank()) {
            File installDir = new File(pathString);
            if(installDir.exists()&&installDir.isDirectory()) {
                if(installDir.listFiles() == null || Objects.requireNonNull(installDir.listFiles()).length == 0) {
                    if(!installDir.delete()) {
                        installDir.deleteOnExit();
                    }
                }
            }
            installDir = null;
        }
        System.gc();
        return true;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getPathString() {
        return pathString;
    }

    @Override
    public FlowUpdater.FlowUpdaterBuilder getBuilder() {
        return builder;
    }

    @Override
    public VanillaVersion.VanillaVersionBuilder getVanillaBuilder() {
        return vanillaBuilder;
    }

    @Override
    public FlowUpdater getInstaller() {
        return installer;
    }

    @Override
    public VanillaVersion getVanillaVersion() {
        return vanillaVersion;
    }

    @Override
    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    @Override
    public VanillaInstaller withPath(File file) {
        return withPath(file.getAbsolutePath());
    }

    @Override
    public VanillaInstaller withPath(Path path) {
        if(!verifyPath(path.toAbsolutePath().toString())) {
            return null;
        }
        this.path = path.toAbsolutePath();
        this.pathString = path.toAbsolutePath().toString();
        return this;
    }

    @Override
    public VanillaInstaller withPath(String pathString) {
        return withPath(Paths.get(pathString));
    }

    @Override
    public VanillaInstaller withBuilder(FlowUpdater.FlowUpdaterBuilder builder) {
        this.builder = builder;
        return this;
    }

    @Override
    public VanillaInstaller withVanillaBuilder(VanillaVersion.VanillaVersionBuilder vanillaBuilder) {
        this.vanillaBuilder = vanillaBuilder;
        return this;
    }

    @Override
    public VanillaInstaller withInstaller(FlowUpdater installer) {
        this.installer = installer;
        return this;
    }

    @Override
    public VanillaInstaller withVanillaVersion(VanillaVersion vanillaVersion) {
        this.vanillaVersion = vanillaVersion;
        return this;
    }

    @Override
    public VanillaInstaller withMinecraftVersion(String version) {
        this.minecraftVersion = version;
        return this;
    }

    @Override
    public boolean install() {
        if(minecraftVersion!=null&&!minecraftVersion.isBlank()&&pathString!=null&&!pathString.isBlank()) {
            if(verify()) {
                try {
                    installer.update(path);
                    return true;
                } catch (Exception e) {
                    Main.getLogger().err("[Vanilla Installer] Failed to install vanilla version ("+minecraftVersion+") at path ("+pathString+") with error: "+e.getMessage());
                }
            }
        } else {
            Main.getLogger().err("[Vanilla Installer] Path and version must be set before calling install(). [path: \""+pathString+"\", version: \""+minecraftVersion+"\"]");
        }
        return false;
    }

    private boolean verify() {
        try {
            if (installer == null) {
                if (vanillaVersion == null) {
                    if (vanillaBuilder == null) {
                        vanillaBuilder = new VanillaVersion.VanillaVersionBuilder();
                    }
                    vanillaVersion = vanillaBuilder.withName(minecraftVersion).build();
                }
                if (builder == null) {
                    builder = new FlowUpdater.FlowUpdaterBuilder();
                }
                installer = builder.withVanillaVersion(vanillaVersion).build();
            }
            return true;
        } catch (Exception e) {
            Main.getLogger().err("[Vanilla Installer] Failed to build & verify installer. [path: \""+pathString+"\", version: \""+minecraftVersion+"\", error: \""+e.getMessage()+"\"]");
        }
        return false;
    }
}