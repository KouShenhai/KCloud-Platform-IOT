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

package org.laokou.common.mybatisplus.dsl;

import lombok.extern.slf4j.Slf4j;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.i18n.dto.PageQuery;
import org.laokou.common.i18n.utils.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.laokou.common.i18n.common.Constant.*;
import static org.laokou.common.mybatisplus.dsl.SelectDSL.Constant.*;

/**
 * @author laokou
 */
@Slf4j
public class SqlTemplate {

	public static List<String> getUserTables(String start, String end) {
		return new ArrayList<>(0);
	}

	public static String toSql(SelectDSL dsl) {
		StringBuilder sql = new StringBuilder(300);
		List<SelectDSL.Column> columns = dsl.columns();
		String alias = dsl.alias();
		List<SelectDSL.Join> joins = dsl.joins();
		String connector = dsl.connector();
		List<SelectDSL.Where> wheres = dsl.wheres();
		if (StringUtil.isNotEmpty(connector)) {
			sql.append(connector).append(WRAP);
		}
		sql.append(SELECT).append(WRAP);
		sql.append(columns(columns, alias).parallelStream().collect(Collectors.joining(COMMA))).append(WRAP);
		sql.append(FROM).append(SPACE).append(dsl.from());
		if (StringUtil.isNotEmpty(alias)) {
			sql.append(SPACE).append(AS).append(SPACE).append(alias);
		}
		sql.append(WRAP);
		if (CollectionUtil.isNotEmpty(joins)) {
			joins.forEach(item -> {
				sql.append(item.type())
					.append(LEFT)
					.append(SELECT)
					.append(SPACE)
					.append(columns(item.columns(), EMPTY).parallelStream().collect(Collectors.joining(COMMA)))
					.append(SPACE)
					.append(FROM)
					.append(SPACE)
					.append(item.from())
					.append(SPACE)
					.append(AS)
					.append(SPACE)
					.append(item.alias())
					.append(SPACE);
				String where = where(item.wheres(), item.alias());
				if (StringUtil.isNotEmpty(where)) {
					sql.append(WHERE).append(SPACE).append(where);
				}
				SelectDSL.GroupBy groupBy = item.groupBy();
				if (groupBy != null && CollectionUtil.isNotEmpty(groupBy.columns())) {
					sql.append(SPACE)
						.append(GROUP_BY)
						.append(SPACE)
						.append(columns(groupBy.columns(), EMPTY).parallelStream().collect(Collectors.joining(COMMA)));
				}
				sql.append(SPACE)
					.append(ORDER_BY)
					.append(SPACE)
					.append(orderBy(item.orderBy().columns()).parallelStream().collect(Collectors.joining(COMMA)));
				sql.append(SPACE).append(LIMIT).append(SPACE).append(item.offset()).append(COMMA).append(item.limit());
				sql.append(RIGHT);
				String joinAlias = item.alias();
				if (StringUtil.isNotEmpty(joinAlias)) {
					sql.append(SPACE).append(AS).append(SPACE).append(joinAlias);
					StringBuilder onBuilder = new StringBuilder(50);
					item.ons()
						.forEach(i -> onBuilder.append(SelectDSL.Constant.AND)
							.append(SPACE)
							.append(columnAlias(i.fromAlias(), i.fromColumn(), EMPTY))
							.append(EQ)
							.append(columnAlias(i.joinAlias(), i.joinColumn(), EMPTY)));
					String on = onBuilder.toString();
					if (StringUtil.isNotEmpty(on)) {
						sql.append(SPACE).append(ON).append(SPACE).append(on, 4, on.length());
					}
				}
				sql.append(WRAP);
			});
		}
		String where = where(wheres, alias);
		if (StringUtil.isNotEmpty(where)) {
			sql.append(WHERE).append(SPACE).append(where);
		}
		return sql.toString();
	}

	private static String where(List<SelectDSL.Where> wheres, String alias) {
		if (CollectionUtil.isEmpty(wheres)) {
			return EMPTY;
		}
		StringBuilder whereBuilder = new StringBuilder(200);
		wheres.forEach(where -> whereBuilder.append(where.concat())
			.append(SPACE)
			.append(columnAlias(alias, where.column(), EMPTY))
			.append(SPACE)
			.append(where.compare1())
			.append(SPACE)
			.append(encodeStr(where.val1()))
			.append(SPACE)
			.append(StringUtil.isNotEmpty(where.compare2()) ? where.compare2() + SPACE + encodeStr(where.val2())
					: EMPTY)
			.append(SPACE));
		return whereBuilder.toString().trim();
	}

	public static void main(String[] args) {
		long l = System.currentTimeMillis();
		PageQuery pageQuery = new PageQuery(1, 1);
		SelectDSL.Where where = new SelectDSL.Where.Builder().withCompare1("between")
			.withCompare2("and")
			.withColumn("create_date")
			.withVal1("2022-01-01 00:00:00")
			.withVal2("2023-12-31 00:00:00")
			.build();
		SelectDSL.Where where2 = new SelectDSL.Where.Builder().withCompare1("=")
			.withConcat("and")
			.withColumn("del_flag")
			.withVal1("0")
			.build();
		SelectDSL boot_sys_user_202201 = getTestWrapper(pageQuery.page(), "boot_sys_user_202201", "",
				Arrays.asList(where, where2));
		SelectDSL boot_sys_user_2022011 = getTestCountWrapper("boot_sys_user_202201", "", Arrays.asList(where, where2));
		log.info(toSql(boot_sys_user_202201));
		log.info(toSql(boot_sys_user_2022011));
		log.info(String.valueOf(l - System.currentTimeMillis()));
	}

	public static List<String> columns(List<SelectDSL.Column> columns, String alias) {
		return columns.parallelStream().map(item -> columnAlias(alias, item.name(), item.alias())).toList();
	}

	public static String columnAlias(String alias, String column, String columnAlias) {
		String as = EMPTY;
		if (StringUtil.isNotEmpty(columnAlias)) {
			as = SPACE + AS + SPACE + columnAlias;
		}
		if (StringUtil.isEmpty(alias)) {
			return column + as;
		}
		return alias + DOT + column + as;
	}

	public static String columnSort(String sort, String column) {
		return column + SPACE + sort;
	}

	public static List<String> orderBy(List<SelectDSL.Column> columns) {
		return columns.parallelStream().map(item -> columnSort(item.sort(), item.name())).toList();
	}

	public static String encodeStr(Object val) {
		return DOUBLE_QUOT + val + DOUBLE_QUOT;
	}

