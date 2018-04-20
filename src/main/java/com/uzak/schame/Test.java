package com.uzak.schame;

import java.util.Date;

import com.uzak.simjdbc.jdbc.annocation.Column;
import com.uzak.simjdbc.jdbc.annocation.Table;
import com.uzak.simjdbc.jdbc.dao.BaseDao;

/**
 * 教师基本信息
 * 
 * @author 梁秀斗
 * @mail liangxiudou@zving.com
 * @date 2018年4月4日
 */
@Table(name = "test")
public class Test extends BaseDao {
	/**
	 * ID
	 */
	@Column(primary = true)
	private Long id;
	/**
	 * 用户名
	 */
	@Column
	private String username;
	/**
	 * 密码
	 */
	@Column
	private String password;
	/**
	 * 电话
	 */
	@Column
	private String phone;
	/**
	 * 邮箱
	 */
	@Column
	private String email;
	/**
	 * 姓名
	 */
	@Column(length = 200)
	private String name;
	/**
	 * 性别
	 */
	@Column
	private String sex;
	/**
	 * 省份
	 */
	@Column
	private String province;
	/**
	 * 市
	 */
	@Column
	private String city;
	/**
	 * 地区
	 */
	@Column
	private String address;
	/**
	 * 生日
	 */
	@Column
	private Date birthday;
	/**
	 * 学历
	 */
	@Column(length = 200)
	private String education;
	/**
	 * 专业
	 */
	@Column
	private String major;
	/**
	 * 学校
	 */
	@Column
	private String school;
	@Column
	private String job;
	/**
	 * 户籍
	 */
	@Column
	private String navitePlace;
	/**
	 * 上次登录时间
	 */
	@Column
	private Date lastLoginTime;
	/**
	 * 头像
	 */
	@Column
	private String logo;
	@Column
	private String prop1;
	@Column
	private String prop2;
	@Column
	private String prop3;
	@Column
	private String prop4;
	@Column
	private String addUser;
	@Column
	private Date addTime;
	@Column
	private String modifyUser;
	@Column
	private Date modifyTime;
	@Column
	private String disable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getNavitePlace() {
		return navitePlace;
	}

	public void setNavitePlace(String navitePlace) {
		this.navitePlace = navitePlace;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getProp1() {
		return prop1;
	}

	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}

	public String getProp2() {
		return prop2;
	}

	public void setProp2(String prop2) {
		this.prop2 = prop2;
	}

	public String getProp3() {
		return prop3;
	}

	public void setProp3(String prop3) {
		this.prop3 = prop3;
	}

	public String getProp4() {
		return prop4;
	}

	public void setProp4(String prop4) {
		this.prop4 = prop4;
	}

	public String getAddUser() {
		return addUser;
	}

	public void setAddUser(String addUser) {
		this.addUser = addUser;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getDisable() {
		return disable;
	}

	public void setDisable(String disable) {
		this.disable = disable;
	}

}
