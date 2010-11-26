package ufu.mestrado.teste;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.Util;
import ufu.mestrado.imagem.CifradorImagemColorida256Cores;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;

public class TesteSistematico1CifragemACImagemColorida256CoresPreImagem {
	
	public static void iniciar(String arqRegras, String strDirImagens, int tamLinhasJanela, int tamColunasJanela,
			int qtdPreImagem, int direcao, int linhaRuido, int colunaRuido, int inicio, int fim,
			boolean rotacionarSensitivdade, String nomeLog, int metade) throws Exception {
		
		File dirImagens = new File(strDirImagens);
		//File dirSaida = new File(strDirSaida);
		
		File arqImagens[] = dirImagens.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toUpperCase().endsWith("PNG");
			}
		});
		
		//arqImagens = new File[] {arqImagens[0]};
		
		
		System.out.println("=================================");
		System.out.println("T E S T E   S I S T E M A T I C O");
		System.out.println("=================================");
		
		List<Regra> regras = null;
		
		if (!"TODAS_REGRAS".equals(arqRegras)) {
			regras = Regra.carregarRegras(arqRegras);
		}
		
		System.out.println("Total de regras carregadas: " + (regras == null ? "TODAS" : "" +  regras.size()));
		System.out.println("Total de imagens carregadas: " + arqImagens.length);
		System.out.println("Janela utilizada no reticulado [linhas x coluna]: [" + tamLinhasJanela + " x " + tamColunasJanela + "]");
		System.out.println("Quantidade de pre-imagem: " + qtdPreImagem);
		System.out.println("Direcao do calculo: " + DirecaoCalculo.toString(direcao));
		System.out.println("Posicao ruido [linha x coluna]: [" + linhaRuido + " x " + colunaRuido + "]");
		System.out.println("Intervalo: " + inicio  + " ate " + fim);
		System.out.println("Rotacao sensitividade: " + rotacionarSensitivdade);
		
		System.out.println();
		System.out.print("Indice\t");
		//System.out.print("Nro_Regra\t");
		System.out.print("Regra\t");
		System.out.print("Entropia_Regra\t");
		
		System.out.print("Metade_Percentual_0s_Media_XOR\t");
		System.out.print("Metade_Desvio_Padrao_Percentual_0s_XOR\t");
		System.out.print("Metade_Percentual_0s_Maximo_XOR\t");
		System.out.print("Metade_Percentual_0s_Minimo_XOR\t");
		System.out.print("Metade_Gerou_Log\t");
		
		System.out.print("Percentual_0s_Media_XOR\t");
		System.out.print("Desvio_Padrao_Percentual_0s_XOR\t");
		System.out.print("Percentual_0s_Maximo_XOR\t");
		System.out.print("Percentual_0s_Minimo_XOR\t");
		System.out.print("Gerou_Log\t");
		
		System.out.print("Tempo_Gasto\n");
		
		NumberFormat  nf = DecimalFormat.getInstance(new Locale("pt", "BR"));
		nf.setMaximumFractionDigits(6);
		nf.setMinimumFractionDigits(6);
		
		final int totalImagens = arqImagens.length;
		
		if (inicio == -1) {
			inicio = 1;
		}
		
		final int REGRA_MAXIMA_RAIO_1 = 65536;
		
		if (fim == -1) {
			fim = regras != null ? regras.size() : REGRA_MAXIMA_RAIO_1;
			
		} else if (regras != null && fim > regras.size()) {
			fim = regras.size();
		}
		
		FileOutputStream fos = new FileOutputStream(nomeLog, true);
		PrintWriter writer = new PrintWriter(fos);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		
		writer.println("====================================");
		writer.println("Log iniciado em " + df.format(new Date()));
		writer.println("====================================");
		
		writer.print("Indice\t");
		//writer.print("Nro_Regra\t");
		writer.print("Regra\t");
		writer.print("Entropia_Regra\t");
		writer.print("Imagem\t");
		writer.print("Percentual_0s\t");
		writer.print("METADE_COMPLETO");
		writer.println();
		writer.flush();
		
		int metadeQtdPreimagens = metade;//qtdPreImagem / 2;
		
		for (int i = inicio -1; i < fim; i++) {
			Regra regra = null;
			
			if (regras != null) {
				regra = regras.get(i);
				
			} else {
				String nucleo = Integer.toBinaryString(i + REGRA_MAXIMA_RAIO_1);
				regra = Regra.criarAPatirNucleo(nucleo.substring(1), direcao);
			}
			
			//Regra regraRuido = regra.aplicarRuido();
			
			double percZerosMaximo = Double.MIN_VALUE;
			double percZerosMinimo = Double.MAX_VALUE;
			double percZerosMedia = 0;

			double metadePercZerosMaximo = Double.MIN_VALUE;
			double metadePercZerosMinimo = Double.MAX_VALUE;
			double metadePercZerosMedia = 0;
			
			double percZeros[] = new double[totalImagens];
			
			double metadePercZeros[] = new double[totalImagens];
			
			int indice = 0;
			
			boolean gerouLog = false;
			boolean metadeGerouLog = false;
			
			Cronometro.iniciar();
			for (File arq: arqImagens) {
				CifradorImagemColorida256Cores cifrador = new CifradorImagemColorida256Cores(arq.getAbsolutePath(), regra, direcao);
				
				Reticulado retInicial = cifrador.getReticuladoInicial();
				Reticulado retRuido = retInicial.aplicarRuido(linhaRuido, colunaRuido);
				
				AutomatoCelular ac = new AutomatoCelular(retInicial, regra);
				ac.rotacionarReticulado = rotacionarSensitivdade;
				
				Reticulado metadePreImagemA = ac.calcularPreImage(0, metadeQtdPreimagens);
				Reticulado preImagemA = ac.calcularPreImage(metadeQtdPreimagens, qtdPreImagem);
				
				ac.setReticulado(retRuido);
				ac.setRegra(regra);
				Reticulado metadePreImagemB = ac.calcularPreImage(0, metadeQtdPreimagens);
				Reticulado preImagemB = ac.calcularPreImage(metadeQtdPreimagens, qtdPreImagem);
				
				
				Reticulado metadeXor = metadePreImagemA.xor(metadePreImagemB);
				Reticulado xor = preImagemA.xor(preImagemB);
				
				xor.contabiliarZeros();
				metadeXor.contabiliarZeros();
				
				double metadePercentualZeros = metadeXor.getPercentualZeros();
				double percentualZeros = xor.getPercentualZeros();
				
				if (percentualZeros > percZerosMaximo)
					percZerosMaximo = percentualZeros;

				if (metadePercentualZeros > metadePercZerosMaximo)
					metadePercZerosMaximo = metadePercentualZeros;
				
				if (percentualZeros < percZerosMinimo)
					percZerosMinimo = percentualZeros;
				
				if (metadePercentualZeros < metadePercZerosMinimo)
					metadePercZerosMinimo = metadePercentualZeros;
				
				percZerosMedia += percentualZeros;
				metadePercZerosMedia += metadePercentualZeros;
				
				percZeros[indice] = percentualZeros;
				metadePercZeros[indice] = metadePercentualZeros;
				
				if (percentualZeros < 49 || percentualZeros > 51) {
					
					writer.print((i + 1) + "\t");
					//writer.print(Integer.parseInt(regra.getNucleo(), 2) + "\t");
					writer.print("[" + regra.getNucleo() + "]\t");
					writer.print(nf.format(regra.entropia()) + "\t");
					writer.print(arq.getName() + "\t");
					writer.print(nf.format(percentualZeros) + "\t");
					writer.print("COMPLETO\n");
					writer.flush();
					
					gerouLog = true;
				}
				
				if (metadePercentualZeros < 49 || metadePercentualZeros > 51) {
					
					writer.print((i + 1) + "\t");
					//writer.print(Integer.parseInt(regra.getNucleo(), 2) + "\t");
					writer.print("[" + regra.getNucleo() + "]\t");
					writer.print(nf.format(regra.entropia()) + "\t");
					writer.print(arq.getName() + "\t");
					writer.print(nf.format(metadePercentualZeros) + "\t");
					writer.print("METADE\n");
					writer.flush();
					
					metadeGerouLog = true;
				}
				
				indice++;
				
				/*if (1==1)
					break;*/
			}

			percZerosMedia = percZerosMedia / totalImagens;
			metadePercZerosMedia = metadePercZerosMedia / totalImagens;
			
			System.out.print((i + 1) + "\t");
			//System.out.print(Integer.parseInt(regra.getNucleo(), 2) + "\t");
			System.out.print("[" + regra.getNucleo() + "]\t");
			System.out.print(nf.format(regra.entropia()) + "\t");
			
			// ----  METADE ----
			System.out.print(nf.format(metadePercZerosMedia) + "\t");
			System.out.print(nf.format(Util.desvioPadrao(metadePercZeros, metadePercZerosMedia)) + "\t");

			System.out.print(nf.format(metadePercZerosMaximo) + "\t");
			System.out.print(nf.format(metadePercZerosMinimo) + "\t");
			
			System.out.print((metadeGerouLog ? "Sim" : "Nao") + "\t");
			
			
			// ----  COMPLETO ----
			System.out.print(nf.format(percZerosMedia) + "\t");
			System.out.print(nf.format(Util.desvioPadrao(percZeros, percZerosMedia)) + "\t");

			System.out.print(nf.format(percZerosMaximo) + "\t");
			System.out.print(nf.format(percZerosMinimo) + "\t");
			
			System.out.print((gerouLog ? "Sim" : "Nao") + "\t");
			
			Cronometro.parar();
		}
		
		writer.println("====================================");
		writer.println("Log finalizado em " + df.format(new Date()));
		writer.println("===================================");
		
		writer.close();
	}
	
	public static void main(String[] args) throws Exception {
		//public static final int LINHAS_JANELA = 3;
		//public static final int COLUNAS_JANELA = 6;
		
		if (args.length != 13) {
			args = new String[] {
				"D:\\desktop\\mestrado\\testes_ac2d\\teste_unitario\\regras_teste_unitario.txt",
				"D:\\desktop\\mestrado\\testes_ac2d\\teste_unitario\\imagens",
				"3",
				"6",
				"50",
				"100",
				"0", //NORTE
				"255",
				"255",
				"1",
				"1", 
				"false",
				"D:\\desktop\\mestrado\\testes_ac2d\\teste_unitario\\resultados\\log.txt"
			};
		} 
		
		iniciar(
				args[0], 
				args[1],
				Integer.parseInt(args[2]),
				Integer.parseInt(args[3]),
				Integer.parseInt(args[5]),
				Integer.parseInt(args[6]),
				Integer.parseInt(args[7]),
				Integer.parseInt(args[8]),
				Integer.parseInt(args[9]),
				Integer.parseInt(args[10]),
				Boolean.parseBoolean(args[11]),
				args[12],
				Integer.parseInt(args[4]));
	}
}
