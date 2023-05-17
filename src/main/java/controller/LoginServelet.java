package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tools.JDBC;
import com.google.gson.Gson;
import tools.PassGen;
import model.vo.User;

/**
 * Servlet implementation class LoginSevelet
 */
@WebServlet("/login.do")
public class LoginServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServelet() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String captcha = request.getParameter("captcha");
		String autoLogin = request.getParameter("remember-me");
		System.out.println(id+","+pass+","+autoLogin);

		boolean success = false;
		String rep = "";
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		HttpSession session=request.getSession();
		System.out.println("Session id: "+session.getId());
		try {
		    // 创建 SQL 查询语句

			String sql = "SELECT * FROM xb_captchas WHERE session = ?";
			PreparedStatement statement = JDBC.getStatement(sql);
			statement.setString(1, session.getId());
			ResultSet resultSet = JDBC.getResultSet(statement);
			System.out.println("Remote captcha: "+captcha.toLowerCase());
			while(resultSet.next()){
				System.out.println("Backend captcha: "+resultSet.getString(2).toLowerCase());
				if(!resultSet.getString(2).toLowerCase().equals(captcha.toLowerCase())){
				out.print("验证码错误！");
				out.flush();
				out.close();
				return;
			}
			}


		    sql = "SELECT * FROM xb_users WHERE id = ? or uname=? or email=? AND pass = ?";

		    // 预编译 SQL 语句并设置参数值
		    statement = JDBC.getStatement(sql);
		    statement.setString(1, id);
			statement.setString(2, id);
			statement.setString(3, id);
		    statement.setString(4, pass);

		    // 执行 SQL 查询
		    resultSet = JDBC.getResultSet(statement);

		    // 若查询结果集不为空，则表明该用户存在
			// System.out.println("result: "+resultSet.next());
		    if (resultSet.next()) {
				if(!resultSet.getString(3).equals(pass)){
					out.print("用户名/邮箱或密码错误！");
					out.flush();
					out.close();
					return;
				}
				if(resultSet.getInt(8)==0){
					out.print("账号未激活！请前往你的邮箱进行激活");
					out.flush();
					out.close();
					return;
				}
				int uid=resultSet.getInt(1);
		    	rep = "success";
		    	success = true;
				//响应设置cookie
				ServletContext contextSetter = getServletContext();
				contextSetter.setAttribute("ID",id);
				int maxage=("true".equals(autoLogin) && success)?7*24*60*60:60*60;
				String token=PassGen.getSHA256(id+pass+System.currentTimeMillis()+maxage*1000+PassGen.addSalt());
				System.out.println("tk: "+token);
				sql = "INSERT INTO xb_logins (cookie, user, expire) VALUES (?, ?, ?)";
				statement=JDBC.getStatement(sql);
				statement.setString(1, token);
				statement.setInt(2, uid);
				statement.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()+maxage*1000));
				System.out.println(statement.toString());
				System.out.println(statement.execute());
				try {
					if (statement != null) {
						statement.close();
					}
				} catch (SQLException e) {
					// 处理关闭资源异常
					e.printStackTrace();
				}
				Cookie cookie = new Cookie("token",token);
				if("true".equals(autoLogin) && success) 
					cookie.setMaxAge(maxage);
				else cookie.setMaxAge(maxage);
				response.addCookie(cookie);
				System.out.println("已发放cookie: "+cookie.getValue());
		    } else {
		    	rep = "用户名/邮箱或密码错误！";
		    }
	    	
		} catch (SQLException e) {
		    // 处理 SQL 异常
		    e.printStackTrace();
		} 
		//最后发送响应
		out.print(rep);
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
