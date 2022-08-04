package Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Transaction {
	QueryCheck queryCheck = new QueryCheck();

	public void getTransaction() {
		System.out.println("------ Enter your transaction queries");
		Scanner sc = new Scanner(System.in);
		String queryString = "";
		List<String> queryList = new ArrayList<>();
		while (true) {
			queryString = sc.nextLine();
			if (queryString.equalsIgnoreCase("commit")||queryString.equalsIgnoreCase("commit;")) {
				boolean flag = runTransaction(queryList, false);
				if (flag) {
					boolean status = runTransaction(queryList, true);
					if(status) {
						System.out.println("Transaction commit success");
					}else {
						System.out.println("Error in transaction");
					}
				}
				break;
			}

			else if (queryString.equalsIgnoreCase("rollback")) {
				boolean flag = runTransaction(queryList, false);
			} else if (queryString.equalsIgnoreCase("end") || queryString.equals("")) {
				break;
			} else {
				queryList.add(queryString);
			}
		}
	}

	boolean runTransaction(List<String> queryList, boolean doWrite) {
		boolean check = true;
		for (String query : queryList) {
			boolean status = queryCheck.queryCheck(query, doWrite);
			if (!status) {
				break;
			}
		}
		return check;
	}

}
