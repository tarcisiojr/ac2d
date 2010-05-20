package ufu.mestrado.teste;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.Util;

public class TesteEntropia {
	public static void escreverLog(FileWriter fw, String log, boolean quebraLinha) throws Exception {
		fw.write(log);
		
		if (quebraLinha)
			fw.write("\n");
		else
			fw.write("\t");
			
	}
	
	public static void escreverLog(FileWriter fw, String log) throws Exception {
		escreverLog(fw, log, false);
	}
	
	public static void testar(int numeroPI, String nomeArquivoReticulados, String nomeArquivoRegras) throws Exception {
//		List<Reticulado> reticulados = Reticulado.carregarReticulados("reticulados16x16.txt");
//		List<Regra> regras = Regra.carregarRegras("regras.txt");
		
//		List<Reticulado> reticulados = Reticulado.carregarReticulados("reticulados_1000_16x16.txt");
//		List<Reticulado> reticulados = Reticulado.carregarReticulados("reticulados_1000_16x16_entropia0.5.txt");
		List<Reticulado> reticulados = Reticulado.carregarReticulados(nomeArquivoReticulados);
//		List<Regra> regras = Regra.carregarRegras("regras_1000_1.txt");
		List<Regra> regras = Regra.carregarRegras(nomeArquivoRegras);
		
		int numerosCalculosPI = numeroPI;
		int linhasJanela = 2;
		int colunasJanela = 4;
		
		String nomeArquivo = "result_" + nomeArquivoRegras + "_" + numerosCalculosPI + "_" + nomeArquivoReticulados;
		
		NumberFormat  nf = DecimalFormat.getInstance();
		nf.setMaximumFractionDigits(3);
		nf.setMinimumFractionDigits(3);
		
		FileWriter fw = new FileWriter(nomeArquivo);
		
		escreverLog(fw, "Nro pre-imagens: " + numerosCalculosPI, true);
		escreverLog(fw, "Janela entropia: " + linhasJanela + "x" + colunasJanela, true);
		escreverLog(fw, "Regras versão 2", true);
		
		escreverLog(fw, "regra");
		escreverLog(fw, "entropia_nucleo_regra");
		escreverLog(fw, "entropia_maxima");
		escreverLog(fw, "entropia_minima");
		escreverLog(fw, "entropia_media");
		escreverLog(fw, "entropia_desvio_padrao");
		escreverLog(fw, "perc0_maximo");
		escreverLog(fw, "perc0_minimo");
		escreverLog(fw, "perc0_medio");
		escreverLog(fw, "perc0_desvio_padrao");
		escreverLog(fw, "regra_alterada");
		escreverLog(fw, "entropia_regra_alterada", true);
		
		int indiceRegra = 0;
		
		//int metade = regras.size() / 2;
		
		//List<Regra> subLstRegra1 = regras.subList(0, metade);
		//List<Regra> subLstRegra2 = regras.subList(metade, regras.size());
		
		for (Regra regra : regras) {
			indiceRegra++;
			
			System.out.println(String.format("%05d", indiceRegra) + "->" + regra);
			
			escreverLog(fw, regra.toString());
			
			double entropiaMaxima = Double.MIN_VALUE;
			double entropiaMinima = Double.MAX_VALUE;
			double entropiaMedia = 0;
			
			int totalReticulados = reticulados.size();
			
			double percZerosMaximo = Double.MIN_VALUE;
			double percZerosMinimo = Double.MAX_VALUE;
			double percZerosMedia = 0;
			
			double entropias[] = new double[totalReticulados];
			double percZeros[] = new double[totalReticulados];
			
			int indice = 0;
			
			for (Reticulado reticulado : reticulados) {
				Reticulado retInicial = reticulado.clone();
				Reticulado retRuido = retInicial.aplicarRuido();
				
				AutomatoCelular ac = new AutomatoCelular(retInicial, regra);
				Reticulado preImagemA = ac.calcularPreImage(numerosCalculosPI);
				
				
				ac.setReticulado(retRuido);
				Reticulado preImagemB = ac.calcularPreImage(numerosCalculosPI);
				
				
				Reticulado xor = preImagemA.xor(preImagemB);
				double entropia = xor.entropia(linhasJanela, colunasJanela);
				
				if (entropia > entropiaMaxima)
					entropiaMaxima = entropia;
				
				if (entropia < entropiaMinima)
					entropiaMinima = entropia;
				
				entropiaMedia += entropia;
				
				entropias[indice] = entropia;
				
				double percentualZeros = xor.getPercentualZeros();
				
				if (percentualZeros > percZerosMaximo)
					percZerosMaximo = percentualZeros;
				
				if (percentualZeros < percZerosMinimo)
					percZerosMinimo = percentualZeros;
				
				percZerosMedia += percentualZeros;
				
				percZeros[indice] = percentualZeros;
				
				indice++;
			}
			
			entropiaMedia = entropiaMedia / totalReticulados;
			percZerosMedia = percZerosMedia / totalReticulados;

			escreverLog(fw, nf.format(regra.entropia()));
			
			// Escrevendo os dados do núcleo convertido.
			String strNucleoConvertido = regra.converterNucleo();
			boolean nucleoConvertido[] = Util.getArray(strNucleoConvertido);
			escreverLog(fw, strNucleoConvertido);
			escreverLog(fw, nf.format(Util.entropiaNormalizada(nucleoConvertido)));
			
			escreverLog(fw, nf.format(entropiaMaxima));
			escreverLog(fw, nf.format(entropiaMinima));
			escreverLog(fw, nf.format(entropiaMedia));
			escreverLog(fw, nf.format(Util.desvioPadrao(entropias, entropiaMedia)));
			
			escreverLog(fw, nf.format(percZerosMaximo));
			escreverLog(fw, nf.format(percZerosMinimo));
			escreverLog(fw, nf.format(percZerosMedia));
			escreverLog(fw, nf.format(Util.desvioPadrao(percZeros, percZerosMedia)), true);

			
		}
		
		fw.close();
	}
	

	public static void main(String[] args) throws Exception {
		(new Thread() {
			@Override
			public void run() {
				try {
					testar(20, "reticulados_1000_16x16_entropia0.5.txt", "regras_1000_1_ver2.txt");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		(new Thread() {
			@Override
			public void run() {
				try {
					testar(20, "reticulados_1000_16x16.txt", "regras_1000_1_ver2.txt");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
		(new Thread() {
			@Override
			public void run() {
				try {
					testar(10, "reticulados_1000_16x16_entropia0.5.txt", "regras_1000_1_ver2.txt");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
		(new Thread() {
			@Override
			public void run() {
				try {
					testar(10, "reticulados_1000_16x16.txt", "regras_1000_1_ver2.txt");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
