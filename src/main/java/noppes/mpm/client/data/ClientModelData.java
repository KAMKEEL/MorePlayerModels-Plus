package noppes.mpm.client.data;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.ModelData;

import java.util.HashMap;
import java.util.UUID;

public class ClientModelData {
    private static ClientModelData Instance;
    private final HashMap<UUID, ModelData> playerData = new HashMap<>();

    private ClientModelData() {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        this.playerData.put(player.getUniqueID(), new ModelData(player));
    }

    public static ClientModelData Instance() {
        if (Instance == null) {
            Instance = new ClientModelData();
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
