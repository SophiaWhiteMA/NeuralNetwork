package cameron.white.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import cameron.white.algebra.Matrix;

public class Main {
	
	public static Random RNG = new Random();
	public static HashMap<NeuralNetwork, Double> generation;
	
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Loading training and test data into memory...");
		//Initialize the FileInputStreams
		FileInputStream trainingData = new FileInputStream("/home/cameron/Desktop/train-images.idx3-ubyte");
		FileInputStream trainingLabels = new FileInputStream("/home/cameron/Desktop/train-labels.idx1-ubyte");
		FileInputStream testData = new FileInputStream("/home/cameron/Desktop/t10k-images.idx3-ubyte");
		FileInputStream testLabels = new FileInputStream("/home/cameron/Desktop/t10k-labels.idx1-ubyte");
		for(int i = 0; i < 16; i++) {
			trainingData.read();
			testData.read();
		}
		for(int i = 0; i < 8; i++) {
			trainingLabels.read();
			testLabels.read();
		}

		
		//Load files into arrays
		byte[] trainingDataArray = new byte[60000*28*28];
		for(int i = 0; i < trainingDataArray.length; i++) {
			trainingDataArray[i] = (byte) trainingData.read();
		}
		
		byte[] trainingLabelsArray = new byte[60000];
		for(int i = 0; i < trainingLabelsArray.length; i++) {
			trainingLabelsArray[i] = (byte) trainingLabels.read();
		}
		
		byte[] testDataArray = new byte[60000*28*28];
		for(int i = 0; i < testDataArray.length; i++) {
			testDataArray[i] = (byte) testData.read();
		}
		
		byte[] testLabelsArray = new byte[60000];
		for(int i = 0; i < testLabelsArray.length; i++) {
			testLabelsArray[i] = (byte) testLabels.read();
		}
		
		System.out.println("Data loaded.");
		
		NeuralNetwork network = new NeuralNetwork(1	, 48, 784, 10, true);
		double accuracy = 0D;
		for(int epoch = 1; epoch <= 10; epoch++) {
			accuracy = trainNetwork(network, trainingDataArray, trainingLabelsArray);
			System.out.println("(" + epoch + ", " + accuracy + ")");
		}
		
		double accuracy2 = testNetwork(network, testDataArray, testLabelsArray);
		System.out.println("Test data accuracy: " + accuracy2);
		

		
	}
	
	public static double testNetwork(NeuralNetwork n, byte[] images, byte[] labels) {
		int imageIndex = 0;
		int labelIndex = 0;
		int attempts = 10000;
		int successes = 0;
		for(int i = 0; i < 10000; i++) {
			//Load data
			Matrix input = new Matrix(n.getInputSize(), 1);
			for(int j = 0; j < input.getHeight(); j++) {
				input.setValue(j, 0, ((double) images[imageIndex++]) / 255D);
			}
			Matrix idealOutput = new Matrix(10, 1);
			idealOutput.setValue(labels[labelIndex++], 0, 1);
			Matrix networkOutput = n.propagate(input, idealOutput, false);
			round(networkOutput);
			if(match(networkOutput, idealOutput)) {
				successes++;
			}
		}
		return 100D * ((double) successes) / ((double) attempts);
	}
	
	public static double trainNetwork(NeuralNetwork n, byte[] images, byte[] labels) {
		
		int imageIndex = 0;
		int labelIndex = 0;
		int attempts = 60000;
		int successes = 0;
		
		for(int i = 0; i < attempts; i++) {

			//Load data
			Matrix input = new Matrix(n.getInputSize(), 1);
			for(int j = 0; j < input.getHeight(); j++) {
				input.setValue(j, 0, ((double) images[imageIndex++]) / 255D);
			}
			Matrix idealOutput = new Matrix(10, 1);
			idealOutput.setValue(labels[labelIndex++], 0, 1);
			Matrix networkOutput = n.propagate(input, idealOutput, true);
			round(networkOutput);
			if(match(networkOutput, idealOutput)) {
				successes++;
			}
		}
		
		return 100D * ((double) successes) / ((double) attempts);

	}

	public static NeuralNetwork breed(NeuralNetwork a, NeuralNetwork b) {
		
		NeuralNetwork[] inputs = new NeuralNetwork[] {a, b};
		NeuralNetwork output = new NeuralNetwork(inputs[RNG.nextInt(2)].getLayers(),
				inputs[RNG.nextInt(2)].getLayerHeight(),
				784,
				10, true);
		
		//Mutation (chance = 10%)
		if(RNG.nextInt(10) == 0) {
			if(RNG.nextInt(2) == 0) {
				output.setLayers(RNG.nextInt(3) + 1);
			} else {
				output.setLayerHeight(RNG.nextInt(100) + 1);
			}
		}
		
		return output;
	}
	
	private static boolean match(Matrix output, Matrix idealOutput) {
		for (int i = 0; i < output.getHeight(); i++) {
			for (int j = 0; j < output.getWidth(); j++) {
				if (output.getValue(i, j) != idealOutput.getValue(i, j))
					return false;
			}
		}
		return true;
	}

	public static void round(Matrix m) {
		for (int i = 0; i < m.getHeight(); i++) {
			if (m.getValue(i, 0) >= 0.5) {
				m.setValue(i, 0, 1D);
			} else {
				m.setValue(i, 0, 0D);
			}
		}
	}

}
