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
package org.apache.pinot.segment.local.utils;

import com.google.common.hash.Hashing;
import org.apache.pinot.spi.config.table.HashFunction;
import org.apache.pinot.spi.data.readers.PrimaryKey;
import org.apache.pinot.spi.utils.ByteArray;


public class HashUtils {
  private HashUtils() {
  }

  public static byte[] hashMurmur3(byte[] bytes) {
    return Hashing.murmur3_128().hashBytes(bytes).asBytes();
  }

  public static byte[] hashMD5(byte[] bytes) {
    return Hashing.md5().hashBytes(bytes).asBytes();
  }

  public static Object hashPrimaryKey(PrimaryKey primaryKey, HashFunction hashFunction) {
    switch (hashFunction) {
      case NONE:
        return primaryKey;
      case MD5:
        return new ByteArray(HashUtils.hashMD5(primaryKey.asBytes()));
      case MURMUR3:
        return new ByteArray(HashUtils.hashMurmur3(primaryKey.asBytes()));
      default:
        throw new IllegalArgumentException(String.format("Unrecognized hash function %s", hashFunction));
    }
  }

  public static byte[] hashPrimaryKeyAsBytes(PrimaryKey primaryKey, HashFunction hashFunction) {
    switch (hashFunction) {
      case NONE:
        return primaryKey.asBytes();
      case MD5:
        return HashUtils.hashMD5(primaryKey.asBytes());
      case MURMUR3:
        return HashUtils.hashMurmur3(primaryKey.asBytes());
      default:
        throw new IllegalArgumentException(String.format("Unrecognized hash function %s", hashFunction));
    }
  }
}
