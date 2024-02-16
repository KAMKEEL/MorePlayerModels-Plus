package noppes.mpm.config;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigPerm {
    public static Configuration permConfig;

    public final static String CONFIG = "CONFIG";
    public final static String PART = "PART";
    public final static String EMOTES = "EMOTES";
    public final static String ADMIN = "ADMIN";

    // Static boolean fields for each permission
    public static boolean CONFIG_SKIN;
    public static boolean CONFIG_SOUND;
    public static boolean CONFIG_ENTITY;
    public static boolean CONFIG_SCALE;
    public static boolean CONFIG_HIDE;
    public static boolean CONFIG_PRESET;

    public static boolean PARTS;
    public static boolean PARTS_BEARD;
    public static boolean PARTS_BREAST;
    public static boolean PARTS_CAPE;
    public static boolean PARTS_CLAWS;
    public static boolean PARTS_EARS;
    public static boolean PARTS_FIN;
    public static boolean PARTS_HAIR;
    public static boolean PARTS_HORNS;
    public static boolean PARTS_LEGS;
    public static boolean PARTS_MOHAWK;
    public static boolean PARTS_PARTICLES;
    public static boolean PARTS_SKIRT;
    public static boolean PARTS_SNOUT;
    public static boolean PARTS_TAIL;
    public static boolean PARTS_WINGS;

    public static boolean EMOTE;
    public static boolean ANGRY;
    public static boolean BOW;
    public static boolean CRAWL;
    public static boolean DANCE;
    public static boolean DEATH;
    public static boolean HUG;
    public static boolean LOVE;
    public static boolean NO;
    public static boolean YES;
    public static boolean POINT;
    public static boolean SING;
    public static boolean SIT;
    public static boolean SLEEP;
    public static boolean WAG;
    public static boolean WAVE;

    public static boolean SETCLOAK;
    public static boolean SETMODEL;
    public static boolean SETNAME;
    public static boolean SETURL;
    public static boolean SIZE;
    public static boolean SCALE;
    public static boolean RELOAD;

    /**
     *  General Main Properties
     **/
    public static void init(File configFile) {
        permConfig = new Configuration(configFile);

        try {
            permConfig.load();

            // Config Permissions
            CONFIG_SKIN = permConfig.get(CONFIG, "CONFIG_SKIN", true).getBoolean();
            CONFIG_SOUND = permConfig.get(CONFIG, "CONFIG_SOUND", true).getBoolean();
            CONFIG_ENTITY = permConfig.get(CONFIG, "CONFIG_ENTITY", true).getBoolean();
            CONFIG_SCALE = permConfig.get(CONFIG, "CONFIG_SCALE", true).getBoolean();
            CONFIG_HIDE = permConfig.get(CONFIG, "CONFIG_HIDE", true).getBoolean();
            CONFIG_PRESET = permConfig.get(CONFIG, "CONFIG_PRESET", true).getBoolean();
            PARTS = permConfig.get(PART, "PARTS", true).getBoolean();
            PARTS_BEARD = permConfig.get(PART, "PARTS_BEARD", true).getBoolean();
            PARTS_BREAST = permConfig.get(PART, "PARTS_BREAST", true).getBoolean();
            PARTS_CAPE = permConfig.get(PART, "PARTS_CAPE", true).getBoolean();
            PARTS_CLAWS = permConfig.get(PART, "PARTS_CLAWS", true).getBoolean();
            PARTS_EARS = permConfig.get(PART, "PARTS_EARS", true).getBoolean();
            PARTS_FIN = permConfig.get(PART, "PARTS_FIN", true).getBoolean();
            PARTS_HAIR = permConfig.get(PART, "PARTS_HAIR", true).getBoolean();
            PARTS_HORNS = permConfig.get(PART, "PARTS_HORNS", true).getBoolean();
            PARTS_LEGS = permConfig.get(PART, "PARTS_LEGS", true).getBoolean();
            PARTS_MOHAWK = permConfig.get(PART, "PARTS_MOHAWK", true).getBoolean();
            PARTS_PARTICLES = permConfig.get(PART, "PARTS_PARTICLES", true).getBoolean();
            PARTS_SKIRT = permConfig.get(PART, "PARTS_SKIRT", true).getBoolean();
            PARTS_SNOUT = permConfig.get(PART, "PARTS_SNOUT", true).getBoolean();
            PARTS_TAIL = permConfig.get(PART, "PARTS_TAIL", true).getBoolean();
            PARTS_WINGS = permConfig.get(PART, "PARTS_WINGS", true).getBoolean();

            // Emote Permissions
            EMOTE = permConfig.get(EMOTES, "EMOTE", true).getBoolean();
            ANGRY = permConfig.get(EMOTES, "ANGRY", true).getBoolean();
            BOW = permConfig.get(EMOTES, "BOW", true).getBoolean();
            CRAWL = permConfig.get(EMOTES, "CRAWL", true).getBoolean();
            DANCE = permConfig.get(EMOTES, "DANCE", true).getBoolean();
            DEATH = permConfig.get(EMOTES, "DEATH", true).getBoolean();
            HUG = permConfig.get(EMOTES, "HUG", true).getBoolean();
            LOVE = permConfig.get(EMOTES, "LOVE", true).getBoolean();
            NO = permConfig.get(EMOTES, "NO", true).getBoolean();
            YES = permConfig.get(EMOTES, "YES", true).getBoolean();
            POINT = permConfig.get(EMOTES, "POINT", true).getBoolean();
            SING = permConfig.get(EMOTES, "SING", true).getBoolean();
            SIT = permConfig.get(EMOTES, "SIT", true).getBoolean();
            SLEEP = permConfig.get(EMOTES, "SLEEP", true).getBoolean();
            WAG = permConfig.get(EMOTES, "WAG", true).getBoolean();
            WAVE = permConfig.get(EMOTES, "WAVE", true).getBoolean();

            // Admin Permissions
            SETCLOAK = permConfig.get(ADMIN, "SETCLOAK", false).getBoolean();
            SETMODEL = permConfig.get(ADMIN, "SETMODEL", false).getBoolean();
            SETNAME = permConfig.get(ADMIN, "SETNAME", false).getBoolean();
            SETURL = permConfig.get(ADMIN, "SETURL", false).getBoolean();
            SIZE = permConfig.get(ADMIN, "SIZE", false).getBoolean();
            SCALE = permConfig.get(ADMIN, "SCALE", false).getBoolean();
            RELOAD = permConfig.get(ADMIN, "RELOAD", false).getBoolean();

        } catch (Exception e) {
            FMLLog.log(Level.ERROR, e, "MPM+ has had a problem loading its perm configuration");
        } finally {
            if (permConfig.hasChanged()) {
                permConfig.save();
            }
        }
    }
}
