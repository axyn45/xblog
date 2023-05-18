package controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

// import model.dao.MultiThreadServerDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.HashSet;
import java.util.concurrent.BlockingQueue;


@WebListener
public class StartupListener implements ServletContextListener {

    // 初始化方法，在Tomcat启动时执行
    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    // 销毁方法，在Tomcat停止时执行
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}