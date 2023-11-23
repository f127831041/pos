package com.soho.pos.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;

import java.util.List;

public interface BaseMapper<SOURCE, TARGET> {

    @InheritConfiguration

    TARGET to(SOURCE var1);


    @InheritConfiguration

    List<TARGET> to(List<SOURCE> var1);


    @InheritInverseConfiguration

    SOURCE from(TARGET var1);


    @InheritInverseConfiguration

    List<SOURCE> from(List<TARGET> var1);
}
