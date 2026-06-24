package com.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finance.entity.Bill;
import com.finance.vo.BillVo;
import java.util.List;

public interface BillService extends IService<Bill> {
    void executeBookkeeping(Bill bill, Long userId);

    // 升级为返回带有联查信息的明细
    List<BillVo> getUserBills(Long userId);
}