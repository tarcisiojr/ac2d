package ufu.mestrado.teste;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import ufu.mestrado.Util;

public class TesteInterseccaoRegras {

	public static void main(String[] args) throws Exception {
		args = new String[] {
			//"D:/desktop/mestrado/testes_ac2d/teste_inserseccao/regras_rotacao.txt",
			"D:/desktop/mestrado/testes_ac2d/teste_inserseccao/regras_esquerda.txt",
			"D:/desktop/mestrado/testes_ac2d/teste_inserseccao/regras_norte.txt",
			"D:/desktop/mestrado/testes_ac2d/teste_inserseccao/regras_direita.txt",
			"D:/desktop/mestrado/testes_ac2d/teste_inserseccao/regras_sul.txt"
		};
		
		Map<String, Integer> interseccao = new HashMap<String, Integer>();
		
		for (String arquivo : args) {
			BufferedReader reader = new BufferedReader(new FileReader(arquivo));
	
			while (true) {
				String linha = reader.readLine();
				
				if (linha == null) {
					break;
				}
				
				Integer contador = interseccao.get(linha);
				
				if (contador == null) {
					contador = 1;
				} else {
					contador++;
				}
				
				interseccao.put(linha, contador);
			}
		}
		
		NumberFormat  nf = DecimalFormat.getInstance(new Locale("pt", "BR"));
		nf.setMaximumFractionDigits(6);
		nf.setMinimumFractionDigits(6);
		
		int totalInterseccao = 0;

		System.out.println("===== Regras da inserccao =====");
		for (Entry<String, Integer> entry : interseccao.entrySet()) {
			if (entry.getValue() == args.length) {
				String regra = entry.getKey().replaceAll("^\\[|\\}$", "");

				double entropia = Util.entropiaNormalizada(Util.getArray(regra));
				
				System.out.println(entry.getKey()+ "\t" + nf.format(entropia));
				
				totalInterseccao++;
			}
		}
		System.out.println();
		System.out.println();
		
		for (String arquivo : args) {
			System.out.println("===== Regras: " + arquivo);
			
			BufferedReader reader = new BufferedReader(new FileReader(arquivo));
	
			while (true) {
				String linha = reader.readLine();
				
				if (linha == null) {
					break;
				}
				
				if (interseccao.get(linha) != args.length) {
					String regra = linha.replaceAll("^\\[|\\}$", "");
					
					double entropia = Util.entropiaNormalizada(Util.getArray(regra));
					
					System.out.println(linha+ "\t" + nf.format(entropia));
				}
			}
			
			System.out.println();
			System.out.println();
		}
		
		System.out.println("TOTAL Interseccao: " + totalInterseccao);
	}
}
