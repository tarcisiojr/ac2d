package ufu.mestrado.teste;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import ufu.mestrado.Regra;

public class RemoverResultadosRegras {

	public static void main(String[] args) throws Exception {
		
		List<Regra> regras = Regra.carregarRegras("c:/temp/regrasx/regras.txt");
		
		BufferedReader reader = new BufferedReader(new FileReader("c:/temp/regrasx/resultado_fechado.txt"));

		while (true) {
			String linha = reader.readLine();
			
			// Final de arquivo?
			if (linha == null) {
				break;
			}
			
			for (Regra r : regras) {
				if (linha.contains(r.getNucleo())) {
					System.out.println(linha);
				}
			}
		}
	}
}
