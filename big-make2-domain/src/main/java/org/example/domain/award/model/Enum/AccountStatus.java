package org.example.domain.award.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账户枚举
 */
@Getter
@AllArgsConstructor
public enum AccountStatus {

    open("open","开启"),
    close("close","冻结");
    private final String code;
    private final String desc;
}
