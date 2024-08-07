package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.common.constant.GithubConstant.*;
import static com.gdschongik.gdsc.global.exception.ErrorCode.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.study.dto.request.RepositoryUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.StudyResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.client.github.GithubClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final MemberUtil memberUtil;
    private final GithubClient githubClient;
    private final StudyRepository studyRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;
    private final StudyHistoryValidator studyHistoryValidator;

    public List<StudyResponse> getAllApplicableStudies() {
        return studyRepository.findAll().stream()
                .filter(Study::isApplicable)
                .map(StudyResponse::from)
                .toList();
    }

    @Transactional
    public void applyStudy(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        Member currentMember = memberUtil.getCurrentMember();

        List<StudyHistory> currentMemberStudyHistories = studyHistoryRepository.findAllByMentee(currentMember);

        studyHistoryValidator.validateApplyStudy(study, currentMemberStudyHistories);

        StudyHistory studyHistory = StudyHistory.create(currentMember, study);
        studyHistoryRepository.save(studyHistory);

        log.info("[StudyService] 스터디 수강신청: studyHistoryId={}", studyHistory.getId());
    }

    @Transactional
    public void cancelStudyApply(Long studyId) {
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        Member currentMember = memberUtil.getCurrentMember();

        studyHistoryValidator.validateCancelStudyApply(study);

        StudyHistory studyHistory = studyHistoryRepository
                .findByMenteeAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));
        studyHistoryRepository.delete(studyHistory);

        log.info("[StudyService] 스터디 수강신청 취소: studyId={}, memberId={}", study.getId(), currentMember.getId());
    }

    @Transactional
    public void updateRepository(Long studyId, RepositoryUpdateRequest request) {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));

        StudyHistory studyHistory = studyHistoryRepository
                .findByMenteeAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));

        boolean isAnyAssignmentSubmitted = assignmentHistoryRepository.existsSubmittedAssignment(currentMember, study);
        studyHistoryValidator.validateUpdateRepository(isAnyAssignmentSubmitted);
        validateRepositoryLink(request.repositoryLink());

        studyHistory.updateRepositoryLink(request.repositoryLink());
        studyHistoryRepository.save(studyHistory);

        log.info("[StudentStudyService] 레포지토리 입력: studyHistoryId={}", studyHistory.getId());
    }

    private void validateRepositoryLink(String repositoryLink) {
        String ownerRepo = getOwnerRepo(repositoryLink);
        githubClient.getRepository(ownerRepo);
    }

    private String getOwnerRepo(String repositoryLink) {
        int startIndex = repositoryLink.indexOf(GITHUB_DOMAIN) + GITHUB_DOMAIN.length();
        return repositoryLink.substring(startIndex);
    }
}
