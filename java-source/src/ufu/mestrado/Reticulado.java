package ufu.mestrado;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Classe que representa um reticulado.
 * 
 * @author Tarcísio Abadio de Magalhães Júnior
 *
 */
public class Reticulado implements Cloneable {
//	private Reticulavel reticulavel;
	private boolean[][] reticulado;
	private int linhas; 
	private int colunas;
	
	private int deslocamentoLinha = 0;
	private int deslocamentoColuna = 0;
	
	private int numeroZeros = 0;
	
	public Reticulado(List<String> lstLinhas) {
		linhas = 0;
		colunas = 0;

		linhas =  lstLinhas.size();
		
		if (linhas > 0)
			colunas = lstLinhas.get(0).length();
		
		reticulado = new boolean[linhas][colunas];
		
		int i = 0;
		for (String l : lstLinhas) {
			reticulado[i++] = Util.getArray(l);
		}
		
		contabiliarZeros();
	}
	
	public Reticulado(String strLinhas[]) {
		linhas = 0;
		colunas = 0;
		
		linhas =  strLinhas.length;
		
		if (linhas > 0)
			colunas = strLinhas[0].length();
		
		reticulado = new boolean[linhas][colunas];
		
		int i = 0;
		for (String l : strLinhas) {
			reticulado[i] = Util.getArray(l);
			i++;
		}
		
		contabiliarZeros();
	}
	
	public Reticulado(boolean[][] reticulado) {
		linhas = 0;
		colunas = 0;
		
		this.reticulado = reticulado;
		
		linhas = reticulado.length;
		
		if (linhas > 0)
			colunas = reticulado[0].length;
		
		contabiliarZeros();
	}
	
	public Reticulado(int linhas, int colunas) {
		this.reticulado = new boolean[linhas][colunas];
		this.linhas = linhas;
		this.colunas = colunas;
		this.numeroZeros = linhas * colunas;
	}
	
