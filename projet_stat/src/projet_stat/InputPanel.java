package projet_stat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import javax.swing.*;

public class InputPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private CalcHandler ch;		
	JButton load;
	JButton calc;
	JPanel calpnl;
	JTextField tf;
	
	boolean NEWDB = false, NEWPLOT = false;
	
	InputPanel(){	
		ch = new CalcHandler();
		setLayout(new BorderLayout());
		load = new JButton("Load");
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				JFileChooser fc = new JFileChooser();
				FileFilter ff = new FileFilter() {

					@Override
					public boolean accept(File pathname) {
						return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(".xlsx");
					}

					@Override
					public String getDescription() {
						return "Excel file (*.xlsx)";
					}
					
				};
				fc.setFileFilter(ff);
				fc.setAcceptAllFileFilterUsed(false);
				f.setLayout(new BorderLayout());
				f.add(fc, BorderLayout.CENTER);
				fc.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser otherfc = (JFileChooser) e.getSource();
						String command = e.getActionCommand();
						if(command.equals(JFileChooser.APPROVE_SELECTION)) {
							File DB = otherfc.getSelectedFile();
							ch.load(DB);
							calc.setEnabled(true);
							tf.setFocusable(true);
							Main.handleLoad(ch);
							f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
						}else if(command.equals(JFileChooser.CANCEL_SELECTION)) {
							f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
						}
					}

					
				});
				f.setVisible(true);
				f.pack();
				f.setLocationRelativeTo(null);
			}
			
		});
		calpnl = new JPanel();
		tf = new JTextField("Enter the polynomial degree",20);
		tf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = Integer.parseInt(tf.getText());
				ch.Calc_Poly(n);
				Main.handlenewPoly(ch);
			}
			
		});
		tf.setForeground(Color.GRAY);
		tf.addFocusListener(new FocusListener() {
			@Override 
			public void focusGained(FocusEvent e) {
				if(tf.getText().equals("Enter the polynomial degree")) {
					tf.setText("");
					tf.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if(tf.getText().isEmpty()) {
					tf.setText("Enter the polynomial degree");
					tf.setForeground(Color.GRAY);
				}
			}
		});
		calc = new JButton("Generate Polynomial");
		calpnl.add(tf);
		calpnl.add(calc);
		calc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = Integer.parseInt(tf.getText());
				ch.Calc_Poly(n);
				Main.handlenewPoly(ch);
			}
			
		});
		calc.setEnabled(false);
		tf.setFocusable(false);
		add(calpnl, BorderLayout.WEST);
		add(load, BorderLayout.EAST);
		
		setBorder(BorderFactory.createEtchedBorder());

	}
	
	public CalcHandler get_CalcHandler() {
		return ch;
	}
	

	
	
}
