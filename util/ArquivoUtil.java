package ufu.mestrado.util;

import java.io.File;

public class ArquivoUtil {

	/**
	 * Padroniza os nomes dos arquivos de um dado diretorio.
	 * @param diretorio Diretorio que sera padronizado
	 * @param prefixo Prefixo que sera utilizado no nome do arquivo.
	 * @throws Exception
	 */
	public static void padronizarNomes(String diretorio, String prefixo, int larguraZeros) throws Exception {
		File dir = new File(diretorio);
		
		File arquivos[] = dir.listFiles();
		
		int i = 1;
		
		diretorio = dir.getAbsolutePath();
		
		String format = "%0" + larguraZeros + "d";
		
		for (File arquivo : arquivos) {
			if (arquivo.isFile()) {
				String extensao = arquivo.getName();
				extensao = extensao.substring(extensao.lastIndexOf('.') + 1);
				
				String nomeArquivo = diretorio + File.separatorChar + prefixo + "_" +
					String.format(format, i) + "." + extensao;
				
				arquivo.renameTo(new File(nomeArquivo));
				
				i++;
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		ArquivoUtil.padronizarNomes("E:/junior/Desktop/mestrado/base_dados_imagens/512x512", "512x512", 4);
		ArquivoUtil.padronizarNomes("E:/junior/Desktop/mestrado/base_dados_imagens/1024x1024", "1024x1024", 4);
	}
}
