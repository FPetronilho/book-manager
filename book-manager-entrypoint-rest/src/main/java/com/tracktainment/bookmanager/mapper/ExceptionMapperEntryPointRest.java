package com.tracktainment.bookmanager.mapper;

import com.tracktainment.bookmanager.exception.BusinessException;
import com.tracktainment.bookmanager.exception.ExceptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ExceptionMapperEntryPointRest {

    ExceptionDto toExceptionDto(BusinessException e);
}
