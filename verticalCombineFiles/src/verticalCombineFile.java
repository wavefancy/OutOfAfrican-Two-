package picb.wavefancy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * This is class is used to combine file vertically,
 * also check the identity between files.
 * 
 * @author wavefancy@gmail.com
 * Date: 2011-11-12
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
		
		File[] files = new File[2];
		files[0] = new File(args[2]);
		files[1] = new File(args[3]);
		
		new verticalCombineFile().runApp(skip, cols, files, args[4]);
	}
	
	public static void help() {
		System.out.println("--------------------------------");
		System.out.println("    verticalCombineFiles    version: 1.0     Author:wavefancy@gmail.com");
		System.out.println("--------------------------------");
		System.out.println("Usages: \nparameter1: How many column do you want to skipped?");
		System.out.println("parameter2: 1,2|1 indentical columns between files, program will check the indentity of these columns between files, and only combine and output the indentical lines.");
		System.out.println("parameter3: input file 1.");
		System.out.println("parameter4: input file 2.");
		System.out.println("parameter5: output file prefix.");
		System.out.println("Column index start from 1.");
		System.out.println("--------------------------------");

		System.exit(0);
	}
	
	public void runApp(int skip, int[] indentitiy, File[] files, String outputPrefix ) {
		BufferedWriter writer =  null;
		BufferedReader reader = null;
		
		//shift the identity
		for (int i = 0; i < indentitiy.length; i++) {
			indentitiy[i]--;
		}
		
		try {
			reader = new BufferedReader(new FileReader(files[0]));
			writer = new BufferedWriter(new FileWriter(new File(outputPrefix+"_combined.txt")));
			int lines  = 0;
			//count the lines for the first file.
			while (reader.readLine() != null) {
				lines++;
			}
			
			reader = new BufferedReader(new FileReader(files[0]));
			
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
			reader = new BufferedReader(new FileReader(files[1]));
			
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
