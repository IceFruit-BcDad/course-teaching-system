package com.icefruit.courseteachingsystem.auth;

public class AuthConstant {

    public static final String COOKIE_NAME = "icefruit-gateway";
    // header set for internal user id
    public static final String CURRENT_USER_HEADER = "icefruit-current-user-id";
    // AUTHORIZATION_HEADER is the http request header
    // key used for accessing the internal authorization.
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // AUTHORIZATION_ANONYMOUS_WEB is set as the Authorization header to denote that
    // a request is being made bu an unauthenticated web user
    public static final String AUTHORIZATION_ANONYMOUS_WEB = "icefruit-anonymous";
    // AUTHORIZATION_SUPPORT_USER is set as the Authorization header to denote that
    // a request is being made by a Staffjoy team member
    public static final String AUTHORIZATION_SUPPORT_USER = "icefruit-support";
    // AUTHORIZATION_AUTHENTICATED_USER is set as the Authorization header to denote that
    // a request is being made by an authenticated we6b user
    public static final String AUTHORIZATION_AUTHENTICATED_USER = "icefruit-authenticated";

    public static final String AUTHORIZATION_ADMINISTRATOR_USER = "icefruit-administrator";

    // AUTH ERROR Messages
    public static final String ERROR_MSG_DO_NOT_HAVE_ACCESS = "You do not have access to this service";
    public static final String ERROR_MSG_MISSING_AUTH_HEADER = "Missing Authorization http header";
}
