package noppes.mpm.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.IChatComponent;
import noppes.mpm.config.ConfigClient;
import org.lwjgl.opengl.GL11;

import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessages {
	private static Map<String,ChatMessages> users = new Hashtable<String,ChatMessages>();

	private Map<Long,TextBlockClient> messages = new TreeMap<Long,TextBlockClient>();

	private int boxLength = 46;
	private float scale = 0.5f;
	
	private String lastMessage = "";
	private long lastMessageTime = 0;
	
	public void addMessage(String message){
		if(!ConfigClient.EnableChatBubbles)
			return;
		long time = System.currentTimeMillis();
		if(message.equals(lastMessage) && lastMessageTime + 1000 > time){
			return;
		}
		Map<Long,TextBlockClient> messages = new TreeMap<Long,TextBlockClient>(this.messages);
		messages.put(time, new TextBlockClient(message, (int) (boxLength * 4)));

		if(messages.size() > 3){
			messages.remove(messages.keySet().iterator().next());
		}
		this.messages = messages;
		lastMessage = message;
		lastMessageTime = time;
	}    
	
	public void renderMessages(double par3, double par5, double par7)
    {
		Map<Long,TextBlockClient> messages = getMessages();
		if(messages.isEmpty())
			return;
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
        float var13 = 1.6F;
        float var14 = 0.016666668F * var13;
        GL11.glPushMatrix();
        int size = 0;
        for(TextBlockClient block : messages.values())
        	size += block.lines.size();
        
        int textYSize = (int) (size * font.FONT_HEIGHT * scale);
        GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + textYSize * var14, (float)par7);
        //GL11.glScalef(textscale, textscale, textscale);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1F, 0.0F);
        GL11.glRotatef(RenderManager.instance.playerViewX, 1F, 0.0F, 0.0F);
        GL11.glScalef(-var14, -var14, var14);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        drawRect(-boxLength - 2, -2, boxLength + 2, textYSize + 1, 0xBBFFFFFF, 0.11);
        
        drawRect(-boxLength - 1, -3, boxLength + 1, -2, 0xFF000000, 0.1); //top
        drawRect(-boxLength - 1, textYSize + 2, -1, textYSize + 1, 0xFF000000, 0.1); //bottom1
        drawRect(3, textYSize + 2, boxLength + 1, textYSize + 1, 0xFF000000, 0.1); //bottom2
        drawRect(-boxLength - 3, -1, -boxLength - 2, textYSize, 0xFF000000, 0.1); //left
        drawRect(boxLength + 3, -1, boxLength + 2, textYSize, 0xFF000000, 0.1); //right

        drawRect(-boxLength - 2, -2, -boxLength - 1, -1, 0xFF000000, 0.1);
        drawRect(boxLength + 2, -2, boxLength + 1, -1, 0xFF000000, 0.1);
        drawRect(-boxLength - 2, textYSize + 1, -boxLength - 1, textYSize, 0xFF000000, 0.1);
        drawRect(boxLength + 2, textYSize + 1, boxLength + 1, textYSize, 0xFF000000, 0.1);

        drawRect(0, textYSize + 1, 3, textYSize + 4, 0xBBFFFFFF, 0.11);
        drawRect(-1, textYSize + 4, 1, textYSize + 5, 0xBBFFFFFF, 0.11);
        
        drawRect(-1, textYSize + 1, 0, textYSize + 4, 0xFF000000, 0.1);
        drawRect(3, textYSize + 1, 4, textYSize + 3, 0xFF000000, 0.1);
        drawRect(2, textYSize + 3, 3, textYSize + 4, 0xFF000000, 0.1);
        drawRect(1, textYSize + 4, 2, textYSize + 5, 0xFF000000, 0.1);
        drawRect(-2, textYSize + 4, -1, textYSize + 5, 0xFF000000, 0.1);
        
        drawRect(-2, textYSize + 5, 1, textYSize + 6, 0xFF000000, 0.1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        
        GL11.glScalef(scale, scale, scale);
        int index = 0;
        for(TextBlockClient block : messages.values()){
        	for(IChatComponent chat : block.lines){
	        	String message = chat.getFormattedText();
	        	font.drawString(message, -font.getStringWidth(message) / 2, index * font.FONT_HEIGHT, 0);
	        	index++;
        	}
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        RenderHelper.enableStandardItemLighting();
        
    }    
	
	public static void drawRect(int par0, int par1, int par2, int par3, int par4, double par5)
    {
        int j1;

        if (par0 < par2)
        {
            j1 = par0;
            par0 = par2;
            par2 = j1;
        }

        if (par1 < par3)
        {
            j1 = par1;
            par1 = par3;
            par3 = j1;
        }

        float f = (float)(par4 >> 24 & 255) / 255.0F;
        float f1 = (float)(par4 >> 16 & 255) / 255.0F;
        float f2 = (float)(par4 >> 8 & 255) / 255.0F;
        float f3 = (float)(par4 & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)par0, (double)par3, par5);
        tessellator.addVertex((double)par2, (double)par3, par5);
        tessellator.addVertex((double)par2, (double)par1, par5);
        tessellator.addVertex((double)par0, (double)par1, par5);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
	
	public static ChatMessages getChatMessages(String username){
		if(users.containsKey(username))
			return users.get(username);
		
		ChatMessages chat = new ChatMessages();
		users.put(username, chat);
		return chat;
	}
	
	private static Pattern[] patterns = new Pattern[]{
		Pattern.compile("^<+([a-zA-z0-9_]{2,16})>[:]? (.*)"), 
		Pattern.compile("^\\[.*[\\]]{1,16}[^a-zA-z0-9]?([a-zA-z0-9_]{2,16})[:]? (.*)"),
		Pattern.compile("^[a-zA-z0-9_]{2,10}[^a-zA-z0-9]([a-zA-z0-9_]{2,16})[:]? (.*)"),
	};
	
	public static void parseMessage(String toParse){
		toParse = toParse.replaceAll("\247.", "");
		for(Pattern pattern : patterns){
			Matcher m = pattern.matcher(toParse);
			if(m.find()){
				String username = m.group(1);
				if(!validPlayer(username))
					continue;
				String message = m.group(2);
				getChatMessages(username).addMessage(message);
				return;
			}
		}
	}
	
	public static void test() {
		test("<Sirnoppes01> :)","Sirnoppes01: :)");
		test("<Sirnoppes01> hey","Sirnoppes01: hey");
		test("<Sir_noppes> hey","Sir_noppes: hey");
		test("<Sirnoppes>: hey","Sirnoppes: hey");
		test("[member]Sirnoppes: hey","Sirnoppes: hey");
		test("[member]Sirnoppes01: hey","Sirnoppes01: hey");
		test("[member]Sir_noppes: hey","Sir_noppes: hey");
		test("[member] Sirnoppes: hey","Sirnoppes: hey");
		test("[g][member]Sirnoppes: hey","Sirnoppes: hey");
		test("[g] [member]Sirnoppes: hey","Sirnoppes: hey");
		test("[g] [member]-Sirnoppes: hey","Sirnoppes: hey");
		test("[Player755: Teleported Player755 to Player885]","");
		test("member Sirnoppes: hey","Sirnoppes: hey");
		test("member-Sirnoppes: hey","Sirnoppes: hey");
		test("member: Sirnoppes: hey","");
	}
	private static void test(String toParse, String result) {
		for(Pattern pattern : patterns){
			Matcher m = pattern.matcher(toParse);
			if(m.find()){
				String username = m.group(1);
				String message = m.group(2);
				if(message == null || username == null)
					continue;
				else if(result.isEmpty()){
					System.err.println("failed: " + toParse + " - " + username + ": " + message);
					return;
				}
				if((username +": " + message).equals(result)){
					System.out.println("success: " + toParse);
					return;
				}
			}
		}
		if(result.isEmpty())
			System.out.println("success: " + toParse);
		else
			System.err.println("failed: " + toParse);
	}

	private static boolean validPlayer(String username){
		return Minecraft.getMinecraft().theWorld.getPlayerEntityByName(username) != null;
	}

	private Map<Long,TextBlockClient> getMessages(){
		Map<Long,TextBlockClient> messages = new TreeMap<Long,TextBlockClient>();
		long time = System.currentTimeMillis();
		for(long timestamp : this.messages.keySet()){
			if(time > timestamp + 10000)
				continue;
			TextBlockClient message = this.messages.get(timestamp);
			messages.put(timestamp, message);
		}
		this.messages = messages;
		return messages;
	}
	
}
