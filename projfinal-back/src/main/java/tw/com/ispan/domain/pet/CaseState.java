package tw.com.ispan.domain.pet;

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

    
    
	public CaseState() {
		super();
	}



	public CaseState(Integer caseStateId, String caseStatement) {
		super();
		CaseStateId = caseStateId;
		this.caseStatement = caseStatement;
	}



	public Integer getCaseStateId() {
		return CaseStateId;
	}



	public void setCaseStateId(Integer caseStateId) {
		CaseStateId = caseStateId;
	}



	public String getCaseStatement() {
		return caseStatement;
	}



	public void setCaseStatement(String caseStatement) {
		this.caseStatement = caseStatement;
	}



	@Override
	public String toString() {
		return "CaseState [CaseStateId=" + CaseStateId + ", caseStatement=" + caseStatement + "]";
	}
    
    
	
	

}