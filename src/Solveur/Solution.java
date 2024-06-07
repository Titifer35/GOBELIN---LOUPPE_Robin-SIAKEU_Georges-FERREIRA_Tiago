	package Solveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//---------------------------------------------------------------------------------------------------
//	
//---------------------------------------------------------------------------------------------------

public class Solution {
	public static void main(String[] args) throws IOException {
		String[] cmdArray = {"python","SSCFLP.py","Donnees.json"};
		ProcessBuilder processBuilder = new ProcessBuilder(cmdArray);
		Process process = processBuilder.start();
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line = null;
		while ( (line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		String result = builder.toString();
		System.out.println(result);
	}
}