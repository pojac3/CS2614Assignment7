import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		//getting the directory/file name
		Scanner userInput = new Scanner(System.in);
		System.out.println("Please enter the name of the file or directory containing the VM instructions: ");
		String inputName = new String(userInput.next());
		userInput.close();
		
		//if fileName has an extension, then its a file, otherwise its a directory. Lets determine that now and have a boolean value "isFile"
		//that is a 1 if the name is a directory and a 0 if it is a file
		boolean isFile = inputName.contains(".");
		
		if (isFile) {
			//since we now know if the input is a directory or not, lets trim the extension off of inputName
			inputName = inputName.substring(0, inputName.indexOf("."));
		}
	}

}
