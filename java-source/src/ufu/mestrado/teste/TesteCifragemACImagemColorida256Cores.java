package ufu.mestrado.teste;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.imagem.CifradorImagemColorida256Cores;

public class TesteCifragemACImagemColorida256Cores {
	
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
		CifradorImagemColorida256Cores cifrador = new CifradorImagemColorida256Cores(
				pastaSaida + nomeArquivo, chave, direcao);
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		Cronometro.iniciar();
		ac.deslocarReticulado = true;
		ac.rotacionarReticulado = false;
		
		ac.calcularPreImage(qtdPI);
		Cronometro.parar("Cifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao);
		
		cifrador.criarImagem(pastaSaida + "cifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) +"_" + nomeArquivo);
		
		if (decifrar) {
			Cronometro.iniciar();
			ac.evoluir(qtdPI);
			Cronometro.parar("Decifragem arquivo '" + nomeArquivo +"', PI=" + qtdPI + ", chave=" + chave + ", direcao=" + direcao);
			
			cifrador.criarImagem(pastaSaida + "decifrada_pi" + qtdPI + "_" + DirecaoCalculo.toString(direcao) + "_" + nomeArquivo);
		}
		
		return ac;
	}

	public static void main(String[] args) throws Exception {
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "circulo.bmp", true);
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "d:/Desktop/mestrado/testes_ac2d/teste_imagem_colorida/", "ferrari.bmp", true);
		cifrar(30, "0111101110110001", DirecaoCalculo.NORTE, "C:\\Temp\\lena\\", "lena_gray_32x32.bmp", true);
		//cifrar(6, "0111101110110001", DirecaoCalculo.NORTE, "d:/Desktop/mestrado/testes_ac2d/teste_imagem_colorida/", "tudo_preto.bmp", true);
		
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "quadrado.bmp", true);
		//cifrar(10, "0111101110110001", DirecaoCalculo.NORTE, "E:/junior/Desktop/mestrado/testes_ac2d/", "triangulo.bmp", true);
	}
}
