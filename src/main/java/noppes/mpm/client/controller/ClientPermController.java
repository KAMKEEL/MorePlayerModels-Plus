package noppes.mpm.client.controller;

import kamkeel.MorePlayerModelsPermissions;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import noppes.mpm.MorePlayerModels;

import java.util.HashMap;

public class ClientPermController {

    public static HashMap<String, Boolean> clientPerms = new HashMap<String, Boolean>();

    public static boolean hasPermission(MorePlayerModelsPermissions.Permission permission){
        if(!MorePlayerModels.HasServerSide)
            return true;

        if(clientPerms.containsKey(permission.name)){
            return clientPerms.get(permission.name);
        }

        return false;
    }

    public static void readNBT(NBTTagCompound compound){
        clientPerms.clear();
        NBTTagList list = compound.getTagList("PermissionMap", 10);
        if(list != null){
            for(int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
                String permissionName = nbttagcompound.getString("Name");
                boolean bool = nbttagcompound.getBoolean("Bool");
                clientPerms.put(permissionName, bool);
            }
        }
    }
}
