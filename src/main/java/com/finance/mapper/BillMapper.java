package com.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.finance.entity.Bill;
import com.finance.vo.BillVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BillMapper extends BaseMapper<Bill> {

    /**
     * 传统的传统多表联查：通过 XML 实现
     *
     * @param userId 用户ID
     * @return 带有账户名和分类名的明细列表
     */
    List<BillVo> selectUserBillDetails(@Param("userId") Long userId);
}