package com.shopdirect.forecasting.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = {"/token"}, produces = APPLICATION_JSON_VALUE)
@Validated
public class TokenController {

    private final JdbcTemplate jdbc;

    private DefaultTokenServices tokenServices;

    @Autowired
    public TokenController(DataSource dataSource, DefaultTokenServices tokenServices) {
        this.jdbc = new JdbcTemplate(dataSource);
        this.tokenServices = tokenServices;
    }

    @RequestMapping(method = GET, path = "/list")
    @ResponseStatus(OK)
    @Secured({"ROLE_ADMIN"})
    public List<String> findAllTokens() {
        final List<byte[]> tokens = jdbc.queryForList("select token from oauth_access_token", byte[].class);

        return tokens.stream().map(this::deserializeToken).collect(Collectors.toList());
    }

    private String deserializeToken(byte[] tokenBytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(tokenBytes);
            ObjectInput in = new ObjectInputStream(bis);
            DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) in.readObject();
            return token.getValue();
        } catch (Exception e) {
            // ignore
        }

        return null;
    }

    @RequestMapping(method = DELETE, path = "/revoke")
    @ResponseStatus(OK)
    public void revokeToken(Authentication authentication) {
        final String userToken = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
        tokenServices.revokeToken(userToken);
    }
}
