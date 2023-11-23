package com.soho.pos.mapper;

import com.soho.pos.entity.ProductRank;
import com.soho.pos.entity.Purchase;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.vo.ProductRankVO;
import com.soho.pos.vo.PurchaseVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = MapperUtils.class)
public interface ProductRankMapper extends BaseMapper<ProductRank, ProductRankVO> {
    ProductRankMapper INSTANCE = Mappers.getMapper(ProductRankMapper.class);

    @Mappings({
            @Mapping(target = "company",expression = "java(MapperUtils.getCompany(entity.getCompanyId()))"),
            @Mapping(target = "product",expression = "java(MapperUtils.getProduct(entity.getProductId()))"),
    })
    ProductRankVO to(ProductRank entity);
}