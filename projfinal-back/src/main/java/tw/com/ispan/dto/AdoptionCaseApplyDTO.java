package tw.com.ispan.dto;

public class AdoptionCaseApplyDTO {

    private String introduction;
    private boolean applicationStatus;
    private Integer memberId;
    private Integer adoptionCaseId; // 單一 AdoptionCase ID

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getAdoptionCaseId() {
        return adoptionCaseId;
    }

    public void setAdoptionCaseId(Integer adoptionCaseId) {
        this.adoptionCaseId = adoptionCaseId;
    }

    public boolean isApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(boolean applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

}
