/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.pinot.segment.local.upsert;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implementation of {@link TableUpsertMetadataManager} that is backed by a {@link ConcurrentHashMap}.
 */
@ThreadSafe
public class MapDBTableUpsertMetadataManager extends BaseTableUpsertMetadataManager {

  private final static Logger LOGGER = LoggerFactory.getLogger(MapDBTableUpsertMetadataManager.class);

  private final Map<Integer, MapDBPartitionUpsertMetadataManager> _partitionMetadataManagerMap =
      new ConcurrentHashMap<>();

  @Override
  public PartitionUpsertMetadataManager getOrCreatePartitionManager(int partitionId) {
    LOGGER.info("Creating partition manager for " + _tableNameWithType + " partition " + partitionId);
    return _partitionMetadataManagerMap.computeIfAbsent(partitionId,
        k -> new MapDBPartitionUpsertMetadataManager(_tableNameWithType, k, _primaryKeyColumns,
            _comparisonColumn, _hashFunction, _partialUpsertHandler, _serverMetrics));
  }

  @Override
  public void close() {
    LOGGER.info("Closing partition manager for " + _tableNameWithType);
    for (MapDBPartitionUpsertMetadataManager partitionUpsertMetadataManager
        : _partitionMetadataManagerMap.values()) {
      partitionUpsertMetadataManager.close();
    }
  }
}
