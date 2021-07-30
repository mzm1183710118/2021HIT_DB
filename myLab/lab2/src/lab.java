import java.sql.*;
/**
 * created by meizhimin on 2021/3/31
 */
public class lab {
    private String url= "jdbc:mysql://localhost:3306/company";//JDBC的URL，在此处标明所使用的数据库
    private String rootName = "root";
    private String pwd ="mzm12138mzm";

    lab(){}

    public void SQL1(String _PNO){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ESSN from works_on where PNO=\'"+_PNO+"\'";
            System.out.println("查询参加了项目编号为"+_PNO+"的项目的员工号");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ESSN");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getLong(1)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL2(String _PNAME){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ENAME " +
                         "from employee,project,works_on " +
                         "where employee.ESSN=works_on.ESSN AND project.PNO=works_on.PNO "+
                         "AND project.PNAME=\'"+_PNAME+"\'";
            System.out.println("查询参加了项目名为"+_PNAME+"的项目的员工名字");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ENAME");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL3(String _DNAME){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ENAME,ADDRESS " +
                    "from employee,department " +
                    "where employee.DNO=department.DNO AND department.DNAME=\'"+_DNAME+"\' ";
            System.out.println("查询在"+_DNAME+"工作的所有工作人员的名字和地址");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ENAME"+"\t"+"ADDRESS");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.print(rs.getString(2)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL4(String _DNAME,Long _SALARY){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ENAME,ADDRESS " +
                    "from employee,department " +
                    "where employee.DNO=department.DNO AND department.DNAME=\'"+_DNAME+"\' "+
                    "AND salary<"+_SALARY;
            System.out.println("查询在"+_DNAME+"工作且工资低于"+_SALARY+"的员工名字和地址");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ENAME"+"\t"+"ADDRESS");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.print(rs.getString(2)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL5(String _PNO){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ENAME " +
                    "from employee " +
                    "where employee.ESSN not in ( " +
                    "select ESSN " +
                    "from works_on " +
                    "where PNO=\'"+_PNO+"\')";
            System.out.println("查询没有参加项目编号为"+_PNO+"的项目的员工名字");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ENAME");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL6(String _ENAME){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ENAME,DNAME " +
                    "from employee,department " +
                    "where employee.DNO=department.DNO " +
                    "AND SUPERSSN in (" +
                    "select ESSN " +
                    "from employee " +
                    "where ENAME=\'"+_ENAME+"\')";
            System.out.println("查询由"+_ENAME+"领导的工作人员的姓名和所在部门的名字");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ENAME"+"\t"+"DNAME");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.print(rs.getString(2)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL7(String _PNO1,String _PNO2){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ESSN " +
                    "from employee " +
                    "where ESSN in (" +
                    "select ESSN " +
                    "from works_on " +
                    "where PNO=\'"+_PNO1+"\')" +
                    "AND ESSN in (" +
                    "select ESSN " +
                    "from works_on " +
                    "where PNO=\'"+_PNO2+"\')";

            System.out.println("查询至少参加了项目编号为"+_PNO1+"和"+_PNO2+"的项目的员工号");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ESSN");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL8(Long _SALARY){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select DNAME " +
                    "from department " +
                    "where DNO in (" +
                    "select DNO " +
                    "from employee " +
                    "group by DNO " +
                    "HAVING  AVG (salary)<"+_SALARY+")";
            System.out.println("查询员工平均工资低于"+_SALARY+"元的部门名称");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("DNAME");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void SQL9(Long _N,Long _HOURS){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //建立数据库连接
            Connection conn = DriverManager.getConnection(url,rootName,pwd);
            Statement stmt  = conn.createStatement();
            String sql = "select ENAME " +
                    "from employee " +
                    "where ESSN in (" +
                    "select ESSN " +
                    "from works_on " +
                    "group by ESSN " +
                    "having count(*)>="+_N+" AND sum(HOURS)<="+_HOURS+")";

            System.out.println("查询至少参与了"+_N+"个项目且工作总时间不超过"+_HOURS+"小时的员工名字");
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("ENAME");
            //遍历查询的结果集
            while (rs.next()) {
                System.out.print(rs.getString(1)+"\t");
                System.out.println();
            }
            //关闭连接
            rs.close();
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
