package com.purplerosechen.qpm.service;

import com.purplerosechen.qpm.dto.AllCallBackDto;

/**
 * @description: TODO
 * @author: chen
 * @date: 2025/04/15
 **/

public interface CallBackService {
    Object execute(AllCallBackDto request) throws Exception;
}
