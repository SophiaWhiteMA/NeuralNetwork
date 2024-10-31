package cameron.white.main;

import cameron.white.algebra.Matrix;


public class NeuralNetwork {

	/*Fields*/
	private Matrix[] weights;
	private Matrix[] biases;
	private Matrix[] activations;
	private Matrix[] error;
	private int layers;
	private int layerHeight;
	private int inputSize;
	private int outputSize;
	
	private double learningRate = 0.45;
	
	/*Constructors*/
	public NeuralNetwork(int layers, int layerHeight, int inputSize, int outputSize, boolean randomize) {
		
		biases = new Matrix[layers+1];
		weights = new Matrix[layers+1]; 
		activations = new Matrix[layers+2];
		error = new Matrix[layers+1];
		
		this.layers = layers;
		this.layerHeight = layerHeight;
		this.inputSize = inputSize;
		this.outputSize = outputSize;
		
		//Init biases
		for(int i = 0; i < biases.length; i++) {
			biases[i] = new Matrix(layerHeight, 1);
			if(randomize) biases[i].randomize(1D);
		}
		biases[biases.length - 1] = new Matrix(outputSize, 1);
		
		//Init weights
		weights[0] = new Matrix(layerHeight, inputSize);
		if(randomize) weights[0].randomize(1D);
		weights[weights.length - 1] = new Matrix(outputSize, layerHeight);
		if(randomize) weights[weights.length - 1].randomize(1D);
		
		for(int i = 1; i < weights.length - 1; i++) {
			weights[i] = new Matrix(layerHeight, layerHeight);
			if(randomize) weights[i].randomize(1D);
		}
		
		//Init activations
		activations[0] = new Matrix(inputSize, 1);
		activations[activations.length - 1] = new Matrix(outputSize, 1);
		for(int i = 1; i < activations.length - 1; i++) {
			activations[i] = new Matrix(layerHeight, 1);
		}
		
		//Init error
		error[error.length - 1] = new Matrix(outputSize, 1);
		for(int i = 0; i < error.length - 1; i++) {
			error[i] = new Matrix(layerHeight, 1);
		}
		
	}
	
	/**
	 * Propagates input data through the neural network and returns the produced output. 
	 * @param input Input of data
	 * @param idealOutput Ideal output to be used for training purposes
	 * @return The output after propagating the input through this Neural Network
	 */
	public Matrix propagate(Matrix input, Matrix idealOutput, boolean backProp) {
		
		/*Input validation*/
		if(input.getWidth() != 1 || idealOutput.getWidth() != 1 || input.getHeight() != inputSize || idealOutput.getHeight() != outputSize) {
			throw new RuntimeException("Propogation inputs and expected outputs must be matrices of size m x 1 and match the expected input and output sizes");
		}
		
		/*Iterative propagation algorithm*/
		activations[0] = input;
		for(int i = 1; i < activations.length; i++) {
			activations[i] = weights[i-1].multiply(activations[i-1]);
			activations[i] = activations[i].add(biases[i-1]);
			sigmoid(activations[i]);
		}
		
		if(backProp) {
			calculateError(idealOutput);
			backPropagate();
		}

		/* Return output layer */
		return activations[activations.length - 1];
		
	}
	
	public void calculateError(Matrix idealOutput) {
		

		
		
		error[error.length - 1] = idealOutput.subtract(activations[activations.length - 1]);
		//for(int i = 0; i < error[error.length - 1].getHeight(); i++) {
		//	boolean negative = error[error.length - 1].getValue(i, 0) < 0;
		//	error[error.length - 1].setValue(i, 0, error[error.length - 1].getValue(i, 0) * error[error.length - 1].getValue(i, 0));
		//	if(negative) error[error.length - 1].setValue(i, 0, -1 * error[error.length - 1].getValue(i, 0));
		//}
		for(int i = error.length - 2; i >= 0; i--) {
			error[i] = weights[i+1].transpose().multiply(error[i+1]);	
		}	
	}
	
	public void backPropagate() {
		for(int i = activations.length - 1; i > 0; i--) {
			Matrix scaledError = error[i-1].multiply(learningRate);
			Matrix delta = new Matrix(activations[i].getHeight(), activations[i].getWidth());
			for(int m = 0; m < delta.getHeight(); m++) {
				for(int n = 0; n < delta.getWidth(); n++) {
					delta.setValue(m, n, activations[i].getValue(m, n) * (1 - activations[i].getValue(m, n)));
				}
			}
			delta = delta.elementWise(scaledError);
			biases[i-1] = biases[i-1].add(delta); //Adjust biases
			
			delta = delta.multiply(activations[i-1].transpose()); 
			weights[i-1] = (weights[i-1].add(delta));//Adjust weights

		}
		
	}
	
	public void print() {
		System.out.println("Input: ");
		activations[0].print();
		for(int i = 0; i < layers+1; i++) {
			weights[i].print();
			biases[i].print();
		}
		System.out.println("----------\nActivations:\n");
		for(Matrix m: activations) {
			m.print();
		}
		System.out.println("----------\nErrors:\n");
		for(Matrix m: error) {
			m.print();
		}
		
		
		
	}
	
	private void sigmoid(Matrix m) {
		for(int i = 0; i < m.getHeight(); i++) {
			for(int j = 0; j < m.getWidth(); j++) {
				m.setValue(i, j, sigmoid(m.getValue(i, j)));
			}
		}
	}
	
	
	
	private double sigmoid(double x) {
		return (1 / (1 + Math.pow(Math.E, -x)));
	}
	
	/*Accessors*/
	public void setWeights(Matrix[] weights) {
		this.weights = weights;
	}
	
	public void setBiases(Matrix[] biases) {
		this.biases = biases;
	}
	
	public Matrix[] getWeights() {
		return this.weights;
	}
	
	public Matrix[] getBiases() {
		return this.biases;
	}
	
	public Matrix[] getActivations() {
		return this.activations;
	}
	
	public int getInputSize() {
		return this.inputSize;
	}
	
	public int getOutputSize() {
		return this.outputSize;
	}
	
	public void setLayers(int layers) {
		this.layers = layers;
	}
	
	public int getLayers() {
		return this.layers;
	}
	
	public void setError(int index, Matrix error) {
		this.error[index] = error;
	}
	
	public Matrix[] getError() {
		return this.error;
	}
	
	public void setLayerHeight(int layerHeight) {
		this.layerHeight = layerHeight;
	}
	
	public int getLayerHeight() {
		return this.layerHeight;
	}
	
	public void setLearningRate(double lr) {
		this.learningRate = lr;
	}
	
	public double getLearningRate() {
		return this.learningRate;
	}
	
}
