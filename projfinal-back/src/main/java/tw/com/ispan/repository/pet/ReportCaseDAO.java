package tw.com.ispan.repository.pet;

import java.util.List;

import org.json.JSONObject;

import tw.com.ispan.domain.pet.ReportCase;

public interface ReportCaseDAO {

    public abstract Long count(JSONObject param);

    public abstract List<ReportCase> find(JSONObject param);
}
