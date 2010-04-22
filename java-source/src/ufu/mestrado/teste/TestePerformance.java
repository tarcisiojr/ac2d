package ufu.mestrado.teste;

import ufu.mestrado.Cronometro;
import ufu.mestrado.Reticulado;
import ufu.mestrado.Util;

public class TestePerformance {
	/**
	 * Obtém o índice do reticulado a partir do bit mais acima.
	 * @param reticulado Reticulado a ser obtido o bit mais acima.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o índica a partir do reticulado fornecido.
	 */
	public static int getIndiceNorteNovo(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		// Indice é montado a partir do bit mais acima da esquerda para direita
		
		int linhaInicio = linha;
		int max = raio * 2 + 1;
		// Cima
		for (int i = 0; i < max; i++, linha++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}

		
		int colunaInicio = coluna;
		linha = linhaInicio + raio;
		
		// Linha central
		coluna = coluna - raio;
		for (int i = 0; i < raio * 2 + 1; i++, coluna++) {
			boolean bit = reticulado.get(linha, coluna);
			
			if (coluna != colunaInicio) {
				indice = indice << 1;
				indice |= Util.toInt(bit);
			}
		}
		
		
		return indice;
	}
	
	public static int getIndiceNorte1(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		// Indice é montado a partir do bit mais acima da esquerda para direita
		
		int baixo = linha + raio + 1;
		
		// Cima
		for (int i = 0; i < raio; i++, linha++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
			
			
			bit = reticulado.get(baixo + i, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}

		// Linha central
		coluna = coluna - raio;
		for (int i = 0; i < raio * 2 + 1; i++, coluna++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		linha++;
		coluna = coluna - raio - 1;
		
		indice = (indice << raio) | indice;
		
		return indice;
	}
	
	
	public static void main(String[] args) {
		//int max = 1000000;
		
		/*
		boolean bool[] = {true, false};
		Random r = new Random();
		
		Cronometro.iniciar();
		for (int k = 0; k < max; k++) {
			int b = Util.toInt(bool[r.nextInt(2)]);
			
			if (b == 1) {
				b = 2;
			} else {
				b = 3;
			}
		}
		Cronometro.parar("func: ");
		
		
		Cronometro.iniciar();
		for (int k = 0; k < max; k++) {
			int b = bool[r.nextInt(2)] ? 1 : 0;
			
			if (b == 1) {
				b = 2;
			} else {
				b = 3;
			}
		}
		Cronometro.parar("tern: ");
		
		if (1==1)
			return;
		*/
		
		/*Reticulado reticulado = new Reticulado(10, 10);
		
		Cronometro.iniciar();
		for (int k = 0; k < max; k++) {
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 10; j++) {
					Transicao.getIndiceNorte(reticulado, i, j, 1);
				}
			}
		}
		Cronometro.parar("Atual: ");*/
		
		int intarray[] = {0, 1};
		boolean boolarray[] = {true, false};
		
		Cronometro.iniciar();
		for (int i = 0; i < 100000000; i++) {
			if (boolarray[i % 2]) {
				;
			}
		}
		Cronometro.parar("b: ");
		
		Cronometro.iniciar();
		for (int i = 0; i < 100000000; i++) {
			if (intarray[i % 2] == 1) {
				;
			}
		}
		Cronometro.parar("i: ");
		
		
		
	}
}
