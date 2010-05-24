package ufu.mestrado;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Util {
	public static String toString(boolean array[]) {
		if (array == null)
            return "null";
        if (array.length == 0)
        	return "empty";
            //return "[]";
 
        StringBuilder buf = new StringBuilder();
//        buf.append('[');
        buf.append(array[0] ? "_" : "0");
        //buf.append("\t");
        for (int i = 1; i < array.length; i++) {
//            buf.append(", ");
            buf.append(array[i] ? "_" : "0");
            //buf.append("\t");
        }
 
//        buf.append("]");
        return buf.toString();
	}
	
	public static boolean [] getArray(String str) {
		boolean array[] = new boolean[str.length()];
		
		for (int i = 0; i < str.length(); i++)
			array[i] = str.charAt(i) != '0';
		
		return array;
	}
	
	public static int descobrirTamanhoJanela(boolean palavra[]) {
		int n = palavra.length;
		
		int tamanho = (int) (Math.log(n) / Math.log(2));
		
		return tamanho;
	}
	
	/**
	 * Calcula o valor da entropia normalizada para a palavra binária fornecida.
	 * @param palavra Palavra a ser calculada a entropia.
	 * @return O valor da entropia.
	 */
	public static double entropiaNormalizada(boolean palavra[]) {
		int tamJanela = descobrirTamanhoJanela(palavra);
		
		return entropia(palavra, tamJanela) / tamJanela;
	}
	
	public static double entropia(boolean palavra[]) {
		return entropia(palavra, descobrirTamanhoJanela(palavra));
	}
	
	public static double entropia(boolean palavra[], int tamJanela) {
		Map<Integer, Integer> ocorrencias = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < palavra.length; i++) {
			
			int indice = 0;
			
			for (int j = 0; j < tamJanela; j++) {
				indice = (indice << 1) | (palavra[(i + j) % palavra.length] ? 1 : 0);
			}
			
			Integer ocorrencia = ocorrencias.get(indice);
			
			if (ocorrencia == null)
				ocorrencia = 0;
			
			ocorrencias.put(indice, ++ocorrencia);
		}
		
		double entropia = 0;
		
		double maximoOcorrencias = (int) Math.pow(2, tamJanela);
		
		for (Integer ocorrencia : ocorrencias.values()) {
			double tmp = ((double)ocorrencia / (double) maximoOcorrencias); 
			
			entropia += tmp * (Math.log(tmp) / Math.log(2));
		}

		entropia = -entropia;
		
		//System.out.println(ocorrencias);
		//System.out.println("entropia=" + entropia);
		return entropia;
	}
	
	/**
	 * Função que realiza uma operação de xor entre os arrys fornecido.
	 * @param arr1 Array 1
	 * @param arr2 Array 2
	 * @return Array resultante entre arr1 xor arr2.
	 */
	public boolean[] xor(boolean arr1[], boolean arr2[]) {
		if (arr1.length != arr2.length)
			throw new RuntimeException("Arrays de tamanhos diferente!");
		
		boolean arrRet[] = new boolean[arr1.length];
		
		for (int i = 0; i < arr1.length; i++)
			arrRet[i] = arr1[i] ^ arr2[i];
		
		return arrRet;
	}
	
	/**
	 * Converte o valor boleano em inteiro.
	 * @param valor Valor booleano.
	 * @return O valor inteiro a partir do valor fornecido.
	 */
	public static int toInt(boolean valor) {
		return valor ? 1 : 0;
	}
	
	/**
	 * Calcula o desvio padrão para a amostra e media fornecidos.
	 * @param valores Amostra.
	 * @param media Média da amostra.
	 * @return O valor do desvio padrão da amostra.
	 */
	public static double desvioPadrao(double valores[], double media) {
		
		double somatorio = 0;
		
		for (int i = 0; i < valores.length; i++) {
			somatorio += Math.pow(valores[i] - media, 2);
		}
		
		double desvioPadrao = Math.sqrt(somatorio / (valores.length));
		
		return desvioPadrao; 
	}
	
	/**
	 * Gera um array aleatório de valores boleanos. 
	 * @param tamanho Tamanho do array.
	 * @return Array aleatório de valores boleanos.
	 */
	public static boolean[] gerarArrayAleatorio(Random random, int tamanho) {

		if (random == null)
			random = new Random();
		
		boolean[] array = new boolean[tamanho];
		
		for (int i = 0; i < tamanho; i++)
			array[i] = random.nextBoolean();
		
		return array;
	}
	
	/**
	 * Retorna o indice absoluto, a partir do valor relativo.
	 * @param max Indice máximo.
	 * @param valor
	 * @return
	 */
	public static final int getIndice(final int max, final int valor) {
		if (valor >= 0) {
			return valor % max;
		}

		final int resto = (valor % max);

		return resto == 0 ? resto : max + resto;
	}
}
