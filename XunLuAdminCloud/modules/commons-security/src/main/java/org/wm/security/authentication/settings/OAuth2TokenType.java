/*
 * Copyright 2020-2022 the original author or authors.
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
package org.wm.security.authentication.settings;


import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * token 类型 jwt token、 opaque token
 *
 * @author Joe Grandja
 * @since 0.2.3
 */
public final class OAuth2TokenType implements Serializable {


	/**
	 * jwt token
	 */
	public static final OAuth2TokenType SELF_CONTAINED = new OAuth2TokenType("self-contained");

	/**
	 *  opaque token
	 */
	public static final OAuth2TokenType REFERENCE = new OAuth2TokenType("reference");

	private final String value;

	/**
	 * Constructs an {@code OAuth2TokenFormat} using the provided value.
	 *
	 * @param value the value of the token format
	 */
	public OAuth2TokenType(String value) {
		Assert.hasText(value, "value cannot be empty");
		this.value = value;
	}

	/**
	 * Returns the value of the token format.
	 *
	 * @return the value of the token format
	 */
	public String getValue() {
		return this.value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		OAuth2TokenType that = (OAuth2TokenType) obj;
		return getValue().equals(that.getValue());
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

}
