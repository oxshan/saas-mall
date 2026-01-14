package cc.oxshan.admin.dto;

import lombok.Data;
import java.util.List;

/**
 * 分配角色请求 DTO
 */
@Data
public class AssignRolesDTO {
    
    private Long userId;
    private List<Long> roleIds;
}
