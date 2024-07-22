package api.authentication.domain;

import api.infrastructure.model.AbstractEntity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "developer_credentials")
public class DeveloperCredentials extends AbstractEntity {
    private String username;
    private String password;

    @Builder
    public DeveloperCredentials(UUID id, long version, Instant createdDate, Instant lastModifiedDate, String username, String password) {
        super(id, version, createdDate, lastModifiedDate);
        this.username = username;
        this.password = password;
    }
}
