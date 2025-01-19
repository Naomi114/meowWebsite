package tw.com.ispan.service.pet;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.repository.pet.ReportCaseRepository;

@Service
public class ReportCaseService {

    @Autowired
    private ReportCaseRepository reportCaseRepository;

    public LostCase insert(LostCase bean) {
        if (bean != null && bean.getLostCaseId() != null) {
            if (!reportCaseRepository.existsById(bean.getLostCaseId())) {
                return reportCaseRepository.save(bean);
            }
        }
        return null;
    }

    public boolean delete(LostCase bean) {
        if (bean != null && bean.getLostCaseId() != null) {
            if (reportCaseRepository.existsById(bean.getLostCaseId())) {
                reportCaseRepository.deleteById(bean.getLostCaseId());
                return true;
            }
        }
        return false;
    }

    public LostCase findById(Integer id) {
        if (id != null) {
            Optional<LostCase> optional = reportCaseRepository.findById(id);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }

    public long count(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return reportCaseRepository.count(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<LostCase> find(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return reportCaseRepository.find(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean exists(Integer reportCaseId) {
        if (reportCaseId == null) {
            return false;
        }
        return reportCaseRepository.existsById(reportCaseId);
    }

    public boolean remove(Integer reportCaseId) {
        if (reportCaseId == null || !reportCaseRepository.existsById(reportCaseId)) {
            return false; // 如果 ID 为 null 或不存在，返回 false
        }
        reportCaseRepository.deleteById(reportCaseId); // 执行删除操作
        return true;
    }

    public List<ReportCase> select(ReportCase condition) {

    }
}
