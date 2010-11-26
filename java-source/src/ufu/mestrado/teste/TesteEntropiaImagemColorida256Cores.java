package ufu.mestrado.teste;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Reticulado;
import ufu.mestrado.imagem.CifradorImagemColorida256Cores;


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
public class TesteEntropiaImagemColorida256Cores {
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
		CifradorImagemColorida256Cores cifrador = new CifradorImagemColorida256Cores(
				pastaSaida + nomeArquivo, chave, direcao);
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		ac.deslocarReticulado = true;
		ac.rotacionarReticulado = false;
		ac.rotacionarNucleo = true;
		
		Reticulado reticulados[] = new Reticulado[3];
		
		if (aplicarRuido) {
			ac.setReticulado(ac.getReticulado().aplicarRuido(127, 127));
//			ac.setReticulado(ac.getReticulado().aplicarRuido(
//					ac.getReticulado().getLinhas() / 2 - 1, 
//					ac.getReticulado().getColunas() / 2 - 1));
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
		
		//double entropia = xor.entropia(LINHAS_JANELA, COLUNAS_JANELA);
		//double entropiaLinha = xor.getMenorEntropiaLinha();
		//double entropiaColuna = xor.getMenorEntropiaColuna();
		
		xor.contabiliarZeros();
		
		System.out.println("=> " + texto);
		System.out.println("% ZEROS............: " + xor.getPercentualZeros());
		//System.out.println("Entropia XOR.......: " + new BigDecimal(entropia).setScale(6, BigDecimal.ROUND_HALF_UP));
		//System.out.println("Entropia linha XOR.: " + new BigDecimal(entropiaLinha).setScale(6, BigDecimal.ROUND_HALF_UP));
		//System.out.println("Entropia coluna XOR: " + new BigDecimal(entropiaColuna).setScale(6, BigDecimal.ROUND_HALF_UP));
		System.out.println();
		
		final int linhas = xor.getLinhas();
		final int colunas = xor.getColunas();
		
		BufferedImage buffer = new BufferedImage(colunas, linhas, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				buffer.setRGB(j, i, xor.get(i, j) ? PRETO : BRANCO);
			}
		}
		
		ImageIO.write(buffer, "BMP", new File(PASTA_SAIDA + nomeArquivo));
	}

	
	public static final String PASTA_SAIDA = "C:\\TEMP\\LENA\\";
	
	public static void main(String[] args) throws Exception {
		String pasta = "c:\\temp\\lena\\";
		String imagem = "128x128_001.png";
		
		int pi = 10;
		
		String regra = "0110100101001010";
		regra = "0000010010110001010000101000000000001010010000101001001100000010100011000000000000000100000001010000001000011000000000010000000000100000000000010100001000001011010100101110000010001100000010001000100100010000000011101010001100001100010010100101000001001000";
		
		
		Reticulado[] ret1 = cifrar(pi, regra, DirecaoCalculo.NORTE, pasta, imagem, true, false);
		Reticulado[] ret2 = cifrar(pi, regra, DirecaoCalculo.NORTE, pasta, imagem, true, true);
			
		computarXOR("XOR lena reticulado inicial", ret1[0], ret2[0], "xor_inicial.bmp");
		computarXOR("XOR lena reticulado cifrado", ret1[1], ret2[1], "xor_cifrado_" + pi +".bmp");
		computarXOR("XOR lena reticulado decifrado", ret1[2], ret2[2], "xor_decifrado.bmp");
		
		System.out.println("FIM");
	}
}
