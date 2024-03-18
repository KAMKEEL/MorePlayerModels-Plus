package noppes.mpm.controllers.data;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

public class PermissionData {

    public static long reloadedTime = System.currentTimeMillis();
    private final EntityPlayer player;
    public HashMap<MorePlayerModelsPermissions.Permission, Boolean> permissionMap = new HashMap<MorePlayerModelsPermissions.Permission, Boolean>();
    public long lastUpdated = System.currentTimeMillis();;

    public PermissionData(EntityPlayer pl){
        this.player = pl;
    }

    public void updatePermissions(){
        for(MorePlayerModelsPermissions.Permission perm : MorePlayerModelsPermissions.Permission.permissionNode){
           permissionMap.put(perm,  MorePlayerModelsPermissions.hasPermission(player, perm));
        }
        lastUpdated = System.currentTimeMillis();
    }

}
