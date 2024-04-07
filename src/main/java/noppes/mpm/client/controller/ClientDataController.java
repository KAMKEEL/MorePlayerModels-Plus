package noppes.mpm.client.controller;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.ModelData;

import java.util.HashMap;

public class ClientDataController {
    private static ClientDataController Instance;
    private final HashMap<String , ModelData> playerData = new HashMap<>();

    private ClientDataController() {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        this.playerData.put(player.getCommandSenderName(), new ModelData(player));
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

    public ModelData getPlayerData(EntityPlayer entity){
        if (!this.playerData.containsKey(entity.getCommandSenderName())) {
            this.playerData.put(entity.getCommandSenderName(), new ModelData(entity));
        }
        return this.playerData.get(entity.getCommandSenderName());
    }
}
