package com.ou.th.mercari.service.impl;

import com.ou.th.mercari.dao.DataRepo;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
@Service
public class MercarModelServiceImpl implements MercarModelService {
    @Autowired
    DataRepo dataRepo;

    @Override
    public MercarModel getByPid(String pid) {
        return dataRepo.findById(pid).orElse(new MercarModel());
    }

    @Override
    public void save(MercarModel mercarModel) {
        dataRepo.save(mercarModel);
    }
}
