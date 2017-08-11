package server;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

import dao.*;
import daoImp.*;

public class Service {
	// ����һ�������
	DataOutputStream output = null;
	// ����һ��������
	DataInputStream input = null;

	SendMes sendMes = new SendMes();

	UserDao userDao = new UserDaoImp();

	public void getMethod(Socket socket) {
		String message = "";
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = socket.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			String temp = null;
			while ((temp = bufferedReader.readLine()) != null) {
				message += temp;
			}
			System.out.println(message);

		} catch (Exception e) {
		}
		String[] temp = message.split(" ");
		String method = temp[0];
		switch (method.toLowerCase()) {
		case "login":
			login(message, socket);
			break;
		case "register":
			register(message, socket);
			break;
		default:
			error(socket);
			break;
		}
	}

	private void error(Socket socket) {
		sendMes.send("Statement wrong", socket);
	}

	public void login(String message, Socket socket) {
		String[] temp = message.split(" ");
		try {
			String user_name = temp[1];
			String password = temp[2];
			if (userDao.login(user_name, password) == true)
				message = "login success";
			else
				message = "login false";
			sendMes.send(message, socket);
		} catch (Exception e) {
			e.printStackTrace();
			message = "login false";
			sendMes.send(message, socket);
		}
	}

	public void register(String message, Socket socket) {
		String[] temp = message.split(" ");
		try {
			String user_name = temp[1];
			String password = temp[2];
			int gender = Integer.valueOf(temp[3]);
			int age = Integer.valueOf(temp[4]);
			String tel = temp[5];
			String mail = temp[6];
			userDao.register(user_name, password, gender, age, tel, mail);
			message = "register success";
			sendMes.send(message, socket);
		} catch (Exception e) {
			e.printStackTrace();
			message = "register false";
			sendMes.send(message, socket);
		}

	}

}