	public static List<String> getUserTables() {
		List<String> list = new ArrayList<>(936);
		list.add(dynamicUserTable("202201"));
		list.add(dynamicUserTable("202202"));
		list.add(dynamicUserTable("202203"));
		list.add(dynamicUserTable("202204"));
		list.add(dynamicUserTable("202205"));
		list.add(dynamicUserTable("202206"));
		list.add(dynamicUserTable("202207"));
		list.add(dynamicUserTable("202208"));
		list.add(dynamicUserTable("202209"));
		list.add(dynamicUserTable("202210"));
		list.add(dynamicUserTable("202211"));
		list.add(dynamicUserTable("202212"));
		list.add(dynamicUserTable("202301"));
		list.add(dynamicUserTable("202302"));
		list.add(dynamicUserTable("202303"));
		list.add(dynamicUserTable("202304"));
		list.add(dynamicUserTable("202305"));
		list.add(dynamicUserTable("202306"));
		list.add(dynamicUserTable("202307"));
		list.add(dynamicUserTable("202308"));
		list.add(dynamicUserTable("202309"));
		list.add(dynamicUserTable("202310"));
		list.add(dynamicUserTable("202311"));
		list.add(dynamicUserTable("202312"));
		list.add(dynamicUserTable("202401"));
		list.add(dynamicUserTable("202402"));
		list.add(dynamicUserTable("202403"));
		list.add(dynamicUserTable("202404"));
		list.add(dynamicUserTable("202405"));
		list.add(dynamicUserTable("202406"));
		list.add(dynamicUserTable("202407"));
		list.add(dynamicUserTable("202408"));
		list.add(dynamicUserTable("202409"));
		list.add(dynamicUserTable("202410"));
		list.add(dynamicUserTable("202411"));
		list.add(dynamicUserTable("202412"));
		list.add(dynamicUserTable("202501"));
		list.add(dynamicUserTable("202502"));
		list.add(dynamicUserTable("202503"));
		list.add(dynamicUserTable("202504"));
		list.add(dynamicUserTable("202505"));
		list.add(dynamicUserTable("202506"));
		list.add(dynamicUserTable("202507"));
		list.add(dynamicUserTable("202508"));
		list.add(dynamicUserTable("202509"));
		list.add(dynamicUserTable("202510"));
		list.add(dynamicUserTable("202511"));
		list.add(dynamicUserTable("202512"));
		list.add(dynamicUserTable("202601"));
		list.add(dynamicUserTable("202602"));
		list.add(dynamicUserTable("202603"));
		list.add(dynamicUserTable("202604"));
		list.add(dynamicUserTable("202605"));
		list.add(dynamicUserTable("202606"));
		list.add(dynamicUserTable("202607"));
		list.add(dynamicUserTable("202608"));
		list.add(dynamicUserTable("202609"));
		list.add(dynamicUserTable("202610"));
		list.add(dynamicUserTable("202611"));
		list.add(dynamicUserTable("202612"));
		list.add(dynamicUserTable("202701"));
		list.add(dynamicUserTable("202702"));
		list.add(dynamicUserTable("202703"));
		list.add(dynamicUserTable("202704"));
		list.add(dynamicUserTable("202705"));
		list.add(dynamicUserTable("202706"));
		list.add(dynamicUserTable("202707"));
		list.add(dynamicUserTable("202708"));
		list.add(dynamicUserTable("202709"));
		list.add(dynamicUserTable("202710"));
		list.add(dynamicUserTable("202711"));
		list.add(dynamicUserTable("202712"));
		list.add(dynamicUserTable("202801"));
		list.add(dynamicUserTable("202802"));
		list.add(dynamicUserTable("202803"));
		list.add(dynamicUserTable("202804"));
		list.add(dynamicUserTable("202805"));
		list.add(dynamicUserTable("202806"));
		list.add(dynamicUserTable("202807"));
		list.add(dynamicUserTable("202808"));
		list.add(dynamicUserTable("202809"));
		list.add(dynamicUserTable("202810"));
		list.add(dynamicUserTable("202811"));
		list.add(dynamicUserTable("202812"));
		list.add(dynamicUserTable("202901"));
		list.add(dynamicUserTable("202902"));
		list.add(dynamicUserTable("202903"));
		list.add(dynamicUserTable("202904"));
		list.add(dynamicUserTable("202905"));
		list.add(dynamicUserTable("202906"));
		list.add(dynamicUserTable("202907"));
		list.add(dynamicUserTable("202908"));
		list.add(dynamicUserTable("202909"));
		list.add(dynamicUserTable("202910"));
		list.add(dynamicUserTable("202911"));
		list.add(dynamicUserTable("202912"));
		list.add(dynamicUserTable("203001"));
		list.add(dynamicUserTable("203002"));
		list.add(dynamicUserTable("203003"));
		list.add(dynamicUserTable("203004"));
		list.add(dynamicUserTable("203005"));
		list.add(dynamicUserTable("203006"));
		list.add(dynamicUserTable("203007"));
		list.add(dynamicUserTable("203008"));
		list.add(dynamicUserTable("203009"));
		list.add(dynamicUserTable("203010"));
		list.add(dynamicUserTable("203011"));
		list.add(dynamicUserTable("203012"));
		list.add(dynamicUserTable("203101"));
		list.add(dynamicUserTable("203102"));
		list.add(dynamicUserTable("203103"));
		list.add(dynamicUserTable("203104"));
		list.add(dynamicUserTable("203105"));
		list.add(dynamicUserTable("203106"));
		list.add(dynamicUserTable("203107"));
		list.add(dynamicUserTable("203108"));
		list.add(dynamicUserTable("203109"));
		list.add(dynamicUserTable("203110"));
		list.add(dynamicUserTable("203111"));
		list.add(dynamicUserTable("203112"));
		list.add(dynamicUserTable("203201"));
		list.add(dynamicUserTable("203202"));
		list.add(dynamicUserTable("203203"));
		list.add(dynamicUserTable("203204"));
		list.add(dynamicUserTable("203205"));
		list.add(dynamicUserTable("203206"));
		list.add(dynamicUserTable("203207"));
		list.add(dynamicUserTable("203208"));
		list.add(dynamicUserTable("203209"));
		list.add(dynamicUserTable("203210"));
		list.add(dynamicUserTable("203211"));
		list.add(dynamicUserTable("203212"));
		list.add(dynamicUserTable("203301"));
		list.add(dynamicUserTable("203302"));
		list.add(dynamicUserTable("203303"));
		list.add(dynamicUserTable("203304"));
		list.add(dynamicUserTable("203305"));
		list.add(dynamicUserTable("203306"));
		list.add(dynamicUserTable("203307"));
		list.add(dynamicUserTable("203308"));
		list.add(dynamicUserTable("203309"));
		list.add(dynamicUserTable("203310"));
		list.add(dynamicUserTable("203311"));
		list.add(dynamicUserTable("203312"));
		list.add(dynamicUserTable("203401"));
		list.add(dynamicUserTable("203402"));
		list.add(dynamicUserTable("203403"));
		list.add(dynamicUserTable("203404"));
		list.add(dynamicUserTable("203405"));
		list.add(dynamicUserTable("203406"));
		list.add(dynamicUserTable("203407"));
		list.add(dynamicUserTable("203408"));
		list.add(dynamicUserTable("203409"));
		list.add(dynamicUserTable("203410"));
		list.add(dynamicUserTable("203411"));
		list.add(dynamicUserTable("203412"));
		list.add(dynamicUserTable("203501"));
		list.add(dynamicUserTable("203502"));
		list.add(dynamicUserTable("203503"));
		list.add(dynamicUserTable("203504"));
		list.add(dynamicUserTable("203505"));
		list.add(dynamicUserTable("203506"));
		list.add(dynamicUserTable("203507"));
		list.add(dynamicUserTable("203508"));
		list.add(dynamicUserTable("203509"));
		list.add(dynamicUserTable("203510"));
		list.add(dynamicUserTable("203511"));
		list.add(dynamicUserTable("203512"));
		list.add(dynamicUserTable("203601"));
		list.add(dynamicUserTable("203602"));
		list.add(dynamicUserTable("203603"));
		list.add(dynamicUserTable("203604"));
		list.add(dynamicUserTable("203605"));
		list.add(dynamicUserTable("203606"));
		list.add(dynamicUserTable("203607"));
		list.add(dynamicUserTable("203608"));
		list.add(dynamicUserTable("203609"));
		list.add(dynamicUserTable("203610"));
		list.add(dynamicUserTable("203611"));
		list.add(dynamicUserTable("203612"));
		list.add(dynamicUserTable("203701"));
		list.add(dynamicUserTable("203702"));
		list.add(dynamicUserTable("203703"));
		list.add(dynamicUserTable("203704"));
		list.add(dynamicUserTable("203705"));
		list.add(dynamicUserTable("203706"));
		list.add(dynamicUserTable("203707"));
		list.add(dynamicUserTable("203708"));
		list.add(dynamicUserTable("203709"));
		list.add(dynamicUserTable("203710"));
		list.add(dynamicUserTable("203711"));
		list.add(dynamicUserTable("203712"));
		list.add(dynamicUserTable("203801"));
		list.add(dynamicUserTable("203802"));
		list.add(dynamicUserTable("203803"));
		list.add(dynamicUserTable("203804"));
		list.add(dynamicUserTable("203805"));
		list.add(dynamicUserTable("203806"));
		list.add(dynamicUserTable("203807"));
		list.add(dynamicUserTable("203808"));
		list.add(dynamicUserTable("203809"));
		list.add(dynamicUserTable("203810"));
		list.add(dynamicUserTable("203811"));
		list.add(dynamicUserTable("203812"));
		list.add(dynamicUserTable("203901"));
		list.add(dynamicUserTable("203902"));
		list.add(dynamicUserTable("203903"));
		list.add(dynamicUserTable("203904"));
		list.add(dynamicUserTable("203905"));
		list.add(dynamicUserTable("203906"));
		list.add(dynamicUserTable("203907"));
		list.add(dynamicUserTable("203908"));
		list.add(dynamicUserTable("203909"));
		list.add(dynamicUserTable("203910"));
		list.add(dynamicUserTable("203911"));
		list.add(dynamicUserTable("203912"));
		list.add(dynamicUserTable("204001"));
		list.add(dynamicUserTable("204002"));
		list.add(dynamicUserTable("204003"));
		list.add(dynamicUserTable("204004"));
		list.add(dynamicUserTable("204005"));
		list.add(dynamicUserTable("204006"));
		list.add(dynamicUserTable("204007"));
		list.add(dynamicUserTable("204008"));
		list.add(dynamicUserTable("204009"));
		list.add(dynamicUserTable("204010"));
		list.add(dynamicUserTable("204011"));
		list.add(dynamicUserTable("204012"));
		list.add(dynamicUserTable("204101"));
		list.add(dynamicUserTable("204102"));
		list.add(dynamicUserTable("204103"));
		list.add(dynamicUserTable("204104"));
		list.add(dynamicUserTable("204105"));
		list.add(dynamicUserTable("204106"));
		list.add(dynamicUserTable("204107"));
		list.add(dynamicUserTable("204108"));
		list.add(dynamicUserTable("204109"));
		list.add(dynamicUserTable("204110"));
		list.add(dynamicUserTable("204111"));
		list.add(dynamicUserTable("204112"));
		list.add(dynamicUserTable("204201"));
		list.add(dynamicUserTable("204202"));
		list.add(dynamicUserTable("204203"));
		list.add(dynamicUserTable("204204"));
		list.add(dynamicUserTable("204205"));
		list.add(dynamicUserTable("204206"));
		list.add(dynamicUserTable("204207"));
		list.add(dynamicUserTable("204208"));
		list.add(dynamicUserTable("204209"));
		list.add(dynamicUserTable("204210"));
		list.add(dynamicUserTable("204211"));
		list.add(dynamicUserTable("204212"));
		list.add(dynamicUserTable("204301"));
		list.add(dynamicUserTable("204302"));
		list.add(dynamicUserTable("204303"));
		list.add(dynamicUserTable("204304"));
		list.add(dynamicUserTable("204305"));
		list.add(dynamicUserTable("204306"));
		list.add(dynamicUserTable("204307"));
		list.add(dynamicUserTable("204308"));
		list.add(dynamicUserTable("204309"));
		list.add(dynamicUserTable("204310"));
		list.add(dynamicUserTable("204311"));
		list.add(dynamicUserTable("204312"));
		list.add(dynamicUserTable("204401"));
		list.add(dynamicUserTable("204402"));
		list.add(dynamicUserTable("204403"));
		list.add(dynamicUserTable("204404"));
		list.add(dynamicUserTable("204405"));
		list.add(dynamicUserTable("204406"));
		list.add(dynamicUserTable("204407"));
		list.add(dynamicUserTable("204408"));
		list.add(dynamicUserTable("204409"));
		list.add(dynamicUserTable("204410"));
		list.add(dynamicUserTable("204411"));
		list.add(dynamicUserTable("204412"));
		list.add(dynamicUserTable("204501"));
		list.add(dynamicUserTable("204502"));
		list.add(dynamicUserTable("204503"));
		list.add(dynamicUserTable("204504"));
		list.add(dynamicUserTable("204505"));
		list.add(dynamicUserTable("204506"));
		list.add(dynamicUserTable("204507"));
		list.add(dynamicUserTable("204508"));
		list.add(dynamicUserTable("204509"));
		list.add(dynamicUserTable("204510"));
		list.add(dynamicUserTable("204511"));
		list.add(dynamicUserTable("204512"));
		list.add(dynamicUserTable("204601"));
		list.add(dynamicUserTable("204602"));
		list.add(dynamicUserTable("204603"));
		list.add(dynamicUserTable("204604"));
		list.add(dynamicUserTable("204605"));
		list.add(dynamicUserTable("204606"));
		list.add(dynamicUserTable("204607"));
		list.add(dynamicUserTable("204608"));
		list.add(dynamicUserTable("204609"));
		list.add(dynamicUserTable("204610"));
		list.add(dynamicUserTable("204611"));
		list.add(dynamicUserTable("204612"));
		list.add(dynamicUserTable("204701"));
		list.add(dynamicUserTable("204702"));
		list.add(dynamicUserTable("204703"));
		list.add(dynamicUserTable("204704"));
		list.add(dynamicUserTable("204705"));
		list.add(dynamicUserTable("204706"));
		list.add(dynamicUserTable("204707"));
		list.add(dynamicUserTable("204708"));
		list.add(dynamicUserTable("204709"));
		list.add(dynamicUserTable("204710"));
		list.add(dynamicUserTable("204711"));
		list.add(dynamicUserTable("204712"));
		list.add(dynamicUserTable("204801"));
		list.add(dynamicUserTable("204802"));
		list.add(dynamicUserTable("204803"));
		list.add(dynamicUserTable("204804"));
		list.add(dynamicUserTable("204805"));
		list.add(dynamicUserTable("204806"));
		list.add(dynamicUserTable("204807"));
		list.add(dynamicUserTable("204808"));
		list.add(dynamicUserTable("204809"));
		list.add(dynamicUserTable("204810"));
		list.add(dynamicUserTable("204811"));
		list.add(dynamicUserTable("204812"));
		list.add(dynamicUserTable("204901"));
		list.add(dynamicUserTable("204902"));
		list.add(dynamicUserTable("204903"));
		list.add(dynamicUserTable("204904"));
		list.add(dynamicUserTable("204905"));
		list.add(dynamicUserTable("204906"));
		list.add(dynamicUserTable("204907"));
		list.add(dynamicUserTable("204908"));
		list.add(dynamicUserTable("204909"));
		list.add(dynamicUserTable("204910"));
		list.add(dynamicUserTable("204911"));
		list.add(dynamicUserTable("204912"));
		list.add(dynamicUserTable("205001"));
		list.add(dynamicUserTable("205002"));
		list.add(dynamicUserTable("205003"));
		list.add(dynamicUserTable("205004"));
		list.add(dynamicUserTable("205005"));
		list.add(dynamicUserTable("205006"));
		list.add(dynamicUserTable("205007"));
		list.add(dynamicUserTable("205008"));
		list.add(dynamicUserTable("205009"));
		list.add(dynamicUserTable("205010"));
		list.add(dynamicUserTable("205011"));
		list.add(dynamicUserTable("205012"));
		list.add(dynamicUserTable("205101"));
		list.add(dynamicUserTable("205102"));
		list.add(dynamicUserTable("205103"));
		list.add(dynamicUserTable("205104"));
		list.add(dynamicUserTable("205105"));
		list.add(dynamicUserTable("205106"));
		list.add(dynamicUserTable("205107"));
		list.add(dynamicUserTable("205108"));
		list.add(dynamicUserTable("205109"));
		list.add(dynamicUserTable("205110"));
		list.add(dynamicUserTable("205111"));
		list.add(dynamicUserTable("205112"));
		list.add(dynamicUserTable("205201"));
		list.add(dynamicUserTable("205202"));
		list.add(dynamicUserTable("205203"));
		list.add(dynamicUserTable("205204"));
		list.add(dynamicUserTable("205205"));
		list.add(dynamicUserTable("205206"));
		list.add(dynamicUserTable("205207"));
		list.add(dynamicUserTable("205208"));
		list.add(dynamicUserTable("205209"));
		list.add(dynamicUserTable("205210"));
		list.add(dynamicUserTable("205211"));
		list.add(dynamicUserTable("205212"));
		list.add(dynamicUserTable("205301"));
		list.add(dynamicUserTable("205302"));
		list.add(dynamicUserTable("205303"));
		list.add(dynamicUserTable("205304"));
		list.add(dynamicUserTable("205305"));
		list.add(dynamicUserTable("205306"));
		list.add(dynamicUserTable("205307"));
		list.add(dynamicUserTable("205308"));
		list.add(dynamicUserTable("205309"));
		list.add(dynamicUserTable("205310"));
		list.add(dynamicUserTable("205311"));
		list.add(dynamicUserTable("205312"));
		list.add(dynamicUserTable("205401"));
		list.add(dynamicUserTable("205402"));
		list.add(dynamicUserTable("205403"));
		list.add(dynamicUserTable("205404"));
		list.add(dynamicUserTable("205405"));
		list.add(dynamicUserTable("205406"));
		list.add(dynamicUserTable("205407"));
		list.add(dynamicUserTable("205408"));
		list.add(dynamicUserTable("205409"));
		list.add(dynamicUserTable("205410"));
		list.add(dynamicUserTable("205411"));
		list.add(dynamicUserTable("205412"));
		list.add(dynamicUserTable("205501"));
		list.add(dynamicUserTable("205502"));
		list.add(dynamicUserTable("205503"));
		list.add(dynamicUserTable("205504"));
		list.add(dynamicUserTable("205505"));
		list.add(dynamicUserTable("205506"));
		list.add(dynamicUserTable("205507"));
		list.add(dynamicUserTable("205508"));
		list.add(dynamicUserTable("205509"));
		list.add(dynamicUserTable("205510"));
		list.add(dynamicUserTable("205511"));
		list.add(dynamicUserTable("205512"));
		list.add(dynamicUserTable("205601"));
		list.add(dynamicUserTable("205602"));
		list.add(dynamicUserTable("205603"));
		list.add(dynamicUserTable("205604"));
		list.add(dynamicUserTable("205605"));
		list.add(dynamicUserTable("205606"));
		list.add(dynamicUserTable("205607"));
		list.add(dynamicUserTable("205608"));
		list.add(dynamicUserTable("205609"));
		list.add(dynamicUserTable("205610"));
		list.add(dynamicUserTable("205611"));
		list.add(dynamicUserTable("205612"));
		list.add(dynamicUserTable("205701"));
		list.add(dynamicUserTable("205702"));
		list.add(dynamicUserTable("205703"));
		list.add(dynamicUserTable("205704"));
		list.add(dynamicUserTable("205705"));
		list.add(dynamicUserTable("205706"));
		list.add(dynamicUserTable("205707"));
		list.add(dynamicUserTable("205708"));
		list.add(dynamicUserTable("205709"));
		list.add(dynamicUserTable("205710"));
		list.add(dynamicUserTable("205711"));
		list.add(dynamicUserTable("205712"));
		list.add(dynamicUserTable("205801"));
		list.add(dynamicUserTable("205802"));
		list.add(dynamicUserTable("205803"));
		list.add(dynamicUserTable("205804"));
		list.add(dynamicUserTable("205805"));
		list.add(dynamicUserTable("205806"));
		list.add(dynamicUserTable("205807"));
		list.add(dynamicUserTable("205808"));
		list.add(dynamicUserTable("205809"));
		list.add(dynamicUserTable("205810"));
		list.add(dynamicUserTable("205811"));
		list.add(dynamicUserTable("205812"));
		list.add(dynamicUserTable("205901"));
		list.add(dynamicUserTable("205902"));
		list.add(dynamicUserTable("205903"));
		list.add(dynamicUserTable("205904"));
		list.add(dynamicUserTable("205905"));
		list.add(dynamicUserTable("205906"));
		list.add(dynamicUserTable("205907"));
		list.add(dynamicUserTable("205908"));
		list.add(dynamicUserTable("205909"));
		list.add(dynamicUserTable("205910"));
		list.add(dynamicUserTable("205911"));
		list.add(dynamicUserTable("205912"));
		list.add(dynamicUserTable("206001"));
		list.add(dynamicUserTable("206002"));
		list.add(dynamicUserTable("206003"));
		list.add(dynamicUserTable("206004"));
		list.add(dynamicUserTable("206005"));
		list.add(dynamicUserTable("206006"));
		list.add(dynamicUserTable("206007"));
		list.add(dynamicUserTable("206008"));
		list.add(dynamicUserTable("206009"));
		list.add(dynamicUserTable("206010"));
		list.add(dynamicUserTable("206011"));
		list.add(dynamicUserTable("206012"));
		list.add(dynamicUserTable("206101"));
		list.add(dynamicUserTable("206102"));
		list.add(dynamicUserTable("206103"));
		list.add(dynamicUserTable("206104"));
		list.add(dynamicUserTable("206105"));
		list.add(dynamicUserTable("206106"));
		list.add(dynamicUserTable("206107"));
		list.add(dynamicUserTable("206108"));
		list.add(dynamicUserTable("206109"));
		list.add(dynamicUserTable("206110"));
		list.add(dynamicUserTable("206111"));
		list.add(dynamicUserTable("206112"));
		list.add(dynamicUserTable("206201"));
		list.add(dynamicUserTable("206202"));
		list.add(dynamicUserTable("206203"));
		list.add(dynamicUserTable("206204"));
		list.add(dynamicUserTable("206205"));
		list.add(dynamicUserTable("206206"));
		list.add(dynamicUserTable("206207"));
		list.add(dynamicUserTable("206208"));
		list.add(dynamicUserTable("206209"));
		list.add(dynamicUserTable("206210"));
		list.add(dynamicUserTable("206211"));
		list.add(dynamicUserTable("206212"));
		list.add(dynamicUserTable("206301"));
		list.add(dynamicUserTable("206302"));
		list.add(dynamicUserTable("206303"));
		list.add(dynamicUserTable("206304"));
		list.add(dynamicUserTable("206305"));
		list.add(dynamicUserTable("206306"));
		list.add(dynamicUserTable("206307"));
		list.add(dynamicUserTable("206308"));
		list.add(dynamicUserTable("206309"));
		list.add(dynamicUserTable("206310"));
		list.add(dynamicUserTable("206311"));
		list.add(dynamicUserTable("206312"));
		list.add(dynamicUserTable("206401"));
		list.add(dynamicUserTable("206402"));
		list.add(dynamicUserTable("206403"));
		list.add(dynamicUserTable("206404"));
		list.add(dynamicUserTable("206405"));
		list.add(dynamicUserTable("206406"));
		list.add(dynamicUserTable("206407"));
		list.add(dynamicUserTable("206408"));
		list.add(dynamicUserTable("206409"));
		list.add(dynamicUserTable("206410"));
		list.add(dynamicUserTable("206411"));
		list.add(dynamicUserTable("206412"));
		list.add(dynamicUserTable("206501"));
		list.add(dynamicUserTable("206502"));
		list.add(dynamicUserTable("206503"));
		list.add(dynamicUserTable("206504"));
		list.add(dynamicUserTable("206505"));
		list.add(dynamicUserTable("206506"));
		list.add(dynamicUserTable("206507"));
		list.add(dynamicUserTable("206508"));
		list.add(dynamicUserTable("206509"));
		list.add(dynamicUserTable("206510"));
		list.add(dynamicUserTable("206511"));
		list.add(dynamicUserTable("206512"));
		list.add(dynamicUserTable("206601"));
		list.add(dynamicUserTable("206602"));
		list.add(dynamicUserTable("206603"));
		list.add(dynamicUserTable("206604"));
		list.add(dynamicUserTable("206605"));
		list.add(dynamicUserTable("206606"));
		list.add(dynamicUserTable("206607"));
		list.add(dynamicUserTable("206608"));
		list.add(dynamicUserTable("206609"));
		list.add(dynamicUserTable("206610"));
		list.add(dynamicUserTable("206611"));
		list.add(dynamicUserTable("206612"));
		list.add(dynamicUserTable("206701"));
		list.add(dynamicUserTable("206702"));
		list.add(dynamicUserTable("206703"));
		list.add(dynamicUserTable("206704"));
		list.add(dynamicUserTable("206705"));
		list.add(dynamicUserTable("206706"));
		list.add(dynamicUserTable("206707"));
		list.add(dynamicUserTable("206708"));
		list.add(dynamicUserTable("206709"));
		list.add(dynamicUserTable("206710"));
		list.add(dynamicUserTable("206711"));
		list.add(dynamicUserTable("206712"));
		list.add(dynamicUserTable("206801"));
		list.add(dynamicUserTable("206802"));
		list.add(dynamicUserTable("206803"));
		list.add(dynamicUserTable("206804"));
		list.add(dynamicUserTable("206805"));
		list.add(dynamicUserTable("206806"));
		list.add(dynamicUserTable("206807"));
		list.add(dynamicUserTable("206808"));
		list.add(dynamicUserTable("206809"));
		list.add(dynamicUserTable("206810"));
		list.add(dynamicUserTable("206811"));
		list.add(dynamicUserTable("206812"));
		list.add(dynamicUserTable("206901"));
		list.add(dynamicUserTable("206902"));
		list.add(dynamicUserTable("206903"));
		list.add(dynamicUserTable("206904"));
		list.add(dynamicUserTable("206905"));
		list.add(dynamicUserTable("206906"));
		list.add(dynamicUserTable("206907"));
		list.add(dynamicUserTable("206908"));
		list.add(dynamicUserTable("206909"));
		list.add(dynamicUserTable("206910"));
		list.add(dynamicUserTable("206911"));
		list.add(dynamicUserTable("206912"));
		list.add(dynamicUserTable("207001"));
		list.add(dynamicUserTable("207002"));
		list.add(dynamicUserTable("207003"));
		list.add(dynamicUserTable("207004"));
		list.add(dynamicUserTable("207005"));
		list.add(dynamicUserTable("207006"));
		list.add(dynamicUserTable("207007"));
		list.add(dynamicUserTable("207008"));
		list.add(dynamicUserTable("207009"));
		list.add(dynamicUserTable("207010"));
		list.add(dynamicUserTable("207011"));
		list.add(dynamicUserTable("207012"));
		list.add(dynamicUserTable("207101"));
		list.add(dynamicUserTable("207102"));
		list.add(dynamicUserTable("207103"));
		list.add(dynamicUserTable("207104"));
		list.add(dynamicUserTable("207105"));
		list.add(dynamicUserTable("207106"));
		list.add(dynamicUserTable("207107"));
		list.add(dynamicUserTable("207108"));
		list.add(dynamicUserTable("207109"));
		list.add(dynamicUserTable("207110"));
		list.add(dynamicUserTable("207111"));
		list.add(dynamicUserTable("207112"));
		list.add(dynamicUserTable("207201"));
		list.add(dynamicUserTable("207202"));
		list.add(dynamicUserTable("207203"));
		list.add(dynamicUserTable("207204"));
		list.add(dynamicUserTable("207205"));
		list.add(dynamicUserTable("207206"));
		list.add(dynamicUserTable("207207"));
		list.add(dynamicUserTable("207208"));
		list.add(dynamicUserTable("207209"));
		list.add(dynamicUserTable("207210"));
		list.add(dynamicUserTable("207211"));
		list.add(dynamicUserTable("207212"));
		list.add(dynamicUserTable("207301"));
		list.add(dynamicUserTable("207302"));
		list.add(dynamicUserTable("207303"));
		list.add(dynamicUserTable("207304"));
		list.add(dynamicUserTable("207305"));
		list.add(dynamicUserTable("207306"));
		list.add(dynamicUserTable("207307"));
		list.add(dynamicUserTable("207308"));
		list.add(dynamicUserTable("207309"));
		list.add(dynamicUserTable("207310"));
		list.add(dynamicUserTable("207311"));
		list.add(dynamicUserTable("207312"));
		list.add(dynamicUserTable("207401"));
		list.add(dynamicUserTable("207402"));
		list.add(dynamicUserTable("207403"));
		list.add(dynamicUserTable("207404"));
		list.add(dynamicUserTable("207405"));
		list.add(dynamicUserTable("207406"));
		list.add(dynamicUserTable("207407"));
		list.add(dynamicUserTable("207408"));
		list.add(dynamicUserTable("207409"));
		list.add(dynamicUserTable("207410"));
		list.add(dynamicUserTable("207411"));
		list.add(dynamicUserTable("207412"));
		list.add(dynamicUserTable("207501"));
		list.add(dynamicUserTable("207502"));
		list.add(dynamicUserTable("207503"));
		list.add(dynamicUserTable("207504"));
		list.add(dynamicUserTable("207505"));
		list.add(dynamicUserTable("207506"));
		list.add(dynamicUserTable("207507"));
		list.add(dynamicUserTable("207508"));
		list.add(dynamicUserTable("207509"));
		list.add(dynamicUserTable("207510"));
		list.add(dynamicUserTable("207511"));
		list.add(dynamicUserTable("207512"));
		list.add(dynamicUserTable("207601"));
		list.add(dynamicUserTable("207602"));
		list.add(dynamicUserTable("207603"));
		list.add(dynamicUserTable("207604"));
		list.add(dynamicUserTable("207605"));
		list.add(dynamicUserTable("207606"));
		list.add(dynamicUserTable("207607"));
		list.add(dynamicUserTable("207608"));
		list.add(dynamicUserTable("207609"));
		list.add(dynamicUserTable("207610"));
		list.add(dynamicUserTable("207611"));
		list.add(dynamicUserTable("207612"));
		list.add(dynamicUserTable("207701"));
		list.add(dynamicUserTable("207702"));
		list.add(dynamicUserTable("207703"));
		list.add(dynamicUserTable("207704"));
		list.add(dynamicUserTable("207705"));
		list.add(dynamicUserTable("207706"));
		list.add(dynamicUserTable("207707"));
		list.add(dynamicUserTable("207708"));
		list.add(dynamicUserTable("207709"));
		list.add(dynamicUserTable("207710"));
		list.add(dynamicUserTable("207711"));
		list.add(dynamicUserTable("207712"));
		list.add(dynamicUserTable("207801"));
		list.add(dynamicUserTable("207802"));
		list.add(dynamicUserTable("207803"));
		list.add(dynamicUserTable("207804"));
		list.add(dynamicUserTable("207805"));
		list.add(dynamicUserTable("207806"));
		list.add(dynamicUserTable("207807"));
		list.add(dynamicUserTable("207808"));
		list.add(dynamicUserTable("207809"));
		list.add(dynamicUserTable("207810"));
		list.add(dynamicUserTable("207811"));
		list.add(dynamicUserTable("207812"));
		list.add(dynamicUserTable("207901"));
		list.add(dynamicUserTable("207902"));
		list.add(dynamicUserTable("207903"));
		list.add(dynamicUserTable("207904"));
		list.add(dynamicUserTable("207905"));
		list.add(dynamicUserTable("207906"));
		list.add(dynamicUserTable("207907"));
		list.add(dynamicUserTable("207908"));
		list.add(dynamicUserTable("207909"));
		list.add(dynamicUserTable("207910"));
		list.add(dynamicUserTable("207911"));
		list.add(dynamicUserTable("207912"));
		list.add(dynamicUserTable("208001"));
		list.add(dynamicUserTable("208002"));
		list.add(dynamicUserTable("208003"));
		list.add(dynamicUserTable("208004"));
		list.add(dynamicUserTable("208005"));
		list.add(dynamicUserTable("208006"));
		list.add(dynamicUserTable("208007"));
		list.add(dynamicUserTable("208008"));
		list.add(dynamicUserTable("208009"));
		list.add(dynamicUserTable("208010"));
		list.add(dynamicUserTable("208011"));
		list.add(dynamicUserTable("208012"));
		list.add(dynamicUserTable("208101"));
		list.add(dynamicUserTable("208102"));
		list.add(dynamicUserTable("208103"));
		list.add(dynamicUserTable("208104"));
		list.add(dynamicUserTable("208105"));
		list.add(dynamicUserTable("208106"));
		list.add(dynamicUserTable("208107"));
		list.add(dynamicUserTable("208108"));
		list.add(dynamicUserTable("208109"));
		list.add(dynamicUserTable("208110"));
		list.add(dynamicUserTable("208111"));
		list.add(dynamicUserTable("208112"));
		list.add(dynamicUserTable("208201"));
		list.add(dynamicUserTable("208202"));
		list.add(dynamicUserTable("208203"));
		list.add(dynamicUserTable("208204"));
		list.add(dynamicUserTable("208205"));
		list.add(dynamicUserTable("208206"));
		list.add(dynamicUserTable("208207"));
		list.add(dynamicUserTable("208208"));
		list.add(dynamicUserTable("208209"));
		list.add(dynamicUserTable("208210"));
		list.add(dynamicUserTable("208211"));
		list.add(dynamicUserTable("208212"));
		list.add(dynamicUserTable("208301"));
		list.add(dynamicUserTable("208302"));
		list.add(dynamicUserTable("208303"));
		list.add(dynamicUserTable("208304"));
		list.add(dynamicUserTable("208305"));
		list.add(dynamicUserTable("208306"));
		list.add(dynamicUserTable("208307"));
		list.add(dynamicUserTable("208308"));
		list.add(dynamicUserTable("208309"));
		list.add(dynamicUserTable("208310"));
		list.add(dynamicUserTable("208311"));
		list.add(dynamicUserTable("208312"));
		list.add(dynamicUserTable("208401"));
		list.add(dynamicUserTable("208402"));
		list.add(dynamicUserTable("208403"));
		list.add(dynamicUserTable("208404"));
		list.add(dynamicUserTable("208405"));
		list.add(dynamicUserTable("208406"));
		list.add(dynamicUserTable("208407"));
		list.add(dynamicUserTable("208408"));
		list.add(dynamicUserTable("208409"));
		list.add(dynamicUserTable("208410"));
		list.add(dynamicUserTable("208411"));
		list.add(dynamicUserTable("208412"));
		list.add(dynamicUserTable("208501"));
		list.add(dynamicUserTable("208502"));
		list.add(dynamicUserTable("208503"));
		list.add(dynamicUserTable("208504"));
		list.add(dynamicUserTable("208505"));
		list.add(dynamicUserTable("208506"));
		list.add(dynamicUserTable("208507"));
		list.add(dynamicUserTable("208508"));
		list.add(dynamicUserTable("208509"));
		list.add(dynamicUserTable("208510"));
		list.add(dynamicUserTable("208511"));
		list.add(dynamicUserTable("208512"));
		list.add(dynamicUserTable("208601"));
		list.add(dynamicUserTable("208602"));
		list.add(dynamicUserTable("208603"));
		list.add(dynamicUserTable("208604"));
		list.add(dynamicUserTable("208605"));
		list.add(dynamicUserTable("208606"));
		list.add(dynamicUserTable("208607"));
		list.add(dynamicUserTable("208608"));
		list.add(dynamicUserTable("208609"));
		list.add(dynamicUserTable("208610"));
		list.add(dynamicUserTable("208611"));
		list.add(dynamicUserTable("208612"));
		list.add(dynamicUserTable("208701"));
		list.add(dynamicUserTable("208702"));
		list.add(dynamicUserTable("208703"));
		list.add(dynamicUserTable("208704"));
		list.add(dynamicUserTable("208705"));
		list.add(dynamicUserTable("208706"));
		list.add(dynamicUserTable("208707"));
		list.add(dynamicUserTable("208708"));
		list.add(dynamicUserTable("208709"));
		list.add(dynamicUserTable("208710"));
		list.add(dynamicUserTable("208711"));
		list.add(dynamicUserTable("208712"));
		list.add(dynamicUserTable("208801"));
		list.add(dynamicUserTable("208802"));
		list.add(dynamicUserTable("208803"));
		list.add(dynamicUserTable("208804"));
		list.add(dynamicUserTable("208805"));
		list.add(dynamicUserTable("208806"));
		list.add(dynamicUserTable("208807"));
		list.add(dynamicUserTable("208808"));
		list.add(dynamicUserTable("208809"));
		list.add(dynamicUserTable("208810"));
		list.add(dynamicUserTable("208811"));
		list.add(dynamicUserTable("208812"));
		list.add(dynamicUserTable("208901"));
		list.add(dynamicUserTable("208902"));
		list.add(dynamicUserTable("208903"));
		list.add(dynamicUserTable("208904"));
		list.add(dynamicUserTable("208905"));
		list.add(dynamicUserTable("208906"));
		list.add(dynamicUserTable("208907"));
		list.add(dynamicUserTable("208908"));
		list.add(dynamicUserTable("208909"));
		list.add(dynamicUserTable("208910"));
		list.add(dynamicUserTable("208911"));
		list.add(dynamicUserTable("208912"));
		list.add(dynamicUserTable("209001"));
		list.add(dynamicUserTable("209002"));
		list.add(dynamicUserTable("209003"));
		list.add(dynamicUserTable("209004"));
		list.add(dynamicUserTable("209005"));
		list.add(dynamicUserTable("209006"));
		list.add(dynamicUserTable("209007"));
		list.add(dynamicUserTable("209008"));
		list.add(dynamicUserTable("209009"));
		list.add(dynamicUserTable("209010"));
		list.add(dynamicUserTable("209011"));
		list.add(dynamicUserTable("209012"));
		list.add(dynamicUserTable("209101"));
		list.add(dynamicUserTable("209102"));
		list.add(dynamicUserTable("209103"));
		list.add(dynamicUserTable("209104"));
		list.add(dynamicUserTable("209105"));
		list.add(dynamicUserTable("209106"));
		list.add(dynamicUserTable("209107"));
		list.add(dynamicUserTable("209108"));
		list.add(dynamicUserTable("209109"));
		list.add(dynamicUserTable("209110"));
		list.add(dynamicUserTable("209111"));
		list.add(dynamicUserTable("209112"));
		list.add(dynamicUserTable("209201"));
		list.add(dynamicUserTable("209202"));
		list.add(dynamicUserTable("209203"));
		list.add(dynamicUserTable("209204"));
		list.add(dynamicUserTable("209205"));
		list.add(dynamicUserTable("209206"));
		list.add(dynamicUserTable("209207"));
		list.add(dynamicUserTable("209208"));
		list.add(dynamicUserTable("209209"));
		list.add(dynamicUserTable("209210"));
		list.add(dynamicUserTable("209211"));
		list.add(dynamicUserTable("209212"));
		list.add(dynamicUserTable("209301"));
		list.add(dynamicUserTable("209302"));
		list.add(dynamicUserTable("209303"));
		list.add(dynamicUserTable("209304"));
		list.add(dynamicUserTable("209305"));
		list.add(dynamicUserTable("209306"));
		list.add(dynamicUserTable("209307"));
		list.add(dynamicUserTable("209308"));
		list.add(dynamicUserTable("209309"));
		list.add(dynamicUserTable("209310"));
		list.add(dynamicUserTable("209311"));
		list.add(dynamicUserTable("209312"));
		list.add(dynamicUserTable("209401"));
		list.add(dynamicUserTable("209402"));
		list.add(dynamicUserTable("209403"));
		list.add(dynamicUserTable("209404"));
		list.add(dynamicUserTable("209405"));
		list.add(dynamicUserTable("209406"));
		list.add(dynamicUserTable("209407"));
		list.add(dynamicUserTable("209408"));
		list.add(dynamicUserTable("209409"));
		list.add(dynamicUserTable("209410"));
		list.add(dynamicUserTable("209411"));
		list.add(dynamicUserTable("209412"));
		list.add(dynamicUserTable("209501"));
		list.add(dynamicUserTable("209502"));
		list.add(dynamicUserTable("209503"));
		list.add(dynamicUserTable("209504"));
		list.add(dynamicUserTable("209505"));
		list.add(dynamicUserTable("209506"));
		list.add(dynamicUserTable("209507"));
		list.add(dynamicUserTable("209508"));
		list.add(dynamicUserTable("209509"));
		list.add(dynamicUserTable("209510"));
		list.add(dynamicUserTable("209511"));
		list.add(dynamicUserTable("209512"));
		list.add(dynamicUserTable("209601"));
		list.add(dynamicUserTable("209602"));
		list.add(dynamicUserTable("209603"));
		list.add(dynamicUserTable("209604"));
		list.add(dynamicUserTable("209605"));
		list.add(dynamicUserTable("209606"));
		list.add(dynamicUserTable("209607"));
		list.add(dynamicUserTable("209608"));
		list.add(dynamicUserTable("209609"));
		list.add(dynamicUserTable("209610"));
		list.add(dynamicUserTable("209611"));
		list.add(dynamicUserTable("209612"));
		list.add(dynamicUserTable("209701"));
		list.add(dynamicUserTable("209702"));
		list.add(dynamicUserTable("209703"));
		list.add(dynamicUserTable("209704"));
		list.add(dynamicUserTable("209705"));
		list.add(dynamicUserTable("209706"));
		list.add(dynamicUserTable("209707"));
		list.add(dynamicUserTable("209708"));
		list.add(dynamicUserTable("209709"));
		list.add(dynamicUserTable("209710"));
		list.add(dynamicUserTable("209711"));
		list.add(dynamicUserTable("209712"));
		list.add(dynamicUserTable("209801"));
		list.add(dynamicUserTable("209802"));
		list.add(dynamicUserTable("209803"));
		list.add(dynamicUserTable("209804"));
		list.add(dynamicUserTable("209805"));
		list.add(dynamicUserTable("209806"));
		list.add(dynamicUserTable("209807"));
		list.add(dynamicUserTable("209808"));
		list.add(dynamicUserTable("209809"));
		list.add(dynamicUserTable("209810"));
		list.add(dynamicUserTable("209811"));
		list.add(dynamicUserTable("209812"));
		list.add(dynamicUserTable("209901"));
		list.add(dynamicUserTable("209902"));
		list.add(dynamicUserTable("209903"));
		list.add(dynamicUserTable("209904"));
		list.add(dynamicUserTable("209905"));
		list.add(dynamicUserTable("209906"));
		list.add(dynamicUserTable("209907"));
		list.add(dynamicUserTable("209908"));
		list.add(dynamicUserTable("209909"));
		list.add(dynamicUserTable("209910"));
		list.add(dynamicUserTable("209911"));
		list.add(dynamicUserTable("209912"));
		return list;
	}

