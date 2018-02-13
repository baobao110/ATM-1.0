import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDAO {
	public int open(String password,Connection conn) throws Exception {//开户需要密码，Connection是由Service层传入
		String sql="insert card(number,money,createtime,modifytime,password) values(?,?,NOW(),NOW(),?);";
		/*
		 * 在这条sql语句中需要注意?和NOW(),NOW()是sql提供的一种方法，可以显示进行当前sql语句时的时间
		 * ?问号是需要注意的它是JDBC中的一种特殊用法,在sql语句中先不设置值，可以在后面手动的添加
		 */
		PreparedStatement pre=null;
		try {
			pre=conn.prepareStatement(sql);//这里用PrepareStatement安全性更高
			pre.setInt(1, Util.createNumber());
			pre.setInt(2, 0);
			pre.setString(3,password);
			/*
			 * 这里的set方法是为了给前边desql语句传参数
			 */
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			conn.rollback();
		}
		return pre.executeUpdate();
	}
	
	public int modify(int number,double money,Connection conn) {
		/*
		 * 这里需要注意的是按照常理进行修改操作是应该在参数传入时有密码,在方法中首先进行密码和账户的
		 * 的验证以及金额的验证但是这里都没有进行，这就是高明的地方，在service层中完成了这些,验能时
		 * 刚好调用了本类dgetBankCard方法获取卡号信息进行验证，这样方便了代码的移植
		 */
		PreparedStatement pre=null;
		try {
			String sql="update card set money=?,modifytime=NOW() where number=?;";
			pre=conn.prepareStatement(sql);
			pre.setDouble(1,money);
			pre.setInt(2,number);
			return pre.executeUpdate();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return 0;
	}
	
	public Card getBankCar(int number,Connection conn) {//这里的返回类型为Card类型，需要注意，这里同样在参数中没有密码
		PreparedStatement pre=null;
		try {
			String sql="select * from card where number=? for update;";
			pre=conn.prepareStatement(sql);
			pre.setInt(1, number);
			ResultSet result=pre.executeQuery();
			while(result.next()) {
				Card a=new Card();
				a.setId(result.getInt("id"));
				a.setNumber(result.getInt("number"));
				a.setMoney(result.getDouble("money"));
				a.setCreatetime(result.getDate("createtime"));
				a.setModifytime(result.getDate("modifytime"));
				a.setPassword(result.getString("password"));
				return a;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
	
}
