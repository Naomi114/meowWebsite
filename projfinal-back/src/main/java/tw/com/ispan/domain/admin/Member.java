package tw.com.ispan.domain.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.ToString;
import tw.com.ispan.domain.pet.Activity;
import tw.com.ispan.domain.pet.ActivityParticipantList;
import tw.com.ispan.domain.pet.AdoptionCase;
import tw.com.ispan.domain.pet.Follow;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.domain.pet.RescueCase;
//import tw.com.ispan.domain.shop.Order;
import tw.com.ispan.domain.pet.forAdopt.AdoptionCaseApply;

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
	private LocalDate birthday;

	@Column(nullable = false)
	private LocalDateTime createDate;

	@Column(nullable = false)
	private LocalDateTime updateDate;

	// 以下為給line login使用
	private String lineId;

	private String lineName;

	private String linePicture;
	
	@Column(nullable = false)
	private boolean userType;    // 1表示註冊會員，0表示line登入會員
	
	//以下為關聯產生的屬性
	// 雙向一對多
	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST })
	@ToString.Exclude
	private List<RescueCase> rescueCases;

	// 雙向一對多
	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST })
	private List<Activity> activity;

	// 會員和活動的中介表 雙向一對多
	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST }, orphanRemoval = true)
	private List<ActivityParticipantList> acitvityParticipantLists;

	// 單向一對多
	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
	private List<Follow> follows;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<LostCase> lostCase = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<AdoptionCase> adoptionCase = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private List<ReportCase> reportCase = new ArrayList<>();

	// 雙向一對多，最後meeting加的
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	private Set<AdoptionCaseApply> adoptionCaseApply = new HashSet<>();

	// 綁定流程:當需要綁定時，為會員生成和 bindingToken和bindingTokenExpiry，存入Member表。
	// 用戶掃描 QR Code 或點擊綁定鏈接時，通過 binding_token 查找對應的會員。
	// 驗證 Token 是否有效（未過期）。
	// 綁定完成後，將 line_user_id 更新到該會員記錄中，並清空 bindingToken。
	private String userLineId;

	private String bindingToken;

	private LocalDateTime bindingTokenExpiry;

	// Constructors, getters, setters, toString()
	public Member() {
		// 這是默認構造函數，Hibernate 需要
	}

	public Member(Integer memberId, String nickName, String password, String name, String email, String phone,
			String address, LocalDate birthday, LocalDateTime createDate, LocalDateTime updateDate,
			List<Activity> activity, List<ActivityParticipantList> acitvityParticipantLists,
			// List<WishListBean> wishList, Set<Cart> cart,List<Order> order,
			List<RescueCase> rescueCases, List<Follow> follow, List<LostCase> lostCase, List<AdoptionCase> adoptionCase,
			List<ReportCase> reportCase, Set<AdoptionCaseApply> adoptionCaseApply) {
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
		this.acitvityParticipantLists = acitvityParticipantLists;
		// this.wishList = wishList;
		// this.cart = cart;
		// this.order = order;
		this.rescueCases = rescueCases;
		this.follows = follow;
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

	public String getUserLineId() {
		return userLineId;
	}

	public void setUserLineId(String userLineId) {
		this.userLineId = userLineId;
	}

	public String getBindingToken() {
		return bindingToken;
	}

	public void setBindingToken(String bindingToken) {
		this.bindingToken = bindingToken;
	}

	public LocalDateTime getBindingTokenExpiry() {
		return bindingTokenExpiry;
	}

	public void setBindingTokenExpiry(LocalDateTime bindingTokenExpiry) {
		this.bindingTokenExpiry = bindingTokenExpiry;
	}

	public String getLineId() {
		return lineId;
	}

	public void setLineId(String lineId) {
		this.lineId = lineId;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public String getLinePicture() {
		return linePicture;
	}

	public void setLinePicture(String linePicture) {
		this.linePicture = linePicture;
	}
	

	public boolean isUserType() {
		return userType;
	}

	public void setUserType(boolean userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "Member [memberId=" + memberId + ", nickName=" + nickName + ", password=" + password + ", name=" + name
				+ ", email=" + email + ", phone=" + phone + ", address=" + address + ", birthday=" + birthday
				+ ", createDate=" + createDate + ", updateDate=" + updateDate + ", rescueCases=" + rescueCases
				+ ", activity=" + activity + ", acitvityParticipantLists=" + acitvityParticipantLists + ", follows="
				+ follows + ", lostCase=" + lostCase + ", adoptionCase=" + adoptionCase + ", reportCase=" + reportCase
				+ ", adoptionCaseApply=" + adoptionCaseApply + "]";
	}

}
