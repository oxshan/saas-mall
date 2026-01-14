package cc.oxshan.common.core.result.rpc;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
public class ListResult<T> extends BaseResult {

  private List<T> data;

}