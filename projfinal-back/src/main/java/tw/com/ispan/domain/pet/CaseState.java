package tw.com.ispan.projfinal_back.domain.pet;

import jakarta.persistence.*;

@Entity
@Table(name = "CaseState")
public class CaseState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //
    @Column(name = "CaseStateId")
    private Integer CaseStateId;

    @Column(name = "CaseStatement", columnDefinition = "NVARCHAR(5)", nullable = false)
    private String caseStatement;

}