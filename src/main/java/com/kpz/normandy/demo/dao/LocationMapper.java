package com.kpz.normandy.demo.dao;

import com.kpz.normandy.demo.model.Location;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LocationMapper {
    @Select("select * from locations where id = #{id}")
    @ResultMap("locationMap")
    Location getById(@Param("id") Long id);
}
