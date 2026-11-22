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
        if (device.isCustom() && //Boolean.TRUE.equals(device.isCustom())
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
    @Transactional
    public ProjectResponse removeDeviceFromProject(User currentUser,UUID projectId,Long projectDeviceId){
        Project project = getOwnedProject(currentUser,projectId);
        ProjectDevice pd = projectDeviceRepository.findByIdAndProjectId(projectDeviceId,projectId)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Project Device not found"));
        project.getDevices().remove(pd);
        Project saved = projectRepository.save(project);
        return ProjectResponse.from(saved);
    }
    private Project getOwnedProject(User currentUser, UUID projectId){
        return projectRepository.findByIdAndUserId(projectId,currentUser.getId())
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Project not Found"));
    }

    private void applyProjectFields(Project project,ProjectRequest request){
        String mode = request.systemMode().trim().toUpperCase();
        String loadMethod = request.loadInputMethod().toUpperCase();

        if("MANUAL".equals(loadMethod)&& request.manualDailyKwh()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Manual Daily KWH is required when load Input Method is manual");
        }
        Location location = locationRepository.findById(request.locationId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Location Not Found"));
        GridOutageProfile outageProfile = null;
        if(request.outageProfileId()!=null){
            outageProfile = outageProfileRepository.findById(request.outageProfileId())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Outage Profile Not Found"));
        }

        Panel panel = null;
        if(request.panelId()!=null){
            panel = panelRepository.findById(request.panelId())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Panel Not Found"));

        }

        Battery battery = null;
        if(request.batteryId()!=null){
            battery = batteryRepository.findById(request.batteryId())
                    .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Battery Not Found"));

        }

        project.setName(request.name().trim());
        project.setSystemMode(mode);
        project.setLocation(location);
        project.setLoadInputMethod(loadMethod);
        project.setManualDailyKwh("MANUAL".equals(loadMethod) ? request.manualDailyKwh() : null);
        project.setAutonomyDays(request.autonomyDays());
        project.setBackupHoursTarget(request.backupHoursTarget());
        project.setOutageProfile(outageProfile);
        project.setTariffPerKwh(request.tariffPerKwh());
        project.setFeedInPerKwh(request.feedInPerKwh());
        project.setPanel(panel);
        project.setBattery(battery);
        project.setTiltDeg(request.tiltDeg());
        project.setAzimuthDeg(request.azimuthDeg());
        project.setSystemLossesPct(request.systemLossesPct() != null ? request.systemLossesPct() : new BigDecimal("14.0"));
    }

}
