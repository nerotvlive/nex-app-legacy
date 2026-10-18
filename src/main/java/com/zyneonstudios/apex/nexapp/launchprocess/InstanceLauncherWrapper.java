package com.zyneonstudios.apex.nexapp.launchprocess;

import com.zyneonstudios.apex.nexapp.launchprocess.hooks.MinecraftLauncherHook;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.ModloaderInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.ModloaderLauncher;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.fabric.FabricInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.forge.ForgeInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.neoforge.NeoForgeInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.quilt.QuiltInstaller;
import com.zyneonstudios.apex.nexapp.search.zyndex.local.LocalInstance;
import com.zyneonstudios.nexus.instance.Zynstance;
import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.versions.IModLoaderVersion;
import fr.flowarg.flowupdater.versions.ModLoaderVersionBuilder;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.flowarg.openlauncherlib.NoFramework;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InstanceLauncherWrapper implements ModloaderInstaller, ModloaderLauncher {

    private MinecraftInstaller installer;
    private MinecraftLauncher launcher;
    private MinecraftLauncherHook preLaunchHook;
    private MinecraftLauncherHook postLaunchHook;
    private MinecraftLauncherHook postCloseHook;

    private final LocalInstance instance;
    private final Zynstance zynstance;

    private String modloaderVersionNameOverride;
    private IModLoaderVersion modloaderVersionOverride;
    private FlowUpdater.FlowUpdaterBuilder updaterBuilderOverride;
    private VanillaVersion.VanillaVersionBuilder vanillaVersionBuilderOverride;

    @SuppressWarnings("rawtypes")
    private ModLoaderVersionBuilder modloaderVersionBuilderOverride;

    private FlowUpdater updaterOverride;
    private VanillaVersion vanillaVersionOverride;
    private String minecraftVersionOverride;
    private String[] jvmArgsOverride;
    private String[] envArgsOverride;
    private int minMemoryOverride = 0;
    private int maxMemoryOverride = 0;
    private NoFramework frameworkOverride;

    public InstanceLauncherWrapper(LocalInstance instance) {
        this.instance = instance;
        this.zynstance = instance.getInstance();

        this.preLaunchHook = MinecraftLauncherHook.getInstancePreLaunchHook(this, instance);
        this.postLaunchHook = MinecraftLauncherHook.getInstancePostLaunchHook(this, instance);
        this.postCloseHook = MinecraftLauncherHook.getInstanceGameCloseHook(this, instance);
    }

    @Override
    public String getModloaderVersion() {
        if(modloaderVersionNameOverride!=null) {
            return modloaderVersionNameOverride;
        } else if(zynstance.getFabricVersion()!=null) {
            return zynstance.getFabricVersion();
        } else if(zynstance.getForgeVersion()!=null) {
            return zynstance.getForgeVersion();
        } else if(zynstance.getNeoForgeVersion()!=null) {
            return zynstance.getNeoForgeVersion();
        } else if(zynstance.getQuiltVersion()!=null) {
            return zynstance.getQuiltVersion();
        } else {
            return null;
        }
    }

    @Override
    public InstanceLauncherWrapper withModloaderVersion(String modloaderVersion) {
        this.modloaderVersionNameOverride = modloaderVersion;
        return this;
    }

    @Override
    public Path getPath() {
        return Paths.get(instance.getPath()).toAbsolutePath();
    }

    @Override
    public String getPathString() {
        return instance.getPath();
    }

    @Override
    public FlowUpdater.FlowUpdaterBuilder getBuilder() {
        if(updaterBuilderOverride!=null) {
            return updaterBuilderOverride;
        } else if(installer != null) {
            return installer.getBuilder();
        } else {
            return null;
        }
    }

    @Override
    public VanillaVersion.VanillaVersionBuilder getVanillaBuilder() {
        if(vanillaVersionBuilderOverride != null) {
            return vanillaVersionBuilderOverride;
        } else if(installer != null) {
            return installer.getVanillaBuilder();
        } else {
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    public ModLoaderVersionBuilder getModloaderBuilder() {
        if(modloaderVersionBuilderOverride!=null) {
            return modloaderVersionBuilderOverride;
        } else if(installer != null) {
            try { return ((FabricInstaller)installer).getFabricBuilder(); } catch (Exception ignore) {}
            try { return ((ForgeInstaller)installer).getForgeBuilder(); } catch (Exception ignore) {}
            try { return ((NeoForgeInstaller)installer).getNeoForgeBuilder(); } catch (Exception ignore) {}
            try { return ((QuiltInstaller)installer).getQuiltBuilder(); } catch (Exception ignore) {}
        }
        return null;
    }

    @Override
    public InstanceLauncherWrapper getInstaller() {
        return this;
    }

    @Override
    public FlowUpdater getUpdater() {
        if(updaterOverride != null) {
            return updaterOverride;
        } else if(installer != null) {
            return installer.getUpdater();
        } else {
            return null;
        }
    }

    @Override
    public File getRunDir() {
        return getPath().toFile();
    }

    @Override
    public Path getRunPath() {
        return getPath();
    }

    @Override
    public String getRunPathString() {
        return getPathString();
    }

    @Override
    public VanillaVersion getVanillaVersion() {
        if(vanillaVersionOverride != null) {
            return vanillaVersionOverride;
        } else if(installer != null) {
            return installer.getVanillaVersion();
        } else {
            return null;
        }
    }

    public IModLoaderVersion getModloaderVersionType() {
        if(modloaderVersionOverride != null) {
            return modloaderVersionOverride;
        } else if(installer != null) {
            try { return ((FabricInstaller)installer).getFabricVersion(); } catch (Exception ignore) {}
            try { return ((ForgeInstaller)installer).getForgeVersion(); } catch (Exception ignore) {}
            try { return ((NeoForgeInstaller)installer).getNeoForgeVersion(); } catch (Exception ignore) {}
            try { return ((QuiltInstaller)installer).getQuiltVersion(); } catch (Exception ignore) {}
        }
        return null;
    }

    @Override
    public String getMinecraftVersion() {
        if(minecraftVersionOverride != null) {
            return minecraftVersionOverride;
        } else if(installer != null) {
            return installer.getMinecraftVersion();
        } else if(launcher != null) {
            return launcher.getMinecraftVersion();
        } else {
            return zynstance.getMinecraftVersion();
        }
    }

    @Override
    public String[] getArgs() {
        if(jvmArgsOverride != null)  {
            return jvmArgsOverride;
        } else if(instance.getJvmArgs() != null && !instance.getJvmArgs().isEmpty()) {
            return instance.getJvmArgs().toArray(new String[0]);
        } else if(launcher != null && launcher.getArgs() != null) {
            return launcher.getArgs();
        }
        return new String[0];
    }

    @Override
    public String[] getEnvArgs() {
        if(envArgsOverride != null) {
            return envArgsOverride;
        } else if(instance.getEnvArgs() != null && !instance.getEnvArgs().isEmpty()) {
            return instance.getEnvArgs().toArray(new String[0]);
        } else if(launcher != null && launcher.getEnvArgs() != null) {
            return launcher.getEnvArgs();
        }
        return new String[0];
    }

    @Override
    public int getMinMemory() {
        if(minMemoryOverride > 0) {
            return Math.max(minMemoryOverride, 512);
        } else if(launcher != null) {
            return launcher.getMinMemory();
        } else if(instance.getMemory() > 512) {
            return instance.getMemory();
        }
        return 1024;
    }

    @Override
    public int getMaxMemory() {
        if(maxMemoryOverride > 0) {
            return Math.max(maxMemoryOverride, 512);
        } else if(launcher != null) {
            return launcher.getMaxMemory();
        } else if(instance.getMemory() > 512) {
            return instance.getMemory();
        }
        return 2048;
    }

    @Override
    public boolean isRunning() {
        if(launcher != null) {
            return launcher.isRunning();
        }
        return false;
    }

    @Override
    public Process getGameProcess() {
        if(launcher != null) {
            return launcher.getGameProcess();
        }
        return null;
    }

    @Override
    public NoFramework getFramework() {
        if(frameworkOverride != null) {
            return frameworkOverride;
        } else if(launcher != null) {
            return launcher.getFramework();
        } else {
            return null;
        }
    }

    @Override
    public MinecraftLauncherHook getPreLaunchHook() {
        return preLaunchHook;
    }

    @Override
    public MinecraftLauncherHook getPostLaunchHook() {
        return postLaunchHook;
    }

    @Override
    public MinecraftLauncherHook getPostCloseHook() {
        return postCloseHook;
    }

    /**
     * Do not use this method.
     * This method is marked as deprecated and for removal to warn you.
     *
     * @param file does nothing, this method is not compatible with the InstanceLauncherWrapper
     * @throws NullPointerException always
     */
    @Override @Deprecated(forRemoval = true)
    public InstanceLauncherWrapper withPath(File file) throws NullPointerException {
        throw new NullPointerException("There is not set-able path in the InstanceLauncherWrapper. Do not use this method. The path is set by LocalInstance variable.");
    }

    /**
     * Do not use this method.
     * This method is marked as deprecated and for removal to warn you.
     *
     * @param path does nothing, this method is not compatible with the InstanceLauncherWrapper
     * @throws NullPointerException always
     */
    @Override @Deprecated(forRemoval = true)
    public InstanceLauncherWrapper withPath(Path path) throws NullPointerException {
        throw new NullPointerException("There is not set-able path in the InstanceLauncherWrapper. Do not use this method. The path is set by LocalInstance variable.");
    }

    /**
     * Do not use this method.
     * This method is marked as deprecated and for removal to warn you.
     *
     * @param pathString does nothing, this method is not compatible with the InstanceLauncherWrapper
     * @throws NullPointerException always
     */
    @Override @Deprecated(forRemoval = true)
    public InstanceLauncherWrapper withPath(String pathString) throws NullPointerException {
        throw new NullPointerException("There is not set-able path in the InstanceLauncherWrapper. Do not use this method. The path is set by LocalInstance variable.");
    }

    @Override
    public InstanceLauncherWrapper withBuilder(FlowUpdater.FlowUpdaterBuilder builder) {
        updaterBuilderOverride = builder;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withVanillaBuilder(VanillaVersion.VanillaVersionBuilder versionBuilder) {
        vanillaVersionBuilderOverride = versionBuilder;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public InstanceLauncherWrapper withModloaderBuilder(ModLoaderVersionBuilder modloaderVersionBuilder) {
        modloaderVersionBuilderOverride = modloaderVersionBuilder;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withInstaller(MinecraftInstaller installer) {
        this.installer = installer;
        return this;
    }

    public InstanceLauncherWrapper withLauncher(MinecraftLauncher launcher) {
        this.launcher = launcher;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withUpdater(FlowUpdater installer) {
        updaterOverride = installer;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withVanillaVersion(VanillaVersion vanillaVersion) {
        vanillaVersionOverride = vanillaVersion;
        return this;
    }

    @SuppressWarnings("rawtypes")
    public InstanceLauncherWrapper withModloaderVersion(IModLoaderVersion modloaderVersion) {
        modloaderVersionOverride = modloaderVersion;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withMinecraftVersion(String version) {
        minecraftVersionOverride = version;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withArgs(String... args) {
        jvmArgsOverride = args;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withEnvArgs(String... envArgs) {
        envArgsOverride = envArgs;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withFramework(NoFramework framework) {
        frameworkOverride = framework;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withMinMemory(int minMemory) {
        minMemoryOverride = minMemory;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withMaxMemory(int maxMemory) {
        maxMemoryOverride = maxMemory;
        return this;
    }

    @Override
    public InstanceLauncherWrapper withPreLaunchHook(MinecraftLauncherHook preLaunchHook) {
        this.preLaunchHook = preLaunchHook;
        this.preLaunchHook.setInstance(instance);
        return this;
    }

    @Override
    public InstanceLauncherWrapper withPostLaunchHook(MinecraftLauncherHook postLaunchHook) {
        this.postLaunchHook = postLaunchHook;
        this.postLaunchHook.setInstance(instance);
        return this;
    }

    @Override
    public InstanceLauncherWrapper withPostCloseHook(MinecraftLauncherHook postCloseHook) {
        this.postCloseHook = postCloseHook;
        this.postCloseHook.setInstance(instance);
        return this;
    }

    @Override
    public boolean install() {
        if(verifyInstaller()) {
            return installer.install();
        }
        return false;
    }

    @Override
    public boolean launch() {

        return false;
    }


    private boolean verifyInstaller() {
        return false;
    }

    private String getModloaderFromZynstance() {
        if(zynstance.getFabricVersion()!=null) {
            return "fabric";
        } else if(zynstance.getForgeVersion()!=null) {
            return "forge";
        } else if(zynstance.getNeoForgeVersion()!=null) {
            return "neoforge";
        } else if(zynstance.getQuiltVersion()!=null) {
            return "quilt";
        }
        return null;
    }

    private String getModloaderVersionFromZynstance() {
        if(zynstance.getFabricVersion()!=null) {
            return zynstance.getFabricVersion();
        } else if(zynstance.getForgeVersion()!=null) {
            return zynstance.getForgeVersion();
        } else if(zynstance.getNeoForgeVersion()!=null) {
            return zynstance.getNeoForgeVersion();
        } else if(zynstance.getQuiltVersion()!=null) {
            return zynstance.getQuiltVersion();
        }
        return null;
    }

    private boolean verifyLauncher() {
        if(launcher == null) {

        } else {
            return true;
        }
        return false;
    }
 }