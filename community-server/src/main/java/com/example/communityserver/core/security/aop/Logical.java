package com.example.communityserver.core.security.aop;

public enum Logical {
    /**
     * 必须满足所有条件
     */
    AND,

    /**
     * 只需满足任意一个条件
     */
    OR,

    /**
     * 必须不满足所有条件
     */
    NOT
}
