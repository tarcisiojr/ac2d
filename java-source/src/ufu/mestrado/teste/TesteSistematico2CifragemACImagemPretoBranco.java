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

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.Util;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;

public class TesteSistematico2CifragemACImagemPretoBranco {
	
	public static void iniciar(String arqRegras, String strDirImagens, int tamLinhasJanela, int tamColunasJanela,
			int qtdPreImagem, int direcao, int linhaRuido, int colunaRuido, int inicio, int fim,
			boolean rotacionarSensitivdade, String nomeLog) throws Exception {
		
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
		
		
		List<Regra> regras = Regra.carregarRegras(arqRegras);
		
		System.out.println("Total de regras carregadas: " + regras.size());
		System.out.println("Total de imagens carregadas: " + arqImagens.length);
		System.out.println("Janela utilizada no reticulado [linhas x coluna]: [" + tamLinhasJanela + " x " + tamColunasJanela + "]");
		System.out.println("Quantidade de pre-imagem: " + qtdPreImagem);
		System.out.println("Direcao do calculo: " + DirecaoCalculo.toString(direcao));
		System.out.println("Posicao ruido [linha x coluna]: [" + linhaRuido + " x " + colunaRuido + "]");
		System.out.println("Intervalo: " + inicio  + " ate " + fim);
		
		System.out.println();
		System.out.print("Indice\t");
		System.out.print("Regra\t");
		System.out.print("Regra_com_Ruido\t");
		System.out.print("Entropia_Regra\t");
		System.out.print("Entropia_Media_XOR\t");
		System.out.print("Entropia_Desvio_Padrao_XOR\t");
		System.out.print("Percentual_0s_Media_XOR\t");
		System.out.print("Desvio_Padrao_Percentual_0s_XOR\t");
		System.out.print("Entropia_Maxima_XOR\t");
		System.out.print("Entropia_Minima_XOR\t");
		System.out.print("Percentual_0s_Maximo_XOR\t");
		System.out.print("Percentual_0s_Minimo_XOR\t");
		System.out.print("Gerou_Log\t");
		System.out.print("Tempo_Gasto\n");
		
		NumberFormat  nf = DecimalFormat.getInstance();
		nf.setMaximumFractionDigits(6);
		nf.setMinimumFractionDigits(6);
		
		final int totalImagens = arqImagens.length;
		
		if (inicio == -1) {
			inicio = 1;
		}
		
		if (fim == -1) {
			fim = regras.size();
		}
		
		FileOutputStream fos = new FileOutputStream(nomeLog, true);
		PrintWriter writer = new PrintWriter(fos);
		
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		
		writer.println("====================================");
		writer.println("Log iniciado em " + df.format(new Date()));
		writer.println("====================================");
		
		writer.print("Indice\t");
		writer.print("Regra\t");
		writer.print("Entropia_Regra\t");
		writer.print("Regra_com_ruido\t");
		writer.print("Entropia_Regra_com_ruido\t");
		writer.print("Imagem\t");
		writer.print("Entropia\t");
		writer.print("Entropia_Linha\t");
		writer.print("Entropia_Coluna\t");
		writer.print("Percentual_0s");
		writer.println();
		writer.flush();
		
		for (int i = inicio -1; i < regras.size() && i < fim; i++) {
			Regra regra = regras.get(i);
			
			Regra regraRuido = regra.aplicarRuido();
			
			double entropiaMaxima = Double.MIN_VALUE;
			double entropiaMinima = Double.MAX_VALUE;
			double entropiaMedia = 0;
			
			double percZerosMaximo = Double.MIN_VALUE;
			double percZerosMinimo = Double.MAX_VALUE;
			double percZerosMedia = 0;
			
			double entropias[] = new double[totalImagens];
			double percZeros[] = new double[totalImagens];
			
			int indice = 0;
			
			boolean gerouLog = false;
			
			Cronometro.iniciar();
			for (File arq: arqImagens) {
				CifradorImagemPretroBranco cifrador = new CifradorImagemPretroBranco(arq.getAbsolutePath(), regra, direcao);
				
				Reticulado retInicial = cifrador.getReticuladoInicial();
				
				AutomatoCelular ac = new AutomatoCelular(retInicial, regra);
				ac.rotacionarReticulado = rotacionarSensitivdade;
				
				Reticulado preImagemA = ac.calcularPreImage(qtdPreImagem);
				
				ac.setRegra(regraRuido);
				ac.setReticulado(retInicial);
				
				Reticulado preImagemB = ac.calcularPreImage(qtdPreImagem);
				
				
				Reticulado xor = preImagemA.xor(preImagemB);
				double entropia = xor.entropia(tamLinhasJanela, tamColunasJanela);
				
				if (entropia > entropiaMaxima)
					entropiaMaxima = entropia;
				
				if (entropia < entropiaMinima)
					entropiaMinima = entropia;
				
				entropiaMedia += entropia;
				
				entropias[indice] = entropia;
				
				xor.contabiliarZeros();
				double percentualZeros = xor.getPercentualZeros();
				
				if (percentualZeros > percZerosMaximo)
					percZerosMaximo = percentualZeros;
				
				if (percentualZeros < percZerosMinimo)
					percZerosMinimo = percentualZeros;
				
				percZerosMedia += percentualZeros;
				
				percZeros[indice] = percentualZeros;
				
				double entropiaLinha = xor.getMenorEntropiaLinha();
				double entropiaColuna = xor.getMenorEntropiaColuna();
				
				if (percentualZeros < 49 || percentualZeros > 51 || entropia < 0.94 || entropiaLinha < 0.8 
						|| entropiaColuna < 0.8) {
					
					writer.print((i + 1) + "\t");
					writer.print("[" + regra.getNucleo() + "]\t");
					writer.print(nf.format(regra.entropia()) + "\t");
					writer.print("[" + regraRuido.getNucleo() + "]\t");
					writer.print(nf.format(regraRuido.entropia()) + "\t");
					writer.print(arq.getName() + "\t");
					writer.print(nf.format(entropia) + "\t");
					writer.print(nf.format(entropiaLinha) + "\t");
					writer.print(nf.format(entropiaColuna) + "\t");
					writer.print(nf.format(percentualZeros) + "\n");
					writer.flush();
					
					gerouLog = true;
				}
				
				indice++;
				
				/*if (1==1)
					break;*/
			}
			
			entropiaMedia = entropiaMedia / totalImagens;
			percZerosMedia = percZerosMedia / totalImagens;
			
			System.out.print((i + 1) + "\t");
			System.out.print("[" + regra.getNucleo() + "]\t");
			System.out.print("[" + regraRuido.getNucleo() + "]\t");
			System.out.print(nf.format(regra.entropia()) + "\t");
			
			System.out.print(nf.format(entropiaMedia) + "\t");
			System.out.print(nf.format(Util.desvioPadrao(entropias, entropiaMedia)) + "\t");
			System.out.print(nf.format(percZerosMedia) + "\t");
			System.out.print(nf.format(Util.desvioPadrao(percZeros, percZerosMedia)) + "\t");

			System.out.print(nf.format(entropiaMaxima) + "\t");
			System.out.print(nf.format(entropiaMinima) + "\t");
			
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
		
		if (args.length != 12) {
			args = new String[] {
				"./testes/regras_1000_1_ver1.txt",
				"E:/junior/Desktop/mestrado/base_dados_imagens/512x512",
				"3",
				"6",
				"10",
				"1", //NORTE
				"255",
				"255",
				"-1",
				"-1", 
				"true",
				"log.txt"
			};
		} 
		
		iniciar(
				args[0], 
				args[1],
				Integer.parseInt(args[2]),
				Integer.parseInt(args[3]),
				Integer.parseInt(args[4]),
				Integer.parseInt(args[5]),
				Integer.parseInt(args[6]),
				Integer.parseInt(args[7]),
				Integer.parseInt(args[8]),
				Integer.parseInt(args[9]),
				Boolean.parseBoolean(args[10]),
				args[11]);
	}
}
