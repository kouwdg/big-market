package org.example.types.model;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 程宇乐
 * @version 1.0
 * @description: 统一返回响应数据的格式
 * @date 2024/12/15 21:11
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {
    private String code;
    private String info;
    private T data;
}
