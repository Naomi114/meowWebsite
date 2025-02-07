package tw.com.ispan.dto;

public class AdoptioncaseDTO {
    private Integer caseStateId; // 前端傳來的 caseStateId
    private String title;
    private String note;

    public Integer getCaseStateId() {
        return caseStateId;
    }

    public void setCaseStateId(Integer caseStateId) {
        this.caseStateId = caseStateId;
    }

    // Getter 和 Setter 方法
    public String getTitle() {
        return title;
    }

    public void setStatus(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
