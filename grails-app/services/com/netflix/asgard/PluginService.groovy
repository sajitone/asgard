/*
 * Copyright 2012 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.asgard

import com.netflix.asgard.plugin.AuthenticationProvider
import com.netflix.asgard.plugin.TaskFinishedListener
import com.netflix.asgard.plugin.UserDataProvider
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class PluginService implements ApplicationContextAware {

    static final String AUTHENTICATION_PROVIDER = 'authenticationProvider'
    static final String TASK_FINISHED_LISTENERS = 'taskFinishedListeners'
    static final String USER_DATA_PROVIDER = 'userDataProvider'

    ApplicationContext applicationContext
    ConfigService configService

    UserDataProvider getUserDataProvider() {
        String beanName = configService.pluginNamesToBeanNames[USER_DATA_PROVIDER] ?: 'defaultUserDataProvider'
        applicationContext.getBean(beanName) as UserDataProvider
    }

    Collection<TaskFinishedListener> getTaskFinishedListeners() {
        List<String> beanNames = configService.pluginNamesToBeanNames[TASK_FINISHED_LISTENERS] ?: [:]
        beanNames.collect { applicationContext.getBean(it) as TaskFinishedListener }
    }

    /**
     * @return The configured {@link AuthenticationProvider} Spring bean, null if one isn't configured.
     */
    AuthenticationProvider getAuthenticationProvider() {
        String beanName = configService.pluginNamesToBeanNames[AUTHENTICATION_PROVIDER] ?: null
        if (beanName) {
            return applicationContext.getBean(beanName) as AuthenticationProvider
        }
    }

}
