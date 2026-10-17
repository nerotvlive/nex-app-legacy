package com.zyneonstudios.apex.nexapp.launchprocess;

import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.fabric.FabricInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.forge.ForgeInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.neoforge.NeoForgeInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.quilt.QuiltInstaller;
import com.zyneonstudios.apex.nexapp.launchprocess.modloaders.quilt.QuiltLauncher;
import com.zyneonstudios.apex.nexapp.launchprocess.vanilla.VanillaInstaller;

public class TestMain {

    public static void main(String[] args) {
        QuiltLauncher launcher = new QuiltLauncher(".run/instances/quilt");
        launcher.withMinecraftVersion("26.3");
        launcher.withModloaderVersion("0.30.1");
        launcher.launch();
    }

    private static void installAll() {
        VanillaInstaller vanillaInstaller = new VanillaInstaller(".run/instances/vanilla").withMinecraftVersion("26.3");
        vanillaInstaller.install();
        vanillaInstaller.withPath(".run/instances/all");
        vanillaInstaller.install();

        FabricInstaller fabricInstaller = new FabricInstaller(".run/instances/fabric");
        fabricInstaller.withMinecraftVersion("26.3");
        fabricInstaller.withModloaderVersion("0.19.5");
        fabricInstaller.install();
        fabricInstaller.withPath(".run/instances/all");
        fabricInstaller.install();

        ForgeInstaller forgeInstaller = new ForgeInstaller(".run/instances/forge");
        forgeInstaller.withMinecraftVersion("26.3");
        forgeInstaller.withModloaderVersion("26.3-66.0.13");
        forgeInstaller.install();
        forgeInstaller.withPath(".run/instances/all");
        forgeInstaller.install();

        NeoForgeInstaller neoForgeInstaller = new NeoForgeInstaller(".run/instances/neoforge");
        neoForgeInstaller.withMinecraftVersion("26.3");
        neoForgeInstaller.withModloaderVersion("26.3.0.88");
        neoForgeInstaller.install();
        neoForgeInstaller.withPath(".run/instances/all");
        neoForgeInstaller.install();

        QuiltInstaller quiltInstaller = new QuiltInstaller(".run/instances/quilt");
        quiltInstaller.withMinecraftVersion("26.3");
        quiltInstaller.withModloaderVersion("0.30.1");
        quiltInstaller.install();
        quiltInstaller.withPath(".run/instances/all");
        quiltInstaller.install();
    }
}