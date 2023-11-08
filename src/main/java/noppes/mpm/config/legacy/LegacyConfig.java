package noppes.mpm.config.legacy;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import noppes.mpm.constants.EnumAnimation;

import java.io.File;

public class LegacyConfig {
    @ConfigProp
    public static int Tooltips = 2;

    @ConfigProp(info="Enable different perspective heights for different model sizes")
    public static boolean EnablePOV = true;

    @ConfigProp(info="Enables the item on your back")
    public static boolean EnableBackItem = true;

    @ConfigProp(info="Enables chat bubbles")
    public static boolean EnableChatBubbles = false;

    @ConfigProp(info="Enables MorePlayerModels startup update message")
    public static boolean EnableUpdateChecker = true;

    @ConfigProp(info="Set to false if you dont want to see player particles")
    public static boolean EnableParticles = true;

    @ConfigProp(info="Set to true if you dont want to see hide player names")
    public static boolean HidePlayerNames = false;

    @ConfigProp(info="Set to true if you dont want to see hide selection boxes when pointing to blocks")
    public static boolean HideSelectionBox = false;

    @ConfigProp(info="Used to register buttons to animations")
    public static int button1 = EnumAnimation.SLEEPING_SOUTH.ordinal();
    @ConfigProp(info="Used to register buttons to animations")
    public static int button2 = EnumAnimation.SITTING.ordinal();
    @ConfigProp(info="Used to register buttons to animations")
    public static int button3 = EnumAnimation.CRAWLING.ordinal();
    @ConfigProp(info="Used to register buttons to animations")
    public static int button4 = EnumAnimation.HUG.ordinal();
    @ConfigProp(info="Used to register buttons to animations")
    public static int button5 = EnumAnimation.DANCING.ordinal();

    public static LegacyLoader Config;

    public void init(){
        MinecraftServer server = MinecraftServer.getServer();
        String dir = "";
        if (server != null) {
            dir = new File(".").getAbsolutePath();
        } else {
            dir = Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
        }

        Config = new LegacyLoader(this.getClass(), new File(dir, "config"), "MorePlayerModels");
        Config.loadConfig();
    }
}