	public static String dynamicUserTable(String ym) {
		return "boot_sys_user_" + ym;
	}

	private static SelectDSL getTestCountWrapper(String tableName, String connector, List<SelectDSL.Where> wheres) {
		return new SelectDSL.Builder().withConnector(connector)
			.withColumns(new SelectDSL.Column.Builder().withName("count(1)").build())
			.withFrom(tableName)
			.withWhere(wheres)
			.build();
	}

	private static SelectDSL getTestWrapper(PageQuery pageQuery, String tableName, String connector,
			List<SelectDSL.Where> wheres) {
		String fromAlias = "a";
		String joinAlias = "b";
		SelectDSL.Join join = new SelectDSL.Join.Builder().withAlias(joinAlias)
			.withColumns(new SelectDSL.Column.Builder().withName("id").build())
			.withFrom(tableName)
			.withOffset(Long.valueOf(pageQuery.getPageIndex()))
			.withLimit(Long.valueOf(pageQuery.getPageSize()))
			.withType(INNER_JOIN)
			.withOrderBy(SelectDSL.OrderBy
				.of(Collections.singletonList(new SelectDSL.Column.Builder().withName("id").withSort(DESC).build())))
			.withWheres(wheres)
			.withOns(new SelectDSL.On.Builder().withFromColumn("id")
				.withFromAlias(fromAlias)
				.withJoinAlias(joinAlias)
				.withJoinColumn("id")
				.build())
			.build();
		return new SelectDSL.Builder().withAlias(fromAlias)
			.withConnector(connector)
			.withColumns(new SelectDSL.Column.Builder().withName("id").build(),
					new SelectDSL.Column.Builder().withName("username").build(),
					new SelectDSL.Column.Builder().withName("super_admin").build(),
					new SelectDSL.Column.Builder().withName("create_date").build(),
					new SelectDSL.Column.Builder().withName("avatar").build(),
					new SelectDSL.Column.Builder().withName("`status`").build(),
					new SelectDSL.Column.Builder().withName("dept_id").build())
			.withFrom(tableName)
			.withJoin(join)
			.build();
	}

}
