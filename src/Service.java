import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Service {
	private static final CardDAO car=new CardDAO();
	private static final AccountDAO account=new AccountDAO();
	/*
	 * ����ֱ�������CardDAO��AccountDAO�ı���Ϊstatic final ,������Ҫע���ʱDAO��ĸ��������ݱ����һ��,����Service��ֻ��
	 * һ����������Ҫ�ر�ע��,���е�DAO�������Service�����������֮ǰ�Լ���Service��Ϊ���ʱ��ȫ�����
	 */
	public void open(String password) {
		Card a=null;;
		int number=0;
		int i=0;
		Connection conn=null;
		try {
			conn=DATA.getConnection();//������п��������Ȼ�ȡһ��Connection
			if(null==conn) {//�����ֹ���ֿ�ָ���쳣���ж��ǲ��ǣ������ֱ��return����Ĵ��벻ִ��
				System.out.println("��ָ�� �쳣");
				return;
			}
			conn.setAutoCommit(false);//����һ�����Ƚ�������ύģʽ����Ϊ�ֶ��ύ
			do {
			number=Util.createNumber();//����Util��ľ�̬������ȡ��������
			a=car.getBankCar(number,conn);
			/*
			 * ���ｫ���ź͵�ǰ��Connection�����뵽CDAO�е����岻һ��,���뿨���������ݿ��в鿴�Ƿ�
			 * �Ѿ����ڸÿ���,ͨ������ֵ�Ƿ�Ϊnull�ж�,����Connection����Ϊ��Ȼ������CardDAO����
			 * ���������ڵ�ǰ�����У����Ա��빲��ͬһ��Connection
			 */
			}while(a!=null);//�жϿ����Ƿ���ڣ�����������»�ȡ
			try {
				i=car.open(password,conn);//��������ͬһ��������
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				conn.rollback();
			}
			if(i==1) {
				System.out.println("�����ɹ�");
			}
			else {
				System.out.println("����ʧ��");
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
		DATA.remove(conn);//�����������
	}
	
	public void save(int number,double money,String password) {
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("��ָ�� �쳣");
			return;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Card a=car.getBankCar(number,conn);
		if(null==a) {//�˻�Card����Ϊ��˵���˻�������
			System.out.println("�˻������벻��ȷ");//�Ի���Ϣ
			return;
		}
		if(!a.getPassword().equals(password)) {//Card�����ÿ�������Ͳ�������Ƚ�����ǳ�����
			System.out.println("�˻������벻��ȷ");
			return;
		}
		a.setMoney(a.getMoney()+money);
		int i=car.modify(number, a.getMoney(),conn);//�˻���Ϣ�޸�
		if(i==1) {
			System.out.println("��Ǯ�ɹ�");
		}
		else {
			System.out.println("��Ǯʧ��");
		}
		Account b=new Account();//Account����
		b.setNumber(number);
		b.setMoney(money);
		b.setType("1");
		b.setDescription("��Ǯ");
		int j=0;
		try {
			j = account.add(b,conn);//������ˮ��Ϣ��¼
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(j==1) {
			System.out.println("��ˮ�˵������ɹ�");
		}
		else {
			System.out.println("��ˮ�˵�����ʧ��");
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
		DATA.remove(conn);//���Connection����
	}
	
	public void draw(int number,double money,String password) {
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("��ָ�� �쳣");
			return;
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Card a=car.getBankCar(number,conn);
		if(null==a) {//�˻�Card����Ϊ��˵���˻�������
			System.out.println("�˻������벻��ȷ");//�Ի���Ϣ
			return;
		}
		if(!a.getPassword().equals(password)) {//Card�����ÿ�������Ͳ�������Ƚ�����ǳ�����
			System.out.println("�˻������벻��ȷ");
			return;
		}
		a.setMoney(a.getMoney()-money);
		int i=car.modify(number, a.getMoney(),conn);//�˻���Ϣ�޸�
		if(i==1) {
			System.out.println("ȡǮ�ɹ�");
		}
		else {
			System.out.println("ȡǮʧ��");
		}
		Account b=new Account();//Account����
		b.setNumber(number);
		b.setMoney(money);
		b.setType("2");
		b.setDescription("ȡǮ");
		int j=0;
		try {
			j = account.add(b,conn);//������ˮ��Ϣ��¼
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(j==1) {
			System.out.println("��ˮ�˵������ɹ�");
		}
		else {
			System.out.println("��ˮ�˵�����ʧ��");
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
		DATA.remove(conn);//���Connection����
	}
	/*
	 * ȡǮ�����ʹ�Ǯ������һ��
	 */
	public void transfer(double money,int OutNumber,int InNumber,String password) {
		int i=0;
		int j=0;
		Card a=null;
		Account b=null;
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("��ָ�� �쳣");
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
			System.out.println("�˻������벻��ȷ");
			return;
		}
		if(!a.getPassword().equals(password)) {
			System.out.println("�˻������벻��ȷ");
			return;
		}
		try {
		if(a.getMoney()<money) {
			System.out.println("����");
			return;
		}
		a.setMoney(a.getMoney()-money);
		i=car.modify(OutNumber, a.getMoney(),conn);
		if(i==1) {
			System.out.println("ת�˳ɹ�");
		}
		else {
			System.out.println("ת��ʧ��");
		}
		b=new Account();
		b.setNumber(OutNumber);
		b.setMoney(money);
		b.setType("3");
		b.setDescription("ת��");
		j=account.add(b,conn);
		if(j==1) {
			System.out.println("��ˮ�˵������ɹ�");
		}
		else {
			System.out.println("��ˮ�˵�����ʧ��");
			return;
		}
		a=car.getBankCar(InNumber,conn);
		if(null==a) {
			System.out.println("�˻�����ȷ");
			return;
		}
		a.setMoney(a.getMoney()+money);
		i=car.modify(InNumber, a.getMoney(),conn);
		if(i==1) {
			System.out.println("��Ǯ�ɹ�");
		}
		else {
			System.out.println("��Ǯʧ��");
			return;
		}
		b=new Account();
		b.setNumber(InNumber);
		b.setMoney(money);
		b.setType("4");
		b.setDescription("ת��");
		j=account.add(b,conn);
		if(j==1) {
			System.out.println("��ˮ�˵������ɹ�");
		}
		else {
			System.out.println("��ˮ�˵�����ʧ��");
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
	//����Ľ��׾���һ������һ��֧����ʵ����ǰ��Ĵ�Ǯ��ȡǮ�Ĵ��븴��
	public void List(int number,String password) {
		Connection conn=DATA.getConnection();
		if(null==conn) {
			System.out.println("��ָ�� �쳣");
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
			System.out.println("�˻������벻��ȷ");
			return;
		}
		if(!a.getPassword().equals(password)) {
			System.out.println("�˻������벻��ȷ");
			return;
		}
		//ǰ����Щ�˻����������֤����ļ�����������,������ȫһ��
		ArrayList<Account> b=account.list(number);
		//��������˻�������鿴���˻�����ˮ��Ϣ����Ϊ��ˮ��Ϣ�����ɶ��������ü��ϴ洢
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
