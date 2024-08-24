package nextstep.auth.domain;

import lombok.Getter;

@Getter
public class LoginMember {
    private Long id;
    private String email;
    private int age;

    public LoginMember(String email) {
        this.email = email;
    }
}
