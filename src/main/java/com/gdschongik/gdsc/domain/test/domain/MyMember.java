package com.gdschongik.gdsc.domain.test.domain;

import com.gdschongik.gdsc.domain.common.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Boolean advanced; // 정회원 여부

    public MyMember(String name) {
        this.name = name;
        this.advanced = false;
    }

    public void advance() {
        this.advanced = true;
        registerEvent(new MemberAdvancedEvent(this.id));
    }

    record MemberAdvancedEvent(Long memberId) {}
}
