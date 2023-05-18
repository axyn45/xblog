package filter;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.vo.User;
import tools.JDBC;

public class PermissionFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		// 类型转换
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// 获取资源的uri
		String uri = req.getRequestURI();
		System.out.println("拦截到资源：" + uri);
		// 某些资源无需检查
		String[] notCheckList = { "login.do", "login.html", "register.do", "register.html", "about.html",
				"get_captcha", "dash.do","log.do","request","index.html"};
		for (String str : notCheckList) {
			if (uri.contains(str)) { // 直接放行
				chain.doFilter(request, response);
				return;
			}
		}
		// 过滤检查
		ServletContext contextGetter = request.getServletContext();
		String Id = (String) contextGetter.getAttribute("ID");
		Cookie[] cookies = req.getCookies();
		// System.out.println("Cookie stat: "+cookies.length);
		boolean check = false;
		PreparedStatement st = JDBC.getStatement("select * from xb_logins where cookie=?");
		ResultSet rs = null;
		if (cookies != null && Id != null) {
			for (Cookie cookie : cookies) {
				try {
					st.setString(1, cookie.getValue());
					rs = JDBC.getResultSet(st);
					while (rs.next()) {
						System.out.println("Response: "+cookie.getValue());
						if (rs.getString(1).equals(cookie.getValue()))
							check = true;
					}
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		}

		System.out.println(check);
		if (!check) {
			resp.sendRedirect("login.html");
			System.out.println("check failed:"+uri);
		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
