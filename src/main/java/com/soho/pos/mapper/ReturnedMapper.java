package com.soho.pos.mapper;

import com.soho.pos.entity.Returned;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.vo.ReturnedVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = MapperUtils.class)
public interface ReturnedMapper extends BaseMapper<Returned, ReturnedVO> {
    ReturnedMapper INSTANCE = Mappers.getMapper(ReturnedMapper.class);

    @Mappings({
            @Mapping(target = "sizeChart",expression = "java(MapperUtils.getSizeChart(entity.getSizeId()))"),
            @Mapping(target = "company",expression = "java(MapperUtils.getCompany(entity.getCompanyId()))"),
            @Mapping(target = "product",expression = "java(MapperUtils.getProduct(entity.getProductId()))"),
    })
    ReturnedVO to(Returned entity);
}