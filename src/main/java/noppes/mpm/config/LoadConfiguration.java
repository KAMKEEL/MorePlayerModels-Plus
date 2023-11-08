package noppes.mpm.config;

import java.io.File;

public class LoadConfiguration {
    public static File mainConfigFile;
    public static File clientConfigFile;

    public static void init(String configpath)
    {
        mainConfigFile = new File(configpath + "main.cfg");
        clientConfigFile = new File(configpath + "client.cfg");

        ConfigMain.init(mainConfigFile);
        ConfigClient.init(clientConfigFile);
    }
}
