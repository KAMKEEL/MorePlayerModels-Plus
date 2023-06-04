package noppes.mpm.config;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.config.legacy.ConfigProp;
import noppes.mpm.config.legacy.LegacyConfig;
import noppes.mpm.constants.EnumAnimation;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigClient
{
    public static Configuration config;

    public final static String CLIENT = "Client";

    public static Property CacheLifeProperty;
    public static int CacheLife = 30;

    public static Property TooltipsProperty;
    public static int Tooltips = 2;

    public static Property EnablePOVProperty;
    public static boolean EnablePOV = true;

    public static Property EnableBackItemProperty;
    public static boolean EnableBackItem = true;

    public static Property EnableChatBubblesProperty;
    public static boolean EnableChatBubbles = false;

    public static Property EnableParticlesProperty;
    public static boolean EnableParticles = true;

    public static Property HidePlayerNamesProperty;
    public static boolean HidePlayerNames = false;

    public static Property HideSelectionBoxProperty;
    public static boolean HideSelectionBox = false;

    public static Property button1Property;
    public static int button1 = EnumAnimation.SLEEPING_SOUTH.ordinal();

    public static Property button2Property;
    public static int button2 = EnumAnimation.SITTING.ordinal();

    public static Property button3Property;
    public static int button3 = EnumAnimation.CRAWLING.ordinal();

    public static Property button4Property;
    public static int button4 = EnumAnimation.HUG.ordinal();

    public static Property button5Property;
    public static int button5 = EnumAnimation.DANCING.ordinal();


    public static void init(File configFile)
    {
        config = new Configuration(configFile);

        try
        {
            config.load();

            // Client
            CacheLifeProperty = config.get(CLIENT, "Cache Life", 30, "How long should downloaded skin data be saved client side? (In minutes)");
            CacheLife = CacheLifeProperty.getInt(30);

            TooltipsProperty = config.get(CLIENT, "Tooltips", 2, "Used to pick positioning of tooltip");
            Tooltips = TooltipsProperty.getInt(2);

            EnablePOVProperty = config.get(CLIENT, "Enable POV", true, "Enable different perspective heights for different model sizes");
            EnablePOV = EnablePOVProperty.getBoolean(true);

            EnableChatBubblesProperty = config.get(CLIENT, "Enable Chat Bubbles", false, "Enable/Disable Chat Bubbles");
            EnableChatBubbles = EnableChatBubblesProperty.getBoolean(false);

            EnableBackItemProperty = config.get(CLIENT, "Enable Back Items", true, "Enables the item on your back");
            EnableBackItem = EnableBackItemProperty.getBoolean(true);

            EnableParticlesProperty = config.get(CLIENT, "Enable Particles", true, "Set to false if you do not want to see player particles");
            EnableParticles = EnableParticlesProperty.getBoolean(true);

            HidePlayerNamesProperty = config.get(CLIENT, "Hide Player Names", false, "Set to true if you do not want to see hide player names");
            HidePlayerNames = HidePlayerNamesProperty.getBoolean(false);

            HideSelectionBoxProperty = config.get(CLIENT, "Hide Selection Box", false, "Set to true if you do not want to see hide selection boxes when pointing to blocks");
            HideSelectionBox = HideSelectionBoxProperty.getBoolean(false);

            button1Property = config.get(CLIENT, "Button 1 Action", EnumAnimation.SLEEPING_SOUTH.ordinal(), "Used to register buttons to animations");
            button1 = button1Property.getInt(EnumAnimation.SLEEPING_SOUTH.ordinal());

            button2Property = config.get(CLIENT, "Button 2 Action", EnumAnimation.SITTING.ordinal(), "Used to register buttons to animations");
            button2 = button2Property.getInt(EnumAnimation.SITTING.ordinal());

            button3Property = config.get(CLIENT, "Button 3 Action", EnumAnimation.CRAWLING.ordinal(), "Used to register buttons to animations");
            button3 = button3Property.getInt(EnumAnimation.CRAWLING.ordinal());

            button4Property = config.get(CLIENT, "Button 4 Action", EnumAnimation.HUG.ordinal(), "Used to register buttons to animations");
            button4 = button4Property.getInt(EnumAnimation.HUG.ordinal());

            button5Property = config.get(CLIENT, "Button 5 Action", EnumAnimation.DANCING.ordinal(), "Used to register buttons to animations");
            button5 = button5Property.getInt(EnumAnimation.DANCING.ordinal());

            // Convert to Legacy
            if(MorePlayerModels.legacyExist){
                Tooltips = LegacyConfig.Tooltips;
                TooltipsProperty.set(Tooltips);

                EnablePOV = LegacyConfig.EnablePOV;
                EnablePOVProperty.set(EnablePOV);

                EnableBackItem = LegacyConfig.EnableBackItem;
                EnableBackItemProperty.set(EnableBackItem);

                EnableChatBubbles = LegacyConfig.EnableChatBubbles;
                EnableChatBubblesProperty.set(EnableChatBubbles);

                EnableParticles = LegacyConfig.EnableParticles;
                EnableParticlesProperty.set(EnableParticles);

                HidePlayerNames = LegacyConfig.HidePlayerNames;
                HidePlayerNamesProperty.set(HidePlayerNames);

                HideSelectionBox = LegacyConfig.HideSelectionBox;
                HideSelectionBoxProperty.set(HideSelectionBox);

                button1 = LegacyConfig.button1;
                button1Property.set(button1);

                button2 = LegacyConfig.button2;
                button2Property.set(button2);

                button3 = LegacyConfig.button3;
                button3Property.set(button3);

                button4 = LegacyConfig.button4;
                button4Property.set(button4);

                button5 = LegacyConfig.button5;
                button5Property.set(button5);
            }

        }
        catch (Exception e)
        {
            FMLLog.log(Level.ERROR, e, "MPM+ has had a problem loading its client configuration");
        }
        finally
        {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}