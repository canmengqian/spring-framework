/*
 * Copyright 2002-2017 the original author or authors.
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

package org.springframework.core.style;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

/**
 * Spring's default {@code toString()} styler.
 *
 * <p>This class is used by {@link ToStringCreator} to style {@code toString()}
 * output in a consistent manner according to Spring conventions.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @since 1.2.2
 */
public class DefaultToStringStyler implements ToStringStyler {

	//值风格
	private final ValueStyler valueStyler;


	/**
	 * Create a new DefaultToStringStyler.
	 * @param valueStyler the ValueStyler to use
	 */
	public DefaultToStringStyler(ValueStyler valueStyler) {
		Assert.notNull(valueStyler, "ValueStyler must not be null");
		this.valueStyler = valueStyler;
	}

	/**
	 * Return the ValueStyler used by this ToStringStyler.
	 */
	protected final ValueStyler getValueStyler() {
		return this.valueStyler;
	}


	@Override
	public void styleStart(StringBuilder buffer, Object obj) {
		// 非数组
		if (!obj.getClass().isArray()) {
			buffer.append('[').append(ClassUtils.getShortName(obj.getClass()));
			// 生成对象hash值
			styleIdentityHashCode(buffer, obj);
		}
		// 生成数组[]形式的对象hash值
		else {
			buffer.append('[');
			styleIdentityHashCode(buffer, obj);
			buffer.append(' ');
			//追加value的风格
			styleValue(buffer, obj);
		}
	}

	private void styleIdentityHashCode(StringBuilder buffer, Object obj) {
		// 生成@+对象hash值
		buffer.append('@');
		buffer.append(ObjectUtils.getIdentityHexString(obj));
	}

	//补全后缀
	@Override
	public void styleEnd(StringBuilder buffer, Object o) {
		buffer.append(']');
	}

	@Override
	public void styleField(StringBuilder buffer, String fieldName, @Nullable Object value) {
		// 补全字段名起始值
		styleFieldStart(buffer, fieldName);
		// 填充值
		styleValue(buffer, value);
		// 补全字段结束标识符
		styleFieldEnd(buffer, fieldName);
	}

	protected void styleFieldStart(StringBuilder buffer, String fieldName) {
		buffer.append(' ').append(fieldName).append(" = ");
	}

	protected void styleFieldEnd(StringBuilder buffer, String fieldName) {
	}

	@Override
	public void styleValue(StringBuilder buffer, @Nullable Object value) {
		buffer.append(this.valueStyler.style(value));
	}

	@Override
	public void styleFieldSeparator(StringBuilder buffer) {
		buffer.append(',');
	}

}
