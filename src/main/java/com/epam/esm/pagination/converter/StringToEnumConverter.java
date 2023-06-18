package com.epam.esm.pagination.converter;

import com.epam.esm.pagination.Sort;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToEnumConverter implements Converter<String, Sort> {
    @Override
    public Sort convert(String source) {
        return Sort.valueOf(source.toUpperCase());
    }
}
