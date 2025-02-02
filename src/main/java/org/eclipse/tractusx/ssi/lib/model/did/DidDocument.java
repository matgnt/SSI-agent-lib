/*
 * ******************************************************************************
 * Copyright (c) 2021,2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * *******************************************************************************
 */

package org.eclipse.tractusx.ssi.lib.model.did;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.ToString;
import org.eclipse.tractusx.ssi.lib.model.JsonLdObject;
import org.eclipse.tractusx.ssi.lib.serialization.SerializeUtil;

// spec https://www.w3.org/TR/did-core/
@ToString
public class DidDocument extends JsonLdObject {

  public static final String DEFAULT_CONTEXT = "https://www.w3.org/ns/did/v1";
  public static final String ID = "id";
  public static final String VERIFICATION_METHOD = "verificationMethod";

  public static final String AUTHENTICATION = "authentication";

  public DidDocument(Map<String, Object> json) {
    super(json);

    try {
      // validate getters
      Objects.requireNonNull(getContext(), "context is null");
      Objects.requireNonNull(getId(), "id is null");
      Objects.requireNonNull(getVerificationMethods(), "verificationMethod is null");
    } catch (Exception e) {
      throw new IllegalArgumentException(
          String.format("Invalid DidDocument: %s", SerializeUtil.toJson(json)), e);
    }
  }

  public URI getId() {
    return SerializeUtil.asURI(get(ID));
  }

  public List<VerificationMethod> getVerificationMethods() {

    List<VerificationMethod> result = new ArrayList<>();

    Object verificationMethod = get(VERIFICATION_METHOD);
    if (verificationMethod instanceof Map) {
      result.add(new VerificationMethod((Map<String, Object>) verificationMethod));
    }
    if (verificationMethod instanceof List) {
      ((List<Map<String, Object>>) verificationMethod)
          .forEach(vm -> result.add(new VerificationMethod(vm)));
    }

    return result;
  }

  public static DidDocument fromJson(String json) {
    var map = SerializeUtil.fromJson(json);
    return new DidDocument(map);
  }
}
