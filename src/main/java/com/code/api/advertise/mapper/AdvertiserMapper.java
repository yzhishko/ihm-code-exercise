package com.code.api.advertise.mapper;

import com.code.api.advertise.model.Advertiser;
import com.code.api.advertise.model.TransactionValidity;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface AdvertiserMapper {

    @Select("select * from advertiser")
    List<Advertiser> findAll();

    @Insert("insert into advertiser(name, contactName, creditLimit) values(#{name}, #{contactName}, #{creditLimit})")
    @SelectKey(statement="call identity()", keyProperty="id",
            before=false, resultType=Long.class)
    void addAdvertiser(Advertiser advertiser);

    @Select("select * from advertiser where id = #{id}")
    Advertiser findAdvertiserByID(Long id);

    @Select("select * from advertiser where name = #{name}")
    Advertiser findAdvertiserByName(String name);

    @Update("update advertiser set name = #{name}, contactName = #{contactName}, creditLimit = #{creditLimit} where id = #{id}")
    void updateAdvertiser(Advertiser advertiser);

    @Select("select #{order} as amount, case when creditLimit >= #{order} then TRUE else FALSE end as valid from advertiser where id = #{id}")
    TransactionValidity hasEnoughCreditById(@Param("id") Long id, @Param("order") BigDecimal order);

    @Delete("delete from advertiser where id = #{id}")
    void deleteAdvertiserbyId(Long id);

}