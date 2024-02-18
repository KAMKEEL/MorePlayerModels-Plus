package noppes.mpm.controllers;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerSelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import noppes.mpm.LogWriter;
import noppes.mpm.ModelData;
import noppes.mpm.util.CacheHashMap;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ModelDataController {
	public static ModelDataController Instance;
	public HashMap<String, String> nameUUIDs;
	private final CacheHashMap<String, CacheHashMap.CachedObject<ModelData>> modelDataCache = new CacheHashMap<>(60 * 60 * 1000);
	public static Executor modelDataThread;

	public ModelDataController(){
		Instance = this;
		modelDataThread = Executors.newSingleThreadExecutor();
	}

	public static File getSaveDir(){
		MinecraftServer server = MinecraftServer.getServer();
		File saves = new File(".");
		if (server != null && !server.isDedicatedServer()) {
			saves = new File(Minecraft.getMinecraft().mcDataDir, "saves");
		}
		if (server != null) {
			File savedir = new File(new File(saves, server.getFolderName()), "moreplayermodels");
			if (!savedir.exists()) {
				savedir.mkdir();
			}
			return savedir;
		}
		return null;
	}

	public NBTTagCompound loadModelData(String player){
		File saveDir = getSaveDir();
		String filename = player;
		if(filename.isEmpty())
			filename = "noplayername";

		filename += ".dat";
		try {
			File file = new File(saveDir, filename);
			if(file.exists()){
				return loadNBTData(file);
			}
		} catch (Exception e) {
			LogWriter.error("Error loading: " + filename, e);
		}

		return null;
	}

	public NBTTagCompound loadNBTData(File file){
		try {
			return CompressedStreamTools.readCompressed(new FileInputStream(file));
		} catch (Exception e) {
			LogWriter.error("Error loading: " + file.getName(), e);
		}
		return new NBTTagCompound();
	}

	public void putModelDataCache(final String uuid, final ModelData playerCompound) {
		synchronized (modelDataCache) {
			modelDataCache.put(uuid, new CacheHashMap.CachedObject<>(playerCompound));
		}
	}

	public ModelData getModelDataCache(final String uuid) {
		synchronized (modelDataCache) {
			if (!modelDataCache.containsKey(uuid)) {
				return null;
			}
			return modelDataCache.get(uuid).getObject();
		}
	}

	public void clearCache() {
		synchronized (modelDataCache) {
			modelDataCache.clear();
		}
	}

	public ModelData getModelData(EntityPlayer player){
		ModelData data = getModelDataCache(player.getUniqueID().toString());
		if(data != null){
			data.player = player;
			return data;
		}

		data = (ModelData) player.getExtendedProperties("MPMData");
		if(data == null){
			player.registerExtendedProperties("MPMData", data = new ModelData(player));
			data.load();
		}
		data.player = player;
		return data;
	}

	public static EntityPlayer getPlayerFromUUID(UUID uuid) {
		MinecraftServer server = MinecraftServer.getServer();
		if (server != null) {
			for (Object playerObj : server.getConfigurationManager().playerEntityList) {
				if (playerObj instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) playerObj;
					if (player.getUniqueID().equals(uuid)) {
						return player;
					}
				}
			}
		}
		return null;
	}

	public String hasPlayer(String username) {
		for(String name : nameUUIDs.keySet()){
			if(name.equalsIgnoreCase(username))
				return name;
		}

		return "";
	}
}