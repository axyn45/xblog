package controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.PreparedStatement;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Random;

import tools.JDBC;
import tools.PassGen;
import tools.Plotter;

/**
 * Servlet implementation class Vcode
 */
@WebServlet("/captcha.jpg")
public class captcha extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public captcha() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public static String mkRandStr() {
		// TODO Auto-generated method stub
		Random rand = new Random();
		String randomstr ="";
		String strs = "23456789"+"abcdefghjklmnpqrstuvwxyz"+"ABCDEFGHJKLMNPQRSTUVWXYZ";
		for(int i = 0;i<4;i++) {
			//随机生成一个0-长度之间的整数
			int index = rand.nextInt(strs.length());
			char c =strs.charAt(index);
			randomstr = randomstr+c;
		}
		return randomstr;
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//随机生成一个指定长度的字符串 一般是4为长 （每个字符范围一般是数字大小写 字母）
		String vcode=mkRandStr();
		String sql="insert into xb_captchas (session, cval) values(?,?) ON DUPLICATE KEY UPDATE cval=?";
		PreparedStatement st=JDBC.getStatement(sql);
		String key=PassGen.getSHA256(vcode+PassGen.addSalt());
		HttpSession session=request.getSession();
		System.out.println("Session id: "+session.getId());
		try{
			st.setString(1,session.getId());
			st.setString(2,vcode);
			st.setString(3,vcode);
		}catch(Exception e){System.out.println(e);}
		System.out.println("CAPTCHA insertion: "+JDBC.executeQuery(st));

        System.out.println("CAPTCHA: "+vcode);
		//为实现多个数据库的数据共享 需要将串保存在session中 （会话）
		
		session.setAttribute("ckey", vcode);//类似于hashmap
		
		//生成一张字符串生成一张内存图片（颜色问题 背景色问题 文字颜色问题 文字大小问题 干扰因素的问题）
		BufferedImage image=Plotter.createImage(vcode);
		response.setContentType("img/jpg");
		//首先要得到一个输出流对象
		ServletOutputStream os=response.getOutputStream();//字节流发送方式
		ImageIO.write(image,"JPEG",os);//发送图片
		
		//将内存的图片放松给客户端
		os.flush();//立即发送
		os.close();//关闭
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
