package com.fds.payflow.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class AuthUser extends User {
    @Getter
    private final Long userId;

    public AuthUser(String username, String encodedPassword, boolean b, boolean b1, boolean b2, boolean b3, Collection<? extends GrantedAuthority> authorities, Long userId) {
        super(username, encodedPassword, b, b1, b2, b3, authorities);
        this.userId = userId;
    }

    public static AuthUserBuilder authBuilder() {
        return new AuthUserBuilder();
    }

    public static final class AuthUserBuilder {
        private String username;

        private String password;

        private List<GrantedAuthority> authorities = new ArrayList<>();

        private boolean accountExpired;

        private boolean accountLocked;

        private boolean credentialsExpired;

        private boolean disabled;

        private Long userId;

        private Function<String, String> passwordEncoder = (password) -> password;

        private AuthUserBuilder() {
        }

        public AuthUserBuilder userId(Long userId) {
            Assert.notNull(userId, "userId cannot be null");
            this.userId = userId;
            return this;
        }


        public AuthUserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public AuthUserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public AuthUserBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public AuthUserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"),
                        () -> role + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return authorities(authorities);
        }

        public AuthUserBuilder authorities(GrantedAuthority... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return authorities(Arrays.asList(authorities));
        }

        public AuthUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public AuthUserBuilder authorities(String... authorities) {
            Assert.notNull(authorities, "authorities cannot be null");
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public AuthUserBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public AuthUserBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public AuthUserBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public AuthUserBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public AuthUser build() {
            String encodedPassword = this.passwordEncoder.apply(this.password);
            return new AuthUser(this.username, encodedPassword, !this.disabled, !this.accountExpired,
                    !this.credentialsExpired, !this.accountLocked, this.authorities, this.userId);
        }
    }
}
