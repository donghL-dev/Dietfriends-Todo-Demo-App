package com.donghun.todo.web.auth;

public class SecurityConstants {
    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_TYPE = "JwtToken";

    public static final String TOKEN_ISSUER = "Todo Server";

    public static final String TOKEN_AUDIENCE = "Todo Client";

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }
}
