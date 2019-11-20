package site.abiao.demo.JDK;

import java.sql.*;

public class JDBCTest {

    private Connection conn;

    public JDBCTest(String url, String username, String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多个线程使用同一个connect
     * */
    public void test() throws InterruptedException, SQLException {
        Thread t1 = new Thread(()->{
            try {
                selectTest(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                selectTest(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();


        t1.join();
        t2.join();
        conn.close();
    }


    private void selectTest(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM user");
        //如果有数据，rs.next()返回true
        while(rs.next()){
            System.out.println(rs.getString("name")+" 年龄："+rs.getShort("age"));
        }
    }


    public static void main(String[] args) throws SQLException, InterruptedException {
        JDBCTest jdbcTest = new JDBCTest("jdbc:mysql://47.93.236.82/testdb",
                "root",
                "hubiao");

        jdbcTest.test();
    }

}
