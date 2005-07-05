package andyr.jacman.console;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Run {

	public Run() {
	}

	public static String[] it(String s) {
                String returnVal[] = {"",""};
		try {
			Process p = Runtime.getRuntime().exec(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s2 = "";
			while ((s2=br.readLine())!=null) {
				//System.out.println(s2);
                                returnVal[0] += (s2+"\n");
			}
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			s2 = "";
			while ((s2=bre.readLine())!=null) {
				//System.out.println(s2);
                                returnVal[1] += (s2+"\n");
			}
		} catch(Exception e) {
                        //returnVal = e.getMessage();
			System.out.println("Executable not found.");
			e.printStackTrace();
		}
                return returnVal;
	}
        
	public static String[] it(String[] cmd, String[] env, File dir) {
                String returnVal[] = {"",""};
		try {
			Process p = Runtime.getRuntime().exec(cmd, env, dir);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String s2 = "";
			while ((s2=br.readLine())!=null) {
				//System.out.println(s2);
                                returnVal[0] += (s2+"\n");
			}
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			s2 = "";
			while ((s2=bre.readLine())!=null) {
				//System.out.println(s2);
                                returnVal[1] += (s2+"\n");
			}
		} catch(Exception e) {
                        //returnVal = e.getMessage();
			System.out.println("Executable not found.");
			e.printStackTrace();
		}
                return returnVal;
	}
        
        public static BufferedReader[] it(String[] cmd, String[] env, File dir, boolean useBufferedOutputs) {
                BufferedReader returnVal[] = {null,null};
		try {
			Process p = Runtime.getRuntime().exec(cmd, env, dir);
			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                        returnVal[0]=br;
                        returnVal[1]=bre;
		} catch(Exception e) {
                        //returnVal = e.getMessage();
			System.err.println("Executable not found.");
			e.printStackTrace();
		}
                return returnVal;
	}

	
}