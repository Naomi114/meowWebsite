package tw.com.ispan.dto;

import java.util.List;
import tw.com.ispan.domain.pet.LostCase;

public class LostCaseResponse {
    private Boolean success;
    private String message;
    private List<LostCase> list;
    private Long count;

    @Override
    public String toString() {
        return "LostCaseResponse [success=" + success + ", message=" + message + ", list=" + list + ", count=" + count
                + "]";
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LostCase> getList() {
        return list;
    }

    public void setList(List<LostCase> list) {
        this.list = list;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
