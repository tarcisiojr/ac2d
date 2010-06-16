package ufu.mestrado.teste;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.Util;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;

public class TesteEntropiaRegrasRuins {
	/*private static final String REGRAS_RUINS[] = {
		"1011111110110111",
		"1100010011001100",
		"1111111101110101",
		"1011110110110111",
		"0010001001110000",
		"0101010011000100",
		"0011001110101111",
		"0111111101111001",
		"0101111011111101",
		"1001110001000100",
		"0001111101010000",
		"1100011011001010",
		"0001001010001010",
		"1010010001000010",
		"0100010001011111",
		"0000000110010110",
		"0001000101010111",
		"0000111001000011",
		"0000111101000110",
		"0100110001101010",
		"0010100110100000",
		"0001110101001110",
		"0100100101010110",
		"0010111011111111",
		"1011111011000010",
		"1001111011101010",
		"1101101010110000",
		"1000011001110000",
		"1010010111010111",
		"0011100001000100",
		"1010001100000001",
		"1110101000010010",
		"1100011010010010",
		"1110011000010010",
		"0101110011111110",
		"1110011011110010",
		"1101110000110010",
		"1110100010111010",
		"0000001110011101",
		"1111101101111101",
		"1101010011011111",
		"1111011011110110",
		"0101111100001111",
		"1010111110001110",
		"0111111110110011",
		"1101001110111111",
		"0000111001111101",
		"0000111101010101",
		"1010111011001110",
		"1011001011110011",
		"1011101110100111",
		"1111111011110001",
		"1100110111011000",
		"0011000101110000",
		"1011000000000011",
		"1001000001010001",
		"1111100011000101",
		"1110110111110000",
		"1000111111111110",
		"1110111011010101",
		"1011011111110100",
		"1101110011110110",
		"0111100000001100",
		"0000011010001111",
		"1111011100110001",
		"1000110110101000",
		"0111111010101111",
		"0001000010101111",
		"1111101111100010",
		"1111001011110110",
		"0011101100001010",
		"1101011100111111",
		"1111110110100011",
		"0000111110000100",
		"1100101011111011",
		"1111110010101110",
		"0011101110001111",
		"0010010100111001",
		"0001101101110111",
		"0001000110001011",
		"0101110101101111",
		"1101010111000100",
		"0000110111001100",
		"1100110101101000",
		"1001011110000010",
		"1111010000011100"};*/
	
	/** Valor RGB da cor preta */
	public static final int PRETO = Color.BLACK.getRGB();
	
	/** Valor RGB da cor branca */
	public static final int BRANCO = Color.WHITE.getRGB();

	// Dados utilizados no calculo da entropia
	public static final int LINHAS_JANELA = 3;
	public static final int COLUNAS_JANELA = 6;
	
