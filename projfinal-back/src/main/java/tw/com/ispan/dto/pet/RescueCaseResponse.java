package tw.com.ispan.dto.pet;

import java.util.List;

import tw.com.ispan.domain.pet.RescueCase;

public class RescueCaseResponse {
	private Boolean success;
    private String message;
    private List<RescueCase> list;
    private Long count;
	
    
    public RescueCaseResponse() {
		super();
	}

	public RescueCaseResponse(Boolean success, String message, List<RescueCase> list, Long count) {
		super();
		this.success = success;
		this.message = message;
		this.list = list;
		this.count = count;
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

	public List<RescueCase> getList() {
		return list;
	}

	public void setList(List<RescueCase> list) {
		this.list = list;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "RescueCaseResponse [success=" + success + ", message=" + message + ", list=" + list + ", count=" + count
				+ "]";
	}
    
    
    
    
    
}
