package Query;

import LogManage.LoggerRun;
import Query.Process.*;

public class QueryCheck {

	private Create parseCreate = new Create();
	private Use parseUse = new Use();
	private Insert parseInsert = new Insert();
	private Update parseUpdate = new Update();
	private Delete parseDelete = new Delete();
	private Select parseSelect = new Select();
	LoggerRun logRunner = new LoggerRun();
	GlobalConfig globalConfig = new GlobalConfig();

	public void queryFormatPrint() {
		System.out.println("create database <database_name>;");
		System.out.println("create database if not exists <database_name>;");
	}

	public boolean queryCheck(String queryString, boolean doWrite) {
		boolean isQueryValid = false;

		queryString = queryString.trim();
		if (globalConfig.getGlobalDatabase() != null) {
			logRunner.QueryLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), queryString);
		}
		if (queryString.toLowerCase().startsWith("create")) {
			if (parseCreate.check(queryString, doWrite)) {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"create", "success");
				System.out.println("Parse success create query");
				isQueryValid = true;
			} else {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"create", "failure");
				System.out.println("Parse error in create query");
			}
		} else if (queryString.toLowerCase().startsWith("use")) {
			if (parseUse.check(queryString)) {
				System.out.println("Parse success use query");
				isQueryValid = true;
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"use", "success");
			} else {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"use", "failure");
				System.out.println("Parse error in use query");
			}
		} else if (queryString.toLowerCase().startsWith("insert")) {
			if (parseInsert.check(queryString, doWrite)) {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"insert", "success");
				System.out.println("Parse success insert query");
				isQueryValid = true;
			} else {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"insert", "failure");
				System.out.println("Parse error in insert query");
			}
		} else if (queryString.toLowerCase().startsWith("select")) {
			System.out.println("Select query");
			if (parseSelect.check(queryString, doWrite)) {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"select", "success");
				System.out.println("Parse success select query");
				isQueryValid = true;
			} else {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"select", "failure");
				System.out.println("Parse error in select query");
			}
		} else if (queryString.toLowerCase().startsWith("update")) {
			System.out.println("update query");
			if (parseUpdate.check(queryString, doWrite)) {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"update", "success");
				System.out.println("Parse success update query");
				isQueryValid = true;
			} else {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"update", "failure");
				System.out.println("Parse error in update query");
			}
		} else if (queryString.toLowerCase().startsWith("delete")) {
			System.out.println("delete query");
			if (parseDelete.check(queryString, doWrite)) {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"delete", "success");
				System.out.println("Parse success delete query");
				isQueryValid = true;
			} else {
				logRunner.EventLogsWrite(globalConfig.getSessionUserName(), globalConfig.getGlobalDatabase(), "tb1",
						"delete", "failure");
				System.out.println("Parse error in delete query");
			}
		} else {
			System.out.println("Invalid query type");
		}

		return isQueryValid;
	}
}
