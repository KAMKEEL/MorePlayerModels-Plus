package kamkeel;

import net.minecraft.entity.player.EntityPlayer;
import noppes.mpm.MorePlayerModels;
import noppes.mpm.config.ConfigMain;
import noppes.mpm.config.ConfigPerm;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MorePlayerModelsPermissions {
	// Config Permissions
	public static final Permission CONFIG_SKIN = new Permission("mpm.config.skin", ConfigPerm.CONFIG_SKIN);
	public static final Permission CONFIG_SOUND = new Permission("mpm.config.sound", ConfigPerm.CONFIG_SOUND);
	public static final Permission CONFIG_ENTITY = new Permission("mpm.config.entity", ConfigPerm.CONFIG_ENTITY);
	public static final Permission CONFIG_SCALE = new Permission("mpm.config.scale", ConfigPerm.CONFIG_SCALE);
	public static final Permission CONFIG_HIDE = new Permission("mpm.config.hide", ConfigPerm.CONFIG_HIDE);
	public static final Permission CONFIG_PRESET = new Permission("mpm.config.preset", ConfigPerm.CONFIG_PRESET);

	// Part Permissions
	public static final Permission PARTS = new Permission("mpm.parts", ConfigPerm.PARTS);
	public static final Permission PARTS_BEARD = new Permission("mpm.parts.beard", ConfigPerm.PARTS_BEARD);
	public static final Permission PARTS_BREAST = new Permission("mpm.parts.breast", ConfigPerm.PARTS_BREAST);
	public static final Permission PARTS_CAPE = new Permission("mpm.parts.cape", ConfigPerm.PARTS_CAPE);
	public static final Permission PARTS_CLAWS = new Permission("mpm.parts.claws", ConfigPerm.PARTS_CLAWS);
	public static final Permission PARTS_EARS = new Permission("mpm.parts.ears", ConfigPerm.PARTS_EARS);
	public static final Permission PARTS_FIN = new Permission("mpm.parts.fin", ConfigPerm.PARTS_FIN);
	public static final Permission PARTS_HAIR = new Permission("mpm.parts.hair", ConfigPerm.PARTS_HAIR);
	public static final Permission PARTS_HORNS = new Permission("mpm.parts.horns", ConfigPerm.PARTS_HORNS);
	public static final Permission PARTS_LEGS = new Permission("mpm.parts.legs", ConfigPerm.PARTS_LEGS);
	public static final Permission PARTS_MOHAWK = new Permission("mpm.parts.mohawk", ConfigPerm.PARTS_MOHAWK);
	public static final Permission PARTS_PARTICLES = new Permission("mpm.parts.particles", ConfigPerm.PARTS_PARTICLES);
	public static final Permission PARTS_SKIRT = new Permission("mpm.parts.skirt", ConfigPerm.PARTS_SKIRT);
	public static final Permission PARTS_SNOUT = new Permission("mpm.parts.snout", ConfigPerm.PARTS_SNOUT);
	public static final Permission PARTS_TAIL = new Permission("mpm.parts.tail", ConfigPerm.PARTS_TAIL);
	public static final Permission PARTS_WINGS = new Permission("mpm.parts.wings", ConfigPerm.PARTS_WINGS);

	// Emote Permissions
	public static final Permission EMOTE = new Permission("mpm.emote", ConfigPerm.EMOTE);
	public static final Permission ANGRY = new Permission("mpm.emote.angry", ConfigPerm.ANGRY);
	public static final Permission BOW = new Permission("mpm.emote.bow", ConfigPerm.BOW);
	public static final Permission CRAWL = new Permission("mpm.emote.crawl", ConfigPerm.CRAWL);
	public static final Permission DANCE = new Permission("mpm.emote.dance", ConfigPerm.DANCE);
	public static final Permission DEATH = new Permission("mpm.emote.death", ConfigPerm.DEATH);
	public static final Permission HUG = new Permission("mpm.emote.hug", ConfigPerm.HUG);
	public static final Permission LOVE = new Permission("mpm.emote.love", ConfigPerm.LOVE);
	public static final Permission NO = new Permission("mpm.emote.no", ConfigPerm.NO);
	public static final Permission YES = new Permission("mpm.emote.yes", ConfigPerm.YES);
	public static final Permission POINT = new Permission("mpm.emote.point", ConfigPerm.POINT);
	public static final Permission SING = new Permission("mpm.emote.sing", ConfigPerm.SING);
	public static final Permission SIT = new Permission("mpm.emote.sit", ConfigPerm.SIT);
	public static final Permission SLEEP = new Permission("mpm.emote.sleep", ConfigPerm.SLEEP);
	public static final Permission WAG = new Permission("mpm.emote.wag", ConfigPerm.WAG);
	public static final Permission WAVE = new Permission("mpm.emote.wave", ConfigPerm.WAVE);

	// Admin Permissions
	public static final Permission SETCLOAK = new Permission("mpm.admin.cloak", ConfigPerm.SETCLOAK);
	public static final Permission SETMODEL = new Permission("mpm.admin.model", ConfigPerm.SETMODEL);
	public static final Permission SETNAME = new Permission("mpm.admin.name", ConfigPerm.SETNAME);
	public static final Permission SETURL = new Permission("mpm.admin.url", ConfigPerm.SETURL);
	public static final Permission SIZE = new Permission("mpm.admin.size", ConfigPerm.SIZE);
	public static final Permission SCALE = new Permission("mpm.admin.scale", ConfigPerm.SCALE);
	public static final Permission RELOAD = new Permission("mpm.admin.reload", ConfigPerm.RELOAD);
	
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
				if(permission.isAllowed()){
					return true;
				}
				if(Instance.bukkit != null){
					return Instance.bukkitPermission(player.getCommandSenderName(), permission.name);
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
		public boolean allowed;
		public String name;
		public Permission(String name, boolean defaultBool){
			this.name = name;
			this.allowed = defaultBool;
			if(!permissions.contains(name)){
				permissions.add(name);
				permissionNode.add(this);
			}
		}

		public boolean isAllowed(){ return allowed; }
	}

	public static boolean enabled() {
		return Instance.bukkit != null;
	}
}
