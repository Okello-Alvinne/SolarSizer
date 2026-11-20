package com.st_albanito.solarsizer.catalog;

import com.st_albanito.solarsizer.auth.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService {
    private final DeviceCategoryRepository deviceCategoryRepository;
    private final DeviceRepository deviceRepository;
    private final PanelRepository panelRepository;
    private final BatteryRepository batteryRepository;
    private final GridOutageProfileRepository gridOutageProfileRepository;
    private final InverterRepository inverterRepository;

    public List<DeviceCategory> getAllCategories() {
        return deviceCategoryRepository.findAll();
    }
    public List<DeviceResponse> getDevices(User currentUser, Short categoryId, String query) {
        String cleanQuery = (query == null) ? "" : query.trim();
        List<Device> devices = (categoryId == null)
                ? deviceRepository.searchAllAvailableDevices(currentUser.getId(), cleanQuery)
                : deviceRepository.searchAvailableDevicesByCategory(currentUser.getId(), categoryId, cleanQuery);

        return devices.stream()
                .map(DeviceResponse::from)
                .toList();
    }
    public DeviceResponse createCustomDevice(User currentUser, CreateCustomDeviceRequest createCustomDeviceRequest){
        DeviceCategory category = deviceCategoryRepository.findById(createCustomDeviceRequest.categoryId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Device Category not found"));

        Device device = Device.builder()
                .category(category)
                .name(createCustomDeviceRequest.name().trim())
                .typicalWatts(createCustomDeviceRequest.typicalWatts())
                .surgeMultiplier(createCustomDeviceRequest.surgeMultiplier() != null ? createCustomDeviceRequest.surgeMultiplier() : BigDecimal.valueOf(1.0))
                .dutyCycle(createCustomDeviceRequest.dutyCycle() != null ? createCustomDeviceRequest.dutyCycle() : BigDecimal.valueOf(1.00))
                .defaultHoursPerDay(createCustomDeviceRequest.defaultHoursPerDay())
                .sourceNote(createCustomDeviceRequest.sourceNote())
                .isCustom(true)
                .createdBy(currentUser)
                .build();

        return DeviceResponse.from(deviceRepository.save(device));
    }
    public List<Panel> getAllPanels(){
        return panelRepository.findAll();
    }
    public List<Battery>getAllBatteries(){
        return batteryRepository.findAll();
    }

    public List<Inverter> getInverters(String type) {
        if (type != null && !type.isBlank()) {
            return inverterRepository.findByType(type.trim().toUpperCase());
        }
        return inverterRepository.findAll();
    }
    public List<GridOutageProfile>getPresetGridOutageProfiles(){
        return gridOutageProfileRepository.findByIsPresetTrue();
    }
}
