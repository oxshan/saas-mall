package cc.oxshan.user.service;

import cc.oxshan.user.entity.SysRole;
import cc.oxshan.user.mapper.SysRoleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色模板服务
 * 用于新店铺创建时复制角色模板
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RoleTemplateService {

    private final SysRoleMapper roleMapper;

    /**
     * 为新店铺复制角色模板
     * @param shopId 新店铺 ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void copyTemplateToShop(Long shopId) {
        log.info("开始为店铺 {} 复制角色模板", shopId);
        
        // 1. 查询系统预置的门店角色模板（shop_id=0, type=2）
        List<SysRole> templates = roleMapper.selectPage(0L, 0, 100);
        
        // 过滤出门店类型的角色模板
        List<SysRole> shopTemplates = templates.stream()
            .filter(role -> role.getType() == 2)
            .toList();
        
        log.info("找到 {} 个门店角色模板", shopTemplates.size());
        
        // 2. 复制角色记录
        for (SysRole template : shopTemplates) {
            // 创建新角色
            SysRole newRole = new SysRole();
            newRole.setShopId(shopId);
            newRole.setTemplateId(template.getId());
            newRole.setCode(template.getCode());
            newRole.setName(template.getName());
            newRole.setType(template.getType());
            newRole.setIsSystem(0); // 复制的角色不是系统内置
            newRole.setStatus(template.getStatus());
            newRole.setSort(template.getSort());
            newRole.setRemark(template.getRemark());
            
            // 插入角色
            roleMapper.insert(newRole);
            
            log.info("复制角色: {} -> {}", template.getName(), newRole.getId());
            
            // 3. 复制角色关联的菜单权限
            List<Long> menuIds = roleMapper.selectMenuIdsByRoleId(template.getId());
            if (!menuIds.isEmpty()) {
                roleMapper.insertRoleMenus(newRole.getId(), menuIds);
                log.info("复制角色 {} 的 {} 个菜单权限", newRole.getName(), menuIds.size());
            }
        }
        
        log.info("店铺 {} 角色模板复制完成", shopId);
    }
}
