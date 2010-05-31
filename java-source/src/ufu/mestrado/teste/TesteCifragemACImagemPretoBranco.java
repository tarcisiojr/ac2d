package ufu.mestrado.teste;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;

public class TesteCifragemACImagemPretoBranco {
	
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
	public static AutomatoCelular cifrar(int qtdPI, String chave, int direcao, String pastaSaida, String nomeArquivo, boolean decifrar) throws Exception {
		CifradorImagemPretroBranco cifrador = new CifradorImagemPretroBranco(
				pastaSaida + nomeArquivo, chave, direcao);
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		Cronometro.iniciar();
		ac.calcularPreImage(qtdPI);
		Cronometro.parar("Cifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao + " ");
		
		cifrador.criarImagem(pastaSaida + "cifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) +"_" + nomeArquivo);
		
		if (decifrar) {
			Cronometro.iniciar();
			ac.evoluir(qtdPI);
			Cronometro.parar("Decifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao + " ");
			
			cifrador.criarImagem(pastaSaida + "decifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) + "_" + nomeArquivo);
		}
		
		return ac;
	}

	public static void main(String[] args) throws Exception {
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "circulo.bmp", true);
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "mulher.bmp", true);
		//cifrar(11, "0111101110110001", DirecaoCalculo.SUL, "E:/junior/Desktop/mestrado/testes_ac2d/", "mulher.bmp", true);
		cifrar(10, "0000000101010110", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "mulher.bmp", true);
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "quadrado.bmp", true);
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "triangulo.bmp", true);
	}
}
