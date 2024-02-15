package noppes.mpm.client.controller;

import kamkeel.MorePlayerModelsPermissions;
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
}
