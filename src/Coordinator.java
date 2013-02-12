import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * The Coordinator class uses the BufferedReader class to read in the contents of a text file
 * in order to attain the parameters for Processor objects. The class creates the Processor
 * objects and puts them into one of 10 queues depending on their priority level - these queues
 * represent a "ready" state for the processes. the class then removes the process from the 
 * highest priority queue that is not empty and it is assigned to a "running" Processor object.
 * It will remain in this state until the user-defined time-slice is up, or its IO interval is
 * up and it starts a simulated IO request. If it's time-slice is up, it returns to its ready
 * queue, if its IO interval is up, it is placed in an array list representing the "blocked" state.
 * Here, processes will carry out their simulated IO requests for a calculated duration. If all 
 * the processes in a higher priority ready queue are undergoing IO in the blocked list, it will
 * allow for processes in lower priority ready queues to enter the running state.
 * 
 * @author Karim Tabet
 * @version 1.0
 */
public class Coordinator 
{
	static int timeSlice; //For determining how long process has in running state.
	String ln; //For determining the line for the BufferedReader to read.
	Random random = new Random(); //For calculations involving random numbers.
	Process running = null; //For running state.
	static String processFile; //File containing processes to coordinate.
	ArrayList<Process> blocked = new ArrayList<Process>(); //For blocked state.
	Queue<Process> queueLevel1 = new LinkedList<Process>(); //For ready state.
	Queue<Process> queueLevel2 = new LinkedList<Process>();
	Queue<Process> queueLevel3 = new LinkedList<Process>();
	Queue<Process> queueLevel4 = new LinkedList<Process>();
	Queue<Process> queueLevel5 = new LinkedList<Process>();
	Queue<Process> queueLevel6 = new LinkedList<Process>();
	Queue<Process> queueLevel7 = new LinkedList<Process>();
	Queue<Process> queueLevel8 = new LinkedList<Process>();
	Queue<Process> queueLevel9 = new LinkedList<Process>();
	Queue<Process> queueLevel10 = new LinkedList<Process>();
	
	/**
	 * Constructs a Coordinator object.
	 * 
	 * @param timeSlice    Duration a process has in the running state.
	 */
	public Coordinator(int timeSlice)
	{
		this.timeSlice = timeSlice;
	}
	
	public static void main(String[] args) throws Exception
    {
		if (args.length != 2) {
			System.out.println("Coordinator Usage: java coordinator <Time Slice> <File to be Processed>");
			System.exit(0);
		}
		
		processFile = args[1];
		timeSlice = Integer.parseInt(args[0]);
		Coordinator coor = new Coordinator(timeSlice); //Change value to alter time-slice.
        coor.run();
    }
	
	/**
	 * The run() method uses the BufferedReader to read each token from the text file and assign 
	 * them to ID, priority value and CPU bound variables which are passed on as parameters for
	 * the createProcess(...) method. It also generates the IO duration and IO interval variables
	 * which are also passed on as parameters to the createProcess(...) method. Finally the
	 * coordinate() method is called.
	 */
	public void run()
	{
		int id;
		int pri;
		int cpu;
		int ioDur;
		double ioIntvl;
		
        try {
        	BufferedReader reader = new BufferedReader(new FileReader(processFile));   
        	while ((ln = reader.readLine()) != null) {
        		StringTokenizer line = new StringTokenizer(ln);
        		
        		id = Integer.parseInt(line.nextToken());
        		pri = Integer.parseInt(line.nextToken());
        		cpu = Integer.parseInt(line.nextToken());
        		ioDur = 5 + random.nextInt(11); //Random number between 5 and 15.
        		ioIntvl = (double) (5 + random.nextInt(11)) / 10 * cpu; //Random number between 0.5*CPU bound and 1.5*CPU Bound.
            
        		createProcess(id, pri, cpu, "ready", ioDur, ioIntvl);
            }
            reader.close();
        }         
        catch (FileNotFoundException fileNotFound) {
            System.out.println("Error: " + fileNotFound);
        }         
        catch (IOException inputOutput) {
            System.out.println("Error: " + inputOutput);
        }
		coordinate();
    }
	
	/**
	 * The createProcess(...) method creates new processes with the parameters passed on from the
	 * run() method and adds them to the queues with correlating priorities.
	 * 
	 * @param id    The unique ID for the Process.
	 * @param pri    The priority value of the Process.
	 * @param cpu    The CPU bound of the Process.
	 * @param state    The current state the Process is in.
	 * @param ioDur    The IO duration the Process has.
	 * @param ioIntvl    The interval between IO requests of the Process.
	 */
	public void createProcess(int id, int pri, int cpu, String state, int ioDur, double ioIntvl) 
	{
		switch(pri) {
            		case 1: queueLevel1.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 2: queueLevel2.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 3: queueLevel3.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 4: queueLevel4.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 5: queueLevel5.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 6: queueLevel6.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 7: queueLevel7.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 8: queueLevel8.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 9: queueLevel9.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
            		case 10: queueLevel10.add(new Process(id, pri, cpu, "ready", ioDur, ioIntvl)); break;
		}
	}
	
