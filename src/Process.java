/**
 * The Process class is used to identify individual Processes used in the Coordinator class. Each
 * Process is assigned a unique ID, a priority value, a CPU bound, an IO duration and an IO
 * interval. The time each Process spends in each state is also calculated and stored here. The
 * class also allows for entire Process details to be printed to the terminal window.
 * 
 * @author Karim Tabet
 * @version 1.0
 */
public class Process 
{
	public int id;
	public int priorityVal;
	public int cpuBound;
	public String state;
	public int ioDuration;
	public double ioInterval;
	public int timeInReady = 0;
	public int timeInRunning = 0;
	public int timeInBlocked = 0;
	
	/**
	 * Constructs a Process object.
	 * 
	 * @param id    The unique ID for the Process.
	 * @param pri    The priority value of the Process.
	 * @param cpu    The CPU bound of the Process.
	 * @param state    The current state the Process is in.
	 * @param ioDur    The IO duration the Process has.
	 * @param ioIntvl    The interval between IO requests of the Process.
	 */
	public Process(int id, int priorityVal, int cpuBound, String state, int ioDuration, double ioInterval) 
	{
		this.id = id;
		this.priorityVal = priorityVal;
		this.cpuBound = cpuBound;
		this.state = state;
		this.ioDuration = ioDuration;
		this.ioInterval = ioInterval;
	}
	
	/**
	 * The printDetails() method simply prints out all values associated with a Process object.
	 */
	public void printDetails()
	{
		System.out.println("ID: " + id + " - Priority: " + priorityVal + " - CPUBound: " + cpuBound 
				+ " - State: " + state + " - IODuration: " + ioDuration + " - IOInterval: " + ioInterval
				+ " - Time in Ready: " + timeInReady + " - Time in Running: " + timeInRunning 
				+ " - Time in Blocked: " + timeInBlocked);
	}
	
	/**
	 * The timeInStates() method checks to see what state the Process object is currently in and
	 * increments the value of the time spent in that state.
	 */
	public void timeInStates()
	{
		if (this.state.equals("ready")) {
			timeInReady++;
		}
		if (this.state.equals("running")) {
			timeInRunning++;
		}
		if (this.state.equals("blocked")) {
			timeInBlocked++;
		}
	}
}
