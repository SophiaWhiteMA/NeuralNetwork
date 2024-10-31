package cameron.white.main;

public class Generation {

	/*Fields*/
	private int size;
	private NeuralNetwork[] networks;
	
	/*Constructors*/
	public Generation(int size) {
		this.size = size;
		networks = new NeuralNetwork[size];
	}
	
	/*Mutators*/
	public void addNetwork(NeuralNetwork n) {
		for(NeuralNetwork m: networks) {
			if(m == null) {
				m = n;
				return;
			}
		}
		throw new RuntimeException("Cannot add neural network: generation is at capacity.");
	}
	
	public void removeNeuralNetwork() {
		for(int i = networks.length - 1; i >= 0; i--) {
			if(networks[i] != null) {
				networks[i] = null;
				return;
			}
		}
		throw new RuntimeException("Cannot remove neural network: generation is empty.");
	}
	
	public int getSize() {
		return this.size;
	}
	
}
