/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.laokou.common.dynamic.router;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.laokou.common.dynamic.router.utils.NameUtil;
import org.springframework.validation.annotation.Validated;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * @author Spencer Gibb
 */
@Getter
@Validated
public class FilterDefinition {

	@NotNull
	private String name;

	private Map<String, String> args = new LinkedHashMap<>();

	public FilterDefinition() {
	}

	public FilterDefinition(String text) {
		int eqIdx = text.indexOf('=');
		if (eqIdx <= 0) {
			setName(text);
			return;
		}
		setName(text.substring(0, eqIdx));

		String[] args = tokenizeToStringArray(text.substring(eqIdx + 1), ",");

		for (int i = 0; i < args.length; i++) {
			this.args.put(NameUtil.generateName(i), args[i]);
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
	}

	public void addArg(String key, String value) {
		this.args.put(key, value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (Objects.isNull(o) || getClass() != o.getClass()) {
			return false;
		}
		FilterDefinition that = (FilterDefinition) o;
		return Objects.equals(name, that.name) && Objects.equals(args, that.args);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, args);
	}

	@Override
	public String toString() {
        return "FilterDefinition{" + "name='" + name + '\'' +
				", args=" + args +
				'}';
	}

}
