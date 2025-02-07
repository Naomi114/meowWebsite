//package tw.com.ispan.service;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import tw.com.ispan.domain.CustomerBean;
//import tw.com.ispan.repository.CustomerRepository;
//
//@Service
//@Transactional
//public class CustomerService {
//	@Autowired
//	private CustomerRepository customerRepository;
//	
//	public CustomerBean login(String username, String password) {
//		if(username!=null && username.length()!=0 &&
//				password!=null && password.length()!=0) {
//			Optional<CustomerBean> optional = customerRepository.findById(username);
//			if(optional.isPresent()) {
//				CustomerBean bean = optional.get();
//				byte[] pass = bean.getPassword();	//資料庫抓出
//				byte[] temp = password.getBytes();	//使用者輸入
//				if(Arrays.equals(pass, temp)) {
//					return bean;
//				}
//			}
//		}
//		return null;
//	}
//	
//	public boolean changePassword(String username, String oldPassword, String newPassword) {
//		CustomerBean bean = this.login(username, oldPassword);
//		if(bean!=null) {
//			byte[] temp = newPassword.getBytes();
//			bean.setPassword(temp);
//			customerRepository.save(bean);
//			return true;
//		}
//		return false;
//	}
//}
