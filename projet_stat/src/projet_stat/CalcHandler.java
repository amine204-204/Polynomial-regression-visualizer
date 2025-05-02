package projet_stat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.apache.commons.math3.linear.*;

public class CalcHandler {
	private double minx, maxx, miny, maxy;
	ArrayList<Double> x,y;
	private ArrayList<double[]> polys;
	public CalcHandler(){
		x = new ArrayList<>();
		y = new ArrayList<>();
		polys = new ArrayList<>();
	}
	
	public void init() {
		polys = new ArrayList<>();
	}
	
	public void load(File DB) {
		x = new ArrayList<>();
		y = new ArrayList<>();
		set_minx(Double.POSITIVE_INFINITY);
		set_maxx(Double.NEGATIVE_INFINITY);
		set_miny(Double.POSITIVE_INFINITY);
		set_maxy(Double.NEGATIVE_INFINITY);
		try {
			String path =DB.getAbsolutePath();
			Workbook wb = WorkbookFactory.create(new FileInputStream(path));
			Sheet sh = wb.getSheetAt(0);
			for(Row r : sh) {
				double xelt = Double.parseDouble(r.getCell(0).toString());
				double yelt = Double.parseDouble(r.getCell(1).toString());
				x.add(xelt);
				y.add(yelt);				
				set_minx(Math.min(get_minx(), xelt));
				set_maxx(Math.max(get_maxx(), xelt));
				set_miny(Math.min(get_miny(), yelt));
				set_maxy(Math.max(get_maxy(), yelt));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Calc_poly(int n) {
		double[] poly = new double[n+1];
		poly[n] = 1;
		polys.add(poly);
	}
	
	

    public void Calc_Poly(int degree) {
        int n = x.size();

        // Create the design matrix A (of size n x (degree+1))
        RealMatrix A = new Array2DRowRealMatrix(n, degree + 1);
        for (int i = 0; i < n; i++) {
            double xi = x.get(i);
            for (int j = 0; j <= degree; j++) {
                A.setEntry(i, j, Math.pow(xi, j));
            }
        }

        // Convert y values to a column matrix (n x 1)
        RealMatrix Y = new Array2DRowRealMatrix(n, 1);
        for (int i = 0; i < n; i++) {
            Y.setEntry(i, 0, y.get(i));
        }

        // Solve the system A * coefficients = Y using least squares
        DecompositionSolver solver = new QRDecomposition(A).getSolver();
        RealMatrix coefficientsMatrix = solver.solve(Y);

        // Extract the coefficients and return them as a list
        double[] coefficients = new double[degree+1];
        for (int i = 0; i <= degree; i++) {
            coefficients[i]=coefficientsMatrix.getEntry(i, 0);
        }

        polys.add(coefficients);
    }

	 

	
	public ArrayList<double[]> getPolys() {
		return polys;
	}
	
	
	public double[] getPoly() {
		return null;
	}
	
	public double get_minx() {
		return minx;
	}
	public double get_maxx() {
		return maxx;
	}
	public double get_miny() {
		return miny;
	}
	public double get_maxy() {
		return maxy;
	}
	
	public void set_minx(double m) {
		minx = m;
	}
	public void set_maxx(double m) {
		maxx = m;
	}
	public void set_miny(double m) {
		miny = m;;
	}
	public void set_maxy(double m) {
		maxy = m;
	}

	public ArrayList<Double> get_X() {
		return x;
	}

	public ArrayList<Double> get_Y() {
		return y;
	}
	
}
