package noppes.mpm.config;

import cpw.mods.fml.common.FMLLog;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.config.legacy.LegacyConfig;
import org.apache.logging.log4j.Level;

import java.io.File;

public class ConfigMain
{
    public static Configuration config;

    public final static String GENERAL = "General";

    public static Property EnableUpdateCheckerProperty;
    public static boolean EnableUpdateChecker = true;

    public static Property EnablePermissionsProperty;
    public static boolean EnablePermissions = false;

    /**
     *  General Main Properties
     **/

    public static void init(File configFile)
    {
        config = new Configuration(configFile);

        try
        {
            config.load();

            // General
            EnableUpdateCheckerProperty = config.get(GENERAL, "Enables Update Checker", true);
            EnableUpdateChecker = EnableUpdateCheckerProperty.getBoolean(true);

            EnablePermissionsProperty = config.get(GENERAL, "Enable Permissions", false);
            EnablePermissions = EnablePermissionsProperty.getBoolean(false);

            // Convert to Legacy
            if(MorePlayerModels.legacyExist){
                EnableUpdateChecker = LegacyConfig.EnableUpdateChecker;
                EnableUpdateCheckerProperty.set(EnableUpdateChecker);
            }

        }
        catch (Exception e)
        {
            FMLLog.log(Level.ERROR, e, "CNPC+ has had a problem loading its main configuration");
        }
        finally
        {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }
}