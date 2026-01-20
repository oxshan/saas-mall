package cc.oxshan.shop.mapper;

import cc.oxshan.shop.entity.CodeSequence;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 编码序列 Mapper
 */
@Mapper
public interface CodeSequenceMapper {

    int insert(CodeSequence codeSequence);

    int updateSequence(@Param("prefix") String prefix, @Param("datePart") String datePart);

    CodeSequence selectByPrefixAndDate(@Param("prefix") String prefix, @Param("datePart") String datePart);
}
