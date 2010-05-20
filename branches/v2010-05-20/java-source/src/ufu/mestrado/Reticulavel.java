package ufu.mestrado;

public interface Reticulavel {

	/**
	 * Configura o valor da célular especificada.
	 * @param linha Linha.
	 * @param coluna Coluna.
	 * @param valor Novo valor da célula.
	 */
	public void set(int linha, int coluna, boolean valor);
	
	/**
	 * Obtém o valor de uma célula do reticulado.
	 * @param linha Linha.
	 * @param coluna Coluna.
	 * @return Valor da célula.
	 */
	public boolean get(int linha, int coluna); 
	
	/**
	 * Retorna o total de linhas do reticulado.
	 * @return O total de linhas.
	 */
	public int getLinhas();
	
	/**
	 * Retorna o total de colunas.
	 * @return Total de colunas.
	 */
	public int getColunas();
}
