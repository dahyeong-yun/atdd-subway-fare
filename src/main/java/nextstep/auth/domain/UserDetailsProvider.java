package nextstep.auth.domain;

public interface UserDetailsProvider {
    String getPrincipal();
    String getCredential();
}
