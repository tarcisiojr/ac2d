package ufu.mestrado;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Regra {

	//private Map<Integer, Transicao> transicoes;
	public Transicao transicoes[];
	public int raio;
	public int direcaoCalculo;
	private String nucleo;

	public Regra(int raio, int direcaoCalculo) {
		this.transicoes = new Transicao[(int) Math.pow(2, raio * 4 + 1)];//new HashMap<Integer, Transicao>();
		this.raio = raio;
		this.direcaoCalculo = direcaoCalculo;
	}
	
	/**
	 * Retorna o raio da regra.
	 * @return O raio da regra.
	 */
	/*public int getRaio() {
		return raio;
	}*/

	/**
	 * O total de transições adicionadas à regra.
	 * @return Número de transições adicionadas.
	 */
	public int getTotalTransicoes() {
//		return transicoes.size();
		return transicoes.length;
	}
	
	/*public Transicao getTransicao(int indice) {
//		return transicoes.get(indice);
		return transicoes[indice];
	}*/
	
	/*public Map<Integer, Transicao> getTransicoes() {
		return transicoes;
	}*/
	
	public void adicionarTransicao(Transicao transicao) {
		//transicoes.put(transicao.getIndice(), transicao);
		transicoes[transicao.indice] = transicao;
	}
	
	/**
	 * Cria uma regra a partir da string fornecida.
	 * @param valores Valores da transições da regra (0 = false, 1 = true).
	 * @param direcaoCalculo Direção de cálculo da regra.
	 * @return
	 */
	public static Regra criar(String valores, int direcaoCalculo) {
		return Regra.criar(Util.getArray(valores), direcaoCalculo);
	}
	
	public static Regra criarAPatirNucleo(String nucleo, int direcaoCalculo) {
		boolean regra[] = new boolean[nucleo.length() * 2];
		
		System.arraycopy(Util.getArray(nucleo), 0, regra, 0, nucleo.length());
		
		for (int i = nucleo.length(); i < regra.length; i++) {
			regra[i] = !regra[i - nucleo.length()];
		}

		Regra novaRegra = Regra.criar(regra, direcaoCalculo); 
		
		novaRegra.nucleo = nucleo;
		
		return novaRegra;
	}
	
	/**
	 * Cria uma regra a partir da string fornecida.
	 * @param valores Valores da transições da regra.
	 * @param direcaoCalculo Direção de cálculo da regra.
	 * @return
	 */
	public static Regra criar(boolean valores[], int direcaoCalculo) {
		double exp = (Math.log(valores.length) / Math.log(2));
		int raio = (int) ((exp - 1) / 4); 
		
		Regra regra = new Regra(raio, direcaoCalculo);
		
		for (int i = 0; i < valores.length; i++) {
			Transicao t = new Transicao(i, regra.raio, direcaoCalculo, valores[i]);
			regra.adicionarTransicao(t);
		}
		
		return regra;
	}
	
	/**
	 * Cria uma regra de contorno a partir da regra principal fornecida.
	 * @param regraPrincipal Regra principal.
	 * @return Regra de contorno.
	 */
	public static Regra criarRegraContorno(Regra regraPrincipal) {
		Regra regraContorno = new Regra(regraPrincipal.raio, regraPrincipal.direcaoCalculo);
		
		Transicao priTransicao = regraPrincipal.transicoes[0];
		
		boolean valor = priTransicao.valor;
		int raio = regraPrincipal.raio;
		
		for (Transicao t : regraPrincipal.transicoes) {
			boolean novoValor = valor;
			
			if (!t.getPrimeiroBit(raio)) {
				novoValor = !valor;
			}
			
			Transicao transicao = new Transicao(t.indice, raio, regraPrincipal.direcaoCalculo, novoValor);
			
			regraContorno.adicionarTransicao(transicao);
		}
		
		/*for (Entry<Integer, Transicao> entry : regraPrincipal.getTransicoes().entrySet()) {
			
			boolean novoValor = valor;
			
			if (!entry.getValue().getPrimeiroBit(raio)) {
				novoValor = !valor;
			}
			
			Transicao transicao = new Transicao(entry.getKey(), raio, regraPrincipal.getDirecaoCalculo(), novoValor);
			
			regraContorno.adicionarTransicao(transicao);
		}*/
		
		return regraContorno;
	}
	
	/**
	 * Retorna o tamanho máximo para o raio fornecido.
	 * @param raio Raio.
	 * @return Tamanho máximo da regra.
	 */
	public static int getTamanhoMaximoRegra(int raio) {
		int tamanho = raio * 4 + 1;
		
		return (int) Math.pow(2, tamanho);
	}
	
	/**
	 * Retorna a direção do cálculo da regra.
	 * @return Direção do cálculo.
	 */
	/*public int getDirecaoCalculo() {
		return direcaoCalculo;
	}*/
	
	
	/**
	 * Carrega as regras a partir do arquivo fornecido.
	 * @param nomeArquivo Nome do arquivo.
	 * @return Regras
	 * @throws Exception
	 */
	public static List<Regra> carregarRegras(String nomeArquivo) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));

		List<Regra> regras = new ArrayList<Regra>();
		
		while (true) {
			String linha = reader.readLine();
			
			// Final de arquivo?
			if (linha == null) {
				break;
			}
			
			if (!linha.startsWith("#")) {
				Regra regra = Regra.criarAPatirNucleo(linha, DirecaoCalculo.NORTE);
				regras.add(regra);
			}
		}
		
		return regras;
	}

	public String getNucleo() {
		if (nucleo == null) {
			throw new RuntimeException("Não implementado!");
		}
		
		return nucleo;
	}
	
	/**
	 * Calcula a entropia normalizada do núcleo da regra.
	 * @return O valor da entropia do núcleo da regra.
	 */
	public double entropia() {
		return Util.entropiaNormalizada(Util.getArray(nucleo));
	}
	
	/**
	 * Gera um novo núcleo a partir de uma outra serialização da regra;
	 * @return Novo nucleo.
	 */
	public String converterNucleo() {
		byte novoNucleo[] = nucleo.getBytes();
		byte nucleo[] = this.nucleo.getBytes();

		int tamNucleo = this.nucleo.length();
		
		int indices[] = {0, 11, 12, 13, 14, 15, 10, 9, 8, 7, 6, 5, 1, 2, 3, 4};
		
		// Preserva o primeiro bit, que é o da sensitividade
		// depois inverte os demais bits.
		
		for (int i = 1; i < tamNucleo; i++) {
			novoNucleo[i] = nucleo[indices[i]];
		}
		
		return new String(novoNucleo);
	}
	
	
	@Override
	public String toString() {
		return getNucleo() + ":" + direcaoCalculo;
	}
}