	public static final int COLUNA_RUIDO = 255;
	public static final int LINHA_RUIDO = 255;
	
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
	public static Reticulado[] cifrar(int qtdPI, String chave, int direcao, String pastaSaida, String nomeArquivo,
			boolean decifrar, boolean aplicarRuido, boolean rotacaoSensitividade) throws Exception {
		
		CifradorImagemPretroBranco cifrador = new CifradorImagemPretroBranco(
				pastaSaida + nomeArquivo, chave, direcao);
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		ac.rotacionarReticulado = rotacaoSensitividade;
		
		if (aplicarRuido) {
			ac.setReticulado(ac.getReticulado().aplicarRuido(LINHA_RUIDO, COLUNA_RUIDO));
		}
		
		Reticulado reticulados[] = new Reticulado[3];
		
		reticulados[0] = ac.getReticulado();
		
		Cronometro.iniciar();
		ac.calcularPreImage(qtdPI);
		//Cronometro.parar("Cifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao);
		
		reticulados[1] = ac.getReticulado();
		
		//cifrador.criarImagem(pastaSaida + "cifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) +"_" + nomeArquivo);
		
		if (decifrar) {
			Cronometro.iniciar();
			ac.evoluir(qtdPI);
			//Cronometro.parar("Decifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao);
		
			reticulados[2] = ac.getReticulado();
			
			//cifrador.criarImagem(pastaSaida + "decifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) + "_" + nomeArquivo);
		}
		
		return reticulados;
	}

	
	private static void computarXOR(String texto, Reticulado ret1, Reticulado ret2, String nomeArquivo, boolean gerarArquivo) throws Exception {
		// Agora vamos calcular o XOR entre as imagens.
		Reticulado xor = ret1.xor(ret2);
		
		double entropia = xor.entropia(LINHAS_JANELA, COLUNAS_JANELA);

		xor.contabiliarZeros();
		
		System.out.print(texto + "\t");
		System.out.print(xor.getPercentualZeros() + "\t");
		System.out.print(new BigDecimal(entropia).setScale(6, BigDecimal.ROUND_HALF_UP) + "\t");
		System.out.print(new BigDecimal(xor.getMenorEntropiaColuna()).setScale(6, BigDecimal.ROUND_HALF_UP) + "\t");
		System.out.print(new BigDecimal(xor.getMenorEntropiaLinha()).setScale(6, BigDecimal.ROUND_HALF_UP) + "\t");
		System.out.println();
		
		final int linhas = xor.getLinhas();
		final int colunas = xor.getColunas();
		
		BufferedImage buffer = new BufferedImage(colunas, linhas, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				buffer.setRGB(j, i, xor.get(i, j) ? PRETO : BRANCO);
			}
		}
		
		if (gerarArquivo) {
			ImageIO.write(buffer, "PNG", new File("E:/junior/Desktop/mestrado/testes_ac2d/teste_entropia_regras_ruins/" + nomeArquivo));
		}
	}

	
	public static void main(String[] args) throws Exception {
		int pi = 60;
		
		//List<Regra> regras = Regra.carregarRegras("E:/workspaces/Mestrado/testes/regras_1000_1_ver1.txt");
		//List<Regra> regras = Regra.carregarRegras("E:/junior/Desktop/mestrado/testes_ac2d/testes_rodando_ufu/regras_ruins.txt");
		List<Regra> regras = Regra.carregarRegras("E:/junior/Desktop/mestrado/testes_ac2d/testes_rodando_ufu/regras_ruins_unitario.txt");
		
		boolean rotacaoSensitividade = false;
		
		System.out.println("Indice\tRegra\tEntropia_Regra\tPercentual_0s\tEntropia_XOR\tEntropia_Coluna\tEntropia_Linha");
		int i = 0;
		for (Regra regra : regras) {
//			Reticulado[] ret1 = cifrar(pi, regra.getNucleo(), DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_entropia_regras_ruins/", "lena.bmp", true, false);
//			Reticulado[] ret2 = cifrar(pi, regra.getNucleo(), DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_entropia_regras_ruins/", "lena_alterada.bmp", true, false);

//			Reticulado[] ret1 = cifrar(pi, regra.getNucleo(), DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_entropia_regras_ruins/", "512x512_0202.png", true, false, rotacaoSensitividade);
//			Reticulado[] ret2 = cifrar(pi, regra.getNucleo(), DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_entropia_regras_ruins/", "512x512_0202.png", true, true, rotacaoSensitividade);
			
			Reticulado[] ret1 = cifrar(pi, regra.getNucleo(), DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_entropia_regras_ruins/", "512x512_0622.png", true, false, rotacaoSensitividade);
			Reticulado[] ret2 = cifrar(pi, regra.getNucleo(), DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_entropia_regras_ruins/", "512x512_0622.png", true, true, rotacaoSensitividade);

			computarXOR(++i + "\t[" + regra + "]\t" + Util.entropiaNormalizada(Util.getArray(regra.getNucleo())), 
					ret1[1], ret2[1], "xor_cifrado_" + regra.toString().substring(0, 16) + ".png", true);
		}
	}
}
