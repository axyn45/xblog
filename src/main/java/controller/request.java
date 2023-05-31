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
// import model.vo.User;

/**
 * Servlet implementation class LoginSevelet
 */
@WebServlet("/request")
public class request extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public request() {
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
		boolean success = false;
		String rep = "";

		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// TODO Auto-generated method stub
		String token = request.getParameter("token");
		String rq = request.getParameter("request");

		if(token==null||token==""){
			HashMap map = new HashMap<>();
			map.put("code", "-1");
			map.put("info", "No token provided");
			out.print(new Gson().toJson(map));
			out.flush();
			out.close();
			return;
		}

		String sql = "select * from xb_logins where cookie=?";
		PreparedStatement st = JDBC.getStatement(sql);
		
		int uid = 0;
		String uname = null;
		ResultSet rs = null;

		try {
			st.setString(1, token);
			System.out.println(st.toString());
			rs=JDBC.getResultSet(st);
			if (rs!=null&&rs.next()) {
				Timestamp tsnow = new Timestamp(System.currentTimeMillis());
				if (rs.getTimestamp(3).compareTo(tsnow) < 0) {
					HashMap map = new HashMap<>();
					map.put("code", "2");
					map.put("info", "Session expired! Please log in again.");
					out.print(new Gson().toJson(map));
					out.flush();
					out.close();
					return;
				} else{
					uid = rs.getInt(2);
					System.out.println("uid: "+uid);
				}

			} else {
				HashMap map = new HashMap<>();
				map.put("code", "1");
				map.put("info", "Invalid token!");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
				return;
			}
			System.out.println("bp1: "+uid);
			st = JDBC.getStatement("select * from xb_users where id=?");
			st.setInt(1, uid);
			rs = JDBC.getResultSet(st);
			if (rs.next()) {
				if (rs.getString(2) == null || rs.getString(2).equals("")) {
					HashMap map = new HashMap<>();
					map.put("code", "4");
					map.put("info", "User not found! Please contact your administrator.");
					out.print(new Gson().toJson(map));
					out.flush();
					out.close();
					return;
				} else{
					uname = rs.getString(2);
					System.out.println("uname: "+uname);
				}
			}
			System.out.println(uid + "," + token + "," + uname);
		} catch (Exception e) {
			System.out.println(e);
		}
		if (rq.equals("get_posts")) {
			String type = request.getParameter("type");
			try {

				sql = type.equals("own")?"select xb_posts.*,xb_users.uname from xb_posts,xb_users where xb_users.id=xb_posts.author and xb_posts.author=?":"select xb_posts.*,xb_users.uname from xb_posts,xb_users where xb_users.id=xb_posts.author and xb_posts.status=1";
				st = JDBC.getStatement(sql);
				if(type.equals("own")) st.setInt(1, uid);
				rs = JDBC.getResultSet(st);
				HashMap map = new HashMap<>();
				HashMap posts = new HashMap<>();
				map.put("code", "0");
				map.put("uid", uid);
				// map.put("posts", "Validated.");

				while (rs.next()) {
					HashMap post = new HashMap<>();
					post.put("author", rs.getString(7));
					post.put("title", rs.getString(3));
					post.put("content", rs.getString(4));
					post.put("status", rs.getInt(6));
					post.put("date", rs.getTimestamp(5).toString());
					posts.put(rs.getInt(1), post);
				}
				map.put("posts", posts);
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.print(e);
			}
		} else if (rq.equals("add_post")) {
			// String id=request.getParameter("pid");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			System.out.println(uname+",title: "+title);
			int status = Integer.parseInt(request.getParameter("publish"));
			sql = "insert into xb_posts (author, title,content,date,status) values(?,?,?,?,?)";
			st = JDBC.getStatement(sql);
			try {
				st.setInt(1, uid);
				st.setString(2, title);
				st.setString(3, content);
				st.setTimestamp(4, new java.sql.Timestamp(System.currentTimeMillis()));
				st.setInt(5, status);
				System.out.println("Add post query: "+JDBC.executeQuery(st));
				HashMap map = new HashMap<>();
				map.put("code", "0");
				map.put("info","post added");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.println(e);
				HashMap map = new HashMap<>();
				map.put("code", "-1");
				map.put("info","post add failed");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			}
		}
		else if (rq.equals("delete_post")) {
			int id = Integer.parseInt(request.getParameter("pid"));
			sql = "delete from xb_posts where id=? and author=?";
			st=JDBC.getStatement(sql);
			try{
				st.setInt(1, id);
				st.setInt(2, uid);
				System.out.println(JDBC.executeQuery(st));
				HashMap map = new HashMap<>();
				map.put("code", "0");
				map.put("info","post deleted");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();

			}catch(Exception e){
				System.out.println(e);
				HashMap map = new HashMap<>();
				map.put("code", "-1");
				map.put("info","post delete failed");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			}
		}else if (rq.equals("update_post")) {
			// String id=request.getParameter("pid");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			System.out.println(uname+",title: "+title);
			System.out.println(request.getParameter("publish")+", "+request.getParameter("pid")+", "+request.getParameter("content"));
			int status = Integer.parseInt(request.getParameter("publish"));
			int pid = Integer.parseInt(request.getParameter("pid"));
			sql = "update xb_posts set title=?,content=?,status=? where id=? ORDER BY date desc";
			st = JDBC.getStatement(sql);
			try {
				st.setString(1, title);
				st.setString(2, content);
				st.setInt(3, status);
				st.setInt(4, pid);
				System.out.println("Add post query: "+JDBC.executeQuery(st));
				HashMap map = new HashMap<>();
				map.put("code", "0");
				map.put("info","Post updated");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.println(e);
				HashMap map = new HashMap<>();
				map.put("code", "-1");
				map.put("info","post update failed");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			}
		}else if (rq.equals("set_status")) {
			int pid = Integer.parseInt(request.getParameter("pid"));
			int goal = Integer.parseInt(request.getParameter("goal"));
			if(goal!=1&&goal!=0){
				HashMap map = new HashMap<>();
				map.put("code", "1");
				map.put("info","Invalid status");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			}
			sql = "update xb_posts set status=? where id=?";
			st = JDBC.getStatement(sql);
			try {
				st.setInt(1, goal);
				st.setInt(2, pid);
				System.out.println("Update status query: "+JDBC.executeQuery(st));
				HashMap map = new HashMap<>();
				map.put("code", "0");
				map.put("info","Updated post "+pid+" to "+(goal==1?"PUBLISHED":"DRAFT"));
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			} catch (Exception e) {
				System.out.println(e);
				HashMap map = new HashMap<>();
				map.put("code", "-1");
				map.put("info","Publish status update failed");
				out.print(new Gson().toJson(map));
				out.flush();
				out.close();
			}
		}


		try {
			if (st != null) {
				st.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		// 最后发送响应

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
