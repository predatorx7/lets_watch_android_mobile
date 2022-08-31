package com.magnificsoftware.letswatch.manager.authentication

enum class AppAuthenticationStatus {
    /** User is unauthenticated & does not have a subscription */
    Unauthenticated,

    /** User is authenticated but does not have a subscription */
    Authenticated,

    /** User is authenticated & has a subscription */
    Subscribed,
}