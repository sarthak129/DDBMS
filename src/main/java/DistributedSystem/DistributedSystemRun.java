package DistributedSystem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import Query.GlobalConfig;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class DistributedSystemRun {

	static GlobalConfig globalConfig = new GlobalConfig();

	/*
	 * vm1 ip - 35.193.195.113 vm1 userName - sarthakpatel0301
	 */
	public static ChannelSftp connectVM(String userName, String ip) {
		Session session = null;
		ChannelSftp channel = null;
		try {
			JSch jsch = new JSch();
			jsch.setKnownHosts("/home/sarthakpatel0301/.ssh/authorized_keys");
			jsch.addIdentity("/home/sarthakpatel0301/.ssh/id_rsa");
//			jsch.setKnownHosts("C://Users/sarth/.ssh/known_host");
//			jsch.addIdentity("C://Users/sarth/.ssh/id_rsa/sarth");
			session = jsch.getSession(userName, ip, 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return channel;
	}

	public static String getFileData(String path) {
		ChannelSftp channel = null;
		String data = "";
		try {
			channel = connectVM("sarthakpatel0301", GlobalConfig.getVmIP());
			InputStream inputStream = channel.get(path);
			byte[] bytes = inputStream.readAllBytes();
			data = new String(bytes);
		} catch (SftpException | IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	public static void uploadFileData(String path, String data, boolean append) {
		ChannelSftp channel = null;
		try {
			channel = connectVM("sarthakpatel0301", GlobalConfig.getVmIP());
			String upload = "";
			if (append) {
				upload = getFileData(path) + data;
			} else {
				upload = data;
			}
			channel.put(new ByteArrayInputStream(upload.getBytes()), path);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	public static boolean isFileAvailable(String path, String name) {
		ChannelSftp channel = null;
		boolean status = false;
		Vector<?> filelist;
		try {
			channel = connectVM("sarthakpatel0301", GlobalConfig.getVmIP());
			filelist = channel.ls("/home/sarthakpatel0301/uploadHere");

			for (int i = 0; i < filelist.size(); i++) {
				LsEntry entry = (LsEntry) filelist.get(i);
				if (entry.getFilename().equals(name)) {
					status = true;
				}
				System.out.println(entry.getFilename());
			}
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return status;
	}

	public static void createFolder(String path) {
		ChannelSftp channel = null;
		try {
			channel = connectVM("sarthakpatel0301", GlobalConfig.getVmIP());
			channel.mkdir(path);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}
}
