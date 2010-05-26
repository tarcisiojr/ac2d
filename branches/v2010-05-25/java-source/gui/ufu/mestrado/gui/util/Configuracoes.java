package ufu.mestrado.gui.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuracoes {
	private final Logger logger = Logger.getClass(Configuracoes.class);
	
	private final String NOME_ARQUIVO = "configuracoes.cfg";
	
	private final String PROP_NOME_ARQUIVO = "nomeArqivo";
	private final String PROP_CAMINHO_PADRAO = "caminhoPadrao";
	
	private Properties properties;
	
	public Configuracoes() {
		properties = new Properties();
		
		carregarDoArquivo();
	}

	private void carregarDoArquivo() {
		try {
			logger.info("Carregando arquivo de configurações.");
			FileInputStream fileInputStream = new FileInputStream(NOME_ARQUIVO);
			properties.load(fileInputStream);
			fileInputStream.close();
		} catch (Exception e) {
			
			properties.setProperty(PROP_NOME_ARQUIVO, NOME_ARQUIVO);
			properties.setProperty(PROP_CAMINHO_PADRAO, "./");
			
			try {
				logger.info("Criando arquivo de configurações...");
				FileOutputStream outputStream = new FileOutputStream(NOME_ARQUIVO);
				properties.store(outputStream, "Arquivo de configurações");
				outputStream.close();
				
			} catch (IOException e1) {
				logger.error("Erro ao carregar o arquivo de configurações" + e1.getMessage());
				e1.printStackTrace();
			}
		}
	}
	
	public String getCaminhoPadrao() {
		return properties.getProperty(PROP_CAMINHO_PADRAO);
	}
}
