package com.jamdb.japi.entities.user;

import com.jamdb.japi.entities.BaseEntity;
import com.jamdb.japi.entities.content.Content;
import com.jamdb.japi.entities.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity(name = "users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)

public class User extends BaseEntity implements UserDetails {

    @Column(nullable = false, unique = true)
    private String username;
    @Email
    @Size(max = 128)
    @Column(nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String password;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;
    @OneToMany
    @JoinTable(name="user_anime",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "anime_id"))
    private Set<Content> anime = new HashSet<>();

    public void setUserRole(String userRole) {
        this.userRole = UserRole.valueOf(userRole.toUpperCase());
    }

    public void setAnime(Content content){
        anime.add(content);
    }
    public void setAnimeList(Set<Content> animeList){
        anime=animeList;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(userRole.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

