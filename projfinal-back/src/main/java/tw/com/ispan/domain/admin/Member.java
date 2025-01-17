package tw.com.ispan.domain.admin;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;<<<<<<<HEAD
// import tw.com.ispan.domain.pet.Activity;
// import tw.com.ispan.domain.pet.ActivityParticipantList;
// import tw.com.ispan.domain.pet.AdoptionCase;
// import tw.com.ispan.domain.pet.Follow;
// import tw.com.ispan.domain.pet.LostCase;
// import tw.com.ispan.domain.pet.ReportCase;
// import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.shop.Cart;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.domain.shop.WishList;=======
import tw.com.ispan.domain.pet.Activity;
import tw.com.ispan.domain.pet.ActivityParticipantList;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Follow;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.shop.Order;

@Entity
@Table(name = "Member")
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;

	@Column(unique = true, length = 20, nullable = false)
	private String nickName;

	@Column(length = 20, nullable = false)
	private String password;

	@Column(length = 70, nullable = false)
	private String name;

	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@Column(length = 10, nullable = false)
	private String phone;

	@Column(length = 100, nullable = false)
	private String address;

	@Column(nullable = false)
	private Date birthday;

	@Column(nullable = false)
	private LocalDateTime createDate;

	@Column(nullable = false)
	private LocalDateTime updateDate;

	<<<<<<<HEAD
	// @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	// CascadeType.REMOVE })
	// private Set<Activity> activity;

	// @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	// CascadeType.REMOVE })
	// private Set<ActivityParticipantList> acitvityParticipantList;

	@OneToMany(mappedBy="member",cascade={CascadeType.PERSIST,CascadeType.REMOVE})
	private List<WishList> wishLists;=======
	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<Activity> activity;

	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<ActivityParticipantList> acitvityParticipantList;

	// // @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	//
	// CascadeType.REMOVE })
	// // private List<WishList> wishList;
	>>>>>>>255185a (修改資料庫程式碼-1)

	// // @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	//
	// CascadeType.REMOVE })
	// // private Set<Cart> cart;

<<<<<<< HEAD
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)

	private List<Orders> order;

	// @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	// CascadeType.REMOVE })
	// private List<RescueCase> rescueCases;

	// @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	// CascadeType.REMOVE }, orphanRemoval = true)
	// private Set<Follow> follow;

	// @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	// private List<LostCase> lostCase;
	=======
	// @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval =
	// true)
	// private List<Order> order;

	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<RescueCase> rescueCases;

	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
	private Set<Follow> follow;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<LostCase> lostCase;>>>>>>>255185a (修改資料庫程式碼-1)

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<AdoptionCase> adoptionCase;

	<<<<<<<HEAD
	// @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	// private List<ReportCase> reportCase;
	=======
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ReportCase> reportCase;>>>>>>>255185a (修改資料庫程式碼-1)

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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

	// public String getStatus() {
	// return status;
	// }

	// public void setStatus(String status) {
	// this.status = status;
	// }

}
