package org.wm.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jackson2.*;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.wm.commons.dto.LoginUser;
import org.wm.commons.dto.TransferDataMap;
import org.wm.jackson2.GrantedAuthorityDeserializer;
import org.wm.jackson2.GrantedAuthoritySerializer;
import org.wm.jackson2.SecurityContextUserMixin;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.*;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;

/**
 * 功能描述：<功能描述>
 *
 * @author dove 
 * @date 2023/08/05 22:25
 * @since 1.0
**/
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
// @AllArgsConstructor
// @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
/*@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(using = GrantedAuthoritySerializer.class)
@JsonDeserialize(using = GrantedAuthorityDeserializer.class)*/
public class SecurityContextUser extends LoginUser implements UserDetails, Principal, Serializable {

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(getRoles().toArray(new String[0]));
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return getUsername();
    }

    public SecurityContextUser(TransferDataMap dataMap) {
        this.setUserId(dataMap.getLong("userId"));
        this.setDeptId(dataMap.getLong("deptId"));
        this.setPermissions(dataMap.getSetString("permissions"));
        this.setUsername(dataMap.getString("username"));
        this.setNickName(dataMap.getString("nickName"));
        this.setEmail(dataMap.getString("email"));
        this.setPhonenumber(dataMap.getString("phonenumber"));
        this.setSex(dataMap.getString("sex"));
        this.setAvatar(dataMap.getString("avatar"));
        this.setStatus(dataMap.getString("status"));
        this.setDelFlag(dataMap.getString("delFlag"));
        this.setRoleIds(dataMap.getLongArray("roleIds"));
        this.setPostIds(dataMap.getLongArray("postIds"));
        this.setRoles(dataMap.getSetString("roles"));
        this.setPassword(dataMap.getString("password"));
    }

    public SecurityContextUser() {
    }




    public static void main(String[] args) throws JsonProcessingException {
        var data = "{\n" +
                "\t\"@class\": \"java.util.Collections$UnmodifiableMap\",\n" +
                "\t\"metadata.token.claims\": {\n" +
                "\t\t\"@class\": \"java.util.Collections$UnmodifiableMap\",\n" +
                "\t\t\"principal\": {\n" +
                "\t\t\t\"@class\": \"org.wm.domain.dto.SecurityContextUser\",\n" +
                "\t\t\t\"userId\": 1,\n" +
                "\t\t\t\"deptId\": 103,\n" +
                "\t\t\t\"token\": null,\n" +
                "\t\t\t\"loginTime\": null,\n" +
                "\t\t\t\"expireTime\": null,\n" +
                "\t\t\t\"ipaddr\": null,\n" +
                "\t\t\t\"loginLocation\": null,\n" +
                "\t\t\t\"browser\": null,\n" +
                "\t\t\t\"os\": null,\n" +
                "\t\t\t\"permissions\": [\"java.util.HashSet\", [\"*:*:*\"]],\n" +
                "\t\t\t\"username\": \"admin\",\n" +
                "\t\t\t\"nickName\": \"若依\",\n" +
                "\t\t\t\"email\": \"ry@163.com\",\n" +
                "\t\t\t\"phonenumber\": \"15888888888\",\n" +
                "\t\t\t\"sex\": \"1\",\n" +
                "\t\t\t\"avatar\": \"/icons/icon-auatar.jpg\",\n" +
                "\t\t\t\"password\": \"$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2\",\n" +
                "\t\t\t\"salt\": null,\n" +
                "\t\t\t\"status\": \"0\",\n" +
                "\t\t\t\"delFlag\": \"0\",\n" +
                "\t\t\t\"loginIp\": null,\n" +
                "\t\t\t\"loginDate\": null,\n" +
                "\t\t\t\"roleIds\": null,\n" +
                "\t\t\t\"postIds\": null,\n" +
                "\t\t\t\"roles\": [\"java.util.HashSet\", [\"admin\"]],\n" +
                "\t\t\t\"name\": \"admin\",\n" +
                "\t\t\t\"enabled\": true,\n" +
                "\t\t\t\"authorities\": [\"java.util.ArrayList\", [{\n" +
                "\t\t\t\t\"@class\": \"org.springframework.security.core.authority.SimpleGrantedAuthority\",\n" +
                "\t\t\t\t\"authority\": \"admin\"\n" +
                "\t\t\t}]],\n" +
                "\t\t\t\"accountNonExpired\": true,\n" +
                "\t\t\t\"accountNonLocked\": true,\n" +
                "\t\t\t\"credentialsNonExpired\": true\n" +
                "\t\t},\n" +
                "\t\t\"sub\": \"admin\",\n" +
                "\t\t\"aud\": [\"java.util.Collections$SingletonList\", [\"messaging-client-opaque\"]],\n" +
                "\t\t\"nbf\": [\"java.time.Instant\", 1691507893.520138000],\n" +
                "\t\t\"scope\": [\"java.util.Collections$UnmodifiableSet\", [\"openid\", \"message.read\", \"message.write\"]],\n" +
                "\t\t\"iss\": [\"java.net.URL\", \"http://localhost:8090\"],\n" +
                "\t\t\"exp\": [\"java.time.Instant\", 1691509693.520138000],\n" +
                "\t\t\"iat\": [\"java.time.Instant\", 1691507893.520138000],\n" +
                "\t\t\"jti\": \"f0436aaa-9257-4fcc-90ef-608612b8bd1a\",\n" +
                "\t\t\"authorities\": [\"java.util.ImmutableCollections$ListN\", [\"admin\"]]\n" +
                "\t},\n" +
                "\t\"metadata.token.invalidated\": false\n" +
                "}";


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CoreJackson2Module());
        ClassLoader classLoader = SecurityContextUser.class.getClassLoader();
        var securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        objectMapper.addMixIn(SecurityContextUser.class, SecurityContextUserMixin.class);
        objectMapper.enableDefaultTyping(NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // objectMapper.registerModule(new MyJackson2Module());
        var obj = objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
        System.err.println(obj);

    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
    @JsonDeserialize(using = UnmodifiableListDeserializer.class)
    public static class UnmodifiableListMixin {

        /**
         * Mixin Constructor
         * @param s the Set
         */
        @JsonCreator
        UnmodifiableListMixin(Set<?> s) {
        }

    }


    public static class UnmodifiableListDeserializer extends JsonDeserializer<List> {

        @Override
        public List deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectMapper mapper = (ObjectMapper) jp.getCodec();
            JsonNode node = mapper.readTree(jp);
            List<Object> result = new ArrayList<>();
            if (node != null) {
                if (node instanceof ArrayNode arrayNode) {
                    for (JsonNode elementNode : arrayNode) {
                        result.add(mapper.readValue(elementNode.traverse(mapper), Object.class));
                    }
                }
                else {
                    result.add(mapper.readValue(node.traverse(mapper), Object.class));
                }
            }
            return Collections.unmodifiableList(result);
        }
    }

    public static class MyJackson2Module extends SimpleModule {

        public MyJackson2Module() {
            super(MyJackson2Module.class.getName(), new Version(1, 0, 0, null, null, null));
        }

        @Override
        public void setupModule(SetupContext context) {
            SecurityJackson2Modules.enableDefaultTyping(context.getOwner());
            context.setMixInAnnotations(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
            context.setMixInAnnotations(Collections.<Object>unmodifiableList(Collections.emptyList()).getClass(),
                    UnmodifiableListMixin.class);

        }

    }
}
