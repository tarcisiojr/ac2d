package ufu.mestrado.teste;

import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.Cronometro;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.Util;
import ufu.mestrado.imagem.CifradorImagemPretroBranco;

public class TesteSistematico1CifragemACImagemPretoBranco {
	
	public static void iniciar(String arqRegras, String strDirImagens, int tamLinhasJanela, int tamColunasJanela,
			int qtdPreImagem, int direcao, int linhaRuido, int colunaRuido, int inicio, int fim,
			boolean rotacionarSensitivdade) throws Exception {
		
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
		System.out.println("Quantidade de pre-imagem:: " + qtdPreImagem);
		System.out.println("Direcao do calculo: " + DirecaoCalculo.toString(direcao));
		System.out.println("Posicao ruido [linha x coluna]: [" + linhaRuido + " x " + colunaRuido + "]");
		System.out.println("Intervalo: " + inicio  + " ate " + fim);
		
		System.out.println();
		System.out.print("Indice\t");
		System.out.print("Regra\t");
		System.out.print("Entropia_Regra\t");
		System.out.print("Entropia_Media_XOR\t");
		System.out.print("Entropia_Desvio_Padrao_XOR\t");
		System.out.print("Percentual_0s_Media_XOR\t");
		System.out.print("Desvio_Padrao_Percentual_0s_XOR\t");
		System.out.print("Entropia_Maxima_XOR\t");
		System.out.print("Entropia_Minima_XOR\t");
		System.out.print("Percentual_0s_Maximo_XOR\t");
		System.out.print("Percentual_0s_Minimo_XOR\t");
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
		
		for (int i = inicio -1; i < regras.size() && i < fim; i++) {
			Regra regra = regras.get(i);
			
			double entropiaMaxima = Double.MIN_VALUE;
			double entropiaMinima = Double.MAX_VALUE;
			double entropiaMedia = 0;
			
			double percZerosMaximo = Double.MIN_VALUE;
			double percZerosMinimo = Double.MAX_VALUE;
			double percZerosMedia = 0;
			
			double entropias[] = new double[totalImagens];
			double percZeros[] = new double[totalImagens];
			
			int indice = 0;
			
			Cronometro.iniciar();
			for (File arq: arqImagens) {
				CifradorImagemPretroBranco cifrador = new CifradorImagemPretroBranco(arq.getAbsolutePath(), regra, direcao);
				
				Reticulado retInicial = cifrador.getReticuladoInicial();
				Reticulado retRuido = retInicial.aplicarRuido(linhaRuido, colunaRuido);
				
				AutomatoCelular ac = new AutomatoCelular(retInicial, regra);
				ac.rotacionarReticulado = rotacionarSensitivdade;
				Reticulado preImagemA = ac.calcularPreImage(qtdPreImagem);
				
				ac.setReticulado(retRuido);
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
				
				indice++;
			}
			
			entropiaMedia = entropiaMedia / totalImagens;
			percZerosMedia = percZerosMedia / totalImagens;
			
			System.out.print((i + 1) + "\t");
			System.out.print("[" + regra.getNucleo() + "]\t");
			System.out.print(nf.format(regra.entropia()) + "\t");
			
			System.out.print(nf.format(entropiaMedia) + "\t");
			System.out.print(nf.format(Util.desvioPadrao(entropias, entropiaMedia)) + "\t");
			System.out.print(nf.format(percZerosMedia) + "\t");
			System.out.print(nf.format(Util.desvioPadrao(percZeros, percZerosMedia)) + "\t");

			System.out.print(nf.format(entropiaMaxima) + "\t");
			System.out.print(nf.format(entropiaMinima) + "\t");
			
			System.out.print(nf.format(percZerosMaximo) + "\t");
			System.out.print(nf.format(percZerosMinimo) + "\t");
			
			Cronometro.parar();
		}
	}
	
	public static void main(String[] args) throws Exception {
		//public static final int LINHAS_JANELA = 3;
		//public static final int COLUNAS_JANELA = 6;
		
		if (args.length != 10) {
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
				"true"
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
				Boolean.parseBoolean(args[10]));
	}
}
