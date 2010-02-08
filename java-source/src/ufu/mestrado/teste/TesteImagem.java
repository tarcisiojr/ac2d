package ufu.mestrado.teste;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;

public class TesteImagem {
	
	
	
	public static void main(String[] args) throws Exception {
		final int QTD_PRE_IMAGENS = 5;
		
		System.out.println("CRIFRANDO\n");
		
		// CIFRANDO
		Cronometro.iniciar("Criando reticulado");
		Reticulado reticulado = TesteImagem.criarReticulado("E:/junior/Desktop/mestrado/imagem3.bmp");
		Cronometro.parar();
		
		
		Cronometro.iniciar("Cifrando");
		AutomatoCelular automatoCelular = new AutomatoCelular(
				reticulado, 
				Regra.criarAPatirNucleo("1100001010000111110000101000011111000010100001111100001010000111110000110100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011", DirecaoCalculo.CIMA));
				//Regra.criarAPatirNucleo("0111101110110001", DirecaoCalculo.CIMA));
		
		//automatoCelular.calcularPreImage(QTD_PRE_IMAGENS);
		Cronometro.parar();
		
		
		Cronometro.iniciar("Criando imagem");
		//TesteImagem.criarImagem(automatoCelular.getReticulado(), "E:/junior/Desktop/mestrado/imagem_cifrada.bmp");
		Cronometro.parar();
		
		
		System.out.println("\n\nDECRIFRANDO\n");
		
		// DECIFRANDO
		Cronometro.iniciar("Criando reticulado");
		reticulado = TesteImagem.criarReticulado("E:/junior/Desktop/mestrado/imagem_cifrada.bmp");
		Cronometro.parar();
		
		Cronometro.iniciar("Decifrando");
		automatoCelular.setReticulado(reticulado);
		automatoCelular.evoluir(QTD_PRE_IMAGENS);
		Cronometro.parar();
		
		Cronometro.iniciar("Criando imagem");
		TesteImagem.criarImagem(automatoCelular.getReticulado(), "E:/junior/Desktop/mestrado/imagem_decifrada.bmp");
		Cronometro.parar();
	}

	/**
	 * Cria um reticulado a partir do caminho da imamgem fornecida.
	 * @param caminhoImagem Caminho da imagem.
	 * @return Um reticulado construido a partir da imagem fornecida.
	 * @throws Exception
	 */
	public static Reticulado criarReticulado(String caminhoImagem) throws Exception {
		BufferedImage buffer =  ImageIO.read(new File(caminhoImagem));
		
		Reticulado reticulado = new Reticulado(buffer.getHeight(), buffer.getWidth() * 8 * 3);
		
		//final int PRETO = Color.BLACK.getRGB();

		// Vamos serializar o pixel RGB em um array de bis de 24 posicoes
		// ou seja, 8 Bits * 3 (cores).
		
		
		for (int i = 0; i < buffer.getHeight(); i++) {
			
			for (int j = 0; j < buffer.getWidth(); j++) {
				
				int rgb = buffer.getRGB(j, i);
				for (int b = 0; b < (8 * 3); b++) {
					reticulado.set(i, j * 8 * 3 + b, (rgb & (1 << b)) != 0);
					//System.out.println((j * 8 * 3 + b));
				}
			}
			
			//System.exit(0);
		}
		
		
		return reticulado;
	}
	
	/**
	 * Cria uma imagem a partir do reticulado fornecido. 
	 * @param reticulado Reticulado o qual será criado a imagem.
	 * @param caminho Caminho onde será criada a imagem.
	 * @throws Exception
	 */
	public static void criarImagem(Reticulado reticulado, String caminho) throws Exception {
		BufferedImage buffer = new BufferedImage(
				reticulado.getColunas() / (8 * 3), 
				reticulado.getLinhas(), 
				BufferedImage.TYPE_INT_RGB);
		
		//final int PRETO = Color.BLACK.getRGB();
		//final int BRANCO = Color.WHITE.getRGB();
		
		for (int i = 0; i < reticulado.getLinhas(); i++) {
			for (int j = 0; j < reticulado.getColunas(); j += (8 * 3)) {
				int rgb = 0;
				for (int b = 0; b < (8 * 3); b++) {
					rgb |= (reticulado.get(i, j + b) ? 1 : 0) << b;
				}
				
				buffer.setRGB(j / (8 * 3), i, rgb);
			}
		}
		
		String tipo = "BMP";
		int i = caminho.lastIndexOf('.');
		if (i != -1 && (i + 1) == caminho.length()) {
			tipo = caminho.substring(i + 1);
		}
		
		ImageIO.write(buffer, tipo.toUpperCase(), new File(caminho));
	}
}
