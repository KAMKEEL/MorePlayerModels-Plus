package noppes.mpm.controllers;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.mpm.controllers.data.PermissionData;

import java.util.HashMap;

public class PermissionController {

    public static PermissionController Instance;
    public HashMap<String, Long> lastRequest = new HashMap<>();
    private HashMap<String, PermissionData> permissionData = new HashMap<>();

    public PermissionController(){
        Instance = this;
    }

    public void reloadPermissionData(){
        permissionData.clear();
        lastRequest.clear();
    }

    public static HashMap<String, Boolean> readNBT(NBTTagCompound compound){
        HashMap<String, Boolean> permissionMap = new HashMap<String, Boolean>();
        NBTTagList list = compound.getTagList("PermissionMap", 10);
        if(list != null){
            for(int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
                String permissionName = nbttagcompound.getString("Name");
                Boolean bool = nbttagcompound.getBoolean("Bool");
                permissionMap.put(permissionName, bool);
            }
        }
        return permissionMap;
    }

    public NBTTagCompound writeNBT(EntityPlayer player){
        String uuid = player.getUniqueID().toString();
        if(!permissionData.containsKey(uuid)){
            PermissionData playerPerms = new PermissionData(player);
            playerPerms.updatePermissions();
            permissionData.put(uuid, playerPerms);
        }

        NBTTagCompound nbt = new NBTTagCompound();
        PermissionData permData = permissionData.get(uuid);
        NBTTagList permList = new NBTTagList();
        for(MorePlayerModelsPermissions.Permission perm : permData.permissionMap.keySet()){
            NBTTagCompound permCompound = new NBTTagCompound();
            permCompound.setString("Name", perm.name);
            permCompound.setBoolean("Bool", permData.permissionMap.get(perm));
            permList.appendTag(permCompound);
        }
        nbt.setTag("PermissionMap", permList);

        return nbt;
    }
}