	/**
	 * The coordinate() method contains a loop that is undergone 10000 times, each iteration
	 * representing a ms of real-time. It first counts how many ms each process has been in the
	 * ready state. It then looks to see if there is a process in the running state, if not, it
	 * applies the highest priority process from the ready state to the running state. If there 
	 * is already a process in the running state, it begins decrementing the time-slice as well 
	 * as the IO interval time. If the time-slice finishes first, the process moves back to the 
	 * ready state and its time-slice and IO interval reset. If the IO interval finishes first, 
	 * the process is moved to the blocked state, and its time-slice and IO intervals reset. The 
	 * method also counts how many iterations the process has been in the running state (to 
	 * simulate time in ms). In the blocked state, the IO duration of the process is decremented, 
	 * and when it finishes the process is moved back to its correlating ready queue. The 
	 * blocked state can consist of many processes, all of which have the IO duration decremented 
	 * for each iteration. The number of iterations each process undergoes in the blocked state 
	 * is counted. Process details, including the time spent in each state, is printed by calling 
	 * the printQueue() method and the printDetails() method from the Process class.
	 */
	public void coordinate()
	{
		int tempTimeSlice = timeSlice;
		int tempIODuration = 0; 
		double tempIOInterval = 0;
		for (int i = 0; i < 10000; i++) {
			countTimeInReadyState();
			
			//Adds a Process to the running state if there is none already.
			if (running == null) {
				if (!queueLevel10.isEmpty()) {
					running = queueLevel10.poll();
					running.state = "running";
				} else if (!queueLevel9.isEmpty()) {
					running = queueLevel9.poll();
					running.state = "running";
				} else if (!queueLevel8.isEmpty()) {
					running = queueLevel8.poll();
					running.state = "running";
				} else if (!queueLevel7.isEmpty()) {
					running = queueLevel7.poll();
					running.state = "running";
				} else if (!queueLevel6.isEmpty()) {
					running = queueLevel6.poll();
					running.state = "running";
				} else if (!queueLevel5.isEmpty()) {
					running = queueLevel5.poll();
					running.state = "running";
				} else if (!queueLevel4.isEmpty()) {
					running = queueLevel4.poll();
					running.state = "running";
				} else if (!queueLevel3.isEmpty()) {
					running = queueLevel3.poll();
					running.state = "running";
				} else if (!queueLevel2.isEmpty()) {
					running = queueLevel2.poll();
					running.state = "running";
				} else if (!queueLevel1.isEmpty()) {
					running = queueLevel1.poll();
					running.state = "running";
				} else {
					System.out.println ("No available processes in ready state.");
				}
				tempIODuration = running.ioDuration;
				tempIOInterval = running.ioInterval;
				
			} else if (running != null) {
				running.printDetails();
				timeSlice--; //Decrements time-slice of Process while in running state.
				running.ioInterval--; //Decrements interval between IO requests.
				running.timeInStates();
				if (running.ioInterval <= 0) {
					running.ioInterval = tempIOInterval;
					timeSlice = tempTimeSlice; //Resets value of time-slice.
					running.state = "blocked"; //Changes state parameter of Process to "blocked."
					blocked.add(running); //Adds it to blocked state.
					running = null; //Removes it from running state.
				} else if (timeSlice == 0) {
					timeSlice = tempTimeSlice; //Resets value of time-slice.
					running.state = "ready"; //Changes state parameter of Process to "ready."
					createProcess (running.id, running.priorityVal, running.cpuBound, running.state, running.ioDuration, running.ioInterval); //Recreates the process and places it at the back of its coordinating queue.
					running = null; //Removes it from running state.
				}
			}
			
			if (!blocked.isEmpty()) {
				for (int j = 0; j < blocked.size(); j++) {
					Process proc = blocked.get(j);
					proc.ioDuration--; //Decrements duration of IO request.
					proc.timeInStates();
					proc.printDetails();
					if (proc.ioDuration == 0) {
						proc.ioDuration = tempIODuration; //Resets IO duration.
						proc.state ="ready"; //Changes state parameter of Process to "ready."
						createProcess (proc.id, proc.priorityVal, proc.cpuBound, proc.state, proc.ioDuration, proc.ioInterval); //Recreates the process and places it at the back of its coordinating queue.
						blocked.remove(j); //Removes it from blocked state.
					}
				}
			}
			printQueue();
		}
	}
	
