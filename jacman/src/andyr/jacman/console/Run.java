package andyr.jacman.console;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Run {

	public static ConsoleBufferVO getConsoleBufferVO(String[] cmd,
			String[] env, File dir) {

		ConsoleBufferVO consoleBufferVO = new ConsoleBufferVO();

		try {
			Process p = Runtime.getRuntime().exec(cmd, env, dir);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			BufferedReader bufferedErrorReader = new BufferedReader(
					new InputStreamReader(p.getErrorStream()));
			BufferedWriter bufferedWriter = new BufferedWriter(
					new OutputStreamWriter(p.getOutputStream()));

			consoleBufferVO.setBufferedReader(bufferedReader);
			consoleBufferVO.setBufferedErrorReader(bufferedErrorReader);
			consoleBufferVO.setBufferedWriter(bufferedWriter);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return consoleBufferVO;
	}

}