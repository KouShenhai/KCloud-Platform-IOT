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
package io.laokou.oauth2.handler;

import lombok.Data;

/**
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/8/10 0010 下午 3:19
 */
@Data
public class SecurityProperties {

    private String clientId;
    private String type;
    private String scopes;
    private String secret;
    private String redirectUri;

}
