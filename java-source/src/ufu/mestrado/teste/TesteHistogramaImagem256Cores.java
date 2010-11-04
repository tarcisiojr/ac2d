package ufu.mestrado.teste;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Util;
import ufu.mestrado.imagem.CifradorImagemColorida256Cores;

public class TesteHistogramaImagem256Cores {
	// Filtro de imagens
	private static final FilenameFilter FILTRO_IMAGENS = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toUpperCase().endsWith("PNG") || name.toUpperCase().endsWith("JPG");
			}
		};

	public static void main(String[] args) throws Exception {
		// Regras
		//String NOME_ARQ_REGRAS 				= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/regras.txt";
		
		// Imagens
		String DIR_IMAGENS 				  	= "D:/desktop/mestrado/testes_ac2d/teste_histogramas/teste_gina/imagens/";
		String DIR_SAIDA_IMAGENS 		  	= "D:/desktop/mestrado/testes_ac2d/teste_histogramas/teste_gina/imagens_decifradas/";
		String DIR_SAIDA_IMAGENS_CIFRADAS 	= "D:/desktop/mestrado/testes_ac2d/teste_histogramas/teste_gina/imagens_cifradas/";
		String NOME_ARQ_REGRAS 				= "D:/desktop/mestrado/testes_ac2d/teste_histogramas/teste_gina/regras.txt";
		
		//String DIR_IMAGENS 				  	= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/imagens/";
		//String DIR_SAIDA_IMAGENS 		  	= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/imagens_decifradas/";
		//String DIR_SAIDA_IMAGENS_CIFRADAS 	= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/imagens_cifradas/";
		
		if (args.length == 4) {
			NOME_ARQ_REGRAS = args[0];
			DIR_IMAGENS = args[1];
			DIR_SAIDA_IMAGENS = args[2];
			DIR_SAIDA_IMAGENS_CIFRADAS = args[3];
		}
		// Indica se é para imprimir os histogramas
		boolean PRINT_HISTOGRAMAS = true;
		
		// Config AC.
		boolean DESLOCAR_RETICULADO = true;
		boolean ROTACIONAR_RETICULADO = true;
		int QTD_PRE_IMAGENS = 30;
		
		final NumberFormat  nf = DecimalFormat.getInstance(new Locale("pt", "BR"));
		nf.setMaximumFractionDigits(6);
		nf.setMinimumFractionDigits(6);
		
		File dirImagens = new File(DIR_IMAGENS);
		
		File arqImagens[] = dirImagens.listFiles(FILTRO_IMAGENS);
		
		List<Regra> regras = Regra.carregarRegras(NOME_ARQ_REGRAS, DirecaoCalculo.NORTE);
		
		System.out.println("Imagem\tRegra\tEntropia_regra\tEntrada\tCifrada\tSaida");
		
		for (Regra regra : regras) {
			for (File arquivo : arqImagens) {
				CifradorImagemColorida256Cores cifrador = new CifradorImagemColorida256Cores(
						DIR_IMAGENS + arquivo.getName(), 
						regra.getNucleo(), 
						regra.direcaoCalculo);
				
				AutomatoCelular ac = new AutomatoCelular(cifrador);
				
				double media = cifrador.buffer.getWidth() * cifrador.buffer.getHeight() / 256;
				
				//Cronometro.iniciar();
				
				ac.deslocarReticulado = DESLOCAR_RETICULADO;
				ac.rotacionarReticulado = ROTACIONAR_RETICULADO;
				
				double desvioPadraoEntrada = Util.desvioPadrao(cifrador.histograma, media);
				
				if (PRINT_HISTOGRAMAS) { imprimir("Original", cifrador.histograma); }
				
				ac.calcularPreImage(QTD_PRE_IMAGENS);
				
				cifrador.criarImagem(DIR_SAIDA_IMAGENS_CIFRADAS + arquivo.getName() + "_" + regra.getNucleo() + ".png");
				
				double desvioPadraoCifrada = Util.desvioPadrao(cifrador.histograma, media);
				
				if (PRINT_HISTOGRAMAS) { imprimir("Cifrada", cifrador.histograma); }
				
				ac.evoluir(QTD_PRE_IMAGENS);
				
				cifrador.criarImagem(DIR_SAIDA_IMAGENS + arquivo.getName() + "_" + regra.getNucleo() + ".png");
				
				double desvioPadraoSaida = Util.desvioPadrao(cifrador.histograma, media);
				
				if (PRINT_HISTOGRAMAS) { imprimir("Decifrada", cifrador.histograma); }
				
				double entropiaRegra = Util.entropiaNormalizada(Util.getArray(regra.getNucleo()));
				
				System.out.print(arquivo.getName() + "\t");
				System.out.print("[" + regra.getNucleo() + "]\t");
				System.out.print(nf.format(entropiaRegra) + "\t");
				System.out.print(nf.format(desvioPadraoEntrada) + "\t");
				System.out.print(nf.format(desvioPadraoCifrada) + "\t");
				System.out.print(nf.format(desvioPadraoSaida) + "\t");
				System.out.println();
			}
		}
	}
	
	public static final void imprimir(String cabecalho, double histograma[]) {
		System.out.println("=====> " + cabecalho);
		for (int i = 0; i < histograma.length; i++) {
			System.out.println((int) histograma[i]);
		}
		System.out.println("=====");
		System.out.println();
	}
}
