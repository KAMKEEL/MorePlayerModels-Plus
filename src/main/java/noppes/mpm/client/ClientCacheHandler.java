package noppes.mpm.client;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.util.ResourceLocation;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.util.CacheHashMap;

import java.io.File;
import java.util.HashMap;

public class ClientCacheHandler {
    private static final CacheHashMap<String, CacheHashMap.CachedObject<ImageData>> imageDataCache = new CacheHashMap<>((long) ConfigClient.CacheLife * 60 * 1000);
    public static HashMap<String, Boolean> clientPerms = new HashMap<String, Boolean>();
    public static boolean loaded = false;

    public static ImageData getPlayerSkin(String directory, boolean x64, ResourceLocation resource) {
        synchronized (imageDataCache) {
            if (!imageDataCache.containsKey(resource.getResourcePath())) {
                imageDataCache.put(resource.getResourcePath(), new CacheHashMap.CachedObject<>(new ImageData(directory, x64, resource)));
            }
            return imageDataCache.get(resource.getResourcePath()).getObject();
        }
    }

    public static ImageData getCapeTexture(String directory) {
        synchronized (imageDataCache) {
            if (!imageDataCache.containsKey(directory)) {
                imageDataCache.put(directory, new CacheHashMap.CachedObject<>(new ImageData(directory)));
            }
            return imageDataCache.get(directory).getObject();
        }
    }

    public static void createCache() {
        ClientModelData.Instance();
        loaded = true;
    }

    public static void clearCache() {
        ClientModelData.ClearInstance();
        ClientCacheHandler.imageDataCache.clear();
        ClientCacheHandler.clientPerms.clear();
    }

    public static void clearSkinData() {
        ClientCacheHandler.imageDataCache.clear();
    }

    public static boolean hasPermission(MorePlayerModelsPermissions.Permission permission){
        return true;
//        if(clientPerms.containsKey(permission.name)){
//            return clientPerms.get(permission.name);
//        }
//        return false;
    }

}
