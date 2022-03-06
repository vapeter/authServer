package eu.vargapeter.demo.log.service;

import eu.vargapeter.demo.log.model.Audit;
import eu.vargapeter.demo.log.model.AuditDTO;
import eu.vargapeter.demo.log.model.AuditMapper;
import eu.vargapeter.demo.log.model.LogType;
import eu.vargapeter.demo.log.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private AuditRepository auditRepository;
    private AuditMapper auditMapper;

    @Autowired
    public AuditServiceImpl(AuditRepository auditRepository, AuditMapper auditMapper) {
        this.auditRepository = auditRepository;
        this.auditMapper = auditMapper;
    }

    @Override
    public void createAudit(String log, LogType type) {

        String user = getUserName();
        this.saveAudit( auditBuilder( user, log, type ));
    }

    @Override
    public void saveAudit(Audit audit) {
        auditRepository.save(audit);
    }

    @Override
    public List<AuditDTO> getAllLog() {
        return auditMapper.toAuditDTOs(auditRepository.findAll());
    }

    @Override
    public List<AuditDTO> getLogByDate() {
        // TODO
        return null;
    }

    @Override
    public List<AuditDTO> getLogByTime() {
        // TODO
        return null;
    }

    @Override
    public Audit auditBuilder(String userName, String log, LogType type) {

        LocalDateTime time = LocalDateTime.now();

        Audit newLog = Audit.builder()
                .user(userName)
                .date(time)
                .log(log)
                .type(type)
                .build();

        return newLog;
    }

    @Override
    public String getUserName() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
