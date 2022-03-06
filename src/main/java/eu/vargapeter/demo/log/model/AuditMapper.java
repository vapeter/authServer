package eu.vargapeter.demo.log.model;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditMapper {

    AuditDTO toAuditDTO(Audit audit);

    List<AuditDTO> toAuditDTOs(List<Audit> audits);
}
