package tw.com.ispan.domain.admin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.shop.Discount;
import tw.com.ispan.domain.shop.Inventory;
import tw.com.ispan.domain.shop.ProductBean;

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

	// 雙向一對多，對應Discount
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
	@JsonManagedReference("admin") // by Naomi
	private List<Discount> discounts = new ArrayList<>();

	// 雙向一對多，對應Inventory
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
	@JsonManagedReference("admin") // by Naomi
	private Set<Inventory> inventory;

	// 雙向一對多，對應ProductBean (by Naomi)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
	@JsonManagedReference("admin")
	private Set<ProductBean> products = new HashSet<>();

	public Admin(Integer adminId, String adminName) {
		this.adminId = adminId;
		this.adminName = adminName;
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