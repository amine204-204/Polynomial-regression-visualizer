package projet_stat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.*;

public class Plot extends JPanel{
	private static final long serialVersionUID = 1L;
	ArrayList<Double> x = new ArrayList<>() ,y = new ArrayList<>();
	double minx, maxx, miny, maxy;
	boolean ispoly = false;
	int r = 1;
	Plot(ArrayList<Double> x, ArrayList<Double> y, double minx, double maxx, double miny, double maxy){
		this.x = x;;
		this.y = y;
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		setLayout(new BorderLayout());
		setOpaque(false);
	}
	
	Plot(double[] poly, double l, double minx, double maxx, double miny, double maxy, int poly_thickness){
		x.add(minx);
		y.add(p(poly, minx));
		double lastx = minx;
		boolean last_with_methode = false;
		while(lastx<=maxx) {
			if(y.get(y.size()-1) > maxy && !last_with_methode) {
				double step = l;
				double eps = l;
				double factor = 0.5;
				boolean crossed_border = false;
				double xcurr = lastx;
				while((Math.abs(p(poly, xcurr)-maxy) > eps || !crossed_border) && xcurr < maxx) {
					while(p(poly,xcurr) > maxy && xcurr <= maxx) {
						xcurr += step;
					}
					crossed_border = true;
					if(p(poly,xcurr)==maxy) break;
					xcurr -= step;
					step *= factor;
				}
				x.add(xcurr);
				y.add(p(poly,xcurr));
				last_with_methode = true;

			}else if(y.get(y.size()-1) < miny  && !last_with_methode) {
				double step = 1;
				double eps = l;
				double factor = 0.5;
				boolean crossed_border = false;
				double xcurr = lastx;
				while((Math.abs(p(poly, xcurr)-miny) > eps || !crossed_border) && xcurr <= maxx) {
					while(p(poly,xcurr) < miny && xcurr <= maxx) {
						xcurr += step;
					}
					crossed_border = true;
					if(p(poly,xcurr)==miny) break;
					xcurr -= step;
					step *= factor;
				}
				x.add(xcurr);
				y.add(p(poly,xcurr));
				last_with_methode = true;

			}else {
				double[] tan = tangent(poly,lastx);
				x.add(lastx+l*Math.sqrt(1/(1+tan[1]*tan[1])));
				y.add(p(poly, lastx+l*Math.sqrt(1/(1+tan[1]*tan[1]))));
				last_with_methode = false;
			}
			
			lastx = x.get(x.size()-1);
		}
		x.remove(x.size()-1);
		y.remove(y.size()-1);
		x.add(maxx);
		y.add(p(poly,maxx));
		
		for(int i = 0 ;i < x.size(); i++) {
			System.out.println(x.get(i)+" "+y.get(i));
		}
		ispoly = true;
		this.minx = minx;
		this.maxx = maxx;
		this.miny = miny;
		this.maxy = maxy;
		r = poly_thickness;
		setLayout(new BorderLayout());
		setOpaque(false);
	}
	
