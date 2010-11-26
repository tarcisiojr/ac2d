package ufu.mestrado.teste;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BenchmarkTempo {

	private static final void testar(String classeTeste) throws Exception {
		for (int i = 0; i < 100; i++) {
			Process p = Runtime.getRuntime().exec("java -server -cp mestrado-ac2d.jar ufu.mestrado.teste." + classeTeste, null, new File("D:/workspace/Mestrado"));
			
			InputStream  in = p.getInputStream();
			
			
			BufferedReader re = new BufferedReader(new InputStreamReader(in));
			
			while (true) {
				String linha = re.readLine();
				
				if (linha == null || linha.equals("FIM")) {
					break;
				}
				
				System.out.println((i < 10 ? "0" : "") + i + " -> " + linha);
			}
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		testar("TesteCifragemAESImagem");
		//testar("TesteEntropiaImagemPretoBranco");
	}
}
