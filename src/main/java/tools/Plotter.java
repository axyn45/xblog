package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Plotter {
	public static Random rand = new Random();
	public static String mkRandStr() {
		// TODO Auto-generated method stub
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
	public static BufferedImage createImage(String str) {
		BufferedImage image = new BufferedImage(100,30,BufferedImage.TYPE_3BYTE_BGR);
		Graphics g = image.getGraphics();//绘制图片是通过调用Graphics相对应的方法完成
		//设置图片颜色
		g.setColor(Color.white);
		//设置绘图区域
		g.fillRect(0, 0, 100, 40);//x,y,width,height
		//调用自定义方法绘图
		draw(str,g);
		//绘制图片
		g.dispose();
		return image;
	}
	//验证码的绘制
	//绘图
	public static void draw(String str,Graphics g) {
		g.setFont(new Font("宋体", Font.BOLD, 35));//字体name,style,size
		for (int i =0;i<str.length();i++) {
			//设置绘制颜色
			g.setColor(getColor());
			char c = str.charAt(i);
			g.drawString(c+"",20*i+10,20);//str,x,y
		}
	}
	//获取随机颜色
	public static Color getColor() {
		return new Color(rand.nextInt(250),rand.nextInt(250),rand.nextInt(250));
	}
	//unit单元测试
	public static void main(String[] args) {
		System.out.print(mkRandStr());
	}

}
