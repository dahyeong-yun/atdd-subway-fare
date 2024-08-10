package nextstep.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@Profile("test")
@Component
public class AcceptanceContext {
    public Map<String, Long> store = new HashMap<>();
    public ExtractableResponse<Response> response;
    public Map<String, String> tokenStore = new HashMap<>();
}
