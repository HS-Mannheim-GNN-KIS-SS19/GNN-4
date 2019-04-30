import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Image {
	
	public static Image readImagePPM(Path path) throws IOException {
		byte[] binary = Files.readAllBytes(path);
		
		int offsetResX = skipToChar(binary, skipToChar(binary, 0, (byte) '\n'), (byte) '\n');
		int offsetResY = skipToChar(binary, offsetResX, (byte) ' ');
		int offsetResEnd = skipToChar(binary, offsetResY, (byte) '\n');
		
		int resX = Integer.parseInt(new String(binary, offsetResX, offsetResY - offsetResX - 1));
		int resY = Integer.parseInt(new String(binary, offsetResY, offsetResEnd - offsetResY - 1));
		
		int offsetData = skipToChar(binary, offsetResEnd, (byte) '\n');
		byte[] data = new byte[(binary.length - offsetData) / 3];
		for (int i = 0; i < data.length; i++)
			data[i] = binary[offsetData + (i * 3)];
		
		return new Image(resX, resY, data);
	}
	
	private static int skipToChar(byte[] data, int start, byte character) throws IOException {
		for (int i = start; i < data.length; i++)
			if (data[i] == character)
				return i + 1;
		throw new IOException("EOF");
	}
	
	public final int xRes, yRes;
	public final byte[] buffer;
	
	public Image(int xRes, int yRes, byte[] buffer) {
		this.xRes = xRes;
		this.yRes = yRes;
		this.buffer = buffer;
	}
	
	public byte pixel(int x, int y) {
		return buffer[x + y * xRes];
	}
	
	public float pixelAsFloat(int x, int y) {
		return Byte.toUnsignedInt(buffer[x + y * xRes]) / 255f;
	}
}
