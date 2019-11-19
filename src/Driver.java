import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws IOException {
		
		Translator tr = new Translator();
		
		//getting the directory/file name
		Scanner userInput = new Scanner(System.in);
		System.out.println("Please enter the name of the file or directory containing the VM instructions: ");
		String inputName = new String(userInput.next());
		userInput.close();
		
		//if fileName has an extension, then its a file, otherwise its a directory. Lets determine that now and have a boolean value "isFile"
		//that is a 1 if the name is a directory and a 0 if it is a file
		File file = new File(inputName);
		
		if (file.isFile()) {
			if (file.getName().contains(".vm")) {
				//since we now know that the inputted path is pointing to a file, lets trim off the extension
				inputName = inputName.substring(0, inputName.indexOf("."));
				tr.parse(file);
			}
		} else if (file.isDirectory()) {
			//need to get the list of files inside this directory, luckily the File API has a method built in for that!
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				//looping through all of the files
				if (files[i].getName().contains(".vm")) {
					tr.parse(files[i]);
				}
			}
		}
		
		//now we need to go ahead and write everything from tr into a file
		//we will use a bufferedwriter for this
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(inputName + ".asm"));
		
		//looping through each line of parsed data inside of vmFile
		for(int i = 0; i < tr.vmFile.size(); i++) {
			bw.append(tr.vmFile.get(i));
			
			//this if statement is so we dont have an extra empty line at the end
			if (i + 1 < tr.vmFile.size()) {
				bw.append("\n");
			}
		}
		bw.close();
	}
}
