package kamkeel;

import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.config.ConfigMain;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MorePlayerModelsPermissions {
	public static final Permission CONFIG_SKIN = new Permission("mpm.config.skin");
	public static final Permission CONFIG_SOUND = new Permission("mpm.config.sound");
	public static final Permission CONFIG_ENTITY = new Permission("mpm.config.entity");
	public static final Permission CONFIG_SCALE = new Permission("mpm.config.scale");
	public static final Permission CONFIG_HIDE = new Permission("mpm.config.hide");
	public static final Permission CONFIG_PRESET = new Permission("mpm.config.preset");

	public static final Permission PARTS = new Permission("mpm.parts");
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

	public static final Permission EMOTE = new Permission("mpm.emote");
	public static final Permission ANGRY = new Permission("mpm.emote.angry");
	public static final Permission BOW = new Permission("mpm.emote.bow");
	public static final Permission CRAWL = new Permission("mpm.emote.crawl");
	public static final Permission DANCE = new Permission("mpm.emote.dance");
	public static final Permission DEATH = new Permission("mpm.emote.death");
	public static final Permission HUG = new Permission("mpm.emote.hug");
	public static final Permission LOVE = new Permission("mpm.emote.love");
	public static final Permission NO = new Permission("mpm.emote.no");
	public static final Permission YES = new Permission("mpm.emote.yes");
	public static final Permission POINT = new Permission("mpm.emote.point");
	public static final Permission SING = new Permission("mpm.emote.sing");
	public static final Permission SIT = new Permission("mpm.emote.sit");
	public static final Permission SLEEP = new Permission("mpm.emote.sleep");
	public static final Permission WAG = new Permission("mpm.emote.wag");
	public static final Permission WAVE = new Permission("mpm.emote.wave");

	public static final Permission SETCLOAK = new Permission("mpm.admin.cloak");
	public static final Permission SETMODEL = new Permission("mpm.admin.model");
	public static final Permission SETNAME = new Permission("mpm.admin.name");
	public static final Permission SETURL = new Permission("mpm.admin.url");
	public static final Permission SIZE = new Permission("mpm.admin.size");
	public static final Permission SCALE = new Permission("mpm.admin.scale");
	public static final Permission RELOAD = new Permission("mpm.admin.reload");
	
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
		if(!ConfigMain.EnablePermissions){
			return true;
		}
		if(player != null){
			if(permission != null){
				if(Instance.bukkit != null){
					return Instance.bukkitPermission(player.getCommandSenderName(), permission.name);
				}
			}
		}
		return true;
	}

	public static boolean hasPermission(EntityPlayer player, String permission){
		if(!ConfigMain.EnablePermissions){
			return true;
		}
		if(player != null){
			if(permission != null){
				if(Instance.bukkit != null){
					return Instance.bukkitPermission(player.getCommandSenderName(), permission);
				}
			}
		}
		return true;
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
		public static final List<Permission> permissionNode = new ArrayList<Permission>();
		public static final List<String> permissions = new ArrayList<String>();
		public String name;
		public Permission(String name){
			this.name = name;
			if(!permissions.contains(name)){
				permissions.add(name);
				permissionNode.add(this);
			}
		}
	}

	public static boolean enabled() {
		return Instance.bukkit != null;
	}
}
