import DataModel.DataModelRun;
import DistributedSystem.DistributedSystemRun;
import ExportData.ExportDataRun;
import LogManage.LoggerRun;
import Query.GlobalConfig;
import Query.QueryCheck;
import Query.Transaction;
import UserManagement.UserLogin;
import UserManagement.UserRegistration;

import java.io.IOException;
import java.util.Scanner;

import com.jcraft.jsch.ChannelSftp;

import Analytics.AnalyticsRun;

public class RunDDBMS {

	static Scanner scanner = new Scanner(System.in);

	static QueryCheck queryParse = new QueryCheck();
	static DataModelRun dataModelRun = new DataModelRun();
	static Transaction transaction = new Transaction();
	static ExportDataRun exportDataRun = new ExportDataRun();
	static UserLogin userLogin = new UserLogin();
	static UserRegistration userRegistration = new UserRegistration();
	static LoggerRun logRunner = new LoggerRun();
	static AnalyticsRun analytics = new AnalyticsRun();
	static DistributedSystemRun distributed = new DistributedSystemRun();

	static GlobalConfig globalConfig = new GlobalConfig();

	public static void main(String[] args) throws IOException {
		GlobalConfig.setVm(args[0]);
		GlobalConfig.setVmIP(args[1]);
		userManage();
	}

	public static void userManage() throws IOException {
		System.out.println("Please select one of the options below:");
		System.out.println("1. LOGIN USER");
		System.out.println("2. REGISTER NEW USER");
		System.out.println("3. EXIT");
		int option = scanner.nextInt();

		switch (option) {
		case 1: {
			if (userLogin.loginUser()) {
				operationMenu();
			}
			userManage();
			break;
		}
		case 2: {
			userRegistration.registerUser();
			userManage();
			break;
		}

		case 3: {
			System.out.println("Exiting the application...");
			System.exit(0);
			scanner.close();
			break;
		}
		default: {
			System.out.println("You entered an invalid input.");
			userManage();
			break;
		}
		}
	}

	public static void operationMenu() throws IOException {
		try {
			System.out.println("\nPlease choose one of the following options:");
			System.out.println("1. WRITE QUERIES");
			System.out.println("2. TRANSACTION");
			System.out.println("3. EXPORT");
			System.out.println("4. DATA MODEL");
			System.out.println("5. ANALYTICS");
			System.out.println("6. SHOW LOGS");
			System.out.println("7. LOGOUT");
			scanner = new Scanner(System.in);
			int option = scanner.nextInt();
			scanner.nextLine();

			switch (option) {
			case 1: {
				queryParse.queryFormatPrint();
				scanner = new Scanner(System.in);
				String queryString = scanner.nextLine();

				queryParse.queryCheck(queryString, true);
				break;
			}

			case 2: {
				transaction.getTransaction();
				break;
			}

			case 3: {
				System.out.println("Enter a Database you want to dump as sql");
				scanner = new Scanner(System.in);
				String dbName = scanner.nextLine();
				exportDataRun.generateDump(dbName);
				break;
			}

			case 4: {
				System.out.println("Enter a Database name to generate entity relation diagram");
				scanner = new Scanner(System.in);
				String erdString = scanner.nextLine();
				dataModelRun.generateERD(erdString);
				break;
			}

			case 5: {
				System.out.println("Your Analytics is right here!");
				System.out.println("Please choose any one of the following analytics you want to perform");
				System.out.println("1-> Count Queries");
				System.out.println("2-> Count Update Operations on a database");
				Scanner s = new Scanner(System.in);
				int analytic_choice = s.nextInt();
				s.nextLine();
				switch (analytic_choice) {
				case 1: {
					analytics.QueriesCount();
					break;
				}
				case 2: {
					System.out.println("Enter a Database name to generate analytics");
					String dbName = scanner.nextLine();
					analytics.updateCount(dbName);
					break;
				}
				}
				break;

			}

			case 6: {
				System.out.println("Your Logs Module is right here!");
				System.out.println("Please choose any one of the following logs you are looking for");
				System.out.println("1-> General Logs");
				System.out.println("2-> Event Logs");
				System.out.println("3-> Query Logs");
				Scanner s = new Scanner(System.in);
				int log_choice = s.nextInt();
				switch (log_choice) {
				case 1: {
					logRunner.GeneralLogsWrite("create", "db1", "2");
					break;
				}
				case 2: {
					logRunner.EventLogsWrite("user1", "db1", "tb1", "update", "success");
					break;
				}
				case 3: {
					logRunner.QueryLogsWrite("user2", "db1", "query3");
					break;
				}
				}

				break;

			}
			case 7: {
				userManage();
				break;
			}

			default: {
				System.out.println("You entered an invalid input.");
				operationMenu();
				break;
			}
			}
			System.out.println("Do you want to stay on our application and perform more actions?");
			System.out.println("Type yes or no");
			scanner = new Scanner(System.in);
			String operation = scanner.nextLine();

			if (operation.equalsIgnoreCase("yes")) {
				operationMenu();
			} else if (operation.equalsIgnoreCase("no")) {
				System.out.println("Exiting the application...");
				System.exit(0);
				scanner.close();
			} else {
				System.out.println("Invalid entry. Please enter either yes or no to continue!");
			}
		} catch (Exception e) {
			operationMenu();
		}
	}
}
