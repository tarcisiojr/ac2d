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
        buf.append(array[0] ? "1" : "0");
        //buf.append("\t");
        for (int i = 1; i < array.length; i++) {
//            buf.append(", ");
        	//buf.append("\t");
            buf.append(array[i] ? "1" : "0");
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
		return descobrirTamanhoJanela(palavra.length);
	}
	
	public static int descobrirTamanhoJanela(int tamanhoArray) {
		return (int) (Math.log(tamanhoArray) / Math.log(2));
	}
	
	/**
	 * Calcula o valor da entropia normalizada para a palavra bin�ria fornecida.
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
	
	// Constantes utilizadas para calcular a entropia em matriz.
	public static final int ENTROPIA_LINHA = 0;
	public static final int ENTROPIA_COLUNA = 1;
	
	/**
	 * Realiza o c�lculo da entropia em uma coluna ou linha da matriz fornecida.
	 * @param matriz Matriz que fornecer� a linha ou a coluna para ser c�lculada a entropia.
	 * @param tipo Indica o c�lculo da entropia ser� realizado na coluna ou na linha.
	 * @param linha Linha da matriz que ser� calculada a entropia.
	 * @param coluna Coluna da matriz que ser� calculada a entropia.
	 * @param tamJanela Tamanho da janela a ser utilizado no c�lculo da entropia.
	 * @return a entropia da linha ou coluna para a matriz fornecida.
	 */
	public static final double entropiaMatriz(final boolean matriz[][], final int tipo, final int linha,
			final int coluna, final int tamJanela) {
		
		final Map<Integer, Integer> ocorrencias = new HashMap<Integer, Integer>();
		
		final int maximo = ENTROPIA_LINHA == tipo ? matriz[0].length : matriz.length;
		
		for (int i = 0; i < maximo; i++) {
			
			int indice = 0;
			
			for (int j = 0; j < tamJanela; j++) {
				boolean valor = (ENTROPIA_LINHA == tipo) ?
					matriz[linha][(i + j) % maximo] :
					matriz[(i + j) % maximo][coluna];
				
				indice = (indice << 1) | (valor ? 1 : 0);
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
		
		return entropia;
	}
	
	/**
	 * Idem ao m�todo entropiaMatriz, por�m retorna o valor da entropia normalizado.
	 */
	public static final double entropiaMatrizNormalizada(final boolean matriz[][], final int tipo, final int linha,
			final int coluna) {
		
		final int tamanhoJanela = descobrirTamanhoJanela(tipo == ENTROPIA_LINHA ? matriz[0].length : matriz.length);
		
		return entropiaMatriz(matriz, tipo, linha, coluna, tamanhoJanela) / tamanhoJanela;
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
		
		return entropia;
	}
	
	/**
	 * Fun��o que realiza uma opera��o de xor entre os arrys fornecido.
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
	 * Calcula o desvio padr�o para a amostra e media fornecidos.
	 * @param valores Amostra.
	 * @param media M�dia da amostra.
	 * @return O valor do desvio padr�o da amostra.
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
	 * Gera um array aleat�rio de valores boleanos. 
	 * @param tamanho Tamanho do array.
	 * @return Array aleat�rio de valores boleanos.
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
	 * @param max Indice m�ximo.
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
