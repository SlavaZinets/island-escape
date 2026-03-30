package com.islandescape;

import com.islandescape.core.GamePanel;
import com.islandescape.input.GameKeyHandler;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Main {
	public static void main(String[] args) {
		// sttart on edt

		SwingUtilities.invokeLater(() -> {
			// build window
			JFrame window = new JFrame("Island Escape");
			GamePanel panel = new GamePanel();
			// attach panel
			window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			window.add(panel);
			window.pack();
			window.setLocationRelativeTo(null);
			// bind input
			panel.setFocusable(true);
			panel.addKeyListener(new GameKeyHandler(panel));
			// show window
			window.setVisible(true);
			panel.requestFocusInWindow();
		});
	}
}
