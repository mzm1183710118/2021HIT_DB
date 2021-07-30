import java.sql.*;

/**
 * created by meizhimin on 2021/3/31
 */

public class MysqlTest {
    public static void main(String[] args) {
        try{
            //1.调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");

            //2.建立连接
            String url= "jdbc:mysql://localhost:3306/company";//JDBC的URL，在此处标明所使用的数据库
            String rootName = "root";
            String pwd ="mzm12138mzm";

            try {
                //建立数据库连接
                Connection conn = DriverManager.getConnection(url,rootName,pwd);
                System.out.println("成功连接到数据库");

                //创建一个Statement对象
                Statement stmt  = conn.createStatement();
                //sql查询数据
                String sql = "select * from department";//要执行的SQL
                /*在查询数据表时，需要用到ResultSet接口，它类似于一个数据表，通过该接口的实例
                 * 可以获得检索结果集，以及对应数据表的接口信息。*/
                ResultSet rs = stmt.executeQuery(sql);//创建数据对象

                System.out.println("DNAME"+"\t"+"DNO"+"\t"+"MGRSSN"+"\t"+"MGRSTARTDATE");
                //遍历查询的结果集
                while (rs.next()) {
                    System.out.print(rs.getString(1)+"\t");
                    System.out.print(rs.getLong(2)+"\t");
                    System.out.print(rs.getLong(3)+"\t");
                    System.out.print(rs.getDate(4)+"\t");
                    System.out.println();

                }
                //关闭连接
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }

}
