package tw.com.ispan.domain.pet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CaseState")
public class CaseState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CaseStateId")
    private Integer caseStateId;

    @Column(name = "CaseStatement", columnDefinition = "NVARCHAR(5)", nullable = false)
    private String caseStatement;

    
    
	public CaseState() {
		super();
	}

	
	public CaseState(Integer caseStateId) {
		super();
		this.caseStateId = caseStateId;
	}


	public CaseState(String caseStatement) {
		super();
		this.caseStatement = caseStatement;
	}


	public Integer getCaseStateId() {
		return caseStateId;
	}



	public void setCaseStateId(Integer caseStateId) {
		this.caseStateId = caseStateId;
	}



	public String getCaseStatement() {
		return caseStatement;
	}



	public void setCaseStatement(String caseStatement) {
		this.caseStatement = caseStatement;
	}



	@Override
	public String toString() {
		return "CaseState [CaseStateId=" + caseStateId + ", caseStatement=" + caseStatement + "]";
	}
    
    
	
	

}