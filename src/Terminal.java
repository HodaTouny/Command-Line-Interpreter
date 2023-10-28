import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
public class Terminal {
    private final Path currentDirectory;

    public Terminal() {
        currentDirectory = Paths.get(System.getProperty("user.dir"));
    }

    public String pwd() {
        return currentDirectory.toString();
    }

    public void echo(String args[]) {
        for (String arg : args) {
            System.out.print(arg + " ");
        }
        System.out.println();
    }

    public void ls() {
        File directory = currentDirectory.toFile();
        File[] directoryContents = directory.listFiles();
        Arrays.sort(directoryContents);
        for (File content : directoryContents) {
            if (content.isFile() || content.isDirectory()) {
                System.out.println(content.getName());
            }
        }
    }

    public void Lsr() {
        File directory = currentDirectory.toFile();
        File[] directoryContents = directory.listFiles();
        Arrays.sort(directoryContents);

        List<File> itemList = Arrays.asList(directoryContents);
        Collections.reverse(itemList);
        for (File content : itemList) {
            if (content.isFile() || content.isDirectory()) {
                System.out.println(content.getName());
            }
        }
    }

    public void mKdir(String[] args) {
        for (String dirName : args) {
            Path newDir = currentDirectory.resolve(dirName);
            File newDirFile = newDir.toFile();
            if (!newDirFile.exists()) {
                if (newDirFile.mkdir()) {
                    System.out.println("Directory '" + dirName + "' created successfully.");
                } else {
                    System.out.println("Failed to create directory '" + dirName + "'.");
                }
            } else {
                System.out.println("Directory '" + dirName + "' already exists.");
            }
        }
    }


    public void rmdir(String[] args) {
        for (String dirName : args) {
            Path dirPath = currentDirectory.resolve(dirName);
            File dir = dirPath.toFile();
            if (dir.exists() && dir.isDirectory()) {
                if (dir.delete()) {
                    System.out.println("Directory '" + dirName + "' deleted successfully.");
                } else {
                    System.out.println("Failed to delete directory '" + dirName + "'.");
                }
            } else {
                System.out.println("Directory '" + dirName + "' does not exist.");
            }
        }
    }

    public void cp(String[] args){
        String firstFile = args[0],secondFile = args[1];
        try{
            BufferedReader fileReader = new BufferedReader(new FileReader(firstFile));
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(secondFile));
            String line = fileReader.readLine();
            while(line != null){
                fileWriter.write(line);
                fileWriter.newLine();
                line = fileReader.readLine();
            }
            System.out.println("Files Copied Successfully");
            fileWriter.close();
            fileReader.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void wc(String[] args){
        String fileName = args[0];
        int numOfWord=0,numOfLines=0,numOfChars=0;
        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
            String line = fileReader.readLine();
            while(line != null){
                numOfLines++;
                String[] words = line.split("\\s");
                numOfWord += words.length;
                numOfChars += line.replace(" ","").length();
                line = fileReader.readLine();
            }
            fileReader.close();
            System.out.println(numOfLines + " " + numOfWord + " " + numOfChars + " " + fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public void chooseCommandAction(String commandName, String[] commandArgs) {
        switch (commandName) {
            case "echo":
                echo(commandArgs);
                break;
            case "pwd":
                System.out.println(pwd());
                break;
            case "cd":
                //cd(commandArgs);
                break;
            case "ls":
                ls();
                break;
            case "ls -r":
                Lsr();
                break;
            case "mkdir":
                mKdir(commandArgs);
                break;
            case "rmdir":
                rmdir(commandArgs);
                break;
            case "touch":
               // touch(commandArgs);
                break;
            case "cp":
                cp(commandArgs);
                break;
            case "cp -r":
                //cpRecursive(commandArgs);
                break;
            case "rm":
               // rm(commandArgs);
                break;
            case "cat":
               // cat(commandArgs);
                break;
            case "wc":
                wc(commandArgs);
                break;
            case "exit":
                System.exit(0);
                break;
            case ">":
                //redirectOutput(commandArgs);
                break;
            case ">>":
                //appendOutput(commandArgs);
                break;
            case "history":
                //displayHistory();
                break;
            default:
                System.out.println("Command not recognized");
        }
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(terminal.pwd() + ">");
            String input = scanner.nextLine();
            Parser parser = new Parser();
            if (parser.parse(input)) {
                String commandName = parser.getCommandName();
                String[] commandArgs = parser.getArgs();
                terminal.chooseCommandAction(commandName, commandArgs);
            }
        }
    }
}
