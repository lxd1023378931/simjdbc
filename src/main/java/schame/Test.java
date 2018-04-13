package schame;

import com.uzak.simjdbc.jdbc.annocation.Column;
import com.uzak.simjdbc.jdbc.annocation.Table;
import com.uzak.simjdbc.jdbc.dao.BaseDao;

@Table
public class Test extends BaseDao {
	@Column(primary = true)
	private int id;
	@Column
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
