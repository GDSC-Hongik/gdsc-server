package com.gdschongik.gdsc.global.common.constant;

public class RegexConstant {
    public static final String STUDENT_ID = "^[A-C]{1}[0-9]{6}$";
    public static final String PHONE = "^010-[0-9]{4}-[0-9]{4}$";
    public static final String NICKNAME = "[ㄱ-ㅣ가-힣]{1,6}$";

    private RegexConstant() {}
}
