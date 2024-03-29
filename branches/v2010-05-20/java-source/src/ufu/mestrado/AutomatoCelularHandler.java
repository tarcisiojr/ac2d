package ufu.mestrado;

public interface AutomatoCelularHandler {

	public Reticulado getReticuladoInicial();
	
	public Regra getRegraPrincipal();
	
	//public Regra getRegraContorno();
	
	public void antesCalcularPreImagem();
	
	public void aposCalcularPreImagem(Reticulado preImagem);
	
	public void aposCriarReticuladoPreImagem(Reticulado preImagem);
	
	public void aposBuscaTransicoes(int indices[]);
	
	public void aposTransicaoEscolhida(int indice);
	
	public void antesCriarReticuladoPreImagem(Reticulado preImagem);
	
	public void aposSetBitReticulado(Reticulado reticulado, int linha, int coluna, boolean preImagem);
	
	public void aposGetBitReticulado(Reticulado reticulado, int linha, int coluna);
	
	//public void aposSetBitPreImagem(Reticulado preImagem, int linha, int coluna);
	
	public void setAutomatoCelular(AutomatoCelular ac);
}
