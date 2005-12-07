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

public class ConsoleBufferVO {
	private BufferedReader bufferedReader;

	private BufferedReader bufferedErrorReader;

	private BufferedWriter bufferedWriter;

	public ConsoleBufferVO() {
		bufferedReader = null;
		bufferedErrorReader = null;
		bufferedWriter = null;
	}

	public BufferedReader getBufferedErrorReader() {
		return bufferedErrorReader;
	}

	public void setBufferedErrorReader(BufferedReader bufferedErrorReader) {
		this.bufferedErrorReader = bufferedErrorReader;
	}

	public BufferedReader getBufferedReader() {
		return bufferedReader;
	}

	public void setBufferedReader(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	public void setBufferedWriter(BufferedWriter bufferedWriter) {
		this.bufferedWriter = bufferedWriter;
	}

}
