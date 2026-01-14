package cc.oxshan.admin.dto;

import lombok.Data;
import java.util.List;

/**
 * 分配菜单请求 DTO
 */
@Data
public class AssignMenusDTO {
    
    private Long roleId;
    private List<Long> menuIds;
}
