/*
 * Copyright (c) 2022-2024 KCloud-Platform-Alibaba Author or Authors. All Rights Reserved.
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

package org.laokou.admin.command.menu;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.dto.menu.MenuDeleteCmd;
import org.laokou.admin.domain.gateway.MenuGateway;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 删除树菜单执行器.
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MenuDeleteCmdExe {

	private final MenuGateway menuGateway;

	/**
	 * 执行删除树菜单.
	 * @param cmd 删除树菜单参数
	 * @return 执行删除结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(MenuDeleteCmd cmd) {
		return Result.of(menuGateway.deleteById(cmd.getId()));
	}

}
