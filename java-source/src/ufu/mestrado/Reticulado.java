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
	private boolean[][] reticulado;
	private int linhas; 
	private int colunas;
	
	/** Deslocamento que será aplicado à linha. */
	public int deslocamentoLinha = 0;
	
	/** Deslocamento que será aplicado à coluna. */
	public int deslocamentoColuna = 0;
	
	private int numeroZeros = 0;
	
	/** Direção padrão do reticulado = NORTE */
	public int direcao = DirecaoCalculo.NORTE;
	
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
		this(linhas, colunas, DirecaoCalculo.NORTE);
	}
	
	public Reticulado(int linhas, int colunas, int direcao) {
		this.reticulado = new boolean[linhas][colunas];
		this.linhas = linhas;
		this.colunas = colunas;
		this.numeroZeros = linhas * colunas;
		this.direcao = direcao;
	}
	
	public Reticulado(Reticulado reticulado) {
		this(reticulado.linhas, reticulado.colunas, reticulado.direcao);
	}
	
	/**
	 * Conta a quantidade de zeros do reticulado.
	 */
	public void contabiliarZeros() {
		numeroZeros = 0;
		
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				if (!_get(i, j))
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
	private boolean _get(int linha, int coluna) {
		linha = (linha >= 0 ? linha % linhas : ((linha % linhas) + linhas) % linhas);

		coluna = (coluna >= 0 ? coluna % colunas : ((coluna % colunas) + colunas) % colunas);
		
		boolean valor = reticulado[linha][coluna]; 
		
		return valor;
	}
	
	
	public boolean get(int linha, int coluna) {
		// A direção padrão é o NORTE, para as demais direções
		// vamos relizar uma rotação do reticulado.
		
		switch (direcao) {
		case DirecaoCalculo.NORTE:
			return _get(linha + deslocamentoLinha, coluna + deslocamentoColuna);
			
		case DirecaoCalculo.ESQUERDA:
			//return _get(coluna, (colunas -1) - linha);
			return _get((linhas - 1) - coluna, linha);
			
		case DirecaoCalculo.SUL:
			//return _get(coluna, (colunas -1) - linha);
			return _get((linhas - 1) - linha, (colunas - 1) - coluna);
			
		case DirecaoCalculo.DIREITA:
			return _get(coluna, (colunas - 1) - linha);
		}
		
		throw new RuntimeException("A direção fornecida não é suportada!");
	}
	
	
	public void set(int linha, int coluna, boolean valor) {
		// A direção padrão é o NORTE, para as demais direções
		// vamos relizar uma rotação do reticulado.
		
		switch (direcao) {
		case DirecaoCalculo.NORTE:
			 _set(linha + deslocamentoLinha, coluna + deslocamentoColuna, valor);
			 return;
			
		case DirecaoCalculo.ESQUERDA:
			_set((linhas - 1) - coluna, linha, valor);
			return;
			
		case DirecaoCalculo.SUL:
			//return _get(coluna, (colunas -1) - linha);
			_set((linhas - 1) - linha, (colunas - 1) - coluna, valor);
			return;
			
		case DirecaoCalculo.DIREITA:
			_set(coluna, (colunas - 1) - linha, valor);
			return;
		}
		
		throw new RuntimeException("A direção fornecida não é suportada!");
	}
	
	/**
	 * Configura o bit do reticulado para a linha e coluna fornecidos.
	 * @param linha Linha.
	 * @param coluna Coluna.
	 * @param valor Valor a ser configurado no reticulado.
	 */
	private void _set(int linha, int coluna, boolean valor) {
		linha = (linha >= 0 ? linha % linhas : ((linha % linhas) + linhas) % linhas);
		
		coluna = (coluna >= 0 ? coluna % colunas : ((coluna % colunas) + colunas) % colunas);
		
		reticulado[linha][coluna] = valor;
	}
	
	public final int getIndiceLinha(int linha) {
		return Util.getIndice(linhas, linha + deslocamentoLinha);
	}
	
	public final int getIndiceColuna(int coluna) {
		return Util.getIndice(colunas, coluna + deslocamentoColuna);
	}
	
	/**
	 * Retorna quatidade de linhas do reticulado.
	 * @return Qtd de linhas.
	 */
	public int getLinhas() {
		return getLinhas(direcao);
	}
	
	/**
	 * Retorna a quantidade colunas do reticulado.
	 * @return Qtd colunas.
	 */
	public int getColunas() {
		return getColunas(direcao);
	}

	/**
	 * Retorna o total de linhas do reticulado a partir da direção fornecida.
	 * @param direcao Direção de calculo do reticulado.
	 * @return Qtd. de linhas.
	 */
	public int getLinhas(int direcao) {
		switch (direcao) {
		case DirecaoCalculo.ESQUERDA:
		case DirecaoCalculo.DIREITA:
			return colunas;
		}
		
		return linhas;
	}
	
	/**
	 * Retorna o total de colunas do reticulado a partir da direção fornecida.
	 * @param direcao Direção de calculo do reticulado.
	 * @return Qtd. de colunas.
	 */
	public int getColunas(int direcao) {
		switch (direcao) {
		case DirecaoCalculo.ESQUERDA:
		case DirecaoCalculo.DIREITA:
			return linhas;
		}
		
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
		return aplicarRuido(15, 6);
	}
	
	public Reticulado aplicarRuido(int linha, int coluna) {
		Reticulado clone = clone();
		
		clone.set(linha, coluna, !clone._get(linha, coluna));
		
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
				
				xor.set(i, j, reticulado._get(i, j) ^ _get(i, j));
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
				indice |= Util.toInt(_get(linha + i, coluna + j));
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
		
		final double maximoOcorrencias = (int) Math.pow(2, qtdLinhasJanela * qtdColunasJanela);
		
		for (Integer ocorrencia : ocorrencias.values()) {
			double tmp = ((double)ocorrencia / (double) maximoOcorrencias); 
			
			entropia += tmp * (Math.log(tmp) / Math.log(2));
		}

		entropia = -entropia;
		
		entropia = entropia / (double) (qtdLinhasJanela * qtdColunasJanela);
		
		//System.out.println(entropia);
		return entropia;
	}
	
	/**
	 * Retorna a menor entropia das colunas.
	 * @return Menor entropia normalizada.
	 */
	public double getMenorEntropiaColuna() {
		double menorEntropia = Double.MAX_VALUE;
		
		for (int i = 0; i < colunas; i++) {
			double entropia = Util.entropiaMatrizNormalizada(reticulado, Util.ENTROPIA_COLUNA, -1, i);
			
			/*if (entropia == 0.0) {
				System.out.println("ENTROPIA ZEROOOO=>" + i);
			}*/
			
			if (entropia < menorEntropia) {
				menorEntropia = entropia;
			}
		}
		
		return menorEntropia;
	}
	
	/**
	 * Retorna a menor entropia das linhas.
	 * @return Menor entropia normalizada.
	 */
	public double getMenorEntropiaLinha() {
		double menorEntropia = Double.MAX_VALUE;
		
		for (int i = 0; i < colunas; i++) {
			double entropia = Util.entropiaMatrizNormalizada(reticulado, Util.ENTROPIA_LINHA, i, -1);
			
			if (entropia < menorEntropia) {
				menorEntropia = entropia;
			}
		}
		
		return menorEntropia;
		
	}
	
	/*public void setDeslocamentoLinha(int deslocamentoLinha) {
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
*/	
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
				if (_get(i, j) != ret._get(i, j))
					return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) {

		Reticulado r = new Reticulado(new String[]{
				"0000000000000000000000000000000000000000",
				"0000000000000000000000000000000000000000",
				"                                    0000",
				"                                    0000",
				"         000000000000               0000",
				"         0          0               0000",
				"         0          0               0000",
				"         0          0               0000",
				"         0          0               0000",
				"         000000000000               0000",
				"         0          0               0000",
				"         0          0               0000",
				"         0          0               0000",
				"         0          0               0000",
				"                                    0000",
				"                                    0000"
		});
		
		System.out.println(r);
		System.out.println();
		
		r.direcao = DirecaoCalculo.DIREITA;
		
		
		for (int i = 0; i < r.getLinhas(); i++) {
			for (int j = 0; j < r.getColunas(); j++) {
				System.out.print(r.get(i, j) ? "_" : "0");
			}
			System.out.println();
		}
	}
}
