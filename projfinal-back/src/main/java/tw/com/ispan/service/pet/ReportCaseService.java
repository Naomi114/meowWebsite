package tw.com.ispan.service.pet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.ispan.domain.admin.Admin;
import tw.com.ispan.domain.admin.Member;
import tw.com.ispan.domain.pet.LostCase;
import tw.com.ispan.domain.pet.ReportCase;
import tw.com.ispan.repository.admin.AdminRepository;
import tw.com.ispan.repository.admin.MemberRepository;
import tw.com.ispan.repository.pet.LostCaseRepository;
import tw.com.ispan.repository.pet.ReportCaseRepository;
import tw.com.ispan.repository.pet.RescueCaseRepository;

@Service
public class ReportCaseService {

    @Autowired
    private ReportCaseRepository reportCaseRepository;
    @Autowired
    private RescueCaseRepository rescueCaseRepository;
    @Autowired
    private LostCaseRepository lostCaseRepository;
    // @Autowired
    // private AdoptionCaseRepository adoptionCaseRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private MemberRepository memberRepository;

    public ReportCase review(Integer reportCaseId, Integer adminId, boolean isApproved, boolean hideCase) {
        // 確認舉報案件是否存在
        ReportCase reportCase = reportCaseRepository.findById(reportCaseId)
                .orElseThrow(() -> new IllegalArgumentException("舉報案件不存在！"));

        // 更新審核狀態
        reportCase.setReportState(isApproved); // true 表示審核完成
        reportCase.setUpdateDate(LocalDateTime.now());

        // 設置修改者 (Admin)
        if (adminId != null) {
            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new IllegalArgumentException("管理員不存在！"));
            reportCase.setAdmin(admin);
        } else {
            throw new IllegalArgumentException("審核時必須指定管理員！");
        }

        // 隱藏案件邏輯
        if (hideCase) {
            if (reportCase.getLostCase() != null) {
                LostCase lostCase = reportCase.getLostCase();
                lostCaseRepository.save(lostCase);
            }
            // else if (reportCase.getRescueCase() != null) {
            // RescueCase rescueCase = reportCase.getRescueCase();
            // rescueCase.setIsHidden(true); // 隱藏救援案件
            // rescueCaseRepository.save(rescueCase);
            // }
            // else if (reportCase.getAdoptionCase() != null) {
            // AdoptionCase adoptionCase = reportCase.getAdoptionCase();
            // adoptionCase.setIsHidden(true); // 隱藏領養案件
            // adoptionCaseRepository.save(adoptionCase);
            // }
        }

