package com.soho.pos.mapper;

import com.soho.pos.entity.PurchaseHistory;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.vo.PurchaseVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = MapperUtils.class)
public interface PurchaseHistoryMapper extends BaseMapper<PurchaseHistory, PurchaseVO> {
    PurchaseHistoryMapper INSTANCE = Mappers.getMapper(PurchaseHistoryMapper.class);

    @Mappings({
            @Mapping(target = "sizeChart",expression = "java(MapperUtils.getSizeChart(entity.getSizeId()))"),
            @Mapping(target = "company",expression = "java(MapperUtils.getCompany(entity.getCompanyId()))"),
            @Mapping(target = "product",expression = "java(MapperUtils.getProduct(entity.getProductId()))"),
    })
    PurchaseVO to(PurchaseHistory entity);
}