package cc.oxshan.shop.inner.impl;

import cc.oxshan.shop.entity.CodeSequence;
import cc.oxshan.shop.inner.CodeGeneratorService;
import cc.oxshan.shop.mapper.CodeSequenceMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 编码生成服务实现
 */
@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService {

    @Resource
    private CodeSequenceMapper codeSequenceMapper;

    private static final String TENANT_PREFIX = "TENANT";
    private static final String SHOP_PREFIX = "SHOP";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    @Transactional
    public String generateTenantCode() {
        return generateCode(TENANT_PREFIX);
    }

    @Override
    @Transactional
    public String generateShopCode() {
        return generateCode(SHOP_PREFIX);
    }

    private String generateCode(String prefix) {
        String datePart = LocalDate.now().format(DATE_FORMATTER);
        
        CodeSequence codeSequence = codeSequenceMapper.selectByPrefixAndDate(prefix, datePart);
        
        if (codeSequence == null) {
            codeSequence = new CodeSequence();
            codeSequence.setPrefix(prefix);
            codeSequence.setDatePart(datePart);
            codeSequence.setSequence(1);
            codeSequenceMapper.insert(codeSequence);
        } else {
            codeSequenceMapper.updateSequence(prefix, datePart);
            codeSequence = codeSequenceMapper.selectByPrefixAndDate(prefix, datePart);
        }
        
        return String.format("%s%s%03d", prefix, datePart, codeSequence.getSequence());
    }
}
