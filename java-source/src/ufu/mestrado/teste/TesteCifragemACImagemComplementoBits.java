package ufu.mestrado.teste;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.imagem.CifradorImagemComplementoBits;

public class TesteCifragemACImagemComplementoBits {

	public static void main(String[] args) throws Exception {
		CifradorImagemComplementoBits cifrador = new CifradorImagemComplementoBits(
				"E:/junior/Desktop/mestrado/imagem3.bmp", 
				//"0111101110110001");
				"1100001010000111110000101000011111000010100001111100001010000111110000110100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011");
		
		AutomatoCelular ac = new AutomatoCelular(cifrador);
		
		
		Cronometro.iniciar();
		ac.calcularPreImage(20);
		Cronometro.parar("Cifragem..");
		
		cifrador.criarImagem("E:/junior/Desktop/mestrado/imagem_cifrada2.bmp");
		
		
		cifrador = new CifradorImagemComplementoBits(
				"E:/junior/Desktop/mestrado/imagem3.bmp", 
				//"0111101110110001");
				"1100001010000111110000101000011111000010100001111100001010000111110000110100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011111000010100001111100001010000111110000101000011");
		
		ac = new AutomatoCelular(cifrador);
		
		Cronometro.iniciar();
		ac.evoluir(20);
		Cronometro.parar("Decifragem");
		
		cifrador.criarImagem("E:/junior/Desktop/mestrado/imagem_decifrada2.bmp");
	}
}
