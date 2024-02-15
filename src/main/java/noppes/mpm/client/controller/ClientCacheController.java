package noppes.mpm.client.controller;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.util.ResourceLocation;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.client.ImageData;
import noppes.mpm.config.ConfigClient;
import noppes.mpm.util.CacheHashMap;

import java.util.HashMap;

public class ClientCacheController {
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
        ClientDataController.Instance();
        loaded = true;
    }

    public static void clearCache() {
        ClientDataController.ClearInstance();
        ClientCacheController.imageDataCache.clear();
        ClientCacheController.clientPerms.clear();
    }

    public static void clearSkinData() {
        ClientCacheController.imageDataCache.clear();
    }

    public static boolean hasPermission(MorePlayerModelsPermissions.Permission permission){
        if(!MorePlayerModels.HasServerSide)
            return true;

        if(clientPerms.containsKey(permission.name)){
            return clientPerms.get(permission.name);
        }

        return false;
    }

}
