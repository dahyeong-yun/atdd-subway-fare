package nextstep.member.domain;

import nextstep.auth.domain.DefaultUserDetails;
import nextstep.auth.domain.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsMapper {
    public UserDetails toUserDetails(Member member) {
        return new DefaultUserDetails(member.getEmail(), member.getPassword());
    }
}
