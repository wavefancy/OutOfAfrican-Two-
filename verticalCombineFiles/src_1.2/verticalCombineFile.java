package picb.wavefancy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This is class is used to combine file vertically,
 * also check the identity between files.
 * 
 * @author wavefancy@gmail.com
 * Date: 2011-11-12
 * 
 * Version 1.2
 * Add the function to combine multiple file at a time
 */
public class verticalCombineFile {

	public static void main(String[] args) {
		if (args.length < 5) {
			help();
		}
		
		int skip = Integer.parseInt(args[0].trim());
		String string = args[1].trim();
//		string = string.substring(1, string.length()-1);
		String sArr[] = string.split(",");
		int[] cols = new int[sArr.length];
		for (int i = 0; i < cols.length; i++) {
			cols[i] = Integer.parseInt(sArr[i]);
		}
		
		String outputFileName = args[2];
		
		File[] files = new File[args.length-3];
		for (int i = 0; i < files.length; i++) {
			files[i] = new File(args[i+3]);
		}
		
		new verticalCombineFile().combineAllFile(skip, cols, files, outputFileName);
	}
	
	public static void help() {
		System.out.println("--------------------------------");
		System.out.println("    verticalCombineFiles    version: 1.0     Author:wavefancy@gmail.com");
		System.out.println("--------------------------------");
		System.out.println("Usages: \nparameter1: How many column do you want to skipped?");
		System.out.println("parameter2: 1,2|1 identical columns between files, program will check the identity of these columns between files, and only combine and output the identical lines.");
		System.out.println("parameter3: output file name.");
		System.out.println("parameter4-n: input files.");
		System.out.println("Column index start from 1. Only output the lines all files share the same identity.");
		System.out.println("--------------------------------");

		System.exit(0);
	}
	
	public  void combineAllFile(int skip, int[] indentitiy, File[] inputFile, String outputFileName ) {
		List<File> tempList = new ArrayList<File>(inputFile.length);
		
		String dirString = System.getProperty("user.dir");
		File folder = new File(dirString);
		
		//shift the identity
		for (int i = 0; i < indentitiy.length; i++) {
			indentitiy[i]--;
		}
		
		try {
			File temp = File.createTempFile("temp_r"+0, ".txt",folder);
			combineTwoFiles(skip, indentitiy, inputFile[0], inputFile[1], temp);
			tempList.add(temp);
			
			for (int i = 2; i < inputFile.length; i++) {
				 temp = File.createTempFile("temp_r"+i, ".txt",folder);
				 combineTwoFiles(skip, indentitiy, tempList.get(tempList.size()-1) ,inputFile[i], temp);
				 tempList.add(temp);
			}
			
			//change file name and keep the file we want.
			tempList.get(tempList.size()-1).renameTo(new File(outputFileName));
			
			for (int i = 0; i < tempList.size()-1; i++) {
				tempList.get(i).deleteOnExit();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Combine two files.
	 * @param skip
	 * @param indentitiy. columns need to compare the identity.
	 * @param file1
	 * @param file2
	 * @param outFile
	 */
	public void combineTwoFiles(int skip, int[] indentitiy, File inFile1, File inFile2, File outFile ) {
		BufferedWriter writer =  null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(inFile1));
			
			int lines  = 0;
			//count the lines for the first file.
			while (reader.readLine() != null) {
				lines++;
			}
			
			reader = new BufferedReader(new FileReader(inFile1));
			
			String title = reader.readLine();
			
			String[][] contentCache = new String[lines-1][];
			String s = "";
			int l = 0;
			Map<String, Integer> indexMap = new HashMap<String, Integer>(lines);
			StringBuilder sBuilder = new StringBuilder();
			while ((s=reader.readLine())!= null) {
				contentCache[l] = s.split("\\s+");
				for (int i : indentitiy) {
					sBuilder.append(contentCache[l][i]);
					sBuilder.append("\t");
				}
				indexMap.put(sBuilder.toString().trim(), l);
				sBuilder.setLength(0);
				l++;
			}
			
			//read the second file.
			reader = new BufferedReader(new FileReader(inFile2));
			writer = new BufferedWriter(new FileWriter(outFile));
			
			//write the title.
			String[] sArr = null;
			sArr = reader.readLine().split("\\s+");
			writer.write(title);
			for (int i = skip; i < sArr.length; i++) {
				writer.write("\t");
				writer.write(sArr[i]);
			}
			writer.newLine();
			
			while ((s = reader.readLine())!= null) {
				sArr = s.split("\\s+");
				for (int i : indentitiy) {
					sBuilder.append(sArr[i]);
					sBuilder.append("\t");
				}
				
				Integer line = indexMap.get(sBuilder.toString().trim());
				sBuilder.setLength(0);
				
				if (line != null) {
					//copy the first file.
					for (String string : contentCache[line]) {
						sBuilder.append(string);
						sBuilder.append("\t");
					}
					//copy the second file.
					for (int i = skip; i < sArr.length; i++) {
						sBuilder.append(sArr[i]);
						sBuilder.append("\t");
					}
					
					writer.write(sBuilder.toString().trim());
					sBuilder.setLength(0);
					writer.newLine();
				}
			}
			
			writer.flush();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (reader != null) {
				try {
					reader.close();
					
					if (writer != null) {
						writer.flush();
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
