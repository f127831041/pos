package com.soho.pos.mapper;

import com.soho.pos.entity.RefundRecord;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.vo.RefundRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = MapperUtils.class)
public interface RefundRecordMapper extends BaseMapper<RefundRecord, RefundRecordVO> {
    RefundRecordMapper INSTANCE = Mappers.getMapper(RefundRecordMapper.class);

    @Mappings({
            @Mapping(target = "sizeChart",expression = "java(MapperUtils.getSizeChart(entity.getSizeId()))"),
            @Mapping(target = "company",expression = "java(MapperUtils.getCompany(entity.getCompanyId()))"),
            @Mapping(target = "product",expression = "java(MapperUtils.getProduct(entity.getProductId()))"),
    })
    RefundRecordVO to(RefundRecord entity);
}