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

package org.laokou.admin.gatewayimpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.convertor.SourceConvertor;
import org.laokou.admin.domain.annotation.DataFilter;
import org.laokou.admin.domain.gateway.SourceGateway;
import org.laokou.admin.domain.source.Source;
import org.laokou.admin.gatewayimpl.database.SourceMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.SourceDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.LogUtil;
import org.laokou.common.mybatisplus.utils.TransactionalUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.BOOT_SYS_SOURCE;

/**
 * 数据源管理.
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SourceGatewayImpl implements SourceGateway {

	private final SourceMapper sourceMapper;

	private final TransactionalUtil transactionalUtil;

	private final SourceConvertor sourceConvertor;

	/**
	 * 查询数据源列表
	 * @param source 数据源对象
	 * @param pageQuery 分页参数
	 * @return 数据源列表
	 */
	@Override
	@DataFilter(tableAlias = BOOT_SYS_SOURCE)
	public Datas<Source> list(Source source, PageQuery pageQuery) {
		IPage<SourceDO> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
		IPage<SourceDO> newPage = sourceMapper.getSourceListFilter(page, source.getName(), pageQuery);
		Datas<Source> datas = new Datas<>();
		datas.setTotal(newPage.getTotal());
		datas.setRecords(sourceConvertor.convertEntityList(newPage.getRecords()));
		return datas;
	}

	/**
	 * 根据ID查看数据源
	 * @param id ID
	 * @return 数据源
	 */
	@Override
	public Source getById(Long id) {
		return sourceConvertor.convertEntity(sourceMapper.selectById(id));
	}

	/**
	 * 新增数据源
	 * @param source 数据源对象
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(Source source) {
		SourceDO sourceDO = sourceConvertor.toDataObject(source);
		return insertSource(sourceDO);
	}

	/**
	 * 修改数据源
	 * @param source 数据源对象
	 * @return 修改结果
	 */
	@Override
	public Boolean update(Source source) {
		SourceDO sourceDO = sourceConvertor.toDataObject(source);
		sourceDO.setVersion(sourceMapper.getVersion(sourceDO.getId(), SourceDO.class));
		return updateSource(sourceDO);
	}

	/**
	 * 根据ID删除数据源
	 * @param id ID
	 * @return 删除结果
	 */
	@Override
	public Boolean deleteById(Long id) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return sourceMapper.deleteById(id) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 修改数据源
	 * @param sourceDO 数据源数据模型
	 * @return 修改结果
	 */
	private Boolean updateSource(SourceDO sourceDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return sourceMapper.updateById(sourceDO) > 0;
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

	/**
	 * 新增数据源
	 * @param sourceDO 数据源数据模型
	 * @return 新增结果
	 */
	private Boolean insertSource(SourceDO sourceDO) {
		return transactionalUtil.defaultExecute(r -> {
			try {
				return sourceMapper.insertTable(sourceDO);
			}
			catch (Exception e) {
				log.error("错误信息：{}，详情见日志", LogUtil.result(e.getMessage()), e);
				r.setRollbackOnly();
				throw new SystemException(e.getMessage());
			}
		});
	}

}
