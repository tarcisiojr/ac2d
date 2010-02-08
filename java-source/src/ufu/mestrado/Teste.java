package ufu.mestrado;

import java.awt.Color;

public class Teste {

	public static void main(String[] args) {
		System.out.println(new Color(0xFFFFFF).getRGB());
		System.out.println(new Color(0x7FFFFF).getRGB());
		
		System.out.println(new Color(0x777777 + 0x777777).getRGB());
	}
}
