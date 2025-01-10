package tw.com.ispan.domain.pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import tw.com.ispan.domain.admin.Member;

@Entity
@Table(name = "AdoptionCase")
public class AdoptionCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adoptionCaseId;

    @Column(name = "caseTitle", columnDefinition = "nvarchar(30)", nullable = false)
    private String caseTitle;

    // 雙向多對一,外鍵,對應member表
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "memberId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_Member"))
    private Member member;
    // 單向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "specieId", nullable = false, foreignKey = @ForeignKey(name = "FK_AdoptionCase_Species"))
    private Species species;
    // 單向多對一
    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "breedId", nullable = false, foreignKey = @ForeignKey(name = "FK_FK_AdoptionCase_Breed"))
    private Breed breed;

}
