package com.uzak.simjdbc.io;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import com.uzak.simjdbc.jdbc.DataBase;

public class DatabaseXml {
	private static Map<String, DataBase> dataBases = new HashMap<String, DataBase>();
	private static String path = "database.xml";

	private static void load() {
		try {
			URL url = DatabaseXml.class.getClassLoader().getResource(path);
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(new File(url.getPath()));
			Element element = document.getRootElement();
			List<Element> children = element.getChildren();
			for (Element ele : children) {
				String id = ele.getAttributeValue("id");
				DataBase db = new DataBase();
				db.setType(ele.getChildText("Type"));
				db.setDriver(ele.getChildText("Driver"));
				db.setHost(ele.getChildText("Host"));
				db.setPort(ele.getChildText("Port"));
				db.setName(ele.getChildText("Name"));
				db.setUserName(ele.getChildText("UserName"));
				db.setPassword(ele.getChildText("Password"));
				db.setInitPoolCount(Integer.parseInt(ele.getChildText("InitPoolCount")));
				db.setMaxPoolCount(Integer.parseInt(ele.getChildText("MaxPoolCount")));
				db.setLoginTimeout(Long.parseLong(ele.getChildText("LoginTimeout")));
				dataBases.put(id, db);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, DataBase> getDataBases() {
		if (dataBases.size() == 0) {
			load();
		}
		return dataBases;
	}
}
