import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public void getFiles() {
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
        if (commandName.equals("echo")) {
            echo(commandArgs);
         } else if (commandName.equals("pwd")) {
        System.out.println(pwd());
       }
        else if (commandName.equals("ls")) {
            getFiles();
        }
        else if (commandName.equals("ls -r")) {
            Lsr();
        }
        else if (commandName.equals("mkdir")) {
           mKdir(commandArgs);
        } else if (commandName.equals("wc")) {
            wc(commandArgs);

        } else if (commandName.equals("exit")) {
            System.exit(0);
        } else {
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
