package UserManagement;

import Query.GlobalConfig;

import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import DistributedSystem.DistributedSystemRun;

public class UserRegistration
{
    static GlobalConfig globalConfig = new GlobalConfig();
    static Scanner sc = new Scanner(System.in);
    static boolean userExists;
    static String delimiter = globalConfig.getDelimiter();
    private String basePath = globalConfig.getBasePath();
    private String filePathSeparator = globalConfig.getPathSeparator();

    public void registerUser() throws IOException {

        System.out.print("Please enter your username: ");
        String username = sc.nextLine();
        System.out.print("Please enter your password: ");
        String password = sc.nextLine();

        String hashedUserID = Hashing.username(username);
        String hashedPassword = Hashing.password(password);

        userExists = checkUserExists(hashedUserID);

        if (!userExists) {
            System.out.print("Please enter a security question: ");
            String securityQuestion = sc.nextLine();
            System.out.print("Please enter answer to your security question: ");
            String securityAnswer = sc.nextLine();
            FileWriter writer = new FileWriter(basePath + "User@Profile.txt", true);
            writer.write(hashedUserID + delimiter + hashedPassword + delimiter + securityQuestion + delimiter + securityAnswer + "\n");
            DistributedSystemRun.uploadFileData(basePath + "User@Profile.txt",hashedUserID + delimiter + hashedPassword + delimiter + securityQuestion + delimiter + securityAnswer + "\n", true);
            writer.close();
            System.out.println("User registered successfully.");
            System.out.println();
        } else {
            System.out.println("Sorry! The User already exists");

        }
    }

    public boolean checkUserExists(String hashedUserID) throws IOException
    {
        String line = "";
        boolean userExists = false;
        BufferedReader br = new BufferedReader(new FileReader(basePath +
                "User@Profile.txt"));
        if ((line = br.readLine()) != null) {
            do {
                String[] allLines = line.split("\n");
                for (String everyLine : allLines) {
                    String[] values = everyLine.split(delimiter);
                    if (values[0].equals(hashedUserID)) {
                        return true;
                    }
                }
            } while ((line = br.readLine()) != null);
        }
        return userExists;
    }
}