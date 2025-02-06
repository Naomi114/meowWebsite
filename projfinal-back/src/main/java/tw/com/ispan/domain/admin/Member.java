package tw.com.ispan.domain.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.pet.Activity;
import tw.com.ispan.domain.pet.ActivityParticipantList;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Follow;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.pet.forAdopt.AdoptionCaseApply;
// import tw.com.ispan.domain.shop.Cart;
// import tw.com.ispan.domain.shop.Order;
// import tw.com.ispan.domain.shop.WishListBean;

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

	public Member() {
		// 這是默認構造函數，Hibernate 需要
	}

	@Column(length = 70, nullable = false)
	private String name;

	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@Column(length = 10, nullable = false)
	private String phone;

	@Column(length = 100, nullable = false)
	private String address;

	@Column(nullable = false)
	private LocalDate birthday;

	@Column(nullable = false)
	private LocalDateTime createDate;

	@Column(nullable = false)
	private LocalDateTime updateDate;

	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<Activity> activity;

	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private Set<ActivityParticipantList> acitvityParticipantList;

	// @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	// CascadeType.REMOVE })
	// private List<WishListBean> wishList;

	// @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST,
	// CascadeType.REMOVE })
	// private Set<Cart> cart;

	// @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval =
	// true)
	// private List<Order> order;

	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<RescueCase> rescueCases;

	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
	private Set<Follow> follow = new HashSet<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<LostCase> lostCase = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<AdoptionCase> adoptionCase = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ReportCase> reportCase = new ArrayList<>();

	// 雙向一對多，最後meeting加的
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private Set<AdoptionCaseApply> adoptionCaseApply = new HashSet<>();

	public Member(Integer memberId, String nickName, String password, String name, String email, String phone,
			String address, LocalDate birthday, LocalDateTime createDate, LocalDateTime updateDate,
			Set<Activity> activity,
			Set<ActivityParticipantList> acitvityParticipantList,
			// List<WishListBean> wishList, Set<Cart> cart,List<Order> order,
			List<RescueCase> rescueCases, Set<Follow> follow, List<LostCase> lostCase,
			List<AdoptionCase> adoptionCase, List<ReportCase> reportCase, Set<AdoptionCaseApply> adoptionCaseApply) {
		this.memberId = memberId;
		this.nickName = nickName;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.birthday = birthday;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.activity = activity;
		this.acitvityParticipantList = acitvityParticipantList;
		// this.wishList = wishList;
		// this.cart = cart;
		// this.order = order;
		this.rescueCases = rescueCases;
		this.follow = follow;
		this.lostCase = lostCase;
		this.adoptionCase = adoptionCase;
		this.reportCase = reportCase;
		this.adoptionCaseApply = adoptionCaseApply;
	}

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

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
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

	@Override
	public String toString() {
		return "Member [memberId=" + memberId +
				", nickName=" + nickName +
				", password=" + password +
				", name=" + name +
				", email=" + email +
				", phone=" + phone +
				", address=" + address +
				", birthday=" + birthday +
				", createDate=" + createDate +
				", updateDate=" + updateDate +
				", activity=" + activity +
				", acitvityParticipantList=" + acitvityParticipantList +
				// ", wishList=" + wishList +
				// ", cart=" + cart +
				// ", order=" + order +
				", rescueCases=" + rescueCases +
				", follow=" + follow +
				", lostCase=" + lostCase +
				", adoptionCase=" + adoptionCase +
				", reportCase=" + reportCase +
				", adoptionCaseApply=" + adoptionCaseApply +
				", getClass()=" + getClass() +
				", hashCode()=" + hashCode() +
				", getMemberId()=" + getMemberId() +
				", getNickName()=" + getNickName() +
				", getPassword()=" + getPassword() +
				", getName()=" + getName() +
				", getEmail()=" + getEmail() +
				", getPhone()=" + getPhone() +
				", getAddress()=" + getAddress() +
				", getBirthday()=" + getBirthday() +
				", getCreateDate()=" + getCreateDate() +
				", getUpdateDate()=" + getUpdateDate() +
				", toString()=" + super.toString() +
				"]";
	}

}
