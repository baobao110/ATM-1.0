
import java.util.Date;

public class Card {
	private int id;
	private int number;
	private double money;//number��Ϊ�������ű���м������,����������뽫number��Ϊ�����������ݿ����
	private Date createtime;
	private Date modifytime;
	private String password;
	private int version;
	public int getId() {
		return id;
	}
	/*
	 * ����ı����Ǹ���card���ݿ��еı������õ�������Ҫע����������ݿ���id������Ϊ�Զ����ӵ�ģʽ�������ں���ķ����Ͳ���Ҫ
	 * �ֶ����޸�idֵ,���������ݿ��е�card��account���ֱ��ж���number����,������������ͬһ��,���ű�Ĺ�������ͨ��
	 * number����м�����������,money����������Ϊdouble���ͣ���������ATM����ʱ�ͻ����һ����ȷ�Ⱥ��������������
	 * ������������һ�����ڳ�������BigDeciamal�����ط������в���������һ�ַ��������ݿ�������С����λ,�����������������,
	 * ���ݿ���Ĭ�ϵ�����������,version��Ϊ���ֹ������õ�ÿ����һ���޸�versionֵ+1��
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public double getMoney() {
		return money;
	}
	
	public void setMoney(double money) {
		this.money = money;
	}
	
	public Date getCreatetime() {
		return createtime;
	}
	
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	
	public Date getModifytime() {
		return modifytime;
	}
	
	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", number=" + number + ", money=" + money + ", createtime=" + createtime
				+ ", modifytime=" + modifytime + ", password=" + password + ", version=" + version + "]";
	}

	
	

}
