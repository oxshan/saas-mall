package cc.oxshan.common.core.result.rpc;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PlainResult<T> extends BaseResult {
  /**
   * 调用返回的数据
   */
  private T data;

}