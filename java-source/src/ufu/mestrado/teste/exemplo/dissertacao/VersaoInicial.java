package ufu.mestrado.teste.exemplo.dissertacao;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;

public class VersaoInicial {

	public static void main(String[] args) throws Exception {
		Reticulado retInicial = new Reticulado(new String[] {
				"01000",
				"11001",
				"00011",
				"11010",
				"11011"
		});
		
		System.out.println(retInicial);
		
		Regra regra = Regra.criarAPatirNucleo("0100010111010110", DirecaoCalculo.NORTE);
		
		System.out.println("===== Regra Principal =======");
		regra.imprimir();
		System.out.println("==================");
		
		System.out.println("===== Regra Contorno =======");
		Regra.criarRegraContorno(regra).imprimir();
		System.out.println("==================");
		
		AutomatoCelular ac = new AutomatoCelular(retInicial, regra);
		
		ac.rotacionarReticulado = false;
		ac.deslocarReticulado = false;
		
		System.out.println(ac.calcularPreImage(1));
		
	}
}
