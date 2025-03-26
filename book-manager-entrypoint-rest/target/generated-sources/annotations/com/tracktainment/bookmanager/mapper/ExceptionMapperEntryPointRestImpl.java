package com.tracktainment.bookmanager.mapper;

import com.tracktainment.bookmanager.exception.BusinessException;
import com.tracktainment.bookmanager.exception.ExceptionDto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-26T20:02:45+0100",
    comments = "version: 1.6.2, compiler: javac, environment: Java 23 (Oracle Corporation)"
)
@Component
public class ExceptionMapperEntryPointRestImpl implements ExceptionMapperEntryPointRest {

    @Override
    public ExceptionDto toExceptionDto(BusinessException e) {
        if ( e == null ) {
            return null;
        }

        ExceptionDto.ExceptionDtoBuilder exceptionDto = ExceptionDto.builder();

        exceptionDto.code( e.getCode() );
        exceptionDto.httpStatusCode( e.getHttpStatusCode() );
        exceptionDto.reason( e.getReason() );
        exceptionDto.message( e.getMessage() );

        return exceptionDto.build();
    }
}
