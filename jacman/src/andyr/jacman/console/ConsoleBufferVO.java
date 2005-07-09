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
