package ufu.mestrado.teste;

import java.util.HashMap;
import java.util.Map;

import ufu.mestrado.Util;

public class Teste {
	public static double entropia(boolean palavra[], int tamJanela) {
		Map<Integer, Integer> ocorrencias = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < palavra.length; i++) {
			
			int indice = 0;
			
			for (int j = 0; j < tamJanela; j++) {
				indice = (indice << 1) | (palavra[(i + j) % palavra.length] ? 1 : 0);
			}
			
			Integer ocorrencia = ocorrencias.get(indice);
			
			if (ocorrencia == null)
				ocorrencia = 0;
			
			ocorrencias.put(indice, ++ocorrencia);
		}
		
		double entropia = 0;
		
		double maximoOcorrencias = (int) Math.pow(2, tamJanela);
		
		for (Integer ocorrencia : ocorrencias.values()) {
			double tmp = ((double)ocorrencia / (double) maximoOcorrencias); 
			
			entropia += tmp * (Math.log(tmp) / Math.log(2));
		}

		entropia = -entropia;
		
		System.out.println(ocorrencias);
		
		return entropia;
	}

	public static void main(String[] args) throws Exception {
		entropia(Util.getArray("1101001110100111"), 4);
		System.out.println(Util.entropiaNormalizada(Util.getArray("1101001110100111")));
		//BufferedImage buffer =  ImageIO.read(new File("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/lena_gray.jpg"));
		//BufferedImage buffer =  ImageIO.read(new File("c:/temp/lena_gray.jpg"));
		/*BufferedImage buffer =  ImageIO.read(new File("c:/temp/decifrada_pi30_NORTE_lena_gray.jpg"));

		
		int pixels[] = buffer.getRaster().getPixels(0, 0, buffer.getWidth(), buffer.getHeight(), (int [] )null);
		
		//buffer.getRaster().setpi
		
		for (int i = 0; i < pixels.length; i++) {
			System.out.println(pixels[i]);
		}
		*/
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



