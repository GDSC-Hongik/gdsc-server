package com.gdschongik.gdsc.domain.member.domain;

import static com.gdschongik.gdsc.global.common.constant.RegexConstant.*;

import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phone {

    String number;

    public Phone(String phone) {
        this.number = getNumberWithoutHyphen(phone);
    }

    public String getNumber() {
        return getNumberWithHyphen(number);
    }

    private String getNumberWithHyphen(String phone) {
        String phoneWithHyphen =
                String.format("%s-%s-%s", phone.substring(0, 3), phone.substring(3, 7), phone.substring(7));
        validateNumber(phoneWithHyphen, PHONE);
        return phoneWithHyphen;
    }

    private String getNumberWithoutHyphen(String phone) {
        String phoneWithoutHyphen = phone.replaceAll("-", "");
        validateNumber(phoneWithoutHyphen, PHONE_WITHOUT_HYPHEN);
        return phoneWithoutHyphen;
    }

    private void validateNumber(String number, String regex) {
        if (!number.matches(regex)) {
            throw new CustomException(ErrorCode.REGEX_VIOLATION);
        }
    }
}
