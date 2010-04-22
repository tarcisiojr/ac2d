package ufu.mestrado.teste;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Reticulado;


/**
 * Classe que realiza testes quando a entropia
 * de uma imagem.
 * 
 * O calculo da entropia será feito da seguinte 
 * maneira, cifra a imagem A e depois cifra a imagem
 * B (imagem A com apenas uma pequena alteração), depois
 * aplica-se o xor entre as immagens e finalmente calcula-se
 * a entropia do reticulado final.
 * 
 * @author Tarcísio Abadio de Magalhães Júnior
 *
 */
public class TesteEntropiaImagemPretoBranco {
	/** Valor RGB da cor preta */
	public static final int PRETO = Color.BLACK.getRGB();
	
	/** Valor RGB da cor branca */
	public static final int BRANCO = Color.WHITE.getRGB();

	public static void main(String[] args) throws Exception {
		final int linhasJanela = 2;
		final int colunasJanela = 4;
		
		AutomatoCelular ac1 = TesteCifragemACImagemPretoBranco.cifrar(10, "0111101110110001", DirecaoCalculo.SUL, "E:/junior/Desktop/mestrado/testes_ac2d/", "mulher.bmp", false);
		AutomatoCelular ac2 = TesteCifragemACImagemPretoBranco.cifrar(10, "0111101110110001", DirecaoCalculo.SUL, "E:/junior/Desktop/mestrado/testes_ac2d/", "mulher_alteracao.bmp", false);
		
		// Agora vamos calcular o XOR entre as imagens.
		Reticulado r1 = ac1.getReticulado();
		Reticulado r2 = ac2.getReticulado();
		
		Reticulado xor = r1.xor(r2);
		
		double entropia = xor.entropia(linhasJanela, colunasJanela);

		xor.contabiliarZeros();
		
		System.out.println("% ZEROS.....: " + xor.getPercentualZeros());
		System.out.println("Entropia XOR: " + entropia);
		
		final int linhas = xor.getLinhas();
		final int colunas = xor.getColunas();
		
		BufferedImage buffer = new BufferedImage(colunas, linhas, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				buffer.setRGB(j, i, xor.get(i, j) ? PRETO : BRANCO);
			}
		}
		
		ImageIO.write(buffer, "BMP", new File("E:/junior/Desktop/mestrado/testes_ac2d/mulher_xor.bmp"));
	}
}
