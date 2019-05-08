package com.shopdirect.forecasting.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping(value = {"/admin"}, produces = APPLICATION_JSON_VALUE)
@Validated
public class TokenController {

    private final JdbcTemplate jdbc;

    @Autowired
    public TokenController(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    @RequestMapping(method = GET, path = "/token/list")
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
}
