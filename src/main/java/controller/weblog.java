package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import controller.eventServer;
/**
 * Servlet implementation class testServelet
 */
@WebServlet("/log")
public class weblog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public weblog() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		eventServer sendServer=new eventServer();	// 创建log发送对象
		
		ServletContext contextGetter = request.getServletContext();

		// log格式为“操作类型, 时间戳”
		String action = request.getParameter("action");
		String time = request.getParameter("time");
		
		ServletContext contextSetter = getServletContext();
        String message = action +", "+time;
        contextSetter.setAttribute("message", message);
        System.out.println("----LOG: " + message);

		// 向客户端发送log
		try{sendServer.send(message);}
		catch(Exception e){
			System.out.println(e);
		}
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print("{code:0,info:'success!'}");
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