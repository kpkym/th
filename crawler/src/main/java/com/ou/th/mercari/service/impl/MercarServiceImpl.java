package com.ou.th.mercari.service.impl;

import com.ou.th.mercari.dao.DataRepo;
import com.ou.th.mercari.model.MercarModel;
import com.ou.th.mercari.service.MercarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author kpkym
 * Date: 2020-03-19 06:41
 */
@Service
public class MercarServiceImpl implements MercarService {
    @Autowired
    DataRepo dataRepo;

    @Override
    public MercarModel getByPid(String pid) {
        return dataRepo.findById(pid).orElse(new MercarModel());
    }

    @Override
    public List<MercarModel> list() {
        return StreamSupport
                .stream(dataRepo.findAll().spliterator(), false)
                .filter(e -> !e.isDisliked())
                .collect(Collectors.toList());
    }

    @Override
    public MercarModel save(MercarModel mercarModel) {
        return dataRepo.save(mercarModel);
    }
}
