package ufu.mestrado;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class TesteImagemPretoBranco {
	
	
	
	public static void main(String[] args) throws Exception {
		final int QTD_PRE_IMAGENS = 20;
		
		System.out.println("CRIFRANDO\n");
		
		// CIFRANDO
		Cronometro.iniciar("Criando reticulado");
		Reticulado reticulado = TesteImagemPretoBranco.criarReticulado("E:/junior/Desktop/mestrado/imagem.bmp");
		Cronometro.parar();
		
		
		Cronometro.iniciar("Cifrando");
		AutomatoCelular automatoCelular = new AutomatoCelular(
				reticulado, 
				Regra.criarAPatirNucleo("1100001010000111110000101000011111000010100001111100001010000111110000110100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011", DirecaoCalculo.CIMA));
				//Regra.criarAPatirNucleo("0111101110110001", DirecaoCalculo.CIMA));
		
		//automatoCelular.calcularPreImage(QTD_PRE_IMAGENS);
		Cronometro.parar();
		
		
		Cronometro.iniciar("Criando imagem");
		//TesteImagemPretoBranco.criarImagem(automatoCelular.getReticulado(), "E:/junior/Desktop/mestrado/imagem_cifrada.bmp");
		Cronometro.parar();
		
		
		System.out.println("\n\nDECRIFRANDO\n");
		
		// DECIFRANDO
		Cronometro.iniciar("Criando reticulado");
		reticulado = TesteImagemPretoBranco.criarReticulado("E:/junior/Desktop/mestrado/imagem_cifrada.bmp");
		Cronometro.parar();
		
		Cronometro.iniciar("Decifrando");
		automatoCelular.setReticulado(reticulado);
		automatoCelular.evoluir(QTD_PRE_IMAGENS);
		Cronometro.parar();
		
		Cronometro.iniciar("Criando imagem");
		TesteImagemPretoBranco.criarImagem(automatoCelular.getReticulado(), "E:/junior/Desktop/mestrado/imagem_decifrada.bmp");
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
		
		Reticulado reticulado = new Reticulado(buffer.getHeight(), buffer.getWidth());
		
		final int PRETO = Color.BLACK.getRGB();

		
		for (int i = 0; i < buffer.getHeight(); i++) {
			
			for (int j = 0; j < buffer.getWidth(); j++) {
				
				int rgb = buffer.getRGB(j, i);
				reticulado.set(i, j, rgb == PRETO);
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
				reticulado.getColunas(), 
				reticulado.getLinhas(), 
				BufferedImage.TYPE_INT_RGB);
		
		final int PRETO = Color.BLACK.getRGB();
		final int BRANCO = Color.WHITE.getRGB();
		
		for (int i = 0; i < reticulado.getLinhas(); i++) {
			for (int j = 0; j < reticulado.getColunas(); j++) {
				buffer.setRGB(j, i, reticulado.get(i, j) ? PRETO : BRANCO);
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
