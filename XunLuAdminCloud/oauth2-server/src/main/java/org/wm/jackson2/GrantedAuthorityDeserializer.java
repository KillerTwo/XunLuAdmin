package org.wm.jackson2;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 功能描述：<功能描述>
 *     Collection<? extends GrantedAuthority> 反序列化器
 *
 * @author dove 
 * @date 2023/08/08 00:22
 * @since 1.0
**/
public class GrantedAuthorityDeserializer extends JsonDeserializer<Collection<? extends GrantedAuthority>> {
    @Override
    public Collection<? extends GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        List<String> authorityStrings = jsonParser.readValueAs(List.class);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String authorityString : authorityStrings) {
            authorities.add(new SimpleGrantedAuthority(authorityString));
        }
        return authorities;
    }
}
