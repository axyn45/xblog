package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tools.JDBC;

/**
 * Servlet implementation class RegisterServelet
 */
@WebServlet("/register.do")
public class RegisterServelet extends HttpServlet {
	private static final long serialVersionUID = 1L; 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServelet() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uname = request.getParameter("uname");
		String pass = request.getParameter("pass");
		String email = request.getParameter("email");
		String fname = request.getParameter("fname");
		String sname = request.getParameter("sname");
		// String role = request.getParameter("role");

		System.out.println(uname+","+pass+","+email+","+fname+","+sname);
		try {
			// 创建 SQL 查询语句
		    String sql = "INSERT INTO xb_users (uname, pass, email, first_name, second_name,role,status) VALUES (?, ?, ?, ?, ?,?,?)";
		    PreparedStatement statement = JDBC.getStatement(sql);
		    statement.setString(1, uname);
		    statement.setString(2, pass);
		    statement.setString(3, email);
		    statement.setString(4, fname);
			statement.setString(5, sname);
			statement.setString(6, "user");
		    statement.setInt(7, 1);

		    // 执行 SQL 查询
		    int rowsAffected = statement.executeUpdate();
		    System.out.println(rowsAffected + " record(s) inserted.");
		    response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
		    if (rowsAffected == 1) {
		    	out.print("success");
		    } else {
		    	out.print("学号或密码错误！");
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
