package com.pocketstone.team_sync.controller.eval;

import com.pocketstone.team_sync.dto.eval.SprintAchievementDto;
import com.pocketstone.team_sync.dto.eval.TotalManMonthDto;
import com.pocketstone.team_sync.entity.User;
import com.pocketstone.team_sync.service.ProjectEvalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/evaluations/sprints")
@RequiredArgsConstructor
public class EvalSprintController {

    private final ProjectEvalService projectEvalService;

    @PostMapping("/{memberId}")
    public ResponseEntity<SprintAchievementDto> createSprintAchievement(
            @AuthenticationPrincipal User user,
            @PathVariable("projectId") Long projectId,
            @PathVariable("memberId") Long memberId,
            @RequestBody SprintAchievementDto dto) {
        return ResponseEntity.ok(projectEvalService.saveSprintAchievement(user, projectId, memberId, dto));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity <List<SprintAchievementDto>> getSprintAchievement(
            @AuthenticationPrincipal User user,
            @PathVariable("projectId") Long projectId,
            @PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(projectEvalService.getSprintAchievement(user, projectId, memberId));
    }

    @GetMapping
    public ResponseEntity <TotalManMonthDto> getTotalManMonthForSprint(
            @AuthenticationPrincipal User user,
            @PathVariable("projectId") Long projectId) {
        return ResponseEntity.ok(projectEvalService.getTotalManMonthForSprint(user, projectId));
    }


    @PutMapping("/{memberId}")
    public ResponseEntity<SprintAchievementDto> updateSprintAchievement(
            @AuthenticationPrincipal User user,
            @PathVariable("projectId") Long projectId,
            @PathVariable("memberId") Long memberId,
            @RequestBody SprintAchievementDto dto) {
        return ResponseEntity.ok(projectEvalService.updateSprintAchievement(user, projectId,memberId, dto));
    }

    @DeleteMapping("{memberId}")
    public ResponseEntity<Void> deleteSprintAchievement(
            @AuthenticationPrincipal User user,
            @PathVariable("projectId") Long projectId,
            @PathVariable("memberId") Long memberId) {
        projectEvalService.deleteSprintAchievement(user, projectId, memberId);
        return ResponseEntity.noContent().build();
    }
}