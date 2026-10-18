package com.zyneonstudios.apex.nexapp.launchprocess.modloaders.forge;

public class ForgeVersionPatches {

    public static String getBaseVersion(String forgeVersion, String minecraftVersion) {
        return forgeVersion.replace(minecraftVersion+"-","").replace("-"+minecraftVersion,"");
    }

    public static String forInstaller(String forgeVersion, String minecraftVersion) {
        return minecraftVersion+"-"+getBaseVersion(forgeVersion,minecraftVersion);
    }

    public static String forLauncher(String forgeVersion, String minecraftVersion) {
        return getBaseVersion(forgeVersion,minecraftVersion);
    }
}