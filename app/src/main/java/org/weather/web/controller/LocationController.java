package org.weather.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.weather.common.service.LocationService;
import org.weather.web.dto.LocationAdditionRequest;
import org.weather.web.dto.LocationRemovalRequest;
import org.weather.web.dto.LocationResponse;
import org.weather.web.dto.LocationSearchRequest;
import org.weather.web.interceptor.PrivateEndpointInterceptor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/location")
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/search")
    public String getLocationSearchPage(Model model) {
        model.addAttribute("locationSearchRequest", new LocationSearchRequest());
        return "search-results";
    }

    @PostMapping("/search")
    public String searchLocationsByName(HttpServletResponse response, @Valid @ModelAttribute LocationSearchRequest locationSearchRequest, Model model) throws URISyntaxException, IOException, InterruptedException {
        List<LocationResponse> locationResponses = locationService.showLocations(locationSearchRequest);
        setResponseStatusIfNoLocations(response, locationResponses);

        model.addAttribute("locationResponses", locationResponses);
        model.addAttribute("previousSearch", locationSearchRequest.getLocationName());
        return "search-results";
    }

    private void setResponseStatusIfNoLocations(HttpServletResponse response, List<LocationResponse> locationResponses) {
        if (locationResponses.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @PostMapping("/add")
    public String addNewLocation(HttpServletResponse response, @Valid @ModelAttribute LocationAdditionRequest locationAdditionRequest, @RequestAttribute(name = PrivateEndpointInterceptor.USER_ID_ATTRIBUTE) Long userId) {
        locationService.addNewLocation(locationAdditionRequest, userId);
        return "redirect:/weather";
    }

    @PostMapping("/delete")
    public String deleteLocation(HttpServletResponse response, @RequestAttribute(name = PrivateEndpointInterceptor.USER_ID_ATTRIBUTE) Long userId, @Valid LocationRemovalRequest locationRemovalRequest) {
        locationService.deleteLocation(locationRemovalRequest, userId);
        return "redirect:/weather";
    }
}
