package tw.com.ispan.domain.admin;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer adminId;

	@Column(unique = true, length = 20, nullable = false)
	private String adminName;

	@Column(length = 20, nullable = false)
	private String password;

	@Column(nullable = false)
	private LocalDateTime createDate;

	@Column(nullable = false)
	private LocalDateTime updateDate;

	public Admin() {
	}

	public Admin(Integer adminId, String adminName, String password, LocalDateTime createDate,
			LocalDateTime updateDate) {
		this.adminId = adminId;
		this.adminName = adminName;
		this.password = password;
		this.createDate = createDate;
		this.updateDate = updateDate;
	}

	// 初始化原始資料用 (by Naomi)
	public Admin(Integer adminId, String adminName) {
		this.adminId = adminId;
		this.adminName = adminName;
	}	

	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", adminName=" + adminName + ", password=" + password + ", createDate="
				+ createDate + ", updateDate=" + updateDate + "]";
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

}
