package com.purplerosechen.qpm.service;

import com.purplerosechen.qpm.dto.AllCallBackDto;

/**
 * @description: TODO 回调消息处理
 * @author: chen
 * @date: 2025/04/16
 **/

public interface GroupAtMessageTypeService {
    Object execute(String groupOpenId,String userOpenId, Object obj) throws Exception;
}
