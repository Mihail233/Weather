package org.weather.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.weather.web.dto.LocationResponse;
import org.weather.web.dto.api.LocationApiResponse;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LocationMapper {

    List<LocationResponse> toResponseList(LocationApiResponse[] locationApiResponses);
}
