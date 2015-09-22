package ide.mainpanel;

import java.util.ArrayList;
import java.util.List;

import language.Connection;
import language.Piece;

public class FunctionData {
	private List<Piece> inputConnections;
	private List<Connection> outputConnections;
	
	public FunctionData(){
		this(0,0);
	}
	public FunctionData(int inputs, int outputs){
		inputConnections = new ArrayList<>();
		outputConnections = new ArrayList<>();
		
		setInputNumber(inputs);
		setOutputNumber(outputs);
	}
	public void setInputNumber(int inputs){
		if(inputs < 0) throw new IllegalArgumentException("inputs must be >= 0");
		
		while(inputConnections.size() < inputs){
			inputConnections.add(null);
		}
		while(inputConnections.size() > inputs){
			inputConnections.remove(inputConnections.size()-1);
		}
	}
	public void setOutputNumber(int outputs){
		if(outputs < 0) throw new IllegalArgumentException("outputs must be >= 0");
		while(outputConnections.size() < outputs){
			outputConnections.add(new Connection(null, outputConnections.size()));
		}
		while(outputConnections.size() > outputs){
			outputConnections.remove(outputConnections.size()-1);
		}
	}
	public int getInputNumber() {
		return inputConnections.size();
	}
	public int getOutputNumber(){
		return outputConnections.size();
	}
}
