import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Service {
	private static final CardDAO car=new CardDAO();
	private static final AccountDAO account=new AccountDAO();
	/*
	 * 这里分别设置了CardDAO和AccountDAO的变量为static final ,这里需要注意的时DAO层的个数和数据表个数一致,但是Service层只有
	 * 一个，这里需要特别注意,所有的DAO类变量在Service层操作，这里之前自己将Service设为多个时完全错误的
	 */
	public void open(String password) {
		Card a=null;;
		int number=0;
		int i=0;
		Connection conn=null;
		try {
			conn=DATA.getConnection();//这里进行开户所以先获取一个Connection
			if(null==conn) {//这里防止出现空指针异常，判断是不是，如果是直接return后面的代码不执行
				System.out.println("空指针 异常");
				return;
			}
			conn.setAutoCommit(false);//这里一样首先将事物额提交模式设置为手动提交
			do {
			number=Util.createNumber();//调用Util类的静态方法获取开户卡号
			a=car.getBankCar(number,conn);
			/*
			 * 这里将卡号和当前的Connection都传入到CDAO中的意义不一样,传入卡号是在数据库中查看是否
			 * 已经存在该卡号,通过返回值是否为null判断,传入Connection是因为虽然调用了CardDAO方法
			 * 但是依旧在当前事物中，所以必须共用同一个Connection
			 */
			}while(a!=null);//判断卡号是否存在，如果存在重新获取
			try {
				i=car.open(password,conn);//开户处于同一个事物中
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				conn.rollback();
			}
			if(i==1) {
				System.out.println("开户成功");
			}
			else {
				System.out.println("开户失败");
			}
			conn.commit();
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
		DATA.remove(conn);//事物结束回收
	}
	
	public void save(int number,double money,String password) {
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("空指针 异常");
			return;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Card a=car.getBankCar(number,conn);
		if(null==a) {//账户Card返回为空说明账户不存在
			System.out.println("账户或密码不正确");//迷惑信息
			return;
		}
		if(!a.getPassword().equals(password)) {//Card存在用卡的密码和参数密码比较这里非常高明
			System.out.println("账户或密码不正确");
			return;
		}
		a.setMoney(a.getMoney()+money);
		int i=car.modify(number, a.getMoney(),conn);//账户信息修改
		if(i==1) {
			System.out.println("存钱成功");
		}
		else {
			System.out.println("存钱失败");
		}
		Account b=new Account();//Account对象
		b.setNumber(number);
		b.setMoney(money);
		b.setType("1");
		b.setDescription("存钱");
		int j=0;
		try {
			j = account.add(b,conn);//进行流水信息记录
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(j==1) {
			System.out.println("流水账单创建成功");
		}
		else {
			System.out.println("流水账单创建失败");
		}
		try {
			conn.commit();
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
		DATA.remove(conn);//最后Connection回收
	}
	
	public void draw(int number,double money,String password) {
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("空指针 异常");
			return;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Card a=car.getBankCar(number,conn);
		if(null==a) {//账户Card返回为空说明账户不存在
			System.out.println("账户或密码不正确");//迷惑信息
			return;
		}
		if(!a.getPassword().equals(password)) {//Card存在用卡的密码和参数密码比较这里非常高明
			System.out.println("账户或密码不正确");
			return;
		}
		a.setMoney(a.getMoney()-money);
		int i=car.modify(number, a.getMoney(),conn);//账户信息修改
		if(i==1) {
			System.out.println("取钱成功");
		}
		else {
			System.out.println("取钱失败");
		}
		Account b=new Account();//Account对象
		b.setNumber(number);
		b.setMoney(money);
		b.setType("2");
		b.setDescription("取钱");
		int j=0;
		try {
			j = account.add(b,conn);//进行流水信息记录
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(j==1) {
			System.out.println("流水账单创建成功");
		}
		else {
			System.out.println("流水账单创建失败");
		}
		try {
			conn.commit();
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
		DATA.remove(conn);//最后Connection回收
	}
	/*
	 * 取钱操作和存钱基本上一致
	 */
	public void transfer(double money,int OutNumber,int InNumber,String password) {
		int i=0;
		int j=0;
		Card a=null;
		Account b=null;
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("空指针 异常");
			return;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		a=car.getBankCar(OutNumber,conn);
		if(null==a) {
			System.out.println("账户或密码不正确");
			return;
		}
		if(!a.getPassword().equals(password)) {
			System.out.println("账户或密码不正确");
			return;
		}
		try {
		if(a.getMoney()<money) {
			System.out.println("余额不足");
			return;
		}
		a.setMoney(a.getMoney()-money);
		i=car.modify(OutNumber, a.getMoney(),conn);
		if(i==1) {
			System.out.println("转账成功");
		}
		else {
			System.out.println("转账失败");
		}
		b=new Account();
		b.setNumber(OutNumber);
		b.setMoney(money);
		b.setType("3");
		b.setDescription("转出");
		j=account.add(b,conn);
		if(j==1) {
			System.out.println("流水账单创建成功");
		}
		else {
			System.out.println("流水账单创建失败");
			return;
		}
		a=car.getBankCar(InNumber,conn);
		if(null==a) {
			System.out.println("账户不正确");
			return;
		}
		a.setMoney(a.getMoney()+money);
		i=car.modify(InNumber, a.getMoney(),conn);
		if(i==1) {
			System.out.println("存钱成功");
		}
		else {
			System.out.println("存钱失败");
			return;
		}
		b=new Account();
		b.setNumber(InNumber);
		b.setMoney(money);
		b.setType("4");
		b.setDescription("转入");
		j=account.add(b,conn);
		if(j==1) {
			System.out.println("流水账单创建成功");
		}
		else {
			System.out.println("流水账单创建失败");
			return;
		}
		
		try {
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		DATA.remove(conn);
	}
	//这里的交易就是一方收入一方支出其实就是前面的存钱和取钱的代码复制
	public void List(int number,String password) {
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("空指针 异常");
			return;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Card a=car.getBankCar(number,conn);
		if(null==a) {
			System.out.println("账户或密码不正确");
			return;
		}
		if(!a.getPassword().equals(password)) {
			System.out.println("账户或密码不正确");
			return;
		}
		//前面这些账户和密码的验证本类的几个方法都有,代码完全一致
		ArrayList<Account> b=account.list(number);
		//这里根据账户和密码查看该账户的流水信息，因为流水信息可能由多条这里用集合存储
		for(Account i:b) {
			System.out.println(i);
		}
		try {
			conn.commit();
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
		DATA.remove(conn);
	}
}
