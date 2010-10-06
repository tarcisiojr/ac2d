package ufu.mestrado.teste;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.io.File;

import javax.imageio.ImageIO;

public class Teste {

	public static void main(String[] args) throws Exception {
		//BufferedImage buffer =  ImageIO.read(new File("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/lena_gray.jpg"));
		//BufferedImage buffer =  ImageIO.read(new File("c:/temp/lena_gray.jpg"));
		BufferedImage buffer =  ImageIO.read(new File("c:/temp/decifrada_pi30_NORTE_lena_gray.jpg"));

		
		int pixels[] = buffer.getRaster().getPixels(0, 0, buffer.getWidth(), buffer.getHeight(), (int [] )null);
		
		//buffer.getRaster().setpi
		
		for (int i = 0; i < pixels.length; i++) {
			System.out.println(pixels[i]);
		}
		
		/*
		
		ColorSpace s = buffer.getColorModel().getColorSpace();

		for (int x = 0; x < buffer.getWidth(); x++) {
			for (int y = 0; y < buffer.getHeight(); y++) {
				int pixel[] = new int[2];
				
				buffer.getRaster().getPixel(x, y, pixel);
				
				System.out.println(pixel[0]);
			}
		}
		
		
		System.out.println();
*/	}
}