	public void paintComponent(Graphics g) {
		int gh =(int)( getParent().getHeight()*0.8);
		int gw = (int) (getParent().getWidth()*0.8);
		int gx = (int)(getParent().getWidth()*0.1);
		int gy = (int)(getParent().getHeight()*0.9);
		
		g.setColor(Color.WHITE);
		g.drawString(minx+"", gx, gy+20);
		g.drawString(maxx+"", gx+gw-20, gy+20);
		g.drawString(miny+"", gx+gw+10, gy);
		g.drawString(maxy+"", gx+gw+10, gy-gh+10);
		g.drawRect(gx, gy-gh, gw, gh);
		
		if(ispoly) {
			
			for(int i = 0; i < x.size()-1; i++) {
				g.setColor(new Color(250,170,20));
				double xelt = x.get(i);
				double yelt = y.get(i);
				int xpos = (int)(gx + gw*(xelt-minx)/(maxx-minx));
				int ypos = (int)(gy - gh*(yelt-miny)/(maxy-miny));
				double nxelt = x.get(i+1);
				double nyelt = y.get(i+1);
				int nxpos = (int)(gx + gw*(nxelt-minx)/(maxx-minx));
				int nypos = (int)(gy - gh*(nyelt-miny)/(maxy-miny));
			
				if(ypos < gy && ypos > gy-gh && nypos < gy && nypos > gy-gh) {
					double l = Math.sqrt((nxpos-xpos)*(nxpos-xpos)+(nypos-ypos)*(nypos-ypos));
					double dx = (nxpos-xpos)/l;
					double dy = (nypos-ypos)/l;
					double x1 = xpos-dy*r/2;
					double x2 = xpos+dy*r/2;
					double x3 = nxpos+dy*r/2;
					double x4 = nxpos-dy*r/2;
					double y1 = ypos+dx*r/2;
					double y2 = ypos-dx*r/2;
					double y3 = nypos-dx*r/2;
					double y4 = nypos+dx*r/2;
					
					g.fillPolygon(new int[] {(int)x1,(int)x2,(int)x3,(int)x4}, new int[] {(int)y1,(int)y2,(int)y3,(int)y4},4);
					g.fillOval(xpos-r/2, ypos-r/2, r, r);
					/*g.drawLine(xpos, ypos, xpos+(int)(dx*10), ypos+(int)(dy*10));
					g.fillOval((int)x1, (int)y1, 5, 5);
					g.fillOval((int)x2, (int)y2, 5, 5);
					g.fillOval((int)x3, (int)y3, 5, 5);
					g.fillOval((int)x4, (int)y4, 5, 5);*/
				}/*else if(ypos <= gy && ypos >= gy-gh && (nypos > gy || nypos < gy-gh)) {
					double l = Math.sqrt((nxpos-xpos)*(nxpos-xpos)+(nypos-ypos)*(nypos-ypos));
					double dx = (nxpos-xpos)/l;
					double dy = (nypos-ypos)/l;
					double x1 = xpos-dy*r/2;
					double x2 = xpos+dy*r/2;
					double x3 = nxpos+dy*r/2;
					double x4 = nxpos-dy*r/2;
					double y1 = ypos+dx*r/2;
					double y2 = ypos-dx*r/2;
					double y3 = nypos-dx*r/2;
					double y4 = nypos+dx*r/2;
					double a1 = (y4-y1)/(x4-x1);
					double a2 = (y3-y2)/(x3-x2);
					double b1 = y1-a1*x1; double b2 = y2-a2*x2;
					if(nypos > gy) {y4 = gy; y3 = gy;}
					else if(nypos < gy-gh) {y4 = gy-gh; y3 = gy-gh;}
					x4 = (y4-b1)/a1;
					x3 = (y3-b2)/a2;
					g.fillPolygon(new int[] {(int)x1,(int)x2,(int)x3,(int)x4}, new int[] {(int)y1,(int)y2,(int)y3,(int)y4},4);
				}/*else if(nypos <= gy && nypos >= gy-gh && (ypos > gy || ypos < gy-gh)) {
					double l = Math.sqrt((nxpos-xpos)*(nxpos-xpos)+(nypos-ypos)*(nypos-ypos));
					double dx = (nxpos-xpos)/l;
					double dy = (nypos-ypos)/l;
					double x1 = xpos-dy*r/2;
					double x2 = xpos+dy*r/2;
					double x3 = nxpos+dy*r/2;
					double x4 = nxpos-dy*r/2;
					double y1 = ypos+dx*r/2;
					double y2 = ypos-dx*r/2;
					double y3 = nypos-dx*r/2;
					double y4 = nypos+dx*r/2;
					double a1 = (y4-y1)/(x4-x1);
					double a2 = (y3-y2)/(x3-x2);
					double b1 = y1-a1*x1; double b2 = y2-a2*x2;
					if(ypos > gy) {y1 = gy; y2 = gy;}
					else if(ypos < gy-gh) {y1 = gy-gh; y2 = gy-gh;}
					x1 = (y1-b1)/a1;
					x2 = (y2-b2)/a2;
					g.fillPolygon(new int[] {(int)x1,(int)x2,(int)x3,(int)x4}, new int[] {(int)y1,(int)y2,(int)y3,(int)y4},4);
				}*/

			}
		}else {
			g.setColor(Color.RED);
			for(int i = 0; i < x.size(); i++) {
				double xelt = x.get(i);
				double yelt = y.get(i);
				int xpos = (int)(gx + gw*(xelt-minx)/(maxx-minx));
				int ypos = (int)(gy - gh*(yelt-miny)/(maxy-miny));
				g.fillOval(xpos-5, ypos-5, 10, 10);
				
			}
		}
		
		
		
	}
	
	private double p(double[] poly, double d) {
		double s = 0;
		for(int i = 0; i < poly.length; i++) {
			s += poly[i] * Math.pow(d, i);
		}
		return s;
	}
	
	private double[] pprime(double[] poly) {
		double[] pprime = new double[poly.length-1];
		for(int i = 1; i < poly.length; i++) {
			pprime[i-1] = poly[i]*i;
		}
		return pprime;
	}
	
	private double[] tangent(double[] poly, double a) {
		return new double[] {p(poly,a)-a*p(pprime(poly),a), p(pprime(poly),a)};
	}
}
