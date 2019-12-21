package foodstore.service;
import java.sql.SQLException;
import javax.security.auth.login.LoginException;

import foodstore.dao.UserDao;
import foodstore.domain.User;
import foodstore.exception.RegisterException;

public class UserService {
	private UserDao dao = new UserDao();
	// 注册操作
	public int register(User user) throws RegisterException {
		// 调用dao完成注册操作
		try {
			int i=dao.panduan(user.getEmail());
			if(i==0) {
				dao.addUser(user);
				return 1;
			}
			else return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RegisterException("注冊失败");
		}
	}
	
	// 登录操作
	public User login(String username, String password) throws LoginException {
		try {
			//根据登录时表单输入的用户名和密码，查找用户
			User user = dao.findUserByUsernameAndPassword(username, password);
			//如果找到，还需要确定用户是否为激活用户
			if (user != null) {
				// 只有是激活才能登录成功，否则提示“用户未激活”
				if (user.getState() == 1) {
					return user;
				}
			}
			throw new LoginException("用户名或密码错误");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LoginException("登录失败");
		}
	}
	public User update(String email, String password, String gender, String telephone) throws LoginException {
		try {
			dao.updateUser(email, password, gender, telephone);
			User user = dao.findUserByUsernameAndPassword(email, password);
			if (user != null) {
				// 只有是激活才能登录成功，否则提示“用户未激活”
				if (user.getState() == 1) {
					return user;
				}
			}
			throw new LoginException("未知错误");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new LoginException("修改失败");
		}
	} 
}
