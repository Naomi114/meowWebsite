package tw.com.ispan.repository.pet;

import java.util.List;

import org.json.JSONObject;

import tw.com.ispan.domain.pet.LostCase;

public interface LostCaseDAO {
    public abstract Long count(JSONObject param);

    public abstract List<LostCase> find(JSONObject param);
}
