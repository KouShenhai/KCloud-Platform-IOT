/*
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
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
 *
 */

package org.laokou.admin.command.query;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.client.dto.MenuListQry;
import org.laokou.admin.client.dto.clientobject.MenuCO;
import org.laokou.admin.gatewayimpl.database.MenuMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.MenuDO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.security.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.admin.common.Constant.DEFAULT_TENANT;

/**
 * @author laokou
 */
@Component
@RequiredArgsConstructor
public class MenuListQryExe {

	private final MenuMapper menuMapper;

	public Result<List<MenuCO>> execute(MenuListQry qry) {
		Long tenantId = UserUtil.getTenantId();
		if (tenantId == DEFAULT_TENANT) {
			List<MenuDO> list = menuMapper.getMenuListLikeName(null, qry.getName());
			return Result.of(ConvertUtil.sourceToTarget(list, MenuCO.class));
		}
		List<MenuDO> list = menuMapper.getMenuListByTenantIdAndLikeName(null, tenantId, qry.getName());
		return Result.of(ConvertUtil.sourceToTarget(list, MenuCO.class));
	}

}
