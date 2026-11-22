package com.st_albanito.solarsizer.project;

import com.st_albanito.solarsizer.auth.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getMyProjects(
            @AuthenticationPrincipal User currentUSer
    ){
        return ResponseEntity.ok(projectService.getUserProjects(currentUSer));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @AuthenticationPrincipal User currentUSer,
            @PathVariable(name = "id") UUID id
    ){
        return ResponseEntity.ok(projectService.getProjectById(currentUSer, id));
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal User currentUSer,
            @Valid @RequestBody ProjectRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(currentUSer, request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @AuthenticationPrincipal User currentUSer,
            @Valid @RequestBody ProjectRequest request,
            @PathVariable(name = "id") UUID id
    ){
        return ResponseEntity.ok(projectService.updateProject(currentUSer,id,request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @AuthenticationPrincipal User currentUSer,
            @PathVariable(name = "id") UUID id
    ){
        projectService.deleteProject(currentUSer, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/devices")
    public ResponseEntity<ProjectResponse> addDevice(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "id") UUID id,
            @Valid @RequestBody ProjectDeviceRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.addDeviceToProject(currentUser, id, request));
    }

    @DeleteMapping("/{id}/devices/{projectDeviceId}")
    public ResponseEntity<ProjectResponse> removeDevice(
            @AuthenticationPrincipal User currentUser,
            @PathVariable(name = "id") UUID id,
            @PathVariable(name = "projectDeviceId") Long projectDeviceId
    ) {
        return ResponseEntity.ok(projectService.removeDeviceFromProject(currentUser, id, projectDeviceId));
    }
}
