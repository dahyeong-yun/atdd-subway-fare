package nextstep.auth.domain;

import nextstep.member.domain.Member;


public interface UserDetails {
    String getPrincipal();
    String getCredential();
}
