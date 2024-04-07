package noppes.mpm.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.common.MinecraftForge;
import noppes.mpm.MorePlayerModels;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateCheck implements Runnable {

    /** Should we check for updates */
	public static boolean checkForUpdates = true;
	
	/** The url to use for update checking */
	private static final String UPDATE_URL = "https://raw.githubusercontent.com/KAMKEEL/MorePlayerModels-Plus/main/update.json";
	
	/** Was an update found. */
	public static boolean updateFound = false;
	
	public static String remoteModVersion;
	
	public static void checkForUpdates() {
		if (!checkForUpdates){
		    return;
		}
		
		(new Thread(new UpdateCheck(), "MorePlayerModels+ update thread.")).start();
	}

	@Override
	public void run() {
		String localVersion = MorePlayerModels.VERSION;
		
		//localVersion = "1.7.10-0.39.9";
		if(localVersion.equals("@VERSION@")) {
		    return;
		}
		
		try {
		    if (localVersion.contains("-")) {
		        String[] lvSplit = localVersion.split("-");
		        localVersion = lvSplit[1];
		    }
		    
			String location = UPDATE_URL;
			HttpURLConnection conn = null;
			while (location != null && !location.isEmpty()) {
				URL url = new URL(location);

				if (conn != null)
					conn.disconnect();

				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; ru; rv:1.9.0.11) Gecko/2009060215 Firefox/3.0.11 (.NET CLR 3.5.30729)");
				conn.setRequestProperty("Referer", "http://" + MorePlayerModels.VERSION);
				conn.connect();
				location = conn.getHeaderField("Location");
			}

			if (conn == null)
				throw new NullPointerException();

			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String data = "";
			
			String line = "";
			while ((line = reader.readLine()) != null) {
			    data += line;
			}
	        conn.disconnect();
	        reader.close();
	        
			JsonObject json = (JsonObject) new JsonParser().parse(data);
			
			remoteModVersion = json.getAsJsonObject("promos").get(MinecraftForge.MC_VERSION + "-latest").getAsString();

			updateFound = versionCompare(localVersion, remoteModVersion) < 0;
			
		} catch (Exception e) {
			updateFound = false;
		}
	}
	
	private int versionCompare(String str1, String str2)
	{
	    String[] vals1 = str1.split("\\.");
	    String[] vals2 = str2.split("\\.");
	    int i = 0;
	    // set index to first non-equal ordinal or length of shortest version string
	    while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) 
	    {
	      i++;
	    }
	    // compare first non-equal ordinal number
	    if (i < vals1.length && i < vals2.length) 
	    {
	        int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
	        return Integer.signum(diff);
	    }
	    // the strings are equal or one string is a substring of the other
	    // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
	    else
	    {
	        return Integer.signum(vals1.length - vals2.length);
	    }
	}
	
}
