package com.soho.pos.mapper;

import com.soho.pos.entity.Inventory;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.vo.InventoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = MapperUtils.class)
public interface InventoryMapper extends BaseMapper<Inventory, InventoryVO> {
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);
    
    @Mappings({
            @Mapping(target = "sizeChart",expression = "java(MapperUtils.getSizeChart(entity.getSizeId()))"),
            @Mapping(target = "company",expression = "java(MapperUtils.getCompany(entity.getCompanyId()))"),
            @Mapping(target = "product",expression = "java(MapperUtils.getProduct(entity.getProductId()))"),
    })
    InventoryVO to(Inventory entity);
}