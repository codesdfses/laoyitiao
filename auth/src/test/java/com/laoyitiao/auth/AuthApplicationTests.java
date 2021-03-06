package com.laoyitiao.auth;

import com.laoyitiao.auth.entities.Authority;
import com.laoyitiao.auth.entities.Group;
import com.laoyitiao.auth.entities.GroupMember;
import com.laoyitiao.auth.entities.UserAccount;
import com.laoyitiao.auth.repositories.GroupMemberRepository;
import com.laoyitiao.auth.repositories.GroupRepository;
import com.laoyitiao.auth.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyAuthoritiesMapper;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.*;

@SpringBootTest
class AuthApplicationTests {
	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	public DataSource dataSource;

	@Bean
	public UserDetailsManager userDetailsManager(){
		JdbcUserDetailsManager detailsManager = new JdbcUserDetailsManager();
		detailsManager.setDataSource(dataSource);
		return detailsManager;
	}

	@Test
	void contextLoads() {
		UserDetails user = User.withUsername("test").password(passwordEncoder.encode("test")).roles("admin").build();
		userDetailsManager().createUser(user);
	}


	@Autowired
	private GroupRepository groupRepository;

	@Test
	public void TestGroup(){
		List<Group> all = groupRepository.findAll();
		for (Group group : all) {
			System.out.println("group = " + group.getGroupName());
		}
	}

	@Test
	public void TestGroupAuthority(){
		List<Group> all = groupRepository.findAll();
		for (Group group : all) {
			System.out.println("group = " + group.getGroupAuthorities());
		}
	}

	@Test
	public void TestGroupMem(){
		List<Group> all = groupRepository.findAll();
		for (Group group : all) {
			System.out.println("group = " + group.getGroupMembers());
		}
	}

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testCreateUser(){
		Optional<com.laoyitiao.auth.entities.User> admin = userRepository.findById("flame_admin");
		if (admin.isPresent()){
			throw new BadCredentialsException("??????????????????");
		}
		// ????????????
		com.laoyitiao.auth.entities.User user = new com.laoyitiao.auth.entities.User(
				"flame_admin",
				passwordEncoder.encode("flame_admin"),
				true,null,null,null,null);

		// ?????????????????????????????????
		UserAccount userAccount = new UserAccount(
				user.getId(),
				"???????????????",
				"??????????????????",
				"????????????",
				99999999L,
				"13299999999@163.com",
				"13299999999",
				"??????",
				new Timestamp(System.currentTimeMillis()).toInstant());

		// ??????????????????
		Authority once_blog_push = new Authority(null,user.getId(),"once_blog_push");
		Authority once_file_down = new Authority(null,user.getId(),"once_file_down");
		Set<Authority> set = new HashSet<>();
		set.add(once_blog_push);
		set.add(once_file_down);

		// ??????????????????????????????
		GroupMember groupMember = new GroupMember(
				user.getId(),
				new Group(5L,"???????????????",null,null));

		// ????????????????????????????????????????????????
		user.setUserAccount(userAccount);
		user.setAuthorities(set);
		user.setGroupMember(groupMember);

		// ????????????
		com.laoyitiao.auth.entities.User saveUser = userRepository.save(user);
		userRepository.flush();
		System.out.println(saveUser);

	}


	@Autowired
	private GroupMemberRepository groupMemberRepository;

}
