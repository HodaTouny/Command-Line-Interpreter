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

    public String pwd() {return currentDirectory.toString();
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
        if(args.length == 0){
            System.err.println("mkdir : missing operand.");
            return;
        }
        for (String dirName : args) {
            Path newDir = currentDirectory.resolve(dirName);
            File newDirFile = newDir.toFile();
            if (!newDirFile.exists()) {
                if (!newDirFile.mkdir()) {
                    System.out.println("mkdir: Filed to create directory '" + dirName + "'.");
                }
            } else {
                System.out.println("mkdir: cannot create directory '" + dirName + "': File exists");
            }
        }
    }


    public void rmdir(String[] args) {
        if(args.length == 0){
            System.err.println("mkdir : missing operand.");
            return;
        }
        for (String dirName : args) {
            Path dirPath = currentDirectory.resolve(dirName);
            File dir = dirPath.toFile();
            if (dir.exists() && dir.isDirectory()) {
                if (!dir.delete()) {
                    System.out.println("rmdir: Filed to delete directory '" + dirName + "'.");
                }
            }else {
                System.out.println("rmdir: failed to remove '" + dirName + "': No such file or directory");
            }
        }
    }

    public void cp(String[] args){
        File file = new File(args[0]);
        File file2 = new File(args[1]);
        if(args.length > 2){
            System.err.println("cp: take more only 2 arguments");
            return;
        }else if(!file.exists() || !file.isFile()){
            System.err.println("cp: The second argument is not a file or doesn't exist");
            return;
        }else if(!file2.exists() || !file2.isFile()){
            System.err.println("cp: The second argument is not a file or doesn't exist");
            return;
        }


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
            fileWriter.close();
            fileReader.close();

        }catch(IOException e){
            System.err.println("cp: error while reading or writing in the file");
        }
    }

    public void cpRecursive(String[] args) {
        File srcDir = new File(args[0]);
        File destDir = new File(args[1]);

        if(args.length > 2){
            System.err.println("cp -r: take more only 2 arguments");
            return;
        }else if(!srcDir.exists() || !srcDir.isDirectory()){
            System.err.println("cp -r: The Source argument is not a Directory or doesn't exist");
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
            System.err.println("cp -r: error while reading or writing in the directory");
        }
    }


    public void wc(String[] args){
        for (String arg : args) {
            File file = new File(arg);
            if(!file.exists() || !file.isFile()){
                System.err.println("wc: argument is not a file or doesn't exist");
                return;
            }

            int numOfWord = 0, numOfLines = 0, numOfChars = 0;
            try {
                BufferedReader fileReader = new BufferedReader(new FileReader(arg));
                String line = fileReader.readLine();
                while (line != null) {
                    numOfLines++;
                    String[] words = line.split("\\s");
                    numOfWord += words.length;
                    numOfChars += line.replace(" ", "").length();
                    line = fileReader.readLine();
                }
                fileReader.close();
                System.out.println(numOfLines + " " + numOfWord + " " + numOfChars + " " + arg);
            } catch (IOException e) {
                System.err.println("wc: error while reading from the file");
            }
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
        for (String arg : args) {
            try {
                File file = new File(arg);
                if (!file.createNewFile()) {
                    System.out.println("touch: File already exists at the specified path.");
                }
            } catch (Exception e) {
                System.err.println("touch: An error occurred.");
            }
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
                System.err.println("cat: file'" + fileName + "' doesn't exist.");
            }
        }
    }

    public void rm(String[] args) {
        String fileName = args[0];
        try {
            File file = new File(fileName);
            if (!file.delete()) {
                System.out.println("rm: file '" + fileName + "' doesn't exist");
            }
        } catch (Exception e) {
            System.err.println("rm: An error occurred while removing.");
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