	/**
	 * The countTimeInReadyState() method is called by the coordinator() method to count how many
	 * iterations (simulating a ms of real time) each process has been in the ready queue for. It
	 * does this by looking through every process in each ready queue and calling the 
	 * timeInStates() method from the Process class. Each process has to be taken off the queue and
	 * replaced on the back until a full cycle is complete, this is because the queue data structure 
	 * was used to represent the ready state. 
	 */
	public void countTimeInReadyState()
	{
		Process proce;
		if (!queueLevel1.isEmpty()) {
			for (int i = 0; i < queueLevel1.size(); i++) {
				proce = queueLevel1.poll();
				proce.timeInStates();
				queueLevel1.add(proce);
			}
		}
		if (!queueLevel2.isEmpty()) {
			for (int i = 0; i < queueLevel2.size(); i++) {
				proce = queueLevel2.poll();
				proce.timeInStates();
				queueLevel2.add(proce);
			}
		}
		if (!queueLevel3.isEmpty()) {
			for (int i = 0; i < queueLevel3.size(); i++) {
				proce = queueLevel3.poll();
				proce.timeInStates();
				queueLevel3.add(proce);
			}
		}
		if (!queueLevel4.isEmpty()) {
			for (int i = 0; i < queueLevel4.size(); i++) {
				proce = queueLevel4.poll();
				proce.timeInStates();
				queueLevel4.add(proce);
			}
		}
		if (!queueLevel5.isEmpty()) {
			for (int i = 0; i < queueLevel5.size(); i++) {
				proce = queueLevel5.poll();
				proce.timeInStates();
				queueLevel5.add(proce);
			}
		}
		if (!queueLevel6.isEmpty()) {
			for (int i = 0; i < queueLevel6.size(); i++) {
				proce = queueLevel6.poll();
				proce.timeInStates();
				queueLevel6.add(proce);
			}
		}
		if (!queueLevel7.isEmpty()) {
			for (int i = 0; i < queueLevel7.size(); i++) {
				proce = queueLevel7.poll();
				proce.timeInStates();
				queueLevel7.add(proce);
			}
		}
		if (!queueLevel8.isEmpty()) {
			for (int i = 0; i < queueLevel8.size(); i++) {
				proce = queueLevel8.poll();
				proce.timeInStates();
				queueLevel8.add(proce);
			}
		}
		if (!queueLevel9.isEmpty()) {
			for (int i = 0; i < queueLevel9.size(); i++) {
				proce = queueLevel9.poll();
				proce.timeInStates();
				queueLevel9.add(proce);
			}
		}
		if (!queueLevel10.isEmpty()) {
			for (int i = 0; i < queueLevel10.size(); i++) {
				proce = queueLevel10.poll();
				proce.timeInStates();
				queueLevel10.add(proce);
			}
		}
	}
	
	/**
	 * The printQueue() method is also called by the coordinator() method and is used to print out 
	 * all the details about each process by looking through every process in each ready queue and 
	 * calling the printDetails() method from the Process class. Each process has to be taken off 
	 * the queue and replaced on the back until a full cycle is complete (similarly to the 
	 * countTimeInStates() method), this is because the queue data structure was used to represent 
	 * the ready state. 
	 */
	public void printQueue()
	{
		for (int i = 0; i < queueLevel1.size(); i++) {
			Process proc = (queueLevel1.poll());
			proc.printDetails();
			queueLevel1.add(proc);
		}
		for (int i = 0; i < queueLevel2.size(); i++) {
			Process proc = (queueLevel2.poll());
			proc.printDetails();
			queueLevel2.add(proc);
		}
		for (int i = 0; i < queueLevel3.size(); i++) {
			Process proc = (queueLevel3.poll());
			proc.printDetails();
			queueLevel3.add(proc);
		}
		for (int i = 0; i < queueLevel4.size(); i++) {
			Process proc = (queueLevel4.poll());
			proc.printDetails();
			queueLevel4.add(proc);
		}
		for (int i = 0; i < queueLevel5.size(); i++) {
			Process proc = (queueLevel5.poll());
			proc.printDetails();
			queueLevel5.add(proc);
		}
		for (int i = 0; i < queueLevel6.size(); i++) {
			Process proc = (queueLevel6.poll());
			proc.printDetails();
			queueLevel6.add(proc);
		}
		for (int i = 0; i < queueLevel7.size(); i++) {
			Process proc = (queueLevel7.poll());
			proc.printDetails();
			queueLevel7.add(proc);
		}
		for (int i = 0; i < queueLevel8.size(); i++) {
			Process proc = (queueLevel8.poll());
			proc.printDetails();
			queueLevel8.add(proc);
		}
		for (int i = 0; i < queueLevel9.size(); i++) {
			Process proc = (queueLevel9.poll());
			proc.printDetails();
			queueLevel9.add(proc);
		}
		for (int i = 0; i < queueLevel10.size(); i++) {
			Process proc = (queueLevel10.poll());
			proc.printDetails();
			queueLevel10.add(proc);
		}
		for (int i = 0; i < blocked.size(); i++) {
			Process proce = blocked.get(i);
			proce.printDetails();
		}
	}
}