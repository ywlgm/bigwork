package foodstore.web.servlet.client;

import java.io.IOException;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import foodstore.domain.User;
import foodstore.service.UserService;

public class ChangeUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		String email = request.getParameter("email");
		String password = request.getParameter("ps");
		String gender=request.getParameter("radiobutton");
		String telephone = request.getParameter("text2");
		UserService service = new UserService();
		try {
			
			User user = service.update(email, password, gender, telephone);
			request.getSession().setAttribute("user", user);
			response.sendRedirect(request.getContextPath() + "/client/changesuccess.jsp");
			return;
		} catch (LoginException e) {
			// 如果出现问题，将错误信息存储到request范围，并跳转回登录页面显示错误信息
			e.printStackTrace();
			request.setAttribute("register_message", e.getMessage());
			request.getRequestDispatcher("/client/myAccount.jsp").forward(request, response);
			return;
		}
	}

}
