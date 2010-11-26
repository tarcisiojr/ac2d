package ufu.mestrado;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Regra {
	/** Cache de regras principais. */
	//private static final Map<String, Regra> cacheRegras = new HashMap<String, Regra>();
	
	/** Cache de regras de contorno. A chave do mapa é o núcleo da regra principal. */
	private static final Map<String, Regra> cacheRegrasContorno = new HashMap<String, Regra>();
	
	/** Transições. */
	private Transicao transicoes[];
	
	/** Raio. */
	public int raio;
	
	/** Direção de cálculo da regra. */
	public int direcaoCalculo;
	
	/** Núcleo utilizado na construção da regra. */
	private String nucleo;
	
	/** Tamanho do núcleo, auxilia na obtenção da transição quando a regra foi rotacionada. */
	public int tamanhoNucleo;
	
	/** 
	 * Quantidade de rotações executadas na regra. 
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
	 * O total de transições adicionadas à regra.
	 * @return Número de transições adicionadas.
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
		
		// A transição pertence a parte superior da regra?
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
			
			if (!linha.startsWith("#") && linha.length() > 0) {
				Regra regra = Regra.criarAPatirNucleo(linha, direcao);
				regras.add(regra);
			}
		}
		
		return regras;
	}

	/**
	 * Retorna o núcleo da regra.
	 * @return Núcleo da regra.
	 */
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

	/**
	 * Cria uma nova regra com ruido a partir da regra original.
	 * @return Regra com ruído.
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
	
	/**
	 * Gerar um arquivo de núcleos aleatorios.
	 * @param raio Tamanho do raio.
	 * @param quantidade Quantidade de regras a serem geradas.
	 * @param nomeArquivo Nome do arquivo que será salvo as regras.
	 */
	public static final void gerarNucleosAleatoriamente(int raio, int quantidade, String nomeArquivo) throws Exception {
		FileOutputStream fos = new FileOutputStream(nomeArquivo);

		final int tamanhoNucleo = (int) Math.pow(2, raio * 4 + 1) / 2;
		
		Set<String> invalidarRepetidos = new HashSet<String>();
		
		Random r =  new Random();
		 
		while (quantidade-- > 0) {
			String nucleo = Util.toString(Util.gerarArrayAleatorio(r, tamanhoNucleo));
			
			if (!invalidarRepetidos.contains(nucleo)) {
				invalidarRepetidos.add(nucleo);
				
				fos.write((nucleo + "\n").getBytes());
			} else {
				quantidade++;
			}
		}
		
		fos.close();
	}
	/**
	 * Gerar um arquivo de núcleos aleatorios.
	 * @param raio Tamanho do raio.
	 * @param quantidade Quantidade de regras a serem geradas.
	 * @param nomeArquivo Nome do arquivo que será salvo as regras.
	 */
	public static final void gerarNucleosAleatoriamente(int raio, int quantidade, String nomeArquivo, 
			Double entropiaMin, Double entropiaMax, double percentualZeros) throws Exception {
		FileOutputStream fos = new FileOutputStream(nomeArquivo);

		final int tamanhoNucleo = (int) Math.pow(2, raio * 4 + 1) / 2;
		
		Set<String> invalidarRepetidos = new HashSet<String>();
		
		//Random r =  new Random();
		 
		while (quantidade-- > 0) {
			Boolean bufferA[] = new Boolean[(int)(tamanhoNucleo * percentualZeros)];
			Boolean bufferB[] = new Boolean[tamanhoNucleo - bufferA.length];
			
			Arrays.fill(bufferA, true);
			
			Boolean buffer[] = new Boolean[tamanhoNucleo];
			System.arraycopy(bufferA, 0, buffer, 0, bufferA.length);
			System.arraycopy(bufferB, 0, buffer, bufferA.length, bufferB.length);
			
			List<?> lst = Arrays.asList(buffer); 
			Collections.shuffle(lst);
			
			boolean n[] = new boolean[buffer.length];
			
			for (int i = 0; i < buffer.length; i++) {
				n[i] = buffer[i] == null ? false : buffer[i];
			}
			
			double entropia = Util.entropiaNormalizada(n);
			
			System.out.println(entropia);
			
			if (entropiaMax != null && (entropia >= entropiaMax || entropia < entropiaMin)) {
				quantidade++;
				continue;
			}
			
			String nucleo = Util.toString(n);
			
			if (!invalidarRepetidos.contains(nucleo)) {
				invalidarRepetidos.add(nucleo);
				
				fos.write((nucleo + "\n").getBytes());
			} else {
				quantidade++;
			}
		}
		
		fos.close();
	}
	
	public static void main(String[] args) throws Exception {
		gerarNucleosAleatoriamente(2, 1000, "c:/temp/regras_raio_2.txt");
	}
	
	public void imprimir() {
		StringBuilder builder = new StringBuilder();
		
		String zeros = Integer.toBinaryString(transicoes.length).substring(1);
		
		for (int i = 0; i < transicoes.length; i++) {
			String vizinhanca = Integer.toBinaryString(i);
			vizinhanca = zeros.substring(0, zeros.length() - vizinhanca.length()) + vizinhanca;
			
			System.out.println(vizinhanca.replaceAll("([0-9])", "$1\t") + "->\t" + Util.toInt(transicoes[i].valor));
		}
		
		System.out.println(builder.toString());
	}
}
