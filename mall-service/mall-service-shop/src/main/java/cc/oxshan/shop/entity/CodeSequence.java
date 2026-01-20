package cc.oxshan.shop.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 编码序列实体
 */
@Data
public class CodeSequence {

    private Long id;
    private String prefix;
    private String datePart;
    private Integer sequence;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
