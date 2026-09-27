package org.weather.web.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.weather.service.LocationService;
import org.weather.web.dto.AddingLocationRequest;
import org.weather.web.dto.LocationResponse;
import org.weather.web.dto.SearchResultRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;

    @GetMapping("/search")
    public String getSearchResultsPage(Model model) {
        model.addAttribute("searchResultRequest", new SearchResultRequest());
        return "search-results";
    }

    @PostMapping("/search")
    public String searchLocations(HttpServletResponse response, @Valid @ModelAttribute SearchResultRequest searchResultRequest, Model model) throws URISyntaxException, IOException, InterruptedException {
        List<LocationResponse> locationResponses = locationService.findAllByName(searchResultRequest);
        setStatusIfNoLocations(response, locationResponses);

        model.addAttribute("locationResponses", locationResponses);
        model.addAttribute("previousSearch", searchResultRequest.getLocationName());
        return "search-results";
    }

    private void setStatusIfNoLocations(HttpServletResponse response, List<LocationResponse> locationResponses) {
        if (locationResponses.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
        }
    }

    @PostMapping("/addition")
    public String addLocation(HttpServletResponse response, @Valid @ModelAttribute AddingLocationRequest addingLocationRequest) {
        return "redirect:/home";
    }
}
