package tw.com.ispan.service;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.repository.admin.MemberRepository;

@Service
@Transactional
public class MemberService {
	@Autowired
	private MemberRepository MemberRepository;

	public Member login(String username, String password) {
		if (username != null && username.length() != 0 &&
				password != null && password.length() != 0) {
			Optional<Member> optional = MemberRepository.findById(username);
			if (optional.isPresent()) {
				Member bean = optional.get();
				byte[] pass = bean.getPassword(); // 資料庫抓出
				byte[] temp = password.getBytes(); // 使用者輸入
				if (Arrays.equals(pass, temp)) {
					return bean;
				}
			}
		}
		return null;
	}

	public boolean changePassword(String username, String oldPassword, String newPassword) {
		Member bean = this.login(username, oldPassword);
		if (bean != null) {
			byte[] temp = newPassword.getBytes();
			bean.setPassword(temp);
			MemberRepository.save(bean);
			return true;
		}
		return false;
	}
}
