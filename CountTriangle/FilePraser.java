package hw3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FilePraser {
	public static void main(String[] args) throws IOException {
		inputParser();
	}

	public static void inputParser() throws IOException {
		PrintWriter writer = new PrintWriter("parsed_graph1.txt", "UTF-8");
		BufferedReader br = new BufferedReader(new FileReader("graph.txt"));
		StringBuilder sb = new StringBuilder();
		String line = br.readLine();
		String[] strs = line.split("\\s+");
		String previousKey = strs[0];
		sb.append(previousKey);
		sb.append(" ");
		sb.append(strs[1]);
		sb.append(" ");
		line = br.readLine();
		int degree = 1;

		while (line != null) {
			strs = line.split("\\s+");
			String key = strs[0];
			String value = strs[1];
			if (key.equals(previousKey)) {
				sb.append(value);
				sb.append(" ");
				degree++;
			} else {
				sb.append(degree);
				writer.write(sb.toString() + "\n");
				sb = new StringBuilder();
				previousKey = key;
				sb.append(previousKey);
				sb.append(" ");
				sb.append(value);
				sb.append(" ");
				degree = 1;
			}
			line = br.readLine();
		}
		br.close();
		writer.close();
	}

}
