package UserManagement;

import Query.GlobalConfig;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class UserProfile
{
    private GlobalConfig globalConfig = new GlobalConfig();
    private String basePath = globalConfig.getBasePath();
    private String filePathSeparator = globalConfig.getPathSeparator();

    public void createUserProfileFile() throws IOException {

        File fileWriter = new File( basePath + "User@Profile.txt");
        if (!fileWriter.exists() && !fileWriter.isFile()) {
            boolean writerNewFile = fileWriter.createNewFile();
            if (writerNewFile)
            {
                System.out.println();
                System.out.println("Location of User_Profile file is at " + fileWriter.getCanonicalPath());
                System.out.println();
            }
        }
    }

}
