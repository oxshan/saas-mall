package cc.oxshan.common.core.result.rpc;

import cc.oxshan.common.core.enums.ErrorCodeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * rpc场景返回包装类
 */
@Data
public class BaseResult implements Serializable {
  private static final long serialVersionUID = 1949910043360896391L;
  /**
   * 标识本次调用是否返回
   */
  private Boolean success;
  /**
   * 本次调用返回code，一般为错误代码
   */
  private Integer code;
  /**
   * 本次调用返回的消息，一般为错误消息
   */
  private String msg;
  /**
   * 请求Id
   */
  private String requestId;
  /**
   * 调用失败返回的数据，可扩展性很强
   * 比如，批量操作场景，可以放哪些操作失败了的序列化数据
   */
  private Map<String, Object> errorData;


  /**
   * server端应用需要拦截全局异常，封装成BaseResult形式返回，方便client端统一处理
   */
  public BaseResult() {
    this.code = ErrorCodeEnum.SUCCESS.getCode();
    this.success = true;
    this.msg = ErrorCodeEnum.SUCCESS.getMsg();
  }

}