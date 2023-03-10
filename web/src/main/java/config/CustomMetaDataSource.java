package config;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.util.AntPathMatcher;


import model.Menu;
import model.Role;
import service.MenuService;
public class CustomMetaDataSource implements FilterInvocationSecurityMetadataSource{
@Autowired
MenuService menuService;
AntPathMatcher antPathMatcher=new AntPathMatcher();
@Override
public Collection<ConfigAttribute> getAttributes(Object o){
	String requestUrl=((FilterInvocation) o).getRequestUrl();
	List<Menu> allMenu=menuService.getAllMenus();
	for(Menu menu:allMenu) {
		if (antPathMatcher.match(menu.getUrl(),requestUrl)&&menu.getRoles().size()>0) {
			List<Role> roles=menu.getRoles();
			int size=roles.size();
			String[] values=new String[size];
			for(int i=0;i<size;i++) {
				values[i]=roles.get(i).getName();
			}
			return SecurityConfig.createList(values);
		}
	}
	return SecurityConfig.createList("ROLE_LOGIN");
}

@Override
public Collection<ConfigAttribute> getAllConfigAttributes(){
	return null;	
}

@Override
public boolean supports(Class<?> aClass) {
	return FilterInvocation.class.isAssignableFrom(aClass);
}
}
