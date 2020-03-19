package com.ou.th.mercari.service;

import com.ou.th.mercari.model.MercarModel;

import java.util.List;

/**
 * @author kpkym
 * Date: 2020-03-19 06:40
 */
public interface MercarService {
    void save(MercarModel mercarModel);

    MercarModel getByPid(String pid);

    List<MercarModel> list();
}
