
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
	 * ����֮�����ü��ϣ�����Ϊ�ں����ATMϵͳ���ж��̲߳���ʱ����Ϊ����̹߳���һ��Connetion,�����һ�����
	 * ��һ���߳����ת�˲��������commit()������,���е��̶߳���ִ����commit()�����������ʱ����һ���߳���
	 * ��������ת�˲���,��ʱ���ת��������һ�����˻�������,����û���ת�����,��ʱִ��commit()�����������ݿ���
	 * �ᷴӳΪת���˻������٣�����ת���˻����û�иı䣬Ǯƽ���޹ʵ���ʧ��
	 */
	static {
		try {
			Properties a=new Properties();
			a.load(DATA.class.getResourceAsStream("/1.properties"));
			Class.forName(a.getProperty("Driver"));
			/*
			 * ���ｫ���ݿ�ĳ�ʼ����װ���������ݿ⿪����Ҫ���˻��������Ϣ����source�е�.properties��ͨ��buildPath����
			 * Eclipse��·����,Properties����Բ鿴API
			 */
			for(int i=0;i<100;i++) {
				conn.add(DriverManager.getConnection(a.getProperty("url"),a.getProperty("user"),a.getProperty("password")));
			}
		/*
		 * ��ΪҪ�ṩ���Connection������������forѭ���ȴ������ConnectionԤ���ڼ�����
		 */
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		return conn.pollFirst();
		/*
		 * ����ľ�̬����Ϊ������ATM�ṩConnection
		 */
	}
	
	public static void remove(Connection e) {
		if(null==e) {
			return;
		}
		conn.addLast(e);
		/*
		 * �ں�����ATM�е�һ���߳���������commit()���������Connection��û�м�ֵ�ˣ�Ϊ�˲��˷���Դ
		 * ������Ե��ø÷�����Connection���չ��������������ظ���ѭ������
		 */
	}
	
}