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

public class TesteHistogramaImagemEscalaCinza {
	// Filtro de imagens
	private static final FilenameFilter FILTRO_IMAGENS = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toUpperCase().endsWith("PNG") || name.toUpperCase().endsWith("JPG");
			}
		};

	public static void main(String[] args) throws Exception {
		// Regras
		String NOME_ARQ_REGRAS 				= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/regras.txt";
		
		// Imagens
		String DIR_IMAGENS 				  	= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/imagens/";
		String DIR_SAIDA_IMAGENS 		  	= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/imagens_decifradas/";
		String DIR_SAIDA_IMAGENS_CIFRADAS 	= "D:/desktop/mestrado/testes_ac2d/teste_histograma_preto_branco/imagens_cifradas/";
		
		// Config AC.
		boolean DESLOCAR_RETICULADO = false;
		boolean ROTACIONAR_RETICULADO = false;
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
				
				ac.calcularPreImage(QTD_PRE_IMAGENS);
				
				cifrador.criarImagem(DIR_SAIDA_IMAGENS_CIFRADAS + arquivo.getName() + "_" + regra.getNucleo() + ".png");
				
				double desvioPadraoCifrada = Util.desvioPadrao(cifrador.histograma, media);
				
				ac.evoluir(QTD_PRE_IMAGENS);
				
				cifrador.criarImagem(DIR_SAIDA_IMAGENS + arquivo.getName() + "_" + regra.getNucleo() + ".png");
				
				double desvioPadraoSaida = Util.desvioPadrao(cifrador.histograma, media);
				
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
}
