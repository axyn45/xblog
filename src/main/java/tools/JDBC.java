package tools;
import java.sql.*;
import java.sql.ResultSet;
public class JDBC {
    private final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	// TODO read conf from profile
    private final static String DB_URL = "jdbc:mysql://localhost:3306/xblog";
    private final static String DB_USER = "xbserver";
    private final static String DB_PASS = "alex2333";
    private static Connection connection = null;
    private static PreparedStatement statement = null;
    private static ResultSet resultSet = null;

    public static boolean initConnection(){
        try {
		    // 注册 JDBC 驱动器并打开连接
		    Class.forName(JDBC_DRIVER);
		    connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
    public static Connection getConnecton(){
        if(connection==null) initConnection();
        return connection;
    }
    public static PreparedStatement getStatement(String sql){
        if(connection==null) initConnection();
        try{statement = connection.prepareStatement(sql);}
        catch(Exception e){System.out.println(e);}
        return statement;
    }
    public static ResultSet getResultSet(PreparedStatement st){
        if(connection==null) initConnection();
        try{return st.executeQuery();}
        catch(Exception e){System.out.println(e);return null;}
    }
    public static boolean executeQuery(PreparedStatement st){
        if(connection==null) initConnection();
        try {
            return st.execute();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
    protected void finalize() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // 处理关闭资源异常
            e.printStackTrace();
        }
    }
}
