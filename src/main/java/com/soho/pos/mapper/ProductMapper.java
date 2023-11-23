package com.soho.pos.mapper;

import com.soho.pos.entity.Product;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.vo.ProductVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = MapperUtils.class)
public interface ProductMapper extends BaseMapper<Product, ProductVO> {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "company",expression = "java(MapperUtils.getCompany(entity.getCompanyId()))"),
    })
    ProductVO to(Product entity);
}