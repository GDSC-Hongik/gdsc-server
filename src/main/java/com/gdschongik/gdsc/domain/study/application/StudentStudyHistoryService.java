package com.gdschongik.gdsc.domain.study.application;

import static com.gdschongik.gdsc.global.exception.ErrorCode.*;
import static java.util.stream.Collectors.*;

import com.gdschongik.gdsc.domain.member.domain.Member;
import com.gdschongik.gdsc.domain.study.dao.AssignmentHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyAchievementRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyDetailRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyHistoryRepository;
import com.gdschongik.gdsc.domain.study.dao.StudyRepository;
import com.gdschongik.gdsc.domain.study.domain.AchievementType;
import com.gdschongik.gdsc.domain.study.domain.AssignmentHistory;
import com.gdschongik.gdsc.domain.study.domain.AssignmentSubmissionFetcher;
import com.gdschongik.gdsc.domain.study.domain.Study;
import com.gdschongik.gdsc.domain.study.domain.StudyAchievement;
import com.gdschongik.gdsc.domain.study.domain.StudyDetail;
import com.gdschongik.gdsc.domain.study.domain.StudyHistory;
import com.gdschongik.gdsc.domain.study.domain.service.AssignmentHistoryGrader;
import com.gdschongik.gdsc.domain.study.domain.service.StudyAssignmentHistoryValidator;
import com.gdschongik.gdsc.domain.study.domain.service.StudyHistoryValidator;
import com.gdschongik.gdsc.domain.study.dto.request.RepositoryUpdateRequest;
import com.gdschongik.gdsc.domain.study.dto.response.AssignmentHistoryResponse;
import com.gdschongik.gdsc.domain.study.dto.response.StudentMyCompleteStudyResponse;
import com.gdschongik.gdsc.global.exception.CustomException;
import com.gdschongik.gdsc.global.util.MemberUtil;
import com.gdschongik.gdsc.infra.github.client.GithubClient;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GHRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentStudyHistoryService {

    private final MemberUtil memberUtil;
    private final GithubClient githubClient;
    private final StudyDetailRepository studyDetailRepository;
    private final StudyHistoryRepository studyHistoryRepository;
    private final AssignmentHistoryRepository assignmentHistoryRepository;
    private final StudyHistoryValidator studyHistoryValidator;
    private final StudyAssignmentHistoryValidator studyAssignmentHistoryValidator;
    private final AssignmentHistoryGrader assignmentHistoryGrader;
    private final StudyRepository studyRepository;
    private final StudyAchievementRepository studyAchievementRepository;

    @Transactional
    public void updateRepository(Long studyId, RepositoryUpdateRequest request) throws IOException {
        Member currentMember = memberUtil.getCurrentMember();
        Study study = studyRepository.findById(studyId).orElseThrow(() -> new CustomException(STUDY_NOT_FOUND));
        StudyHistory studyHistory = studyHistoryRepository
                .findByStudentAndStudy(currentMember, study)
                .orElseThrow(() -> new CustomException(STUDY_HISTORY_NOT_FOUND));

        GHRepository repository = githubClient.getRepository(request.repositoryLink());
        // TODO: GHRepository 등을 wrapper로 감싸서 테스트 가능하도록 변경
        studyHistoryValidator.validateUpdateRepository(
                String.valueOf(repository.getOwner().getId()), currentMember.getOauthId());

        studyHistory.updateRepositoryLink(request.repositoryLink());
        studyHistoryRepository.save(studyHistory);

        log.info(
                "[StudyHistoryService] 레포지토리 입력: studyHistoryId={}, repositoryLink={}",
                studyHistory.getId(),
                request.repositoryLink());
    }

    @Transactional(readOnly = true)
    public List<AssignmentHistoryResponse> getAllAssignmentHistories(Long studyId) {
        Member currentMember = memberUtil.getCurrentMember();

        return assignmentHistoryRepository.findAssignmentHistoriesByStudentAndStudyId(currentMember, studyId).stream()
                .map(AssignmentHistoryResponse::from)
                .toList();
    }

    @Transactional
    public void submitAssignment(Long studyDetailId) {
        Member currentMember = memberUtil.getCurrentMember();
        StudyDetail studyDetail = studyDetailRepository
                .findById(studyDetailId)
                .orElseThrow(() -> new CustomException(STUDY_DETAIL_NOT_FOUND));
        Optional<StudyHistory> studyHistory =
                studyHistoryRepository.findByStudentAndStudy(currentMember, studyDetail.getStudy());
        LocalDateTime now = LocalDateTime.now();

        AssignmentHistory assignmentHistory = findOrCreate(currentMember, studyDetail);

        studyAssignmentHistoryValidator.validateSubmitAvailable(studyHistory.isPresent(), now, studyDetail);

        AssignmentSubmissionFetcher fetcher = githubClient.getLatestAssignmentSubmissionFetcher(
                studyHistory.get().getRepositoryLink(), Math.toIntExact(studyDetail.getWeek()));

        assignmentHistoryGrader.judge(fetcher, assignmentHistory);

        assignmentHistoryRepository.save(assignmentHistory);

        log.info(
                "[StudyHistoryService] 과제 제출: studyDetailId={}, studentId={}, submissionStatus={}, submissionFailureType={}",
                studyDetailId,
                currentMember.getId(),
                assignmentHistory.getSubmissionStatus(),
                assignmentHistory.getSubmissionFailureType());
    }

    @Transactional(readOnly = true)
    public List<StudentMyCompleteStudyResponse> getMyCompletedStudies() {
        Member currentMember = memberUtil.getCurrentMember();
        List<StudyHistory> studyHistories = studyHistoryRepository.findAllByStudent(currentMember);
        List<StudyAchievement> studyAchievements = studyAchievementRepository.findAllByStudent(currentMember);

        Map<Study, List<AchievementType>> achievementsByStudy = studyAchievements.stream()
                .collect(groupingBy(
                        StudyAchievement::getStudy, mapping(StudyAchievement::getAchievementType, toList())));

        return studyHistories.stream()
                .map(history -> {
                    List<AchievementType> achievementTypes =
                            achievementsByStudy.getOrDefault(history.getStudy(), new ArrayList<>());
                    return StudentMyCompleteStudyResponse.of(history, achievementTypes);
                })
                .toList();
    }

    private AssignmentHistory findOrCreate(Member currentMember, StudyDetail studyDetail) {
        // todo: create로 분기할 경우 내부에서 save 호출하도록 수정
        return assignmentHistoryRepository
                .findByMemberAndStudyDetail(currentMember, studyDetail)
                .orElseGet(() -> AssignmentHistory.create(studyDetail, currentMember));
    }
}
