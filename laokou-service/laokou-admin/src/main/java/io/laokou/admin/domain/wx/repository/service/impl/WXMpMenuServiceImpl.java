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
package io.laokou.admin.domain.wx.repository.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.laokou.admin.domain.wx.entity.WXMpMenuDO;
import io.laokou.admin.domain.wx.repository.mapper.WXMpMenuMapper;
import io.laokou.admin.domain.wx.repository.service.WXMpMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * 公众号自定义菜单
 * @author Kou Shenhai
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WXMpMenuServiceImpl extends ServiceImpl<WXMpMenuMapper, WXMpMenuDO> implements WXMpMenuService {

}