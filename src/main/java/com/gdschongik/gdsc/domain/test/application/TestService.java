package com.gdschongik.gdsc.domain.test.application;

import com.gdschongik.gdsc.domain.test.dao.MyMemberRepository;
import com.gdschongik.gdsc.domain.test.dao.MyMembershipRepository;
import com.gdschongik.gdsc.domain.test.dao.MyOrderRepository;
import com.gdschongik.gdsc.domain.test.domain.MyMember;
import com.gdschongik.gdsc.domain.test.domain.MyMembership;
import com.gdschongik.gdsc.domain.test.domain.MyOrder;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    private final MyOrderRepository myOrderRepository;
    private final MyMembershipRepository myMembershipRepository;
    private final MyMemberRepository myMemberRepository;

    @Transactional
    public void reset() {
        // 삭제
        myOrderRepository.deleteAll();
        myMembershipRepository.deleteAll();
        myMemberRepository.deleteAll();

        // 재생성
        MyMember member = new MyMember("안재현");
        myMemberRepository.save(member);

        MyMembership membership = new MyMembership(member);
        myMembershipRepository.save(membership);

        MyOrder order = new MyOrder(membership.getId(), member.getId());
        myOrderRepository.save(order);

        log.info("Reset complete");
    }

    @Transactional
    public void confirm() {
        MyOrder order = myOrderRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        log.info("Order found: {}", order);

        order.confirm();

        log.info("Order confirmed: {}", order);

        myOrderRepository.save(order); // 명시적으로 save하여 이벤트 발행

        log.info("Order saved after confirmation: {}", order);
    }

    public void verifyMembership(Long orderId) {
        MyOrder order =
                myOrderRepository.findById(orderId).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        log.info("Order found for verifying membership: {}", order);

        MyMembership membership = myMembershipRepository
                .findById(order.getMembershipId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        log.info("Membership found for verifying: {}", membership);

        if (membership.getPaid()) {
            throw new CustomException(ErrorCode.MEMBERSHIP_ALREADY_SATISFIED);
        }

        membership.verifyPayment();

        log.info("Membership verified: {}", membership);

        myMembershipRepository.save(membership);

        log.info("Membership saved after verification: {}", membership);
    }

    public void advanceMember(Long membershipId) {
        MyMembership membership = myMembershipRepository
                .findById(membershipId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND));

        log.info("Membership found for advancing member: {}", membership);

        MyMember member = myMemberRepository
                .findById(membership.getMember().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        log.info("Member found for advancing: {}", member);

        member.advance();

        log.info("Member advanced: {}", member);

        myMemberRepository.save(member);

        log.info("Member saved after advancing: {}", member);
    }
}
