package UserManagement;

import Query.GlobalConfig;

import java.io.*;
import java.util.Scanner;

public class UserLogin {

    static Scanner sc = new Scanner(System.in);
    static GlobalConfig globalConfig = new GlobalConfig();
    static String delimiter = globalConfig.getDelimiter();
    static String username ="";
    private String basePath = globalConfig.getBasePath();
    private String filePathSeparator = globalConfig.getPathSeparator();

    UserRegistration userRegistration = new UserRegistration();

    public boolean loginUser()
    {
        boolean logged = false;
        try
        {
            System.out.print("Please enter your username: ");
            username = sc.nextLine();
            String toCheckUser = Hashing.username(username);
            if (userRegistration.checkUserExists(toCheckUser))
            {
                System.out.println("User exists in the database.");
                System.out.print("Please enter your password: ");
                String password = sc.nextLine();
                String toCheckPass = Hashing.password(password);
                if (validateUserProfile(toCheckUser, toCheckPass))
                {
                    System.out.println("Entered Password is correct.");
                    if (checkSecurityAnswer(toCheckUser))
                    {
                        System.out.println("User logged in successfully!");
                        System.out.println("================================= New session created for the user. Welcome  " + username + "! ==============================================");
                        globalConfig.setSessionUserName(username);
                        return true;
                    }
                    else
                    {
                        System.out.println("Entered a wrong security answer. Retry logging in again!");
                    }
                }
                else
                {
                    System.out.println("The username and password are incompatible. Please try again!");
                }
            }
            else
            {
                System.out.println("The user is not found in the database. Please register first!");

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return logged;
    }

    private boolean validateUserProfile(String username, String password)
    {
        boolean isValid = false;
        try
        {
            String eachUser = "";
            BufferedReader br = new BufferedReader(new FileReader(basePath +
                    "User@Profile.txt"));
            do {
                if ((eachUser = br.readLine()) == null) break;
                String[] allLines = eachUser.split("\n");
                for (String everyLine : allLines) {
                    String[] values = everyLine.split(delimiter);
                    if (values[0].equals(username)) {
                        if (values[1].equals(password)) {
                            isValid = true;
                        }
                    }
                }
            } while (true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return isValid;
    }

    private boolean checkSecurityAnswer(String username)
    {
        boolean isValid = false;
        String eachLine = "";
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(basePath+
                    "User@Profile.txt"));
            do {
                if ((eachLine = br.readLine()) == null) break;
                String[] allLines = eachLine.split("\n");
                for (String everyLine : allLines) {
                    String[] values = everyLine.split(delimiter);
                    if (values[0].equals(username)) {
                        System.out.println("Your security questions is: '" + values[2] + "'");
                        System.out.print("Please enter the answer to your security question: ");
                        String toCheckAns = sc.nextLine();
                        if (values[3].equals(toCheckAns)) {
                            isValid = true;
                        }
                    }
                }
            } while (true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return isValid;
    }

}


