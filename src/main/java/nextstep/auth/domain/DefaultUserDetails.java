package nextstep.auth.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultUserDetails implements UserDetails {
    private final String principal;
    private final String credential;

    @Override
    public String getPrincipal() {
        return principal;
    }

    @Override
    public String getCredential() {
        return credential;
    }
}
