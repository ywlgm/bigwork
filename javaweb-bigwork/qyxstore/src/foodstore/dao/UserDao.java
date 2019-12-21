package foodstore.dao;
import java.sql.SQLException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import foodstore.domain.User;
import foodstore.utils.DataSourceUtils;

public class UserDao {
	// 添加用户
	public void addUser(User user) throws SQLException {
		String sql = "insert into user(username,password,gender,email,telephone,introduce,activecode,state) values(?,?,?,?,?,?,0,1)";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		int row = runner.update(sql, user.getUsername(), user.getPassword(),
				user.getGender(), user.getEmail(), user.getTelephone(),
				user.getIntroduce());
		if (row == 0) {
			throw new RuntimeException();
		}
	}
	
	//根据用户名与密码查找用户
	public User findUserByUsernameAndPassword(String username, String password) throws SQLException {
		String sql="select * from user where email=? and password=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		return runner.query(sql, new BeanHandler<User>(User.class),username,password);
	}
	
	public int panduan(String email) throws SQLException{
		String sql="select count(*) from user where email=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		Long count = (Long) runner.query(sql, new ScalarHandler(),email);
		return count.intValue();
	}
	public void updateUser(String email, String password, String gender, String telephone) throws SQLException{
		String sql = "update user set password=?,gender=?,telephone=? where email=?";
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		int row = runner.update(sql, password,gender,telephone,email);
		if (row == 0) {
			throw new RuntimeException();
		}
	}
}
