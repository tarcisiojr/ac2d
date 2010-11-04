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

public class TesteSubtracaoRegras {

	public static void main(String[] args) throws Exception {
		args = new String[] {
			"D:/desktop/mestrado/testes_ac2d/teste_inserseccao/regras_norte.txt",
			"D:/desktop/mestrado/testes_ac2d/teste_inserseccao/regras_rotacao.txt"
		};
		
		Map<String, Integer> interseccao = new HashMap<String, Integer>();
		
		boolean subtrair = false;
		
		for (String arquivo : args) {
			BufferedReader reader = new BufferedReader(new FileReader(arquivo));
	
			while (true) {
				String linha = reader.readLine();
				
				if (linha == null) {
					break;
				}
				
				if (subtrair) {
					interseccao.remove(linha);
					continue;
				}
				
				Integer contador = interseccao.get(linha);
				
				if (contador == null) {
					contador = 1;
				} else {
					contador++;
				}
				
				interseccao.put(linha, contador);
			}
			
			subtrair = true;
		}
		
		NumberFormat  nf = DecimalFormat.getInstance(new Locale("pt", "BR"));
		nf.setMaximumFractionDigits(6);
		nf.setMinimumFractionDigits(6);
		
		int totalInterseccao = 0;

		System.out.println("===== Regras da inserccao =====");
		for (Entry<String, Integer> entry : interseccao.entrySet()) {
			String regra = entry.getKey().replaceAll("^\\[|\\}$", "");

			double entropia = Util.entropiaNormalizada(Util.getArray(regra));
			
			System.out.println(entry.getKey()+ "\t" + nf.format(entropia));
			
			totalInterseccao++;
		}
		
		
		System.out.println("TOTAL: " + totalInterseccao);
	}
}
