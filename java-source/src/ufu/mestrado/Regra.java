package ufu.mestrado;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Regra {
	/** Cache de regras principais. */
	//private static final Map<String, Regra> cacheRegras = new HashMap<String, Regra>();
	
	/** Cache de regras de contorno. A chave do mapa � o n�cleo da regra principal. */
	private static final Map<String, Regra> cacheRegrasContorno = new HashMap<String, Regra>();
	
	/** Transi��es. */
	private Transicao transicoes[];
	
	/** Raio. */
	public int raio;
	
	/** Dire��o de c�lculo da regra. */
	public int direcaoCalculo;
	
	/** N�cleo utilizado na constru��o da regra. */
	private String nucleo;
	
	/** Tamanho do n�cleo, auxilia na obten��o da transi��o quando a regra foi rotacionada. */
	public int tamanhoNucleo;
	
	/** 
	 * Quantidade de rota��es executadas na regra. 
	 * Se positiva indica que a regra foi rotacionada n vezes para direita, se negativa, indica que a regra foi 
	 * rotacionada Math.abs(n) vezes para esquerda. 
	 */
	public int rotacaoNucleo = 0;

	public Regra(int raio, int direcaoCalculo) {
		this.transicoes = new Transicao[(int) Math.pow(2, raio * 4 + 1)];//new HashMap<Integer, Transicao>();
		this.raio = raio;
		this.direcaoCalculo = direcaoCalculo;
		this.tamanhoNucleo = transicoes.length / 2;
	}
	
	/**
	 * O total de transi��es adicionadas � regra.
	 * @return N�mero de transi��es adicionadas.
	 */
	public int getTotalTransicoes() {
		return transicoes.length;
	}
	
	public void adicionarTransicao(Transicao transicao) {
		transicoes[transicao.indice] = transicao;
	}
	
	public final Transicao getTransicao(int indice) {
		if (rotacaoNucleo == 0/* || true*/) {
			// Nenhuma rotacao na regra.
			return transicoes[indice];

		}
		
		int indiceNucleo = indice;
		
		// A transi��o pertence a parte superior da regra?
		if (indice >= tamanhoNucleo) {
			indiceNucleo = indice - tamanhoNucleo;
		} 
		
		indiceNucleo = Util.getIndice(tamanhoNucleo, indiceNucleo + rotacaoNucleo);
		
		if (indice >= tamanhoNucleo) {
			// Ajustando o que foi retirado...
			indiceNucleo += tamanhoNucleo;
		}
		
		// Rotocionou a regra para direita ou esquerda.
		return transicoes[indiceNucleo];
	}
	
	/**
	 * Cria uma regra a partir da string fornecida.
	 * @param valores Valores da transi��es da regra (0 = false, 1 = true).
	 * @param direcaoCalculo Dire��o de c�lculo da regra.
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
	 * @param valores Valores da transi��es da regra.
	 * @param direcaoCalculo Dire��o de c�lculo da regra.
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
		
		return regraContorno;
	}
	
	/**
	 * Retorna o tamanho m�ximo para o raio fornecido.
	 * @param raio Raio.
	 * @return Tamanho m�ximo da regra.
	 */
	public static int getTamanhoMaximoRegra(int raio) {
		int tamanho = raio * 4 + 1;
		
		return (int) Math.pow(2, tamanho);
	}
	
	/**
	 * Carrega as regras a partir do arquivo fornecido.
	 * @param nomeArquivo Nome do arquivo.
	 * @return Regras
	 * @throws Exception
	 */
	public static List<Regra> carregarRegras(String nomeArquivo) throws Exception {
		return carregarRegras(nomeArquivo, DirecaoCalculo.NORTE);
	}
	
	public static List<Regra> carregarRegras(String nomeArquivo, int direcao) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(nomeArquivo));

		List<Regra> regras = new ArrayList<Regra>();
		
		while (true) {
			String linha = reader.readLine();
			
			// Final de arquivo?
			if (linha == null) {
				break;
			}
			
			if (!linha.startsWith("#")) {
				Regra regra = Regra.criarAPatirNucleo(linha, direcao);
				regras.add(regra);
			}
		}
		
		return regras;
	}

	/**
	 * Retorna o n�cleo da regra.
	 * @return N�cleo da regra.
	 */
	public String getNucleo() {
		if (nucleo == null) {
			throw new RuntimeException("N�o implementado!");
		}
		
		return nucleo;
	}
	
	/**
	 * Calcula a entropia normalizada do n�cleo da regra.
	 * @return O valor da entropia do n�cleo da regra.
	 */
	public double entropia() {
		return Util.entropiaNormalizada(Util.getArray(nucleo));
	}
	
	/**
	 * Gera um novo n�cleo a partir de uma outra serializa��o da regra;
	 * @return Novo nucleo.
	 */
	public String converterNucleo() {
		byte novoNucleo[] = nucleo.getBytes();
		byte nucleo[] = this.nucleo.getBytes();

		int tamNucleo = this.nucleo.length();
		
		int indices[] = {0, 11, 12, 13, 14, 15, 10, 9, 8, 7, 6, 5, 1, 2, 3, 4};
		
		// Preserva o primeiro bit, que � o da sensitividade
		// depois inverte os demais bits.
		
		for (int i = 1; i < tamNucleo; i++) {
			novoNucleo[i] = nucleo[indices[i]];
		}
		
		return new String(novoNucleo);
	}

	/**
	 * Cria uma nova regra com ruido a partir da regra original.
	 * @return Regra com ru�do.
	 */
	public Regra aplicarRuido() {
		
		int indiceRuido = new Random().nextInt(nucleo.length());
		
		byte buffer[] = nucleo.getBytes();
		
		buffer[indiceRuido] = (byte)(buffer[indiceRuido] == '0' ? '1' : '0');
		
		String novoNucleo = new String(buffer);
		
		return Regra.criarAPatirNucleo(novoNucleo, direcaoCalculo);
	}
	
	/**
	 * Retorna a regra de contorno a partir da regra principal fornecida.
	 * @param regraPrincipal Regra principal.
	 * @return A regra de contorno para regra principal fornecida.
	 */
	public static final Regra getRegraContorno(Regra regraPrincipal) {
		Regra regraContorno = cacheRegrasContorno.get(regraPrincipal.getNucleo());
		
		if (regraContorno == null) {
			regraContorno = criarRegraContorno(regraPrincipal);
			cacheRegrasContorno.put(regraPrincipal.getNucleo(), regraContorno);
		}
		
		return regraContorno;
	}
	
	@Override
	public String toString() {
		return getNucleo() + ":" + direcaoCalculo;
	}
}
