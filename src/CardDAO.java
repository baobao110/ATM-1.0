import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDAO {
	public int open(String password,Connection conn) throws Exception {//������Ҫ���룬Connection����Service�㴫��
		String sql="insert card(number,money,createtime,modifytime,password) values(?,?,NOW(),NOW(),?);";
		/*
		 * ������sql�������Ҫע��?��NOW(),NOW()��sql�ṩ��һ�ַ�����������ʾ���е�ǰsql���ʱ��ʱ��
		 * ?�ʺ�����Ҫע�������JDBC�е�һ�������÷�,��sql������Ȳ�����ֵ�������ں����ֶ������
		 */
		PreparedStatement pre=null;
		try {
			pre=conn.prepareStatement(sql);//������PrepareStatement��ȫ�Ը���
			pre.setInt(1, Util.createNumber());
			pre.setInt(2, 0);
			pre.setString(3,password);
			/*
			 * �����set������Ϊ�˸�ǰ��desql��䴫����
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
		 * ������Ҫע����ǰ��ճ�������޸Ĳ�����Ӧ���ڲ�������ʱ������,�ڷ��������Ƚ���������˻���
		 * ����֤�Լ�������֤�������ﶼû�н��У�����Ǹ����ĵط�����service�����������Щ,����ʱ
		 * �պõ����˱���dgetBankCard������ȡ������Ϣ������֤�����������˴������ֲ
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
	
	public Card getBankCar(int number,Connection conn) {//����ķ�������ΪCard���ͣ���Ҫע�⣬����ͬ���ڲ�����û������
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
