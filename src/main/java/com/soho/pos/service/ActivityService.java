package com.soho.pos.service;

import com.soho.pos.dao.ActivityDAO;
import com.soho.pos.dto.ActivityDTO;
import com.soho.pos.dto.InventoryDTO;
import com.soho.pos.entity.Activity;
import com.soho.pos.entity.Inventory;
import com.soho.pos.enums.ErrorText;
import com.soho.pos.ex.PortalException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActivityService extends BaseService<Activity, Long, ActivityDAO> {
    
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void upd(ActivityDTO rq) throws PortalException {
        Activity activity = new Activity();
        if(rq.getId() != null){
            activity = dao.findById(rq.getId()).get();
        }
        activity.setBonusMoney(rq.getBonusMoney());
        activity.setBonusType(rq.getBonusType());
        activity.setBonusConvert(rq.getBonusConvert());
        activity.setDiscountMoney(rq.getDiscountMoney());
        activity.setDiscountType(rq.getDiscountType());
        activity.setDiscountConvert(rq.getDiscountConvert());
        dao.save(activity);
    }
}
