package ufu.mestrado.teste;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class TesteHistogramaImagem {
	
	/**
	 * Gera um histograma da imagem fornecida.
	 * @param caminhoImagem
	 */
	public static void calcularHistograma(String caminhoImagem, int qtdBits) throws Exception {
		BufferedImage buffer =  ImageIO.read(new File(caminhoImagem));
		
		
		final int COR_MAXIMA = (int) Math.pow(2, qtdBits);
		
		int []histograma = new int[COR_MAXIMA];
		
		for (int i = 0; i < buffer.getHeight(); i++) {
			for (int j = 0; j < buffer.getWidth(); j++) {
				int pixel[] = new int[1];
				buffer.getRaster().getPixel(i, j, pixel);
				
				//System.out.println(b[0] + (Byte.MIN_VALUE * -1));
				
				//int rgb = buffer.getRGB(j, i) & COR_MAXIMA;
				
				/*int cor = b[0];
				
				if (cor < 0) {
					cor = Byte.MIN_VALUE * -2 + cor;
				}*/
				//System.out.println(cor);
				
				histograma[pixel[0]]++;
			}
		}
		
		System.out.println("Cor\tOcorrencias");
		for (int i = 0; i < histograma.length; i++) {
			System.out.println(i + "\t" + histograma[i]);
		}
	}
	
	public static void main(String[] args) throws Exception {
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/ferrari_16cores.bmp", 4);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/ferrari_256cores.bmp", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi10_NORTE_ferrari_256cores.bmp", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/lena_gray.jpg", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi10_NORTE_lena_gray.jpg", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi40_NORTE_lena_gray.jpg", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/lena_gray.jpg", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi30_NORTE_lena_gray.jpg", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi30_NORTE_tudo_preto.bmp", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi30_NORTE_lena_gray.bmp", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/decifrada_pi30_NORTE_lena_gray.jpg", 8);
		calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi30_NORTE_lena_gray.jpg", 8);
		
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/cifrada_pi10_NORTE_lena_gray_24bits.bmp", 24);
		
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/ferrari_cinza.bmp", 8);
		//calcularHistograma("D:/desktop/mestrado/testes_ac2d/teste_imagem_colorida/preto.bmp", 8);
		
		
		//System.out.println(Short.MIN_VALUE);
	}
}
