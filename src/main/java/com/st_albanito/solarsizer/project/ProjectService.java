package com.st_albanito.solarsizer.project;
import com.st_albanito.solarsizer.auth.User;
import com.st_albanito.solarsizer.catalog.*;
import com.st_albanito.solarsizer.irradiance.Location;
import com.st_albanito.solarsizer.irradiance.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectDeviceRepository projectDeviceRepository;
    private final LocationRepository locationRepository;
    private final DeviceRepository deviceRepository;
    private final PanelRepository panelRepository;
    private final BatteryRepository batteryRepository;
    private final GridOutageProfileRepository outageProfileRepository;

    @Transactional(readOnly = true)
    public List<ProjectResponse> getUserProjects(User currentUser){
        return projectRepository.findByUserIdOrderByUpdatedAtDesc(currentUser.getId())
                .stream()
                .map(ProjectResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(User currentUser, UUID projectId){
        Project project = getOwnedProject(currentUser,projectId);
        return ProjectResponse.from(project);
    }

    @Transactional
    public ProjectResponse createProject(User currentUser, ProjectRequest request){
        Project project = new Project();
        project.setUser(currentUser);
        applyProjectFields(project,request);

        Project saved = projectRepository.save(project);
        return ProjectResponse.from(saved);
    }

    @Transactional
    public ProjectResponse updateProject(User currentUser,UUID projectId,ProjectRequest request){
        Project project = getOwnedProject(currentUser, projectId);
        applyProjectFields(project,request);

        Project saved = projectRepository.save(project);
        return ProjectResponse.from(saved);
    }

    @Transactional
    public void deleteProject(User currentUser, UUID projectId){
        Project project = getOwnedProject(currentUser,projectId);
        projectRepository.delete(project);
    }
    @Transactional
    public ProjectResponse addDeviceToProject(User currentUser,UUID projectId,ProjectDeviceRequest request){
        Project project = getOwnedProject(currentUser,projectId);
        Device device = deviceRepository.findById(request.deviceId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Device not found"));
        if (Boolean.TRUE.equals(device.isCustom()) &&
                (device.getCreatedBy() == null || !device.getCreatedBy().getId().equals(currentUser.getId()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this custom device");
        }
        boolean alwaysOn = Boolean.TRUE.equals(request.alwaysOn());
        BigDecimal hours = alwaysOn ? new BigDecimal("24.0"):request.hoursPerDay();

        ProjectDevice pd = ProjectDevice.builder()
                .project(project)
                .device(device)
                .quantity(request.quantity())
                .hoursPerDay(hours)
                .startHour(alwaysOn ? 0 : request.startHour())
                .alwaysOn(alwaysOn)
                .build();

        project.getDevices().add(pd);
        Project saved = projectRepository.save(project);
        return ProjectResponse.from(saved);
    }

    private Project getOwnedProject(User currentUser, UUID projectId){
        return projectRepository.findByIdAndUserId(projectId,currentUser.getId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Project not Found"));
    }

    private void applyProjectFields(Project project,ProjectRequest request){}

}
