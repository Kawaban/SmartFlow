package api.infrastructure.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Version
    private Long version;

    private Instant createdDate;
    private Instant lastModifiedDate;

    @Override
    public String toString() {
        return "uuid="
                + uuid
                + ", version="
                + version
                + ", createDate="
                + createdDate
                + ", lastModifiedDate="
                + lastModifiedDate
                + ",";
    }
}
