/*
 * Copyright (C) 2013~2017 dinstone<dinstone@163.com>
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
package com.dinstone.jrpc.benchmark.client;

public class CaseConfig {

    public int concurrents = 10;

    public int runTimeSeconds = 60;

    public int dataLength = 1024;

    public String transportSchema = "netty";

    public String caseClassName = StringCaseRunnable.class.getName();

    public int connectPoolSize = Runtime.getRuntime().availableProcessors();
}
