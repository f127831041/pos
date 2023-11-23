package com.soho.pos.mapper;

import com.soho.pos.entity.Refund;
import com.soho.pos.entity.Revenue;
import com.soho.pos.utils.MapperUtils;
import com.soho.pos.vo.RefundVO;
import com.soho.pos.vo.RevenueVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(imports = MapperUtils.class)
public interface RefundMapper extends BaseMapper<Refund, RefundVO> {
    RefundMapper INSTANCE = Mappers.getMapper(RefundMapper.class);

    @Mappings({
            @Mapping(target = "member",expression = "java(MapperUtils.getMember(entity.getMemberId()))"),
            @Mapping(target = "createDate", expression = "java(MapperUtils.getLocalDate(entity.getCreateDateTime()))"),
    })
    RefundVO to(Refund entity);
}