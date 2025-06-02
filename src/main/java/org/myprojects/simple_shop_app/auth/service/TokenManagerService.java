package org.myprojects.simple_shop_app.auth.service;

public interface TokenManagerService {
    Boolean doesAccessTokenExist(String token);
}
