package com.purplerosechen.qpm.pojo.gd;

import lombok.Getter;

/**
 * @description: TODO
 * @author: chen
 * @date: 2025/04/16
 **/
@Getter
public enum ResStatus {
    OK(1),
    ERROR(0),
    INFO_SUCCESS(10000)
    ;

    private final int status;

    ResStatus(int status) {
        this.status = status;
    }
}