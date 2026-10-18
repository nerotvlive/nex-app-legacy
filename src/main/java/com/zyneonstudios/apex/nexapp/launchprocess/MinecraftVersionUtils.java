package com.zyneonstudios.apex.nexapp.launchprocess;

import com.zyneonstudios.nexus.utilities.NexusUtilities;

public class MinecraftVersionUtils {

    public static MinecraftVersionUtils.Type getType(String version) {
        if (version.contains(".")) {
            try {
                if (version.startsWith("1.")) {
                    version = version.substring(2);
                }

                int i = Integer.parseInt(version.split("\\.")[0]);
                if (i < 17) {
                    return MinecraftVersionUtils.Type.VERY_OLD;
                }

                if (i < 21) {
                    return MinecraftVersionUtils.Type.OLD;
                }

                if (i < 26) {
                    return MinecraftVersionUtils.Type.SEMI_NEW;
                }

                return MinecraftVersionUtils.Type.NEW;
            } catch (Exception e) {
                NexusUtilities.getLogger().err("[SYSTEM] Couldn't resolve Minecraft version " + version + ": " + e.getMessage());
            }
        }

        return MinecraftVersionUtils.Type.NEW;
    }

    public static MinecraftVersionUtils.ForgeType getForgeType(String mcVersion) {
        if (mcVersion.contains(".")) {
            try {
                int i = Integer.parseInt(mcVersion.split("\\.")[1]);
                if (i < 12) {
                    return MinecraftVersionUtils.ForgeType.OLD;
                }

                return MinecraftVersionUtils.ForgeType.NEW;
            } catch (Exception e) {
                NexusUtilities.getLogger().err("[SYSTEM] Couldn't resolve Minecraft version " + mcVersion + ": " + e.getMessage());
            }
        }

        return null;
    }

    public static String getBaseForgeVersion(String forgeVersion, String minecraftVersion) {
        return forgeVersion.replace(minecraftVersion+"-","").replace("-"+minecraftVersion,"");
    }

    public static String forgeForInstaller(String forgeVersion, String minecraftVersion) {
        return minecraftVersion+"-"+getBaseForgeVersion(forgeVersion,minecraftVersion);
    }

    public static String forgeForLauncher(String forgeVersion, String minecraftVersion) {
        return getBaseForgeVersion(forgeVersion,minecraftVersion);
    }

    public static enum Type {
        VERY_OLD,
        OLD,
        SEMI_NEW,
        NEW;

        private Type() {
        }
    }

    public static enum ForgeType {
        OLD,
        NEW;

        private ForgeType() {
        }
    }
}
