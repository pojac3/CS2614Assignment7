import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Translator {
	
	//The list of files. This should not contain any directories or any files that do not have the .vm extension
	//This is because these files get filtered out in Driver. 
	ArrayList<File> files = new ArrayList<File>();
	
	//This is our current total list of lines. At the end of Driver this will get parsed into a single .vm file
	ArrayList<String> vmFile = new ArrayList<String>();
	
	//Keeping track of where we are on the stack
	int stack = 256;

	public void parse(File file) throws IOException {
		files.add(file);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String currentLine = br.readLine();
		while (!currentLine.equals(null)) {
			String[] split;
			if (currentLine.contains("push")) {
				split = currentLine.split(" ");
				if (currentLine.contains("constant")) {
					vmFile.add("@" + split[2]);
					vmFile.add("D=A");
				} else if (currentLine.contains("local")) {
					vmFile.add("@" + split[2]);
					vmFile.add("@" + split[2]);
				}
				
				
			}
		}
		
		br.close();
	}

}
