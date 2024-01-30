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

package org.laokou.common.domain.publish;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.domain.convertor.DomainEventConvertor;
import org.laokou.common.domain.event.DecorateDomainEvent;
import org.laokou.common.domain.service.DomainEventService;
import org.laokou.common.i18n.common.EventStatusEnums;
import org.laokou.common.i18n.common.JobModeEnums;
import org.laokou.common.i18n.dto.DomainEvent;
import org.laokou.common.mybatisplus.utils.DynamicUtil;
import org.laokou.common.rocketmq.template.RocketMqTemplate;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static org.laokou.common.i18n.common.EventStatusEnums.PUBLISH_FAILED;
import static org.laokou.common.i18n.common.EventStatusEnums.PUBLISH_SUCCEED;
import static org.laokou.common.i18n.common.PropertiesConstants.SPRING_APPLICATION_NAME;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventPublishTask {

	private final Executor executor;

	private final DomainEventService domainEventService;

	private final Environment environment;

	private final RocketMqTemplate rocketMqTemplate;

	private final DynamicUtil dynamicUtil;

	public void publishEvent(List<DomainEvent<Long>> list, JobModeEnums jobMode) {
		List<DomainEvent<Long>> modifyList = Collections.synchronizedList(new ArrayList<>());
		switch (jobMode) {
			case SYNC -> {
				if (CollectionUtil.isNotEmpty(list)) {
					List<CompletableFuture<Void>> futures = list.stream()
						.map(item -> CompletableFuture.runAsync(() -> handleMqSend(modifyList, item), executor))
						.toList();
					// 阻塞所有任务
					CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
				}
			}
			case ASYNC -> jobHandler();
		}
		// 批量修改事件状态
		if (CollectionUtil.isNotEmpty(modifyList)) {
			domainEventService.modify(modifyList);
		}
	}

	private void jobHandler() {
		domainEventService.finds(getSourceNames(), getAppName(), resultContext -> {
		});
	}

	private String getAppName() {
		return environment.getProperty(SPRING_APPLICATION_NAME);
	}

	private Set<String> getSourceNames() {
		return dynamicUtil.getDataSources().keySet();
	}

	private void handleMqSend(List<DomainEvent<Long>> modifyList, DomainEvent<Long> item) {
		// 同步发送并修改事件状态
		boolean result = rocketMqTemplate.sendSyncOrderlyMessage(item.getTopic(),
				DomainEventConvertor.toDataObject(item), item.getAppName());
		if (result) {
			// 发布成功
			addEvent(modifyList, item, PUBLISH_SUCCEED);
		}
		else {
			// 发布失败
			addEvent(modifyList, item, PUBLISH_FAILED);
		}
	}

	private void addEvent(List<DomainEvent<Long>> modifyList, DomainEvent<Long> item,
			EventStatusEnums eventStatusEnums) {
		DecorateDomainEvent event = new DecorateDomainEvent(item.getId(), eventStatusEnums, item.getSourceName());
		modifyList.add(event);
	}

}
