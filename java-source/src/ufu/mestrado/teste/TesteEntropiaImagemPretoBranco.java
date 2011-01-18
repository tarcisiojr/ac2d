package ufu.mestrado.teste;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;


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

	// Dados utilizados no calculo da entropia
	public static final int LINHAS_JANELA = 6;
	public static final int COLUNAS_JANELA = 3;
	
	/**
	 * Realiza cifragem da imagem fornecida e cria um novo arquivo a partir da imagem cifrada.
	 * @param qtdPI Quantidade de pré-imagens.
	 * @param chave Chave para a cifragem.
	 * @param direcao Direção do cálculo.
	 * @param pastaSaida Pasta de saída do arquivos.
	 * @param nomeArquivo Nome do arquivo que será cifrado.
	 * @param decifrar Indica se é para realizar o processo de decifragem.
	 * @throws Exception
	 */
	public static Reticulado[] cifrar(int qtdPI, String chave, int direcao, String pastaSaida, String nomeArquivo, boolean decifrar, boolean aplicarRuido) throws Exception {
		CifradorImagemPretroBranco cifrador = new CifradorImagemPretroBranco(
				pastaSaida + nomeArquivo, chave, direcao);
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		ac.deslocarReticulado = true;
		ac.rotacionarReticulado = false;
		ac.rotacionarNucleo = true;
		
		Reticulado reticulados[] = new Reticulado[3];
		
		if (aplicarRuido) {
			ac.setReticulado(ac.getReticulado().aplicarRuido(
					ac.getReticulado().getLinhas() / 2 - 1, 
					ac.getReticulado().getColunas() / 2 - 1));
		}
		
		reticulados[0] = ac.getReticulado();
		
		Cronometro.iniciar();
		ac.calcularPreImage(qtdPI);
		Cronometro.parar("Cifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + DirecaoCalculo.toString(direcao)+ " TEMPO: ");
		
		reticulados[1] = ac.getReticulado();
		
		cifrador.criarImagem(pastaSaida + "cifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) +"_" + nomeArquivo);
		
		if (decifrar) {
			Cronometro.iniciar();
			ac.evoluir(qtdPI);
			Cronometro.parar("Decifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao);
		
			reticulados[2] = ac.getReticulado();
			
			cifrador.criarImagem(pastaSaida + "decifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) + "_" + nomeArquivo);
		}
		
		return reticulados;
	}

	
	private static void computarXOR(String texto, Reticulado ret1, Reticulado ret2, String nomeArquivo) throws Exception {
		// Agora vamos calcular o XOR entre as imagens.
		Reticulado xor = ret1.xor(ret2);
		
		double entropia = xor.entropia(LINHAS_JANELA, COLUNAS_JANELA);
		double entropiaLinha = xor.getMenorEntropiaLinha();
		double entropiaColuna = xor.getMenorEntropiaColuna();
		
		xor.contabiliarZeros();
		
		System.out.println("=> " + texto);
		System.out.println("% ZEROS............: " + xor.getPercentualZeros());
		System.out.println("Entropia XOR.......: " + new BigDecimal(entropia).setScale(6, BigDecimal.ROUND_HALF_UP));
		System.out.println("Entropia linha XOR.: " + new BigDecimal(entropiaLinha).setScale(6, BigDecimal.ROUND_HALF_UP));
		System.out.println("Entropia coluna XOR: " + new BigDecimal(entropiaColuna).setScale(6, BigDecimal.ROUND_HALF_UP));
		System.out.println();
		
		final int linhas = xor.getLinhas();
		final int colunas = xor.getColunas();
		
		BufferedImage buffer = new BufferedImage(colunas, linhas, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				buffer.setRGB(j, i, xor.get(i, j) ? PRETO : BRANCO);
			}
		}
		
		ImageIO.write(buffer, "BMP", new File("d:/Desktop/mestrado/testes_ac2d/teste_entropia/" + nomeArquivo));
	}

	
	public static void main(String[] args) throws Exception {
		
		int pi = 5;
		//String regra = "0000000101100001";
		String regra = "0000000101100001";
		//regra = "0101110111010010010001101010011000101001001101101111101000111111011100100111111110111100110000100111001111101000011101001001101110101001110000000110011100100010000101110010001100000000000110100111101100110010011000010111000001111010111011111111101111101001";
		//String regra = "0000001000001000100100011100110010010100000000000100000100000000010000100101100000000001000000000000110010010001100001000001001010100100000010000100010101010001001100000000001100100111010010101010000001000100101000010100101100000000100000000001000001000000";
		
		String imagem = "512x512_0251.png";
		
		//for (int i = 1; i <= 5; i++) {

			//pi = i * 5;

//		Reticulado[] ret1 = cifrar(pi, regra, DirecaoCalculo.NORTE, "d:/Desktop/mestrado/testes_ac2d/teste_entropia/", "512x512_0251.png", false, false);
		
			Reticulado[] ret1 = cifrar(pi, regra, DirecaoCalculo.NORTE, "d:/Desktop/mestrado/testes_ac2d/teste_entropia/", imagem, true, false);
			Reticulado[] ret2 = cifrar(pi, regra, DirecaoCalculo.NORTE, "d:/Desktop/mestrado/testes_ac2d/teste_entropia/", imagem, true, true);
			
			//System.out.println(ret1[0]);
			///System.out.println("========");
			//sSystem.out.println(ret1[1]);
			//computarXOR("XOR lena reticulado inicial", ret1[0], ret2[0], "xor_inicial.bmp");
			computarXOR("XOR lena reticulado cifrado", ret1[1], ret2[1], "xor_cifrado_" + pi +".bmp");
			//computarXOR("XOR lena reticulado decifrado", ret1[2], ret2[2], "xor_decifrado.bmp");
		//}
		
		System.out.println("FIM");
	}
}
