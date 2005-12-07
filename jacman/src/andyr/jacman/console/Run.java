/*
 * Copyright 2005 The Jacman Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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