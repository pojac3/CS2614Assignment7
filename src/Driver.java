import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		
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
		
		for (int i = 0; i < tr.files.size(); i++) {
			System.out.println(tr.files.get(i).getName());
		}
	}
}
