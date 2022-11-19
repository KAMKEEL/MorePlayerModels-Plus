package noppes.mpm.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.util.ChatComponentTranslation;

public class VersionChecker extends Thread{
	private int revision = 7;
	public VersionChecker(){
		
	}
	public void run(){
		String name = '\u00A7'+ "2MorePlayerModels+" + '\u00A7' + "f";
		String link = '\u00A7'+"9"+'\u00A7' + "nClick here"; 
		String text =  name +" installed. More info at " + link;
		if(hasUpdate())
			text = name+'\u00A7'+"4 update available " + link;
		
        EntityPlayer player;
		try{
			player = Minecraft.getMinecraft().thePlayer;
		}
		catch(NoSuchMethodError e){
			return;
		}
        while((player = Minecraft.getMinecraft().thePlayer) == null){
        	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        ChatComponentTranslation message = new ChatComponentTranslation(text);
        message.getChatStyle().setChatClickEvent(new ClickEvent(Action.OPEN_URL, "https://www.curseforge.com/minecraft/mc-mods/moreplayermodels-plus"));
        player.addChatMessage(message);
	}
	private boolean hasUpdate(){

		try{
			URL url = new URL("https://dl.dropboxusercontent.com/u/3096920/update/minecraft/1.7/MorePlayerModels.txt");
	        URLConnection yc = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
	        String inputLine = in.readLine();
	        if(inputLine == null)
	        	return false;
	        int newVersion = Integer.parseInt(inputLine);
	        return revision < newVersion;
		}
		catch(Exception e){
		}
		return false;
	}
}
