package ufu.mestrado.teste;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Reticulado;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;

/**
 * Neste teste vamos crifrar uma imagem, depois aplicar um ruido na imagem cifrada, e em seguida decifrar a imagem. 
 * Assim poderemos verificar como se comporta a decifragem de um imagem com ruídos.
 *  
 * @author Tarcísio Abadio de Magalhães Júnior
 */
public class TesteDecifragemRuidoImagemPretoBranco {
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
	public static AutomatoCelular cifrar(int qtdPI, String chave, int direcao, String pastaSaida, String nomeArquivo,
			boolean decifrar) throws Exception {
		CifradorImagemPretroBranco cifrador = new CifradorImagemPretroBranco(
				pastaSaida + nomeArquivo, chave, direcao);
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		Cronometro.iniciar();
		ac.calcularPreImage(qtdPI);
		Cronometro.parar("Cifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao);
		
		cifrador.criarImagem(pastaSaida + "cifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) +"_" + nomeArquivo);
		
		Reticulado reticulado = ac.getReticulado();
		
		//Random r = new Random();
		int linhaInicial = reticulado.getLinhas() / 2 - 5;
		int colunaInicial = reticulado.getColunas() / 2 - 50;
		
		//int linhaInicial = r.nextInt(reticulado.linhas);
		//int colunaInicial = r.nextInt(reticulado.colunas);
		
		// Aplicando ruido na mensagem cifrada.
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 10; j++) {
				reticulado.set(linhaInicial + i, colunaInicial + j, true);
			}
		}
		
		cifrador.criarImagem(pastaSaida + "imagem_cifrada_pi_" + qtdPI + "_" + DirecaoCalculo.toString(direcao) + "_com_ruido_" + nomeArquivo);
		
		if (decifrar) {
			Cronometro.iniciar();
			ac.evoluir(qtdPI);
			Cronometro.parar("Decifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao);
			
			cifrador.criarImagem(pastaSaida + "decifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) + "_" + nomeArquivo);
		}
		
		return ac;
	}
	
	public static void main(String[] args) throws Exception {
		cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_ruido_mensagem/", "mulher.bmp", true);
		cifrar(20, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_ruido_mensagem/", "mulher.bmp", true);
		cifrar(30, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_ruido_mensagem/", "mulher.bmp", true);
		//cifrar(20, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/teste_ruido_mensagem/", "lena.bmp", true);
		
	}
}
