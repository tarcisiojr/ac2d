package ufu.mestrado;

import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Random;

public abstract class Gerador {
	public static boolean []DISTRIBUICAO_ZEROS = Util.getArray("0000001");
	public static boolean []DISTRIBUICAO_UNS   = Util.getArray("1000000");
	
	/**
	 * Gera um arquivo de reticulados aleatórios.
	 * @param quantidade Quantidade de reticulados.
	 * @param linhas Quantidade de linhas dos reticulados.
	 * @param colunas Quantidade de coluasn dos reticulados.
	 * @return O nome do arquivo criado.
	 */
	public static String gerarReticulados(int quantidade, int linhas, int colunas) {
		return gerarReticulados(quantidade, linhas, colunas, null, 0, 0);
	}
	
	/**
	 * Gera um arquivo de reticulados aleatórios.
	 * @param quantidade Quantidade de reticulados.
	 * @param linhas Quantidade de linhas dos reticulados.
	 * @param colunas Quantidade de coluasn dos reticulados.
	 * @param entropia Entropia máxima permitida, caso seja null o parâmetro não será considerado.
	 * @return O nome do arquivo criado.
	 */
	public static String gerarReticulados(int quantidade, int linhas, int colunas, Double entropia, int qtdLinhasJanela, int qtdColunasJanela) {
		try {
			String nomeArquivo = "reticulados_" + 
				quantidade + "_" + 
				linhas + "x" + colunas +
				(entropia != null ? "_entropia" + entropia : "") +
				".txt"; 
			
			FileOutputStream fos = new FileOutputStream(nomeArquivo);
			
			String delim = "";
			
			for (int i = 0; i < quantidade; i++) {

				Reticulado reticulado = Reticulado.gerarReticulado(linhas, colunas, DISTRIBUICAO_ZEROS); 
				
				// Só adiciona se a entropia nao for considerada, 
				// ou a entropia do reticulado é menor que o parametro fornecido
				if (entropia == null || reticulado.entropia(qtdLinhasJanela, qtdColunasJanela) <= entropia) {
					
					fos.write(delim.getBytes());
					fos.write(reticulado.toString().getBytes());
					
					delim = "\n\n";
				} else {
					i--;
				}
			}
			
			fos.close();
			
			return nomeArquivo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gera uma arquivo com núcleos aleatórios.
	 * @param quantidade Quantidade de núcleos.
	 * @param tamanhoRaio Tamanho do raio.
	 * @return Retorna o nome do arquivo criado.
	 */
	public static String gerarRegras(int quantidade, int tamanhoRaio) {
		try {
			String nomeArquivo = "regras_" + quantidade + "_" + tamanhoRaio + ".txt"; 
			
			FileOutputStream fos = new FileOutputStream(nomeArquivo);
			
			int tamanhoMaximo = (int) Math.pow(2, tamanhoRaio * 4 + 1);
			
			int tamanhoNucleo = tamanhoMaximo / 2;
			
			byte buffer[] = new byte[tamanhoNucleo];
			
			Arrays.fill(buffer, (byte)'0');
			
			String delim = "";
			
			String zeros = new String(buffer);
			
			Random random = new Random();
			
			for (int i = 0; i < quantidade; i++) {
			
				fos.write(delim.getBytes());
				
				String nucleo = Util.toString(Util.gerarArrayAleatorio(random, tamanhoNucleo)); 
				//Integer.toBinaryString(random.nextInt((int) Math.pow(2, tamanhoNucleo)));
				
				fos.write(zeros.substring(0, zeros.length() - nucleo.length()).getBytes());
				fos.write(nucleo.getBytes());
				
				delim = "\n";
			}
			
			fos.close();
			
			return nomeArquivo;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		//Gerador.gerarReticulados(1000, 16, 16, 0.5, 2, 4);
		Gerador.gerarRegras(1000, 1);
	}
}
