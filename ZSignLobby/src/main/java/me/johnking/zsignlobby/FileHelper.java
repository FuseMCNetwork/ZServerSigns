package me.johnking.zsignlobby;

import java.io.*;

public class FileHelper {
	
	public static void stringToFile(File file, String string) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(string);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("resource")
	public static String stringFromFile(File file) {
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stringBuilder.toString();
	}
}
