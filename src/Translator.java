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
		
		//Need to push memory address 256 to @SP first, because that is where the stack starts
		vmFile.add("@256");
		vmFile.add("D=A");
		vmFile.add("@SP");
		vmFile.add("M=D");
		
		
		String currentLine = br.readLine();
		while (currentLine != null) {
			if (!currentLine.contains("//")) {
				String[] split;
				if (currentLine.contains("push")) {
					split = currentLine.split(" ");

					//pushing a constant
					if (currentLine.contains("constant")) {
						vmFile.add("@" + split[2]);
						vmFile.add("D=A");

						//pushing a local variable
					} else if (currentLine.contains("local")) {
						vmFile.add("@LCL");
						for (int i = 0; i < Integer.parseInt(split[2]); i++) {
							vmFile.add("A=A+1");
						}
					}

					//These lines of assembly code will be for any push, the first few up there ^^
					//were just to get the correct value into the D register. Now we must push it to the stack
					vmFile.add("@SP");
					vmFile.add("A=M");
					vmFile.add("M=D");
					vmFile.add("@SP");
					vmFile.add("M=M+1");

				} else if (currentLine.contains("pop")) {
					split = currentLine.split(" ");
				
					//Implement Pop
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					if (currentLine.contains("local")) {
						vmFile.add("@LCL");
					} else if (currentLine.contains("argument")) {
						vmFile.add("@ARG");
					} else if (currentLine.contains("this")) {
						vmFile.add("@THIS");
					} else if (currentLine.contains("that")) {
						vmFile.add("@THAT");
					} else if (currentLine.contains("temp")) {
						vmFile.add("@TMP");
					}
					//for loop, gets us to the ith location of the current memory segment
					for (int i = 0; i < Integer.parseInt(split[2]); i++) {
						vmFile.add("A=M+1");
					}
					vmFile.add("M=D");
					
					
				} else if (currentLine.contains("add")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("A=A-1");
					vmFile.add("M=D+M");
				} else if (currentLine.contains("sub")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("A=A-1");
					vmFile.add("M=D-M");
				} else if (currentLine.contains("neg")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("M=-D");
				} else if (currentLine.contains("eq")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("A=A-1");
					vmFile.add("D=D-M"); //<- now, if D holds 0, the two values are equal. Otherwise, they are not
					vmFile.add("@EQUAL");
					vmFile.add("D;JEQ");
					vmFile.add("@NOTEQUAL");
					vmFile.add("D;JNE");
					vmFile.add("(EQUAL)");
					vmFile.add("@SP");
					vmFile.add("A=M");
					vmFile.add("A=A-1");
					vmFile.add("M=-1");
					vmFile.add("@FINISH");
					vmFile.add("0;JMP");
					vmFile.add("(NOTEQUAL)");
					vmFile.add("@SP");
					vmFile.add("A=M");
					vmFile.add("A=A-1");
					vmFile.add("M=0");
					vmFile.add("(FINISH)");
				} else if (currentLine.contains("lt")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("A=A-1");
					vmFile.add("D=D-M"); //<- now, if D holds 0, the two values are equal. Otherwise, they are not
					vmFile.add("@LESS");
					vmFile.add("D;JGT");
					vmFile.add("@NOTLESS");
					vmFile.add("D;JLE");
					vmFile.add("(LESS)");
					vmFile.add("@SP");
					vmFile.add("A=M");
					vmFile.add("A=A-1");
					vmFile.add("M=-1");
					vmFile.add("@FINISH");
					vmFile.add("0;JMP");
					vmFile.add("(NOTLESS)");
					vmFile.add("@SP");
					vmFile.add("A=M");
					vmFile.add("A=A-1");
					vmFile.add("M=0");
					vmFile.add("(FINISH)");
				} else if (currentLine.contains("gt")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("A=A-1");
					vmFile.add("D=D-M"); //<- now, if D holds 0, the two values are equal. Otherwise, they are not
					vmFile.add("@GREATER");
					vmFile.add("D;JLT");
					vmFile.add("@NOTGREATER");
					vmFile.add("D;JGE");
					vmFile.add("(GREATER)");
					vmFile.add("@SP");
					vmFile.add("A=M");
					vmFile.add("A=A-1");
					vmFile.add("M=-1");
					vmFile.add("@FINISH");
					vmFile.add("0;JMP");
					vmFile.add("(NOTGREATER)");
					vmFile.add("@SP");
					vmFile.add("A=M");
					vmFile.add("A=A-1");
					vmFile.add("M=0");
					vmFile.add("(FINISH)");
				} else if (currentLine.contains("and")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("A=A-1");
					vmFile.add("M=D&M");
				} else if (currentLine.contains("or")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("A=A-1");
					vmFile.add("M=D|M");
				} else if (currentLine.contains("not")) {
					vmFile.add("@SP");
					vmFile.add("M=M-1");
					vmFile.add("A=M");
					vmFile.add("D=M");
					vmFile.add("M=!D");
				}
				currentLine = br.readLine();
			} else {
				currentLine = br.readLine();
			}
			
		}
		
		//Adding end lines to the end of the asm file
		vmFile.add("(END)");
		vmFile.add("@END");
		vmFile.add("0;JMP");
		br.close();
	}

}
