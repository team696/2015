import java.util.ArrayList;
import java.util.List;


public class GameMap {
	
	double[] robotOffset = new double[2];
	int feederStation1Stock = 10; 
	int feederStation2Stock = 10;
	List<double[]> stacks = new ArrayList<double[]>();
	
	public GameMap(double[] _robotOffset){
		robotOffset =_robotOffset;
	}
	
	public void placedStack(double[] _stack){
		stacks.add(_stack);
	}
	public List<double[]> getStacks(){
		return stacks;
	}
}
