package kamkeel;

import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.MorePlayerModels;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MorePlayerModelsPermissions {
	public static final Permission CONFIG_SKIN = new Permission("mpm.config.skin");
	public static final Permission CONFIG_SOUND = new Permission("mpm.config.sound");
	public static final Permission CONFIG_ENTITY = new Permission("mpm.config.sound");
	public static final Permission CONFIG_SCALE = new Permission("mpm.config.scale");
	public static final Permission CONFIG_HIDE = new Permission("mpm.config.hide");
	public static final Permission CONFIG_SAVE = new Permission("mpm.config.save");
	public static final Permission CONFIG_LOAD = new Permission("mpm.config.load");

	public static final Permission PARTS_BEARD = new Permission("mpm.parts.beard");
	public static final Permission PARTS_BREAST = new Permission("mpm.parts.breast");
	public static final Permission PARTS_CAPE = new Permission("mpm.parts.cape");
	public static final Permission PARTS_CLAWS = new Permission("mpm.parts.claws");
	public static final Permission PARTS_EARS = new Permission("mpm.parts.ears");
	public static final Permission PARTS_FIN = new Permission("mpm.parts.fin");
	public static final Permission PARTS_HAIR = new Permission("mpm.parts.hair");
	public static final Permission PARTS_HORNS = new Permission("mpm.parts.horns");
	public static final Permission PARTS_LEGS = new Permission("mpm.parts.legs");
	public static final Permission PARTS_MOHAWK = new Permission("mpm.parts.mohawk");
	public static final Permission PARTS_PARTICLES = new Permission("mpm.parts.particles");
	public static final Permission PARTS_SKIRT = new Permission("mpm.parts.skirt");
	public static final Permission PARTS_SNOUT = new Permission("mpm.parts.snout");
	public static final Permission PARTS_TAIL = new Permission("mpm.parts.tail");
	public static final Permission PARTS_WINGS = new Permission("mpm.parts.wings");
	
	public static MorePlayerModelsPermissions Instance;
	private Class<?> bukkit;
	private Method getPlayer;
	private Method hasPermission;
	
	public MorePlayerModelsPermissions(){
		Instance = this;
		try {
			bukkit = Class.forName("org.bukkit.Bukkit");
			getPlayer = bukkit.getMethod("getPlayer", String.class);
			hasPermission = Class.forName("org.bukkit.entity.Player").getMethod("hasPermission", String.class);
			LogManager.getLogger(MorePlayerModels.class).info("Bukkit permissions enabled");
			LogManager.getLogger(MorePlayerModels.class).info("Permissions available:");
			Collections.sort(Permission.permissions, String.CASE_INSENSITIVE_ORDER);
			for(String p : Permission.permissions){
				LogManager.getLogger(MorePlayerModels.class).info(p);
			}
		} catch (ClassNotFoundException e) {
			// bukkit/mcpc+ is not loaded
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasPermission(EntityPlayer player, Permission permission){
		if(player != null){
			if(permission != null){
				if(Instance.bukkit != null){
					return Instance.bukkitPermission(player.getCommandSenderName(), permission.name);
				}
			}
		}
		return true;
	}

	public static boolean hasCustomPermission(EntityPlayer player, String permission){
		if(Instance.bukkit != null){
			return Instance.bukkitPermission(player.getCommandSenderName(), permission);
		}
		return false;
	}

	private boolean bukkitPermission(String username, String permission) {
		try {
			Object player = getPlayer.invoke(null, username);
			return (Boolean) hasPermission.invoke(player, permission);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static class Permission{
		private static final List<String> permissions = new ArrayList<String>();
		public String name;
		public Permission(String name){
			this.name = name;
			if(!permissions.contains(name))
				permissions.add(name);
		}
	}

	public static boolean enabled() {
		return Instance.bukkit != null;
	}
}
