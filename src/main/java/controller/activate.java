package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.sql.ResultSet;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tools.JDBC;
import com.google.gson.Gson;

/**
 * Servlet implementation class LoginSevelet
 */
@WebServlet("/activate")
public class activate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public activate() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		System.out.println("Session id: " + session.getId());

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// TODO Auto-generated method stub
		String key = request.getParameter("key");

		String sql = "select * from xb_users where activation=?";
		PreparedStatement st = JDBC.getStatement(sql);
		// System.out.println("Hit dash!3");
		try {
			st.setString(1, key);
			ResultSet rs = JDBC.getResultSet(st);
			if(rs.next()) {
				if(rs.getInt(8)!=0){
                    HashMap map = new HashMap<>();
                    map.put("code", 2);
                    map.put("info", "Account has already activated!");
                    out.print(new Gson().toJson(map));
                    out.flush();
                    out.close();
                    return;
                }
                sql="update xb_users set status=1 where activation=?";
                st=JDBC.getStatement(sql);
                st.setString(1, key);
                System.out.println("Affected: "+st.executeUpdate());
			}else{
				HashMap map = new HashMap<>();
				map.put("code", 1);
				map.put("info", "Invalid activation link!");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
				return;
			}
			// st = JDBC.getStatement("select * from xb_users where id=?");
			// st.setInt(1, uid);
			// rs = JDBC.getResultSet(st);
			// if(rs.next()) {
			// 	if (rs.getString(2) == null || rs.getString(2).equals("")) {
			// 		HashMap map = new HashMap<>();
			// 		map.put("code", 3);
			// 		map.put("info", "User not found! Please contact your administrator.");
			// 		out.print(new Gson().toJson(map));
			// 		out.flush();
			// 		out.close();
			// 		return;
			// 	} else
			// 		uname = rs.getString(2);
			// }
			// System.out.println(uid + "," + token + "," + uname);
		} catch (Exception e) {
			System.out.print(e);
		}

		// 最后发送响应
		HashMap map = new HashMap<>();
		map.put("code", 0);
		map.put("info", "Successfully activated");
		out.print(new Gson().toJson(map));
		out.flush();
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
