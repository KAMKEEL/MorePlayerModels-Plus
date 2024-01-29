package noppes.mpm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {
	public static byte[] compress(String s) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(s.length());
		GZIPOutputStream gos = new GZIPOutputStream(os);
		gos.write(s.getBytes());
		gos.close();
		byte[] compressed = os.toByteArray();
		os.close();
		return compressed;
	}
	
	public static String compressToString(String s) throws IOException{
        byte[] compressed = compress(s);
        return Base64.getEncoder().encodeToString(compressed);
	}

	public static String decompress(byte[] compressed) throws IOException {
		final int BUFFER_SIZE = 64;
		ByteArrayInputStream is = new ByteArrayInputStream(compressed);
		GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] data = new byte[BUFFER_SIZE];
		int bytesRead;
		while ((bytesRead = gis.read(data)) != -1) {
			baos.write(data, 0, bytesRead);
		}
		gis.close();
		return baos.toString("UTF-8");
	}
	
	public static String decompressFromString(String textToDecode) throws IOException{
        byte[] data = Base64.getDecoder().decode(textToDecode);
        return decompress(data);
	}
}
