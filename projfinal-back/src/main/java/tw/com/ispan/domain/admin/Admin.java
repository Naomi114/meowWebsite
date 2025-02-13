package tw.com.ispan.domain.admin;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import tw.com.ispan.domain.shop.Discount;
import tw.com.ispan.domain.shop.Inventory;
import tw.com.ispan.domain.shop.Product;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// import tw.com.ispan.domain.shop.Discount;
// import tw.com.ispan.domain.shop.Inventory;
// import tw.com.ispan.domain.shop.ProductBean;

@Entity
@Table(name = "Admin") // Ensure the table name matches convention
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

	// One-to-many relationship with Discount (no back reference in Admin)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
	@JsonManagedReference("admin-discounts") // Using a unique reference name for serialization
	private List<Discount> discounts;

	// One-to-many relationship with Inventory
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "admin", orphanRemoval = true)
	private Set<Inventory> inventory = new HashSet<>();

	// One-to-many relationship with Product
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH,
			CascadeType.REFRESH }, mappedBy = "admin", orphanRemoval = true)
	@JsonManagedReference("admin") // Unique reference for Products
	private Set<Product> products = new HashSet<>();

	public Admin() {
	}

	public Admin(Integer adminId, String adminName, String password, LocalDateTime createDate, LocalDateTime updateDate,
			List<Discount> discounts, Set<Inventory> inventory, Set<Product> products) {
		this.adminId = adminId;
		this.adminName = adminName;
		this.password = password;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.discounts = discounts;
		this.inventory = inventory;
		this.products = products;
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

	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", adminName=" + adminName + ", password=" + password + ", createDate="
				+ createDate + ", updateDate=" + updateDate + ", discounts=" +
				// discounts + ", inventory=" + inventory+
				", getAdminId()=" + getAdminId() + ", getAdminName()=" + getAdminName() + ", getPassword()="
				+ getPassword() + ", getCreateDate()=" + getCreateDate() + ", getUpdateDate()=" + getUpdateDate()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	public List<Discount> getDiscounts() {
		return discounts;
	}

	public void setDiscounts(List<Discount> discounts) {
		this.discounts = discounts;
	}

	public Set<Inventory> getInventory() {
		return inventory;
	}

	public void setInventory(Set<Inventory> inventory) {
		this.inventory = inventory;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

}
