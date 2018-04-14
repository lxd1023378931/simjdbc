package schame;

import java.util.Date;

import com.uzak.simjdbc.jdbc.annocation.Column;
import com.uzak.simjdbc.jdbc.annocation.Table;
import com.uzak.simjdbc.jdbc.dao.BaseDao;

@Table
public class Test extends BaseDao {
	@Column(primary = true)
	private int id;
	@Column
	private Date name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getName() {
		return name;
	}

	public void setName(Date name) {
		this.name = name;
	}

}
