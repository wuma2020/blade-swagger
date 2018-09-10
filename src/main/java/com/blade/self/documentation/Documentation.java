package com.blade.self.documentation;

import com.blade.self.apibean.ApiBean;
import com.blade.self.apibean.ApiMethodBean;
import com.blade.self.apibean.ApiPathBean;
import lombok.Data;

import java.util.List;

/**
 * 封装 所有的 controller 的 api 信息
 */
@Data
public class Documentation {
    String controllerName;
    ApiBean apiBean;
    ApiPathBean apiPathBean;
}
