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
import tw.com.ispan.domain.shop.Cart;
import com.fasterxml.jackson.annotation.JsonBackReference;

import tw.com.ispan.domain.shop.CartActionLog;
import tw.com.ispan.domain.shop.Orders;
import tw.com.ispan.domain.shop.WishList;

@Entity
@Table(name = "Member") // Ensure using table name "Member"
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

    // 以下為給line login使用(冠)
    private String lineId;

    private String lineName;

    private String linePicture;

    // 以下為給追蹤line商家帳號使用(冠)
    private boolean followed = false;

    @Column(nullable = false)
    private boolean userType; // 1表示註冊會員，0表示line登入會員(冠)

 	// 雙向一對多
     @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST })
     private List<Activity> activity;

   // 會員和活動的中介表 雙向一對多
	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST }, orphanRemoval = true)
	private List<ActivityParticipantList> acitvityParticipantLists;

   // 雙向一對多
	@OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST })
	private List<RescueCase> rescueCases;

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

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    @JsonBackReference("member")
    private List<WishList> wishList;

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private Set<Cart> cart;

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JsonBackReference("member")
    private Set<CartActionLog> cartActionLog;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Orders> orders;

    // Constructors
    public Member() {
    }

    public Member(Integer memberId, String nickName, String password, String name, String email, String phone,
            String address, LocalDate birthday, LocalDateTime createDate, LocalDateTime updateDate,
            List<Activity> activity, List<ActivityParticipantList> acitvityParticipantList, String lineId,
            String lineName, String linePicture, boolean followed, boolean userType, List<WishList> wishList,
            Set<Cart> cart, Set<CartActionLog> cartActionLog, List<Orders> orders, List<Follow> follows,
            List<LostCase> lostCase, List<AdoptionCase> adoptionCase, List<ReportCase> reportCase,
            Set<AdoptionCaseApply> adoptionCaseApply) {
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
        this.acitvityParticipantLists = acitvityParticipantList;
        this.lineId = lineId;
        this.lineName = lineName;
        this.linePicture = linePicture;
        this.followed = followed;
        this.userType = userType;
        this.wishList = wishList;
        this.cart = cart;
        this.cartActionLog = cartActionLog;
        this.orders = orders;
        this.follows = follows;
        this.lostCase = lostCase;
        this.adoptionCase = adoptionCase;
        this.reportCase = reportCase;
        this.adoptionCaseApply = adoptionCaseApply;
    }

  

    public List<Activity> getActivity() {
        return activity;
    }

    public void setActivity(List<Activity> activity) {
        this.activity = activity;
    }

    public List<ActivityParticipantList> getAcitvityParticipantLists() {
        return acitvityParticipantLists;
    }

    public void setAcitvityParticipantLists(List<ActivityParticipantList> acitvityParticipantLists) {
        this.acitvityParticipantLists = acitvityParticipantLists;
    }

    public List<RescueCase> getRescueCases() {
        return rescueCases;
    }

    public void setRescueCases(List<RescueCase> rescueCases) {
        this.rescueCases = rescueCases;
    }

    public List<Follow> getFollows() {
        return follows;
    }

    public void setFollows(List<Follow> follows) {
        this.follows = follows;
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

    public boolean isFollowed() {
        return followed;
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public boolean isUserType() {
        return userType;
    }

    public void setUserType(boolean userType) {
        this.userType = userType;
    }

    public Set<CartActionLog> getCartActionLog() {
        return cartActionLog;
    }

    public void setCartActionLog(Set<CartActionLog> cartActionLog) {
        this.cartActionLog = cartActionLog;
    }
    public List<LostCase> getLostCase() {
        return lostCase;
    }

    public void setLostCase(List<LostCase> lostCase) {
        this.lostCase = lostCase;
    }

    public List<AdoptionCase> getAdoptionCase() {
        return adoptionCase;
    }

    public void setAdoptionCase(List<AdoptionCase> adoptionCase) {
        this.adoptionCase = adoptionCase;
    }

    public List<ReportCase> getReportCase() {
        return reportCase;
    }

    public void setReportCase(List<ReportCase> reportCase) {
        this.reportCase = reportCase;
    }

    public Set<AdoptionCaseApply> getAdoptionCaseApply() {
        return adoptionCaseApply;
    }

    public void setAdoptionCaseApply(Set<AdoptionCaseApply> adoptionCaseApply) {
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

    public List<WishList> getWishList() {
        return wishList;
    }

    public void setWishList(List<WishList> wishList) {
        this.wishList = wishList;
    }

    public Set<Cart> getCart() {
        return cart;
    }

    public void setCart(Set<Cart> cart) {
        this.cart = cart;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    // Ensure getId method returns memberId
    public Integer getId() {
        return this.memberId;
    }


    
    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", birthday=" + birthday +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", wishList=" + wishList +
                ", cart=" + cart +
                ", orders=" + orders +
                '}';
    }
}
