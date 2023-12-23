package ir.bigz.springboot.springmuldatasource;

import ir.bigz.springboot.springmuldatasource.post.Post;
import ir.bigz.springboot.springmuldatasource.post.PostService;
import ir.bigz.springboot.springmuldatasource.subscriber.Subscriber;
import ir.bigz.springboot.springmuldatasource.subscriber.SubscriberService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
public class SpringMulDatasourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMulDatasourceApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(PostService postService, SubscriberService subscriberService) {
		return args -> {
			List<Post> posts = postService.findAll();
			System.out.println(posts);


			List<Subscriber> subscribers = subscriberService.findAll();
			System.out.println(subscribers);
		};
	}

	@Bean
	CommandLineRunner dsCommandLineRunner(@Qualifier("blogDataSource") DataSource blogDataSource, @Qualifier("subscriberDataSource") DataSource subscriberDataSource) {
		return args -> {
			System.out.println(blogDataSource.getConnection().getMetaData().getURL());
			System.out.println(subscriberDataSource.getConnection().getMetaData().getURL());
		};
	}

	@Bean
	JdbcClient blogJdbcClient(@Qualifier("blogDataSource") DataSource dataSource) {
		return JdbcClient.create(dataSource);
	}

	@Bean
	JdbcClient subscriberJdbcClient(@Qualifier("subscriberDataSource") DataSource dataSource) {
		return JdbcClient.create(dataSource);
	}

}