        // 保存舉報案件的審核結果
        return reportCaseRepository.save(reportCase);
    }

    public ReportCase modify(String json) {
        try {
            // 解析 JSON 请求
            JSONObject obj = new JSONObject(json);
            Integer reportId = obj.optInt("reportId", -1); // 获取报告 ID
            boolean reportState = obj.optBoolean("reportState", false); // 获取报告状态
            boolean hideCase = obj.optBoolean("hideCase", false); // 是否隐藏案件
            Integer adminId = obj.optInt("adminId", -1); // 获取管理员 ID

            // 验证报告 ID
            if (reportId == -1) {
                throw new IllegalArgumentException("報告 ID 無效！");
            }

            // 查找报告记录
            ReportCase reportCase = reportCaseRepository.findById(reportId)
                    .orElseThrow(() -> new IllegalArgumentException("找不到指定的報告！"));

            // 验证管理员 ID
            if (adminId == -1) {
                throw new IllegalArgumentException("管理員 ID 無效！");
            }

            Admin admin = adminRepository.findById(adminId)
                    .orElseThrow(() -> new IllegalArgumentException("無效的管理員 ID！"));

            // 更新报告状态
            reportCase.setReportState(reportState); // 标记为已审核
            reportCase.setAdmin(admin); // 设置审核管理员
            reportCase.setUpdateDate(LocalDateTime.now()); // 设置最后更新时间

            // 隱藏案件邏輯
            if (hideCase) {
                if (reportCase.getLostCase() != null) {
                    LostCase lostCase = reportCase.getLostCase();
                    lostCaseRepository.save(lostCase);
                }
                // else if (reportCase.getRescueCase() != null) {
                // RescueCase rescueCase = reportCase.getRescueCase();
                // rescueCase.setIsHidden(true); // 隱藏救援案件
                // rescueCaseRepository.save(rescueCase);
                // }
                // else if (reportCase.getAdoptionCase() != null) {
                // AdoptionCase adoptionCase = reportCase.getAdoptionCase();
                // adoptionCase.setIsHidden(true); // 隱藏領養案件
                // adoptionCaseRepository.save(adoptionCase);
                // }
            }

            // 保存并返回修改后的报告
            return reportCaseRepository.save(reportCase);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("修改報告失敗：" + e.getMessage());
        }
    }

    public ReportCase create(String json) {
        try {
            JSONObject obj = new JSONObject(json);

            // 獲取報告參數
            Integer memberId = obj.optInt("memberId", 0);
            Integer rescueCaseId = obj.optInt("rescueCaseId", -1);
            Integer lostCaseId = obj.optInt("lostCaseId", -1);
            Integer adoptionCaseId = obj.optInt("adoptionCaseId", -1);
            String reportTitle = obj.optString("reportTitle");
            String reportNotes = obj.optString("reportNotes");

            // 驗證必填字段
            if (memberId == 0) {
                throw new IllegalArgumentException("必須指定舉報者 ID！");
            }

            if ((rescueCaseId == -1 && lostCaseId == -1 && adoptionCaseId == -1) || reportTitle == null
                    || reportNotes == null) {
                throw new IllegalArgumentException("必須指定一種類型的案件進行舉報，且標題和內容為必填項！");
            }

            // 確保只有一種類型的案件被舉報
            if ((rescueCaseId != -1 ? 1 : 0) + (lostCaseId != -1 ? 1 : 0) + (adoptionCaseId != -1 ? 1 : 0) > 1) {
                throw new IllegalArgumentException("只能舉報一種類型的案件！");
            }

            // 確認 memberId 是否存在
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("舉報者不存在！"));

            // 檢查唯一性
            if (reportCaseRepository
                    .existsByRescueCase_RescueCaseIdAndLostCase_LostCaseIdAndAdoptionCase_AdoptionCaseIdAndReportTitle(
                            rescueCaseId, lostCaseId, adoptionCaseId, reportTitle)) {
                throw new IllegalArgumentException("該舉報案件已經存在！");
            }

            // 創建報告
            ReportCase reportCase = new ReportCase();
            reportCase.setRescueCase(
                    rescueCaseId != -1 ? rescueCaseRepository.findById(rescueCaseId).orElse(null) : null);
            reportCase.setLostCase(
                    lostCaseId != -1 ? lostCaseRepository.findById(lostCaseId).orElse(null) : null);
            // reportCase.setAdoptionCase(
            // adoptionCaseId != -1 ?
            // adoptionCaseRepository.findById(adoptionCaseId).orElse(null) : null);
            reportCase.setMember(member); // 設置舉報者
            reportCase.setReportDate(LocalDateTime.now());
            reportCase.setReportTitle(reportTitle);
            reportCase.setReportNotes(reportNotes);
            reportCase.setReportState(false); // 初始狀態為未審核
            reportCase.setAdmin(null); // 創建時沒有管理員

            return reportCaseRepository.save(reportCase);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("創建報告失敗：" + e.getMessage());
        }
    }

    public ReportCase findById(Integer id) {
        if (id != null) {
            Optional<ReportCase> optional = reportCaseRepository.findById(id);
            if (optional.isPresent()) {
                return optional.get();
            }
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

    public ReportCase insert(ReportCase bean) {
        if (bean != null && bean.getReportId() != null) {
            if (!reportCaseRepository.existsById(bean.getReportId())) {
                return reportCaseRepository.save(bean);
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

    public List<ReportCase> find(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return reportCaseRepository.find(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
