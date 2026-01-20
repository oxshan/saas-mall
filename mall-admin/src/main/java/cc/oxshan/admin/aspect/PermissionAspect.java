package cc.oxshan.admin.aspect;

import cc.oxshan.admin.annotation.RequiresPermission;
import cc.oxshan.admin.service.PermissionService;
import cc.oxshan.admin.util.HeaderUtils;
import cc.oxshan.common.core.context.ShopContext;
import cc.oxshan.common.core.exception.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 权限校验切面
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    private final PermissionService permissionService;

    @Before("@annotation(cc.oxshan.admin.annotation.RequiresPermission)")
    public void checkPermission(JoinPoint joinPoint) {
        // 1. 获取当前用户 ID
        Long userId = HeaderUtils.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }

        // 2. 获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequiresPermission annotation = method.getAnnotation(RequiresPermission.class);

        // 3. 获取需要的权限
        String permission = annotation.value();

        // 4. 校验权限
        boolean hasPermission = permissionService.hasPermission(userId, permission);

        if (!hasPermission) {
            log.warn("用户 {} 无权限访问: {}", userId, permission);
            throw new BizException(403, "无权限访问");
        }

        log.debug("用户 {} 权限校验通过: {}", userId, permission);
    }
}
