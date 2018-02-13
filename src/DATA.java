
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DATA {
	private static  LinkedList<Connection> conn=new LinkedList<Connection>();
	/*
	 * 这里之所以用集合，是因为在后面的ATM系统进行多线程操作时，因为多个线程共用一个Connetion,会出现一种情况
	 * 当一个线程完成转账操作后调用commit()方法后,所有的线程都被执行了commit()方法，如果此时在另一个线程中
	 * 真正进行转账操作,此时完成转出操作，一方的账户金额减少,但还没完成转入操作,这时执行commit()方法，在数据库上
	 * 会反映为转出账户金额减少，但是转入账户金额没有改变，钱平白无故的消失了
	 */
	static {
		try {
			Properties a=new Properties();
			a.load(DATA.class.getResourceAsStream("/1.properties"));
			Class.forName(a.getProperty("Driver"));
			/*
			 * 这里将数据库的初始化封装起来，数据库开启需要的账户密码等信息放在source中的.properties中通过buildPath放在
			 * Eclipse的路径下,Properties类可以查看API
			 */
			for(int i=0;i<100;i++) {
				conn.add(DriverManager.getConnection(a.getProperty("url"),a.getProperty("user"),a.getProperty("password")));
			}
		/*
		 * 因为要提供多个Connection，所以这里用for循环先创建多个Connection预存在集合中
		 */
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return conn.pollFirst();
		/*
		 * 这里的静态方法为后续的ATM提供Connection
		 */
	}
	
	public static void remove(Connection e) {
		if(null==e) {
			return;
		}
		conn.addLast(e);
		/*
		 * 在后续的ATM中当一个线程中最后调用commit()方法后这个Connection就没有价值了，为了不浪费资源
		 * 这里可以调用该方法将Connection回收过来，这样可以重复的循环利用
		 */
	}
	
}