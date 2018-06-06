/*
 * Copyright 2010-2018 Boxfuse GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.command;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.callback.Event;
import org.flywaydb.core.api.callback.FlywayCallback;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.internal.callback.CallbackExecutor;
import org.flywaydb.core.internal.database.Connection;
import org.flywaydb.core.internal.database.Database;
import org.flywaydb.core.internal.database.Schema;
import org.flywaydb.core.internal.info.MigrationInfoServiceImpl;
import org.flywaydb.core.internal.schemahistory.SchemaHistory;
import org.flywaydb.core.internal.util.jdbc.TransactionTemplate;

import java.util.List;
import java.util.concurrent.Callable;

public class DbInfo {
    private final MigrationResolver migrationResolver;
    private final SchemaHistory schemaHistory;
    private final Configuration configuration;
    private final CallbackExecutor callbackExecutor;

    public DbInfo(MigrationResolver migrationResolver, SchemaHistory schemaHistory,
                  Configuration configuration, CallbackExecutor callbackExecutor) {

        this.migrationResolver = migrationResolver;
        this.schemaHistory = schemaHistory;
        this.configuration = configuration;
        this.callbackExecutor = callbackExecutor;
    }

    public MigrationInfoService info() {
        callbackExecutor.executeOnMainConnection(Event.BEFORE_INFO);


        MigrationInfoServiceImpl migrationInfoService;
        try {
            migrationInfoService =
                    new MigrationInfoServiceImpl(migrationResolver, schemaHistory, configuration.getTarget(),
                            configuration.isOutOfOrder(), true, true, true, true);
            migrationInfoService.refresh();
        } catch (FlywayException e) {
            callbackExecutor.executeOnMainConnection(Event.AFTER_INFO_ERROR);
            throw e;
        }

        callbackExecutor.executeOnMainConnection(Event.AFTER_INFO);

        return migrationInfoService;
    }
}