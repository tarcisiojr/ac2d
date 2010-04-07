package ufu.mestrado.teste;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

public class TesteCifragemAES {
	/**
	 * Turns array of bytes into string
	 * @param buf Array of bytes to convert to hex string
	 * @return Generated hex string
	 *//*
	public static String asHex(byte buf[]) {
		StringBuffer strbuf = new StringBuffer(buf.length * 2);
		int i;

		for (i = 0; i < buf.length; i++) {
			if (((int) buf[i] & 0xff) < 0x10)
				strbuf.append("0");

			strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
		}

		return strbuf.toString();
	}*/

	public static void main(String[] args) throws Exception {
		BufferedImage buffer =  ImageIO.read(new File("E:/junior/Desktop/mestrado/imagem.bmp"));
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		int height = buffer.getHeight();
		int width = buffer.getWidth();
		
		for (int i = 0; i < buffer.getHeight(); i++) {
			for (int j = 0; j < buffer.getWidth(); j++) {
				baos.write(buffer.getRGB(j, i));
			}
		}

		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);

		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal(baos.toByteArray());

		buffer = new BufferedImage(
				width, 
				height, 
				BufferedImage.TYPE_INT_RGB);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(encrypted);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				buffer.setRGB(j, i, bais.read());
			}
		}
		
		ImageIO.write(buffer, "BMP", new File("E:/junior/Desktop/mestrado/imagem_cifrada_aes.bmp"));
		
		/*
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original);
		System.out.println("Original string: " + originalString + " " + asHex(original));
		*/
	}
}
