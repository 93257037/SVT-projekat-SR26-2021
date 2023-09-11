package com.ftn.SVT.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.ftn.SVT.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Autowired
    private TokenUtils tokenUtils;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
        // prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
        authProvider.setUserDetailsService(userDetailsService());
        // 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu
        // da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Handler za vracanje 401 kada klijent sa neodogovarajucim korisnickim imenom i lozinkom pokusa da pristupi resursu
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // svim korisnicima dopusti da pristupe sledecim putanjama:
        // komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST aplikacija
        // ovo znaci da server ne pamti nikakvo stanje, tokeni se ne cuvaju na serveru
        // ovo nije slucaj kao sa sesijama koje se cuvaju na serverskoj strani - STATEFUL aplikacija
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeRequests()
				        .antMatchers(HttpMethod.POST, "/api/users/login").permitAll()
				        .antMatchers(HttpMethod.POST, "/api/users/register").permitAll()
				        .antMatchers(HttpMethod.PUT, "/api/users/changePassword/{username}").permitAll()
				        .antMatchers(HttpMethod.GET, "/api/users/all").permitAll()

		                .antMatchers(HttpMethod.POST,"/api/groups/create/{username}").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/groups").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/groups/{group.id}").permitAll()
		                .antMatchers(HttpMethod.PUT,"/api/groups/update/{groupId}").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/groups/delete/{groupId}").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/groups/groups/{username}").permitAll()

		                .antMatchers(HttpMethod.GET,"/api/posts/group/{groupId}").permitAll()
		                .antMatchers(HttpMethod.POST,"/api/posts/create").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/posts/all").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/posts/delete/{postId}").permitAll()
	             		.antMatchers(HttpMethod.PUT,"/api/posts/update/{postId}").permitAll()
		                .antMatchers(HttpMethod.PUT,"/api/posts/update/counter/${postId}").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/posts/group/{groupId}").permitAll()

		                .antMatchers(HttpMethod.POST,"/api/reactions/create").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/reactions/all").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/reactions//delete/{reactionId}").permitAll()

                // ukoliko ne zelimo da koristimo @PreAuthorize anotacije nad metodama kontrolera, moze se iskoristiti hasRole() metoda da se ogranici
                // koji tip korisnika moze da pristupi odgovarajucoj ruti. Npr. ukoliko zelimo da definisemo da ruti 'admin' moze da pristupi
                // samo korisnik koji ima rolu 'ADMIN', navodimo na sledeci nacin:
                //.antMatchers("/api/clubs").hasRole("ADMIN")// ili .antMatchers("/admin").hasAuthority("ROLE_ADMIN")

                // za svaki drugi zahtev korisnik mora biti autentifikovan
                .anyRequest().authenticated().and()
                // za development svrhe ukljuci konfiguraciju za CORS iz WebConfig klase
                .cors().and()

                // umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera JWT tokena umesto cistih korisnickog imena i lozinke (koje radi BasicAuthenticationFilter)
                .addFilterBefore(new AuthenticationTokenFilter(userDetailsService(), tokenUtils), BasicAuthenticationFilter.class);

        // zbog jednostavnosti primera ne koristimo Anti-CSRF token (https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html)
        http.csrf().disable();


        // ulancavanje autentifikacije
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    // metoda u kojoj se definisu putanje za igorisanje autentifikacije
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpSecurity http) {
        return (web) -> {
            try {
				http.authorizeRequests()
				        .antMatchers(HttpMethod.POST, "/api/users/login").permitAll()
				        .antMatchers(HttpMethod.POST, "/api/users/signup").permitAll()
				        .antMatchers(HttpMethod.PUT, "/api/users/changePassword/{username}").permitAll()
				        .antMatchers(HttpMethod.GET, "/api/users/all").permitAll()

		                .antMatchers(HttpMethod.POST,"/api/groups/create/{username}").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/groups").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/groups/{group.id}").permitAll()
		                .antMatchers(HttpMethod.PUT,"/api/groups/update/{groupId}").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/groups/delete/{groupId}").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/groups/groups/{username}").permitAll()

		                .antMatchers(HttpMethod.GET,"/api/posts/group/{groupId}").permitAll()
		                .antMatchers(HttpMethod.POST,"/api/posts/create").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/posts/all").permitAll()
		                .antMatchers(HttpMethod.PUT,"/api/posts/update/counter/${postId}").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/posts/delete/{postId}").permitAll()
						.antMatchers(HttpMethod.PUT,"/api/posts/update/{postId}").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/posts/group/{groupId}").permitAll()

		                .antMatchers(HttpMethod.POST,"/api/reactions/create").permitAll()
		                .antMatchers(HttpMethod.GET,"/api/reactions/all").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/reactions/delete/{reactionId}").permitAll()
		                .antMatchers(HttpMethod.DELETE,"/api/reactions/post/{postId}").permitAll()

		                
				        .anyRequest().authenticated();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
                    "/**/*.html", "/**/*.css", "/**/*.js");
        };
    }
}