
import java.util.Date;

public class Card {
	private int id;
	private int number;
	private double money;//number作为连接两张表的中间过渡量,如果加锁必须将number作为索引，在数据库添加
	private Date createtime;
	private Date modifytime;
	private String password;
	private int version;
	public int getId() {
		return id;
	}
	/*
	 * 这里的变量是根据card数据库中的变量设置的其中需要注意的是在数据库中id是设置为自动增加的模式，这样在后面的方法就不需要
	 * 手动的修改id值,还有在数据库中的card和account两种表中都有number变量,这两个变量是同一个,两张表的关联就是通过
	 * number这个中间量关联起来,money在这里设置为double类型，浮点型在ATM操作时就会存在一个精确度和四舍五入的问题
	 * 对于这种问题一种是在程序中用BigDeciamal类的相关方法进行操作，还有一种方法在数据库中设置小数点位,至于四舍五入的问题,
	 * 数据库中默认的是四舍五入,version是为了乐观锁设置的每进行一次修改version值+1。
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
