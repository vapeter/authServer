package eu.vargapeter.demo.log.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditDTO {

    private LocalDateTime date;
    private String user;
    private String log;
    private String type;
}
