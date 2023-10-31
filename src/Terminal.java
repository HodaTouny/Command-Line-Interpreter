import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.StandardCopyOption;


public class Terminal {
    private final Path currentDirectory;

    private List<String> commandHistory = new ArrayList<>();

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
        for (int i = directoryContents.length - 1; i >= 0; --i) {
            if (directoryContents[i].isFile() || directoryContents[i].isDirectory()) {
                System.out.println(directoryContents[i].getName());
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

    public void cpRecursive(String[] args) {
        String sourceDir = args[0];
        String destinationDir = args[1];

        File srcDir = new File(sourceDir);
        File destDir = new File(destinationDir);

        if (!srcDir.exists() || !srcDir.isDirectory()) {
            System.out.println("Source directory does not exist or is not a directory.");
            return;
        }

        try {
            if (!destDir.exists()) {
                destDir.mkdirs();
            }

            File[] files = srcDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        String[] temp= {file.getAbsolutePath(), destDir + File.separator + file.getName()};
                        cpRecursive(temp);
                    } else {
                        Files.copy(file.toPath(), new File(destDir, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        } catch (IOException e) {
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
    public void history() {
        int cnt = 1;
        for (String command : commandHistory) {
            System.out.println(cnt + " " + command);
            cnt++;
        }

    }
    public void touch(String[] args) {
        String fileName = args[0];
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }

    public void cat(String... args) {
        for (String fileName : args) {
            File file = new File(fileName);
            if (file.exists()) {
                try {
                    BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
                    String line = fileReader.readLine();
                    while (line != null) {
                        System.out.println(line);
                        line = fileReader.readLine();
                    }
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("File '" + fileName + "' does not exist.");
            }
        }
    }

    public void rm(String[] args) {
        String fileName = args[0];
        try {
            File file = new File(fileName);
            if (file.delete()) {
                System.out.println("File deleted: " + file.getName());
            } else {
                System.out.println("File does not exist.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred.");
        }
    }



    public void chooseCommandAction(String commandName, String[] commandArgs) {
            String fullCommand = commandName;
            if (commandArgs.length > 0) {
                fullCommand += " " + String.join(" ", commandArgs);
            }
            commandHistory.add(fullCommand);
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
                touch(commandArgs);
                break;
            case "cp":
                cp(commandArgs);
                break;
            case "cp -r":
                cpRecursive(commandArgs);
                break;
            case "rm":
                rm(commandArgs);
                break;
            case "cat":
                cat(commandArgs);
                break;
            case "wc":
                wc(commandArgs);
                break;
            case "exit":
                System.exit(0);
                break;
//            case ">":

//            break;
//            case ">>":

//                break;
            case "history":
               history();
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




