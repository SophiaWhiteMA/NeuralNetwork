package cameron.white.algebra;

import java.util.Random;

/**
 * 0-Index implementation of a Matrix.
 * @author Cameron White
 *
 */
public class Matrix {

	/*Fields*/
	private int height;
	private int width;
	private double[][] values;
	
	/*Constructors*/
	public Matrix(int m, int n) {
		this.height = m;
		this.width = n;
		values = new double[m][n];
	}
	
	public Matrix(double[][] values) { //TODO: Input validation
		this.values = values;
		this.height = values.length;
		this.width = values[0].length;
	}
	
	/*Methods*/
	public void print() {
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < values[i].length; j++) {
				System.out.print(values[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * Randomizes this matrix with a minimum value of 0 and a maximum value of max
	 * @param max
	 */
	public void randomize(double max) {
		Random RNG = new Random();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				values[i][j] = RNG.nextDouble() * max;
			}
		}
	}
	
	public static double dotProduct(double[] d1, double[] d2) {
		if(d1.length != d2.length) {
			throw new RuntimeException("When calculating the dot product, vectors must have the same length.");
		}
		double output = 0;
		for(int i = 0; i < d1.length; i++) {
			output += d1[i] * d2[i];
		}
		return output;
	}
	
	/*Elementary Operationas*/
	public Matrix add(Matrix m) {
		if(m.getWidth() != width || m.getHeight() != height) {
			print();
			m.print();
			throw new RuntimeException("You can only add two matrices of the same size.");
		}
		Matrix output = new Matrix(height, width);
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				output.setValue(i, j, m.getValue(i, j) + getValue(i, j));
			}
		}
		
		return output;
	}
	
	public Matrix subtract(Matrix m) {
		if(m.getWidth() != width || m.getHeight() != height) {
			throw new RuntimeException("You can only subtract two matrices of the same size.");
		}
		Matrix output = new Matrix(height, width);
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				output.setValue(i, j, getValue(i, j) - m.getValue(i, j));
			}
		}
		
		return output;
	}
	
	public Matrix subtract(double d) {
		Matrix output = new Matrix(height, width);
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				output.setValue(i, j, getValue(i, j) - d);
			}
		}
		
		return output;
	}
	
	public Matrix multiply(Matrix m) {
		if(width != m.getHeight()) {
			throw new RuntimeException("Error: When multiplying matrix A by matrix B, the number of columns in A must == number of rows in B");
		}
		Matrix output = new Matrix(this.height, m.getWidth());
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < m.getWidth(); j++) {
				output.setValue(i, j, dotProduct(this.getRow(i), m.getColumn(j)));
			}
		}
		
		return output;
	}
	
	public Matrix multiply(double d) {
		Matrix output = new Matrix(height, width);
		for(int i = 0; i < this.height; i++) {
			for(int j = 0; j < this.width; j++) {
				output.setValue(i, j, getValue(i, j) * d);
			}
		}
		return output;
	}
	
	public Matrix elementWise(Matrix m) {
		if(m.getWidth() != width || m.getHeight() != height) {
			print();
			m.print();
			throw new RuntimeException("piecewiseMultiply requires that both inputs are the exact same dimensions");
		}
		Matrix output = new Matrix(height, width);
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				output.setValue(i, j, getValue(i,j) * m.getValue(i, j));
			}
		}
		
		return output;
	}
	
	/*Accessors*/
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public double[] getRow(int row) {
		return values[row];
	}
	
	public double[] getColumn(int column) {
		double[] output = new double[height];
		for(int i = 0; i < height; i++) {
			output[i] = getRow(i)[column];
		}
		return output;
	}
	
	public void setValue(int m, int n, double val) {
		values[m][n] = val;
	}
	
	public double getValue(int m, int n) {
		return values[m][n];
	}

	public Matrix transpose() {
		Matrix output = new Matrix(width, height);
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				output.setValue(j, i, getValue(i, j));
			}
		}
		return output;
	}
	
}
