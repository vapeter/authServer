package eu.vargapeter.demo.log.service;

import eu.vargapeter.demo.log.model.Audit;
import eu.vargapeter.demo.log.model.AuditDTO;
import eu.vargapeter.demo.log.model.LogType;

import java.util.List;

public interface AuditService {

    void createAudit(String log, LogType type);

    void saveAudit(Audit audit);

    List<AuditDTO> getAllLog();

    List<AuditDTO> getLogByDate();

    List<AuditDTO> getLogByTime();

    Audit auditBuilder(String userName, String log, LogType type);

    String getUserName();
}