	public Reticulado(Reticulavel reticulavel) {
//		this.reticulavel = reticulavel;
		this.linhas = reticulavel.getLinhas();
		this.colunas = reticulavel.getColunas();
		
		this.numeroZeros = 0;
		this.reticulado = new boolean[linhas][colunas];
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				boolean valor = reticulavel.get(i, j);
				this.reticulado[i][j] = valor;
				this.numeroZeros += valor ? 0 : 1;
			}
		}
	}

	/**
	 * Conta a quantidade de zeros do reticulado.
	 */
	public void contabiliarZeros() {
		numeroZeros = 0;
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				if (!get(i, j))
					numeroZeros++;
			}
		}
	}
	
	/**
	 * Retorna o número de zeros contidos no reticulado. 
	 * @return A quantidade de zeros do reticulado
	 */
	public int getNumeroZeros() {
		return numeroZeros;
	}
	
	/**
	 * Retorna o percentual de zeros do reticulado.
	 * @return Retorna o percentual de zeros do reticulado.
	 */
	public double getPercentualZeros() {
		int total = linhas * colunas;
		
		return ((double) getNumeroZeros() / (double) total) * 100.0;
	}
	
	/**
	 * Obté o valor para a linha e coluna informados.
	 * @param linha Linha 
	 * @param coluna Coluna
	 * @return Obtém o valor do reticulado a partir da linha e coluna fornecidos.
	 */
	public boolean get(int linha, int coluna) {
		linha = getIndiceLinha(linha);
		coluna = getIndiceColuna(coluna);
		
		boolean valor = reticulado[linha][coluna]; 
		
//		if (reticulavel != null && reticulavel.get(linha, coluna) != valor) {
//			throw new RuntimeException("O valor do reticulado está diferente do valor do reticulavel: reticulado[" 
//					+ linha + ", " + coluna + "] = " + valor);
//		}
		
		return valor;
	}
	
	/**
	 * Configura o bit do reticulado para a linha e coluna fornecidos.
	 * @param linha Linha.
	 * @param coluna Coluna.
	 * @param valor Valor a ser configurado no reticulado.
	 */
	public void set(int linha, int coluna, boolean valor) {
		linha = getIndiceLinha(linha);
		coluna = getIndiceColuna(coluna);
		
		/*boolean valorAntigo = get(linha, coluna);
		
		if (valorAntigo != valor) {
			numeroZeros += valor ? -1 : 1;
		}*/
		
		reticulado[linha][coluna] = valor;
		
//		if (reticulavel != null) {
//			reticulavel.set(linha, coluna, valor);
//		}
	}
	
	public final int getIndiceLinha(int linha) {
		return getIndice(linhas, linha + deslocamentoLinha);
	}
	
	public final int getIndiceColuna(int coluna) {
		return getIndice(colunas, coluna + deslocamentoColuna);
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
	
	/**
	 * Retorna quatidade de linhas do reticulado.
	 * @return Qtd de linhas.
	 */
	public int getLinhas() {
		return linhas;
	}
	
	/**
	 * Retorna a quantidade colunas do reticulado.
	 * @return Qtd colunas.
	 */
	public int getColunas() {
		return colunas;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		String quebra = "";
		
		for (boolean l[] : reticulado) {
			builder.append(quebra);
			builder.append(Util.toString(l));
			
			quebra = "\n";
		}
		
		return builder.toString();
	}
	
	@Override
	public Reticulado clone() {
		try {
			Reticulado clone = (Reticulado) super.clone();
			
			clone.reticulado = new boolean[this.linhas][this.colunas];
			
			for (int i = 0; i < clone.reticulado.length; i++) {
				clone.reticulado[i] = Arrays.copyOf(this.reticulado[i], this.reticulado[i].length);
			}
				 
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Cria um novo reticulado com um ruído.
	 * @return Novo reticulado.
	 */
	public Reticulado aplicarRuido() {
		Reticulado clone = clone();
		
		//Random random = new Random();
		
		int linha = 15;//random.nextInt(linhas);
		int coluna = 6;//random.nextInt(colunas);
		
		clone.set(linha, coluna, !clone.get(linha, coluna));
		
		return clone;
	}
	
	/**
	 * Aplica um xor com o reticulado fornecido.
	 * @param reticulado Reticulado resultante.
	 * @return
	 */
	public Reticulado xor(Reticulado reticulado) {
		Reticulado xor = new Reticulado(linhas, colunas);
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				
				xor.set(i, j, reticulado.get(i, j) ^ get(i, j));
			}
		}
		
		return xor;
	}
	
	/**
	 * Carrega os reticulados a partir do arquivo fornecido.
	 * @param nomeArquivo Nome do arquivo.
	 * @return Reticulados
	 * @throws Exception
	 */
	public static List<Reticulado> carregarReticulados(String nomeArquivo) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));
		
		List<Reticulado> reticulados = new ArrayList<Reticulado>();

		List<String> linhas = new ArrayList<String>();
		
		while (true) {
			String linha = reader.readLine();
			
			// Chegou ao final do arquivo
			if (linha == null)
				break;
			
			// Linha vazia indica que será fornecido um novo reticulado.
			if (linha.length() == 0) {
				Reticulado reticulado = new Reticulado(linhas);
				reticulados.add(reticulado);
				
				linhas = new ArrayList<String>();
			} else {
				linhas.add(linha);
			}
		}
		
		if (linhas.size() > 0) {
			Reticulado reticulado = new Reticulado(linhas);
			reticulados.add(reticulado);
		}
		
		reader.close();
		
		return reticulados;
	}
	
	/**
	 * Gera um reticulado aleatório de acordo com a linhas e colunas fornecidas.
	 * @param linhas Número de linhas.
	 * @param colunas Número de colunas.
	 * @return Reticulado aleatório.
	 */
	public static Reticulado gerarReticulado(int linhas, int colunas, boolean []distribuicao) {
		boolean reticulado[][] = new boolean[linhas][colunas];
		
		Random random = new Random();
		
		for (boolean linha[] : reticulado) {
			for (int i = 0; i < linha.length; i++) {
				linha[i] = distribuicao[random.nextInt(distribuicao.length)];
			}
		}
		
		return new Reticulado(reticulado);
	}
	
	/**
	 * Gera um reticulado aleatório de acordo com a linhas e colunas fornecidas.
	 * @param linhas Número de linhas.
	 * @param colunas Número de colunas.
	 * @return Reticulado aleatório.
	 */
	public static Reticulado gerarReticulado(int linhas, int colunas) {
		return gerarReticulado(linhas, colunas, new boolean[] {false, true});
	}
	
	private int getIndiceEntropia(int linha, int coluna, int qtdLinhas, int qtdColunas) {
		int indice = 0;
		for (int i = 0; i < qtdLinhas; i++) {
			for (int j = 0; j < qtdColunas; j++) {
				indice = indice << 1;
				indice |= Util.toInt(get(linha + i, coluna + j));
			}
		}
		
		return indice;
	}
	
	/**
	 * Calcula a entropia do reticulado.
	 * @param qtdLinhasJanela Quantidade de linhas para a janela.
	 * @param qtdColunasJanela Quantidade de colunas para a janela.
	 * @return
	 */
	public double entropia(int qtdLinhasJanela, int qtdColunasJanela) {
		Map<Integer, Integer> ocorrencias = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Integer indice = getIndiceEntropia(i, j, qtdLinhasJanela, qtdColunasJanela);
				
				Integer quantidade = ocorrencias.get(indice);
				
				if (quantidade == null)
					quantidade = 1;
				else
					quantidade++;
				
				ocorrencias.put(indice, quantidade);
			}
		}
		
		double entropia = 0;
		
		double maximoOcorrencias = (int) Math.pow(2, qtdLinhasJanela * qtdColunasJanela);
		
		for (Integer ocorrencia : ocorrencias.values()) {
			double tmp = ((double)ocorrencia / (double) maximoOcorrencias); 
			
			entropia += tmp * (Math.log(tmp) / Math.log(2));
		}

		entropia = -entropia;
		
		entropia = entropia / (double) (qtdLinhasJanela * qtdColunasJanela);
		
		//System.out.println(entropia);
		return entropia;
	}
	
	public void setDeslocamentoLinha(int deslocamentoLinha) {
		this.deslocamentoLinha = deslocamentoLinha;
	}

	public void setDeslocamentoColuna(int deslocamentoColuna) {
		this.deslocamentoColuna = deslocamentoColuna;
	}

	public Integer getDeslocamentoLinha() {
		return this.deslocamentoLinha;
	}

	public Integer getDeslocamentoColuna() {
		return this.deslocamentoColuna;
	}
	
	public void inverterDeslocamentos() {
		deslocamentoColuna = -deslocamentoColuna;
		deslocamentoLinha = -deslocamentoLinha;
	}
	
	/**
	 * Remove o deslocamento da linha e da coluna.
	 */
	public void removerDeslocamento() {
		deslocamentoColuna = 0;
		deslocamentoLinha = 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Reticulado))
			return false;
		
		Reticulado ret = (Reticulado) obj;

		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				if (get(i, j) != ret.get(i, j))
					return false;
			}
		}
		
		return true;
	}
}
