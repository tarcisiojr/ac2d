package ufu.mestrado.teste;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;

public class TesteCifragemACImagemPretoBranco {

	public static void main(String[] args) throws Exception {
		CifradorImagemPretroBranco cifrador = new CifradorImagemPretroBranco(
				"E:/junior/Desktop/mestrado/imagem.bmp", 
				"0111101110110001");
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		
		Cronometro.iniciar();
		ac.calcularPreImage(10);
		Cronometro.parar("Cifragem..");
		
		cifrador.criarImagem("E:/junior/Desktop/mestrado/imagem_cifrada.bmp");
		
		Cronometro.iniciar();
		ac.evoluir(10);
		Cronometro.parar("Decifragem");
		
		cifrador.criarImagem("E:/junior/Desktop/mestrado/imagem_decifrada.bmp");
	}
}
