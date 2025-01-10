package tw.com.ispan.domain.admin;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import tw.com.ispan.domain.pet.Activity;
import tw.com.ispan.domain.pet.ActivityParticipantList;
import tw.com.ispan.domain.pet.Follow;
import tw.com.ispan.domain.pet.RescueCase;
import tw.com.ispan.domain.shop.Order;

@Entity
@Table(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer memberId;
    
     @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval =
     true)
     private List<Order> orders;

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private List<RescueCase> rescueCases;

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    private Set<Activity> activity;

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private Set<ActivityParticipantList> acitvityParticipantLists;

    @OneToMany(mappedBy = "member", cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private Follow follows;

    // Constructors, getters, setters, toString()
}
