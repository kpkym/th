package com.ou.th.mercari.service;

import com.ou.th.mercari.model.MercarModel;

/**
 * @author kpkym
 * Date: 2020-03-19 06:40
 */
public interface MercarModelService {
    void save(MercarModel mercarModel);

    MercarModel getByPid(String pid);
}
