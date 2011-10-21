package com.laazy.mysqlclusterwebapp;

import com.mysql.clusterj.ClusterJHelper;
import com.mysql.clusterj.Session;
import com.mysql.clusterj.SessionFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Servlet extends HttpServlet {
	private BlockingQueue<Session> q = new LinkedBlockingQueue<>();

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		Properties props = new Properties();
		props.setProperty("com.mysql.clusterj.connectstring", "localhost:1186");
		props.setProperty("com.mysql.clusterj.database", "dewey");
		props.setProperty("com.mysql.clusterj.connect.retries", "4");
		props.setProperty("com.mysql.clusterj.connect.delay", "5");
		props.setProperty("com.mysql.clusterj.connect.verbose", "1");
		props.setProperty("com.mysql.clusterj.connect.timeout.before", "30");
		props.setProperty("com.mysql.clusterj.connect.timeout.after", "20");
		props.setProperty("com.mysql.clusterj.max.transactions", "1024");

		SessionFactory factory = ClusterJHelper.getSessionFactory(props);
		for (int i = 0; i < 10; i++) {
			q.add(factory.getSession());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response
	) throws ServletException, IOException {

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//response.getOutputStream().print("You have 3 wishes.");

		Session session = null;
		try {
			session = q.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		User user = session.newInstance(User.class);
		user.setId((long) (Math.random() * 1000000000000l));
		user.setEmail("asdf@asdf.com");
		user.setPassword("asdfasdf");
		user.setSalt("0123456789");

		session.persist(user);

		q.offer(session);
	}
}
