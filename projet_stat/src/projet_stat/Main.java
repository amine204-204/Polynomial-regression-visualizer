package projet_stat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

public class Main {
	
	static JFrame f;
	static JPanel pp;
	static InputPanel ip;
	static JPanel cp;
	static JSplitPane sp;
	static Color BGcolor, BorderColor, ButtonColor;
	static int BorderThickness = 3;
	static ArrayList<JCheckBox> cbs = new ArrayList<>();
	
	Main() {
		BGcolor = new Color(20,20,20);
		BorderColor = new Color(40,40,45);
		ButtonColor = new Color(250,170,20);
		f = new JFrame();
		pp = new JPanel();
		ip = new InputPanel();
		cp = new JPanel();
		sp = new JSplitPane();
		pp.setBackground(BGcolor);
		ip.setBackground(BGcolor);
		cp.setBackground(BGcolor);
		ip.calpnl.setBackground(BGcolor);
		
		pp.setBorder(BorderFactory.createMatteBorder(BorderThickness, BorderThickness, BorderThickness, BorderThickness, BorderColor));
		ip.setBorder(BorderFactory.createMatteBorder(BorderThickness, BorderThickness, BorderThickness, BorderThickness, BorderColor));
		cp.setBorder(BorderFactory.createMatteBorder(BorderThickness, BorderThickness, BorderThickness, BorderThickness, BorderColor));
		
		ip.load.setBackground(ButtonColor);

		
		pp.setLayout(new BorderLayout());
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		
		f.setTitle("Mini projet Proba_Stat");
		f.setLayout(new BorderLayout());
		f.setSize(1080,720);
		f.setLocationRelativeTo(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.add(sp, BorderLayout.CENTER);
		sp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		sp.setLeftComponent(pp);
		sp.setRightComponent(cp);
		
		
		f.add(ip, BorderLayout.SOUTH);

		f.setVisible(true);
		sp.setDividerLocation(1.0);

	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Main();
				
			}
			
		});
	}
	
	public static void handleLoad(CalcHandler ch) {
		pp.removeAll();
		cbs.clear();;
		ch.init();
		ArrayList<Double> x = ch.get_X();
		ArrayList<Double> y = ch.get_Y();
		Plot p = new Plot(x, y, ch.get_minx(), ch.get_maxx(), ch.get_miny(), ch.get_maxy());
		pp.add(p, BorderLayout.CENTER);
		JCheckBox cb = new JCheckBox("Points");
		cb.setBackground(BGcolor);
		cb.setForeground(Color.white);
		cb.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int s = e.getStateChange();
				if(s==ItemEvent.SELECTED) {
					p.setVisible(true);
				}else if(s==ItemEvent.DESELECTED) {
					p.setVisible(false);
				}
			}
			
		});
		cb.setSelected(true);
		cbs.add(cb);
		updateCp();
		sp.setDividerLocation(0.9);
		pp.revalidate();
	}
	
	public static void handlenewPoly(CalcHandler ch) {
		double[] poly = ch.getPolys().get(ch.getPolys().size()-1);
		Plot p = new Plot(poly, 0.5, ch.get_minx(), ch.get_maxx(), ch.get_miny(), ch.get_maxy(),3);
		pp.add(p, BorderLayout.CENTER);
		JCheckBox cb = new JCheckBox("Coeff: "+toString(poly));
		cb.setBackground(BGcolor);
		cb.setForeground(Color.white);
		cb.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				int s = e.getStateChange();
				if(s==ItemEvent.SELECTED) {
					p.setVisible(true);
				}else if(s==ItemEvent.DESELECTED) {
					p.setVisible(false);
				}
			}
			
		});
		cb.setMinimumSize(new Dimension(0,0));
		cb.setSelected(true);
		cbs.add(cb);
		updateCp();
		pp.revalidate();

	}
	
	public static void updateCp() {		
		cp.removeAll();
		for(JCheckBox cb : cbs) {
			cp.add(cb);
		}
		f.validate();
	}
	
	public static String toString(double[] poly) {
		String s = "";
		NumberFormat scientificFormat = new DecimalFormat("0.###E0");
    
		for(int i = poly.length-1; i > 0; i--) {
		    String c = scientificFormat.format(poly[i]) + " |";
			//s += Math.ceil(poly[i]*Math.pow(10, trunc))/Math.pow(10,trunc)+",";
		    s += c;
		}
		//s+=Math.ceil(poly[0]*Math.pow(10, trunc))/Math.pow(10, trunc)+"";
	    s += scientificFormat.format(poly[0]);;
		return s;
	}
}
