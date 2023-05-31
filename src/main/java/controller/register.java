package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tools.JDBC;
import tools.MailSender;
import tools.PassGen;

/**
 * Servlet implementation class RegisterServelet
 */
@WebServlet("/register.do")
public class register extends HttpServlet {
	private static final long serialVersionUID = 1L; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public register() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		String email = request.getParameter("email");
		String fname = request.getParameter("fname");
		String sname = request.getParameter("sname");
		// String role = request.getParameter("role");

		System.out.println(uname+","+pass+","+email+","+fname+","+sname);
		try {
			String sql = "select * from xb_users where uname=?";
			PreparedStatement statement=JDBC.getStatement(sql);
			statement.setString(1, uname);
			ResultSet rs=JDBC.getResultSet(statement);
			if(rs.next()){
				HashMap map = new HashMap<>();
				map.put("code", 1);
				map.put("info", "Username existed!");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
				return;
			}
			sql="select * from xb_users where email=?";
			statement=JDBC.getStatement(sql);
			statement.setString(1, email);
			rs=JDBC.getResultSet(statement);
			if(rs.next()){
				HashMap map = new HashMap<>();
				map.put("code", 2);
				map.put("info", "Email existed!");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
				return;
			}

			// 创建 SQL 查询语句
		    sql = "INSERT INTO xb_users (uname, pass, email, first_name, second_name,role,status,activation) VALUES (?, ?, ?, ?, ?,?,?,?)";
		    statement = JDBC.getStatement(sql);
		    statement.setString(1, uname);
		    statement.setString(2, pass);
		    statement.setString(3, email);
		    statement.setString(4, fname);
			statement.setString(5, sname);
			statement.setString(6, "user");
		    statement.setInt(7, 0);
			String activateKey=PassGen.getRandSHA256();
			statement.setString(8,activateKey);

		    // 执行 SQL 查询
		    int rowsAffected = statement.executeUpdate();
		    System.out.println(rowsAffected + " record(s) inserted.");
		    response.setContentType("text/html;charset=utf-8");
		    if (rowsAffected == 1) {
				MailSender ms=new MailSender();
				ms.setAddrList(email);
				ms.setSubject("激活你的xBlog账号");
				ms.setContent("点击<a href=\"https://dev.okkk.cc/xblog/activate?key="+activateKey+"\">激活</a>");
				ms.sendMail();
		    	HashMap map = new HashMap<>();
				map.put("code", 0);
				map.put("info", "注册成功！已向你的邮箱发送了一封激活邮件，请前往邮箱查看并激活你的账号");
				out.print(new Gson().toJson(map));
		    } else {
		    	HashMap map = new HashMap<>();
				map.put("code", 3);
				map.put("info", "Unknown error");
				out.print(new Gson().toJson(map));
		    }
			out.flush();
			out.close();
		} catch (SQLException e) {
		    // 处理 SQL 异常
		    e.printStackTrace();
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
