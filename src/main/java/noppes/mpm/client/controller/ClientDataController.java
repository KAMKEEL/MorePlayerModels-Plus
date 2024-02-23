package noppes.mpm.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.ModelData;

import java.util.HashMap;
import java.util.UUID;

public class ClientDataController {
    private static ClientDataController Instance;
    private final HashMap<UUID, ModelData> playerData;

    private ClientDataController() {
        playerData = new HashMap<>();
    }

    public static ClientDataController Instance() {
        if (Instance == null) {
            Instance = new ClientDataController();
        }
        return Instance;
    }

    public static void ClearInstance() {
        Instance = null;
    }

    public ModelData getPlayerData(EntityPlayer entity) {
        if (!this.playerData.containsKey(entity.getUniqueID())) {
            this.playerData.put(entity.getUniqueID(), new ModelData(entity));
        }
        return this.playerData.get(entity.getUniqueID());
    }
}
