
public class ExtendedCommand extends Command{
	
	public ExtendedCommand(int arg1, int arg2, int arg3, boolean _parallel) {
		counter = arg1;
		counter += arg2;
		counter += arg3;
		parallel = _parallel;
	}
	
	@Override
	
	@Override
	public void update(){
		super.update();
		System.out.println("updating");
	}
}
