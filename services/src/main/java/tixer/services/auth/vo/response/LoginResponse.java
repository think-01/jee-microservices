package tixer.services.auth.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Created by slawek@t01.pl on 2016-03-15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class LoginResponse {

    public JWTHeader header;

    public class JWTHeader
    {
        public String alg = "HS256";
        public String typ = "JWT";
    }

    public JWTClaim claim;

    @JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
    public class JWTClaim
    {
        public String iss;
        public String email;
        public String name;
        public Integer role;
        public Integer company;
    }

    public String token;

    public LoginResponse(
            String iss,
            String email,
            String token,
            Integer company,
            Integer role) {
        this.header = new JWTHeader();

        this.claim = new JWTClaim();

        this.claim.iss = iss;
        this.claim.email = email;
        this.claim.role = role;
        this.claim.company = company;
        this.token = token;
    }

    public LoginResponse(
            String iss,
            String email,
            String token,
            String name
    ) {
        this.header = new JWTHeader();

        this.claim = new JWTClaim();

        this.claim.iss = iss;
        this.claim.email = email;
        this.claim.name = name;
        this.token = token;
    }

}
