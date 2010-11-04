package ufu.mestrado.teste.exemplo.dissertacao;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;

public class ExemploPreImagemRotacao {
	
	private static void imprimir(String reticulado[], String regraStr, int direcao) throws Exception {
		Reticulado retInicial = new Reticulado(reticulado);
		
		System.out.println(retInicial);
		System.out.println("==================");
		
		Regra regra = Regra.criarAPatirNucleo(regraStr, DirecaoCalculo.NORTE);
		
		AutomatoCelular ac = new AutomatoCelular(retInicial, regra);
		
		ac.rotacionarReticulado = true;
		ac.deslocarReticulado = true;
		ac.rotacionarNucleo = true;
		
		System.out.println(ac.calcularPreImage(1));
	}

	public static void main(String[] args) throws Exception {
		String regra = "1001010110101110";
		
		imprimir(new String[] {
				"1111111111111111",
				"1111111111111111",
				"1100000000000011",
				"1100001111000011",
				"1100011111100011",
				"1100110000110011",
				"1100110000110011",
				"1100110000110011",
				"1100111111110011",
				"1100110000110011",
				"1100110000110011",
				"1100110000110011",
				"1100110000110011",
				"1100000000000011",
				"1111111111111111",
				"1111111111111111"}, regra, DirecaoCalculo.NORTE);
		
	}
}
