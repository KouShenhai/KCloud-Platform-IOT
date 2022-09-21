/**
 * Copyright (c) 2022 KCloud-Platform Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.laokou.admin.interfaces.vo;
import lombok.Data;
import java.util.Date;
import java.util.List;
@Data
public class SysUserVO {
    private Long id;
    private Date createDate;
    private String username;
    private String imgUrl;
    private Long deptId;
    private Integer superAdmin;
    private Integer status;
    private List<Long> roleIds;
}
