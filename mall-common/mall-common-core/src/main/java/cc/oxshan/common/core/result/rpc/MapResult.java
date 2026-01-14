package cc.oxshan.common.core.result.rpc;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper=true)
public class MapResult<K, V> extends BaseResult {

  private Map<K, V> data;

}
