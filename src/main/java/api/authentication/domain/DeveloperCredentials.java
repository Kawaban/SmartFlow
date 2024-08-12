package api.authentication.domain;

import api.developer.domain.Developer;
import api.infrastructure.model.AbstractEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "developer_credentials")
class DeveloperCredentials extends AbstractEntity implements UserDetails {
    private String username;
    private char[] password;
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    private Developer developer;

    @Builder
    public DeveloperCredentials(UUID id, long version, Instant createdDate, Instant lastModifiedDate, String username, char[] password, Role role, Developer developer) {
        super(id, version, createdDate, lastModifiedDate);
        this.username = username;
        this.password = password;
        this.role = role;
        this.developer = developer;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword(){return new String(password);}


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
