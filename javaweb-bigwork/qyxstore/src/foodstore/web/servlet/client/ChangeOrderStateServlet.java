package foodstore.web.servlet.client;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.mail.util.MailSSLSocketFactory;

import foodstore.domain.Order;
import foodstore.domain.OrderItem;
import foodstore.domain.Product;
import foodstore.domain.User;
import foodstore.service.OrderService;

public class ChangeOrderStateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 获得订单号数据
		String orderid = request.getParameter("orderid");
		User user = (User) request.getSession().getAttribute("user");
		String paySuccess = "恭喜您支付成功！";
		if (null != orderid) {
				OrderService service = new OrderService();
				// 根据订单号修改订单状态
				try {
					service.updateState(orderid);
					Order order=service.findOrderById(orderid);
					List<OrderItem> oit=order.getOrderItems();
					String product="";
					for(int i=0;i<oit.size();i++){
					   Product pro=oit.get(i).getP();
					   product=product+pro.getName()+"*"+oit.get(i).getBuynum();
					   if(i!=oit.size()-1) product=product+"、";
					}
					Properties prop = new Properties();
			        prop.setProperty("mail.host", "smtp.qq.com"); //// 设置QQ邮件服务器
			        prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
			        prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码
			        MailSSLSocketFactory sf = new MailSSLSocketFactory();
			        sf.setTrustAllHosts(true);
			        prop.put("mail.smtp.ssl.enable", "true");
			        prop.put("mail.smtp.ssl.socketFactory", sf);
			        Session session = Session.getDefaultInstance(prop, new Authenticator() {
			            public PasswordAuthentication getPasswordAuthentication() {
			                return new PasswordAuthentication("1679513199@qq.com", "ghcdsjseqvsvdcgg");
			            }
			        });
			        session.setDebug(true);
			        Transport ts = session.getTransport();
			        ts.connect("smtp.qq.com", "1679513199@qq.com", "ghcdsjseqvsvdcgg");
			        MimeMessage message = new MimeMessage(session);
			        message.setFrom(new InternetAddress("1679513199@qq.com"));
			        message.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
			        message.setSubject("您购买的订单已发货");
			        String emailtext = "订单号："+orderid+"<br>订单价格："+order.getMoney()+"<br>订单物品及数量："+product+
			        		"<br>收货地址："+order.getReceiverAddress()+"<br>收件人："+order.getReceiverName();
			        message.setContent(emailtext, "text/html;charset=UTF-8");
			        ts.sendMessage(message, message.getAllRecipients());
			        ts.close();
					request.setAttribute("paySuccess", paySuccess);
					request.getRequestDispatcher("/findOrderByUser").forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
					response.getWriter().write("修改订单状态失败");
				}
			}
	}
}
