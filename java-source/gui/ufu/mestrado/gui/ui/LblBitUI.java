package ufu.mestrado.gui.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ufu.mestrado.Util;

public class LblBitUI extends JLabel {
	private static final long serialVersionUID = 1L;
	private Boolean bit;
	
	public LblBitUI() {
		this(null);
	}
	
	public LblBitUI(Boolean bit) {
		setBit(bit);
		setMinimumSize(new Dimension(16, 16));
		setMaximumSize(new Dimension(16, 16));
		setPreferredSize(new Dimension(16, 16));

		// Se foi fornecido um bit, então configura o label
		// para exibi-lo.
		if (bit != null) {
			setHorizontalAlignment(SwingConstants.CENTER);
			setVerticalAlignment(SwingConstants.CENTER);
			
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			setBackground(Color.WHITE);
			setForeground(Color.BLUE);
			setOpaque(true);
			
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					// Se der um duplo clique, muda o valor do bit
					if (e.getClickCount() > 1) {
						setBit(!getBit());
					}
				}
			});
		}
	}
	
	public void setBit(Boolean bit) {
		this.bit = bit;
		setText(String.valueOf(Util.toInt(bit)));
		updateUI();
	}
	
	public Boolean getBit() {
		return this.bit;
	}
}
