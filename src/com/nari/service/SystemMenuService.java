package com.nari.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.nari.dao.SystemMenuDao;
import com.nari.po.SystemMenu;
import com.nari.po.User;

@Service
public class SystemMenuService {
	@Resource
	private SystemMenuDao menudao;

	public List<SystemMenu> findMenuCode(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		int userid = user.getId();
		String code = menudao.findMenuCode(userid);
		List<SystemMenu> list = findMenuByCode(code);
		return list;

	}

	public List<SystemMenu> findMenuByCode(String code) {
		List<SystemMenu> list = new ArrayList<SystemMenu>();
		if ((code != null) && (!code.equals(""))) {
			if (code.contains(",")) {
				String[] menucode = code.split(",");
				int[] intArr = new int[menucode.length];
				for (int i = 0; i < menucode.length; i++) {
					intArr[i] = Integer.parseInt(menucode[i]);
					list = menudao.findMenuByCode(intArr);
				}
			} else {
				int[] arr = new int[1];
				arr[0] = Integer.parseInt(code);
				list = menudao.findMenuByCode(arr);
			}
		}
		return list;

	};

	public List<SystemMenu> findMenuByPCode(String parentcode) {
		List<SystemMenu> list = menudao.findMenuByPCode(parentcode);
		return list;

	};

}
